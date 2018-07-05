package com.patsnap.automation.contants;

/**
 *
 * @author Liuyikai (Alex)
 * @date 2017/10/16
 */
public class Constant {
    
    
    public static final  String TASK_DISPATCH_THREAD_POOL_NAME = "taskDispatchAsyncExecutor";
    
    //only for iteration execution
    public static final  String EXECUTION_THREAD_POOL_NAME = "testcaseExecutionAsyncExecutor";
    
    //Only for logging and reporting
    public static final String REPORT_THREAD_POOL_NAME = "reportAsyncExecutor";
    
    public static final String TEST_SUITE_ROOT_DIR = "./TestConfiguration/TestSuite/";
    
    public static final String TEST_DATA_ROOT_DIR = "./TestConfiguration/TestData/";
    
    public static final String ENVIRONMENT_VARIABLES_ROOT_DIR = "./TestConfiguration/EnvironmentVariable/";
    
    public static final String REPORT_ROOT_DIR = "./AutomationReport/";
    
    public static final String SCREENSHOT_DIR =  "Screenshot/";
    
    public static final String SCREENSHOT_DIR_PATH = REPORT_ROOT_DIR + SCREENSHOT_DIR;
}
