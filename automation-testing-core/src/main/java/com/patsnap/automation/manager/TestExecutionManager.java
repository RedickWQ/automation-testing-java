package com.patsnap.automation.manager;

import com.patsnap.automation.entity.TestcaseRuntimeInstance;

import java.util.concurrent.Future;

/**
 * Created by liuyikai on 2017/9/12.
 */
public interface TestExecutionManager {
    
    Future<TestcaseRuntimeInstance> asyncRun(TestcaseRuntimeInstance testcaseRuntimeInstance, boolean isParallel) throws Exception;
    
    TestcaseRuntimeInstance syncRun(TestcaseRuntimeInstance testcaseRuntimeInstance, boolean isParallel) throws Exception;


}
