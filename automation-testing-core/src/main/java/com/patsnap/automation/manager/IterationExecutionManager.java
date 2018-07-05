package com.patsnap.automation.manager;

import com.patsnap.automation.entity.Iteration;
import com.patsnap.automation.entity.TestcaseRuntimeInstance;

import java.util.concurrent.Future;

/**
 * Created by liuyikai on 2017/9/13.
 */
public interface IterationExecutionManager {
    
    Future<Iteration> asyncRunIteration(TestcaseRuntimeInstance testcaseRuntimeInstance, Iteration iteration) throws Exception;
    
    
    Iteration runIteration(TestcaseRuntimeInstance testcaseRuntimeInstance, Iteration iteration) throws Exception;

}
