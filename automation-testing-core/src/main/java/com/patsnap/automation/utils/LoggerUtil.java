package com.patsnap.automation.utils;

import com.patsnap.automation.log.Logger;
import com.patsnap.automation.context.TestContextManager;
import com.patsnap.automation.report.Reporter;

/**
 * @author liuyikai(Alex)
 * @date 2017/12/6
 */
public class LoggerUtil {
    
    public static Logger getLogger(){
        
        Reporter reporter = TestContextManager.getContext().getReporter();
        return new Logger(reporter);
    }
    
}
