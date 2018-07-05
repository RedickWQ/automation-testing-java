package com.patsnap.automation.log;

import com.patsnap.automation.report.Reporter;

/**
 * @author liuyikai(Alex)
 * @date 2017/12/6
 */
public class Logger {
    
    private Reporter reporter;
    
   
    
    public Logger(Reporter reporter)
    {
        this.reporter = reporter;
        
    }
    
    
    public void debug(String content){
        reporter.logEvent(LogLevel.DEBUG, content);
    }
    
    public void info(String content){
        reporter.logEvent(LogLevel.INFO, content);
    }
    
    public void warning(String content){
        reporter.logEvent(LogLevel.WARNING, content);
    }
    
    public void error(String content){
        reporter.logEvent(LogLevel.ERROR, content);
    }
    
    public void error(String content, Throwable t){
        reporter.logError(t);
    }
    
    public void fatal(String content){
        reporter.logEvent(LogLevel.FATAL, content);

    }
    
    public void fatal(String content, Throwable t){
        reporter.logFatal(t);
    }
}
