package com.patsnap.automation.manager.impl;

import com.patsnap.automation.entity.TestcaseRuntimeInstance;
import com.patsnap.automation.enums.RunStatus;
import com.patsnap.automation.manager.IterationExecutionManager;
import com.patsnap.automation.manager.TestExecutionManager;
import com.patsnap.automation.contants.Constant;
import com.patsnap.automation.entity.Iteration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by liuyikai on 2017/9/12.
 */
@Service
public class TestExecutionManagerImpl implements TestExecutionManager {
    

    
    @Autowired
    IterationExecutionManager iterationExecutionManager;
    
    @Override
    public TestcaseRuntimeInstance syncRun(TestcaseRuntimeInstance testcaseRuntimeInstance, boolean isParallel) throws Exception {
        return run(testcaseRuntimeInstance, isParallel);
    }
    
    @Async(Constant.TASK_DISPATCH_THREAD_POOL_NAME)
    @Override
    public Future<TestcaseRuntimeInstance> asyncRun(TestcaseRuntimeInstance testcaseRuntimeInstance, boolean isParallel) throws Exception {
    
        System.out.println("Main task Thread id ---> " + Thread.currentThread().getId());
        
        testcaseRuntimeInstance = run(testcaseRuntimeInstance, isParallel);
        
        return new AsyncResult<>(testcaseRuntimeInstance);
    }
    
    
    private TestcaseRuntimeInstance run(TestcaseRuntimeInstance testcaseRuntimeInstance, boolean isParallel)throws Exception {
    
        
        testcaseRuntimeInstance.setStartTime(LocalDateTime.now());
    
    
        if (isParallel){
            runInParallel(testcaseRuntimeInstance);
        } else  {
            runInSequence(testcaseRuntimeInstance);
        }
        
        evaluateStatus(testcaseRuntimeInstance);
        
        
        testcaseRuntimeInstance.setEndTime(LocalDateTime.now());
        testcaseRuntimeInstance.setTotalDuration(Duration.between(testcaseRuntimeInstance.getStartTime(),testcaseRuntimeInstance.getEndTime()));
        testcaseRuntimeInstance.getExtentTest().info("Total duration: " + testcaseRuntimeInstance.getTotalDuration().toMillis() + " ms");
        
        
        return testcaseRuntimeInstance;
    }
    
    private void evaluateStatus(TestcaseRuntimeInstance testcaseRuntimeInstance){
       Iteration failedIteration =  testcaseRuntimeInstance.getIterationList().stream().filter(iteration ->
                iteration.getStatus().equals(RunStatus.FAILED.getDesc())
                || iteration.getStatus().equals(RunStatus.ERROR.getDesc())).findAny().orElse(null);
        
       if (null != failedIteration){
           testcaseRuntimeInstance.setStatus(RunStatus.FAILED.getDesc());
       } else  {
           testcaseRuntimeInstance.setStatus(RunStatus.PASSED.getDesc());
       }
    }
    
 
    private void runInParallel(TestcaseRuntimeInstance testcaseRuntimeInstance) throws Exception {
        
        List<Future<Iteration>> taskList = new ArrayList<>();
        for (Iteration iteration: testcaseRuntimeInstance.getIterationList()){
        
            Future<Iteration> task = iterationExecutionManager.asyncRunIteration(testcaseRuntimeInstance, iteration);
            taskList.add(task);
        
        }
        
        for (Future<Iteration> task : taskList) {
            try {
                while (true) {
                    if (task.isDone() && !task.isCancelled()) {
                        break;
                    } else {
                        Thread.sleep(100);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        
        }
    }
    
    private void runInSequence(TestcaseRuntimeInstance testcaseRuntimeInstance) throws Exception {
        
        try {
            for (Iteration iteration : testcaseRuntimeInstance.getIterationList()) {
                Iteration result = iterationExecutionManager.runIteration(testcaseRuntimeInstance, iteration);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    
    
}
