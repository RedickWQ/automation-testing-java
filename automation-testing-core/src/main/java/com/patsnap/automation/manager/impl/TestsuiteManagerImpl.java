package com.patsnap.automation.manager.impl;

import com.patsnap.automation.entity.TestSuite;
import com.patsnap.automation.enums.RunStatus;
import com.patsnap.automation.contants.Constant;
import com.patsnap.automation.entity.Testcase;
import com.patsnap.automation.entity.TestcaseRuntimeInstance;
import com.patsnap.automation.manager.TestComponentManager;
import com.patsnap.automation.manager.TestsuiteManager;
import com.patsnap.automation.report.ExtentReportManager;
import com.patsnap.automation.utils.CommonUtil;
import com.patsnap.automation.utils.JsonUtil;

import com.alibaba.fastjson.JSON;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 *
 * @author Liuyikai (Alex)
 * @date 2017/10/16
 */
@Service
public class TestsuiteManagerImpl implements TestsuiteManager {
    
    @Autowired
    TestComponentManager testComponentManager;
    
    public TestsuiteManagerImpl () throws IOException {
        FileUtils.forceMkdir(new File(Constant.TEST_SUITE_ROOT_DIR));
    }
    
    
    @Async(Constant.EXECUTION_THREAD_POOL_NAME)
    @Override
    public Future<TestSuite> asyncRunTestSuite(TestSuite testSuite) throws Exception {
        
        testSuite = invokeTestSuite(testSuite);
        return new AsyncResult<>(testSuite);
    }
    
    @Override
    public TestSuite invokeTestSuite(TestSuite testSuite) throws Exception {
    
        //init testsuite
        testSuite.setRunning(true);
        testSuite.setStartTime(LocalDateTime.now());
        testSuite.setStatus(RunStatus.RUNNING.getDesc());
        
        testSuite.setTestcaseRuntimeInstanceList(new ArrayList<>());
        
        //init report
        ExtentReportManager extentReportManager = new ExtentReportManager();
        testSuite.setExtentReportManager(extentReportManager);
        extentReportManager.initReportInstance(testSuite);
        
        
        if (testSuite.isCaseParallel()) {
            runTestCasesInParallel(testSuite);
        } else {
            runTestCasesBySequential(testSuite);
        }
        
        evaluateStatus(testSuite);
        testSuite.setEndTime(LocalDateTime.now());
        testSuite.setDuration(Duration.between(testSuite.getStartTime(),testSuite.getEndTime()));
        testSuite.setRunning(false);
        
        //end report
        testSuite.getExtentReportManager().endTestSuite();
        
        return testSuite;
    }
    
    
    private void runTestCasesInParallel(TestSuite testSuite) throws Exception {
    
    
        List<Future<TestcaseRuntimeInstance>> taskList = new ArrayList<>();
        for (Testcase testcase: testSuite.getTestcaseList()){
        
            Future<TestcaseRuntimeInstance> task = testComponentManager.asyncRunTestcaseFromSuite(testcase, testSuite);
            taskList.add(task);
        }
        
        for (Future<TestcaseRuntimeInstance> task : taskList) {
            try {
                while (true) {
                    if (task.isDone() && !task.isCancelled()) {
                        TestcaseRuntimeInstance tr = task.get();
                        if (!tr.getStatus().equals(RunStatus.RUNNING.getDesc())) {
                            break;
                        }
                        
                    } else {
                        Thread.sleep(300);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private void runTestCasesBySequential(TestSuite testSuite) throws Exception {
        for (Testcase testcase: testSuite.getTestcaseList()) {
            TestcaseRuntimeInstance testcaseRuntimeInstance =  testComponentManager.syncRunTestcaseFromSuite(testcase, testSuite);
       
        }
    }
    
    private void evaluateStatus(TestSuite testSuite){
        TestcaseRuntimeInstance failedCase =  testSuite.getTestcaseRuntimeInstanceList().stream().filter(tr ->
                tr.getStatus().equals(RunStatus.FAILED)
                        || tr.getStatus().equals(RunStatus.ERROR)).findAny().orElse(null);
        
        if (null != failedCase){
            testSuite.setStatus(RunStatus.FAILED.getDesc());
        } else  {
            testSuite.setStatus(RunStatus.PASSED.getDesc());
        }
    }
    
    
    
    
    @Override
    public TestSuite saveTestSuite(TestSuite testSuite) throws Exception {
    
        FileUtils.forceMkdir(new File(Constant.TEST_SUITE_ROOT_DIR));
        
        if (testSuite.getId() == null) {
            testSuite.setId(CommonUtil.getGuid());
        }
    
        String testSuiteJsonString = JsonUtil.toJsonString(testSuite);
        File testSuiteFile = new File(Constant.TEST_SUITE_ROOT_DIR + testSuite.getId() + ".json");
        testSuiteFile.createNewFile();
        FileUtils.write(testSuiteFile, testSuiteJsonString, false);
 
        return testSuite;
    }

    @Override
    public List<TestSuite> getTestSuiteList() throws Exception {
        List<TestSuite> testSuiteList = new ArrayList<>();
    
        File rootDir = new File(Constant.TEST_SUITE_ROOT_DIR);
        if (rootDir.exists() && rootDir.isDirectory()) {
    
            File[] files = rootDir.listFiles();
    
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file.isFile()) {
                    String testSuiteContent = FileUtils.readFileToString(file);
                    TestSuite testSuite = JSON.parseObject(testSuiteContent, TestSuite.class);
                    testSuiteList.add(testSuite);
                }
            }
        }
        return testSuiteList;
    }
    
    @Override
    public void deleteSuiteById(String id) throws Exception {
    
        File rootDir = new File(Constant.TEST_SUITE_ROOT_DIR);
        if (rootDir.exists() && rootDir.isDirectory()) {
            File[] files = rootDir.listFiles();
    
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file.getName().equals(id + ".json")) {
                    file.delete();
                    break;
                }
        
            }
        }
    }
}
