package com.patsnap.automation.manager.impl;

import com.patsnap.automation.entity.TestResult;
import com.patsnap.automation.enums.TestcaseType;
import com.patsnap.automation.gui.framework.GuiTestContext;
import com.patsnap.automation.log.LogLevel;
import com.patsnap.automation.manager.IterationExecutionManager;
import com.patsnap.automation.annotation.AfterTestcase;
import com.patsnap.automation.annotation.BeforeTestcase;
import com.patsnap.automation.annotation.TestParam;
import com.patsnap.automation.contants.Constant;
import com.patsnap.automation.context.TestContext;
import com.patsnap.automation.context.TestContextManager;
import com.patsnap.automation.entity.Iteration;
import com.patsnap.automation.entity.TestParameter;
import com.patsnap.automation.entity.TestcaseRuntimeInstance;
import com.patsnap.automation.enums.RunStatus;
import com.patsnap.automation.exception.ExecutionInterruptException;
import com.patsnap.automation.manager.ValidationManager;
import com.patsnap.automation.report.MemoryReporter;
import com.patsnap.automation.utils.SpringContextUtil;
import com.patsnap.automation.utils.TestDataMappingUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 *
 * @author liuyikai
 * @date 2017/9/13
 */
@Service
public class IterationExecutionManagerImpl implements IterationExecutionManager {
    
    @Autowired
    ValidationManager validationManager;
    
    @Override
    @Async(Constant.EXECUTION_THREAD_POOL_NAME)
    public Future<Iteration> asyncRunIteration(TestcaseRuntimeInstance testcaseRuntimeInstance, Iteration iteration) throws Exception {
        
        System.out.println("Async run Thread id ---> " + Thread.currentThread().getId());
        
        iteration = run(testcaseRuntimeInstance, iteration);
        return new AsyncResult<>(iteration);
        
    }
    
    @Override
    public Iteration runIteration(TestcaseRuntimeInstance testcaseRuntimeInstance, Iteration iteration) throws Exception {
        return run(testcaseRuntimeInstance, iteration);
    }
    
    
    
    private Iteration run(TestcaseRuntimeInstance testcaseRuntimeInstance, Iteration iteration) {
        System.out.println("Sequential run Thread id ---> " + Thread.currentThread().getId());
    
    
        //init context
        TestContext context;
        if (testcaseRuntimeInstance.getTestcase().getCaseType() == TestcaseType.WEB_GUI_TEST.getCode()){
            context  = new GuiTestContext();
        } else  {
            context = new TestContext();
        }
        
        iteration.setThreadId(Thread.currentThread().getId());
        context.setIteration(iteration);
        
        if (testcaseRuntimeInstance.getExtentTest() != null) {

            context.setReporter(SpringContextUtil.getBean("extentReporterEx", testcaseRuntimeInstance.getExtentTest()));
        } else  {
            context.setReporter(new MemoryReporter());
        }
        context.getReporter().setIteration(iteration);
        context.getReporter().setLogLevel(testcaseRuntimeInstance.getLogLevel());
        
        
        context.setEnvironmentVariable(testcaseRuntimeInstance.getTestcase().getEnvironmentVariable());
        TestContextManager.setContext(context);
    
        iteration.setStartTime(LocalDateTime.now());
        iteration.setLogItemList(new ArrayList<>());
    
        if (CollectionUtils.isEmpty(iteration.getCheckpointList())){
            iteration.setCheckpointList(new ArrayList<>());
        }
        
        //create new bean for each iteration
        Object bean = SpringContextUtil.getBean(testcaseRuntimeInstance.getTestcase().getComponentName());
        
        //Exec Before test
        runBeforeTestcase(bean, iteration);
        
        //run
        Method testcaseMethod = testcaseRuntimeInstance.getMethod();
        try {
            Object[] args = getArgsForTestMethod(testcaseMethod, iteration.getTestParameterList());
            TestResult result = (TestResult) testcaseMethod.invoke(bean, args);
            iteration.setTestcaseResult(result);
        } catch (InvocationTargetException e) {
        
            if (e.getTargetException() instanceof ExecutionInterruptException) {
                iteration.setTestcaseResult(new TestResult("Stopped on checkpoint failure.", false));

            } else  {
                iteration.setError(e.getTargetException());
                e.printStackTrace();
                context.getReporter().logError(e.getTargetException());
            }
    

            
        } catch (Exception e) {
            iteration.setError(e);
            context.getReporter().logError(e);
        } finally {

        }
        
        //validate
        validationManager.validate(iteration);
        
        //Exec after test
        runAfterTestcase(bean, iteration);
        
        //tear down
        iteration.setEndTime(LocalDateTime.now());
        iteration.setDuration(Duration.between(iteration.getStartTime(),iteration.getEndTime()));
        
        context.getReporter().logEvent(LogLevel.INFO, "Total duration: " + iteration.getDuration().toMillis() + " ms");
        TestContextManager.remove();
        return iteration;
    }
    
    
    private Object[] getArgsForTestMethod(Method method, List<TestParameter> testParameterList){
        Parameter[] methodParameters = method.getParameters();
        Object [] args = new Object[methodParameters.length];
        
        for (int i = 0; i < methodParameters.length; i++) {
            Parameter p = methodParameters[i];
            TestParam paramInfo = p.getAnnotation(TestParam.class);
            TestParameter testParameter = testParameterList.stream().filter(tp -> tp.getName().equals(paramInfo.name())).findFirst().orElse(null);
            
            String parameterStringValue;
            if (testParameter != null) {
                parameterStringValue = testParameter.getValue();
            } else  {
                //use default name defined in annotation
                parameterStringValue = paramInfo.value();
            }
            
            Object arg = TestDataMappingUtil.convertTestParameterToObject(parameterStringValue, p);
            args[i] = arg;
        }
        return args;
    }
    
    private void runBeforeTestcase(Object bean, Iteration iteration){
        Method beforeTestcase = SpringContextUtil.getMethodByAnnotation(bean, BeforeTestcase.class);
        
        if (beforeTestcase != null) {
            try {
                beforeTestcase.setAccessible(true);
                beforeTestcase.invoke(bean);
            } catch(InvocationTargetException e) {
                iteration.setError((Exception) e.getTargetException());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
    
        }
   
    }
    
    private void runAfterTestcase(Object bean, Iteration iteration){
        Method afterTestcaseMethod = SpringContextUtil.getMethodByAnnotation(bean, AfterTestcase.class);
        
        if (afterTestcaseMethod != null ) {
            AfterTestcase afterTestcase = afterTestcaseMethod.getAnnotation(AfterTestcase.class);
            if (!iteration.getStatus().equals(RunStatus.PASSED.getDesc()) &&  !afterTestcase.alwaysRun()) {
                return;
            }
            
            try {
                afterTestcaseMethod.setAccessible(true);
                afterTestcaseMethod.invoke(bean);
            } catch(InvocationTargetException e) {
                iteration.setError((Exception) e.getTargetException());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        
    }
    
}
