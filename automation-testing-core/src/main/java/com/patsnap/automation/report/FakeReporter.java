package com.patsnap.automation.report;

import com.aventstack.extentreports.Status;
import com.patsnap.automation.entity.Checkpoint;
import com.patsnap.automation.entity.Iteration;
import com.patsnap.automation.log.LogLevel;

/**
 * Created by  Liuyikai (Alex) on 2017/11/13.
 */
public class FakeReporter  implements  Reporter{
    
    
    @Override
    public void setLogLevel(LogLevel level) {
    
    }
    
    @Override
    public void setIteration(Iteration iteration) {
    
    }
    
    @Override
    public void logCheckpoint(Status status, String content) {
    
    }
    
    @Override
    public void logCheckpoint(Status status, Throwable t) {
    
    }
    
    @Override
    public void logCheckpoint(Checkpoint checkpoint) {
    
    }
    
    @Override
    public void logEvent(LogLevel level, String content) {
        System.out.println(content);
    }
    
    
    @Override
    public void logError(Throwable t) {
        t.printStackTrace();
    }
    
    @Override
    public void logFatal(Throwable t) {
        t.printStackTrace();
    }
}
