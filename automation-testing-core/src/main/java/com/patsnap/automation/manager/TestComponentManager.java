package com.patsnap.automation.manager;

import com.patsnap.automation.entity.TestSuite;
import com.patsnap.automation.entity.Testcase;
import com.patsnap.automation.entity.TestcaseRuntimeInstance;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by liuyikai on 2017/9/4.
 */

public interface TestComponentManager {
    
    List<Testcase> getTestCaseList();
    
    List<Testcase> getTestCaseListFromComponent(Object bean);
    
    TestcaseRuntimeInstance invokeTestCase(Testcase testcase, boolean isParallel, int logLevel) throws Exception;
    
    
    Testcase getTestCase(String componentName, String entryPoint) throws Exception;
    
    /**
     * only for testsuite run,  testcase only contains basic info
     * @param testcase
     * @return
     */
    Future<TestcaseRuntimeInstance> asyncRunTestcaseFromSuite(Testcase testcase, TestSuite testSuite) throws Exception;
    
    
    TestcaseRuntimeInstance syncRunTestcaseFromSuite(Testcase testcase, TestSuite testSuite) throws Exception;
    
    /**
     * 搜索符合条件的测试用例
     * @param keyword
     * @return
     * @throws Exception
     */
    List<Testcase> search(String keyword) throws  Exception;
    
    /**
     * 保存页面上配置好的测试数据
     * @param testcase
     * @throws Exception
     */
    void saveTestData(Testcase testcase) throws Exception;
    
}
