package com.patsnap.automation.report;

import com.patsnap.automation.entity.Checkpoint;
import com.patsnap.automation.entity.Iteration;

import com.aventstack.extentreports.Status;
import com.patsnap.automation.log.LogLevel;

/**
 * Created by  Liuyikai (Alex) on 2017/10/9.
 */
public interface Reporter {
    
    void setLogLevel(LogLevel level);
    
    
    void logEvent(LogLevel level, String content);
    
    void logError(Throwable t);
    
    void logFatal(Throwable t);
    
    void setIteration(Iteration iteration);
    
   
    
    void logCheckpoint(Status status, String content);
    void logCheckpoint(Status status, Throwable t);
    
    void logCheckpoint(Checkpoint checkpoint);
    
    
}
