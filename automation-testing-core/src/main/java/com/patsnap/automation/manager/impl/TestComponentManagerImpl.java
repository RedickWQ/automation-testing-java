package com.patsnap.automation.manager.impl;

import com.patsnap.automation.annotation.AutomationComponent;
import com.patsnap.automation.annotation.AutomationTestCase;
import com.patsnap.automation.annotation.DataSource;
import com.patsnap.automation.annotation.TestParam;
import com.patsnap.automation.contants.Constant;
import com.patsnap.automation.entity.Checkpoint;
import com.patsnap.automation.entity.Iteration;
import com.patsnap.automation.entity.TestParameter;
import com.patsnap.automation.entity.TestSuite;
import com.patsnap.automation.entity.Testcase;
import com.patsnap.automation.entity.TestcaseRuntimeInstance;
import com.patsnap.automation.enums.RunStatus;
import com.patsnap.automation.log.LogLevel;
import com.patsnap.automation.manager.TestComponentManager;
import com.patsnap.automation.manager.TestDataManager;
import com.patsnap.automation.manager.TestExecutionManager;
import com.patsnap.automation.manager.ValidationManager;
import com.patsnap.automation.utils.SpringContextUtil;

import com.aventstack.extentreports.ExtentTest;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 *
 * @author liuyikai
 * @date 2017/9/4
 */
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class TestComponentManagerImpl implements TestComponentManager {
    
    @Autowired
    TestDataManager testDataManager;
    
    @Autowired
    ValidationManager validationManager;
    
    @Autowired
    TestExecutionManager testExecutionManager;
    
    
    private List<Testcase> testcaseList;
    
    private List<Class> testComponentClassList;
    
    private String [] testComponentNameList;
    
    /**
     * 读取所有已经配置好的testcase
     * @return
     */
    @Override
    public List<Testcase> getTestCaseList(){
        
        if (!CollectionUtils.isEmpty(this.testcaseList)) {
            
            return  this.testcaseList;
        }
        List<Testcase> testCaseList = new ArrayList<>();
    
        this.testComponentNameList = SpringContextUtil.getBeanDefinitionNamesByAnnotation(AutomationComponent.class);
        //scan only once
        
        
        try {
           
            for (String  componentName: testComponentNameList) {
                Class clazz = SpringContextUtil.getBeanClassByName(componentName);
                List<Testcase> testcaseListInComponent = getTestcaseListFromComponent(clazz);
                if (!CollectionUtils.isEmpty(testcaseListInComponent)){
                    testcaseListInComponent.stream().forEach(t -> {
                        t.setComponentName(componentName);
                    });
                    testCaseList.addAll(testcaseListInComponent);
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
    
        this.testcaseList = testCaseList;
        return testCaseList;
    }
    
    /**
     * 从单个component类里获取所有testcase
     * @param bean
     * @return
     */
    @Override
    public List<Testcase> getTestCaseListFromComponent(Object bean){
        
        Class clazz;
        if (AopUtils.isAopProxy(bean)){
            clazz = AopUtils.getTargetClass(bean);
        } else  {
            clazz = bean.getClass();
        }
        
        return getTestcaseListFromComponent(clazz);
        
    }
    
    private List<Testcase> getTestcaseListFromComponent(Class clazz){
        List<Testcase> testcaseList = new ArrayList<>();
    
        Method[] methods = clazz.getMethods();
    
        for (Method method: methods) {
            if (method.getAnnotation(AutomationTestCase.class) != null){
                Testcase testcase = getTestcaseBasicInfo(method);
//                testcase.setCategory(clazz.getPackage().getName());
                testcaseList.add(testcase);
            }
        }
    
        return testcaseList;
    }
    
    
    
    @Override
    public List<Testcase> search(String keyword) throws Exception {
        String k = keyword.toLowerCase();
        List<Testcase> testcaseList = getTestCaseList();
        return testcaseList.stream()
                .filter(testcase -> testcase.getName().toLowerCase().contains(k) || testcase.getEntryPoint().toLowerCase().contains(k))
                .limit(10).collect(Collectors.toList());
        
    }
    
    private Testcase getTestcaseBasicInfo(Method method){
        // get testcase name
        String testCaseName;
        AutomationTestCase caseInfo = method.getDeclaredAnnotation(AutomationTestCase.class);
        if (!StringUtils.isEmpty(caseInfo.name())) {
            testCaseName = caseInfo.name();
        } else  {
            testCaseName = method.getName();
        }
        
        String[] tags = caseInfo.tags();
        
        
        //get datasource
        DataSource dataSource = method.getDeclaredAnnotation(DataSource.class);
       
        
        // assemble testcase info
        Testcase testcase = Testcase.builder()
                .name(testCaseName)
                .description(caseInfo.description())
                .entryPoint(method.getName())
                .author(caseInfo.author())
                .caseType(caseInfo.testcaseType().getCode())
                .tags(Arrays.asList(tags))
                .isDatasourceConfigured(dataSource != null)
                .build();
    
        return testcase;
        
        
    }
    
    
    
    /**
     * 根据method，反射读取case的基本信息，给前台展示用， 默认只有一个迭代
     * @param method
     * @return
     */
    private Iteration getDefaultIteration(Method method){
        
  
        // get parameter info
        List<TestParameter> testParameterList = new ArrayList<>();
        Parameter[] methodParameters = method.getParameters();
        for (Parameter parameter: methodParameters){
            TestParam paramInfo = parameter.getAnnotation(TestParam.class);
            TestParameter testParameter = TestParameter.builder()
                    .name(paramInfo.name())
                    .type(parameter.getType().getTypeName())
                    .value(paramInfo.value())
                    .build();
            testParameterList.add(testParameter);
        }
        
        Iteration iteration = Iteration.builder()
                .testParameterList(testParameterList)
                .build();
        
     
        return iteration;
        
    }
    
    
    /**
     * 读取单个case的信息
     * @param componentName
     * @param entryPoint
     * @return
     * @throws Exception
     */
    @Override
    public Testcase getTestCase(String componentName, String entryPoint) throws Exception {
        
        Class clazz = SpringContextUtil.getBeanClassByName(componentName);
        
        Method method = getMethodByName(clazz, entryPoint);
        Testcase testcase =  getTestcaseBasicInfo(method);
        
        List<Iteration> iterationList = testDataManager.getIterationList(method);
        
        Iteration defaultIteration = getDefaultIteration(method);
        
        if (CollectionUtils.isEmpty(iterationList)){
            iterationList.add(defaultIteration);
        } else  {
    
            mappingDefaultParameterWithExistingConfig(iterationList ,defaultIteration.getTestParameterList());
            //todo fix parameter info
        }
        
        testcase.setIterationList(iterationList);
        
        
//        List<Checkpoint> checkpointList = validationManager.getCheckpointList(method);
//        testcase.setCheckpointList(checkpointList);
        
        testcase.setComponentName(componentName);
        return testcase;
    }
    
    
    
    /**
     * 当前配置文件中的参数配置信息可能会与当前代码注解不一致
     * 以当前代码注解为准,
     * 找得到配置信息，则用配置的值，
     * 否则以方法签名上的注解内容为准
     * @param configuredIterationList
     */
    private void mappingDefaultParameterWithExistingConfig(List<Iteration> configuredIterationList, List<TestParameter> defaultParameterList){
    
        configuredIterationList.stream().forEach(iteration -> {
            List<TestParameter> finalParameterList = new ArrayList<>();
    
            defaultParameterList.stream().forEach(p -> {
                TestParameter configuredParameter = iteration.getTestParameterList().stream()
                        .filter(cp -> cp.getName().equals(p.getName()))
                        .findAny().orElse(null);
                
                if (null != configuredParameter){
                    configuredParameter.setType(p.getType());
                    finalParameterList.add(configuredParameter);
                } else  {
                    finalParameterList.add(p);
                }
            });
            
            iteration.setTestParameterList(finalParameterList);
        });
        
    }
    
    @Override
    public void saveTestData(Testcase testcase) throws Exception{
        
        if (CollectionUtils.isEmpty(testcase.getIterationList())) {
           throw new RuntimeException("Iteration list cannot be empty!");
        }
        
        Class clazz = SpringContextUtil.getBeanClassByName(testcase.getComponentName());
        Method method = getMethodByName(clazz, testcase.getEntryPoint());
        testDataManager.saveIterationList(method, testcase.getIterationList());

    }
    

    
    private TestcaseRuntimeInstance prepareTestcaseRuntimeInstance(Testcase testcase) throws  Exception {
        // init bean
        Object bean = SpringContextUtil.getBean(testcase.getComponentName());
    
        Class clazz;
        if (AopUtils.isAopProxy(bean)){
            clazz = AopUtils.getTargetClass(bean);
        } else  {
            clazz = bean.getClass();
        }
    
        Method method = getMethodByName(clazz, testcase.getEntryPoint());
    
        TestcaseRuntimeInstance testcaseRuntimeInstance
                = TestcaseRuntimeInstance.builder()
                .testcase(testcase)
                .bean(bean)
                .clazz(clazz)
                .method(method)
                .status(RunStatus.RUNNING.getDesc())
                .build();

        testcaseRuntimeInstance.setIterationList(testcase.getIterationList());
        testcaseRuntimeInstance.getIterationList().stream().forEach(iteration -> {
        
//            List<Checkpoint> checkpointList = new ArrayList<>();
//            testcase.getCheckpointList().stream().forEach(cp -> {
//                try {
//                    checkpointList.add(cp.clone());
//                } catch (CloneNotSupportedException e) {
//                    e.printStackTrace();
//                }
//            });
//            iteration.setCheckpointList(checkpointList);
            
            iteration.setStatus(RunStatus.RUNNING.getDesc());
        });
        
        return testcaseRuntimeInstance;
        
    }
    
    
    private TestcaseRuntimeInstance invokeTestcase(TestcaseRuntimeInstance testcaseRuntimeInstance, boolean isParallel,  boolean isAsync) throws  Exception {
        
        System.out.println("Main request Thread id ---> " + Thread.currentThread().getId());
    
        if (isAsync) {
            Future<TestcaseRuntimeInstance> mainTask = testExecutionManager.asyncRun(testcaseRuntimeInstance, isParallel);
        } else  {
            testExecutionManager.syncRun(testcaseRuntimeInstance,isParallel);
        }

        return testcaseRuntimeInstance;
        
    }
    
    
    
    
    @Override
    public TestcaseRuntimeInstance invokeTestCase(Testcase testcase, boolean isParallel, int logLevel) throws Exception{
        //by default is run by async
        TestcaseRuntimeInstance runtimeInstance = prepareTestcaseRuntimeInstance(testcase);
        runtimeInstance.setLogLevel(LogLevel.getLogLevel(logLevel));
        
        return invokeTestcase(runtimeInstance, isParallel, true);
    }
    

    private Method getMethodByName(Class clazz, String methodName){
        Method[] methods = clazz.getMethods();
        Method method = null;
        for (Method m: methods ){
            if (m.getName().equals(methodName)){
                method = m;
                break;
            }
        }
        if (method == null) {
            throw new RuntimeException("Testcase entry point [" + methodName + "] not found");
        }
        return method;
    }
    
    @Async(Constant.TASK_DISPATCH_THREAD_POOL_NAME)
    @Override
    public Future<TestcaseRuntimeInstance> asyncRunTestcaseFromSuite(Testcase testcase, TestSuite testSuite) throws Exception {
        TestcaseRuntimeInstance testcaseRuntimeInstance = runTestcaseFromSuite(testcase, testSuite);
        return new AsyncResult<>(testcaseRuntimeInstance);
    }
    
    @Override
    public TestcaseRuntimeInstance syncRunTestcaseFromSuite(Testcase testcase, TestSuite testSuite) throws Exception {
        TestcaseRuntimeInstance testcaseRuntimeInstance =  runTestcaseFromSuite(testcase, testSuite);
        while (true) {
            if (!testcaseRuntimeInstance.getStatus().equals(RunStatus.RUNNING.getDesc())){
                break;
            } else  {
                Thread.sleep(200);
            }
        }
        return testcaseRuntimeInstance;
    }
    
    
    private TestcaseRuntimeInstance runTestcaseFromSuite(Testcase testcase, TestSuite testSuite) throws Exception {
    
        
        String componentName = testcase.getComponentName();
        String entryPoint = testcase.getEntryPoint();
        Testcase testcaseWithDetailInfo = getTestCase(componentName, entryPoint);
        testcaseWithDetailInfo.setEnvironmentVariable(testSuite.getEnvironmentVariable());
        TestcaseRuntimeInstance testcaseRuntimeInstance = prepareTestcaseRuntimeInstance(testcaseWithDetailInfo);
        testcaseRuntimeInstance.setLogLevel(LogLevel.INFO);
        
        //init report
        ExtentTest extentTest = testSuite.getExtentReportManager()
                .startTestcase(testcaseWithDetailInfo.getName(), testcaseWithDetailInfo.getComponentName() + "." + testcaseWithDetailInfo.getEntryPoint());
    
        extentTest.info("initialized");
        testcaseRuntimeInstance.setExtentTest(extentTest);
       

        testcaseRuntimeInstance =  invokeTestcase(testcaseRuntimeInstance, testSuite.isIterationParallel(), true);
    
        testSuite.getTestcaseRuntimeInstanceList().add(testcaseRuntimeInstance);
        return testcaseRuntimeInstance;
        
    }
    
    
    
    
}
