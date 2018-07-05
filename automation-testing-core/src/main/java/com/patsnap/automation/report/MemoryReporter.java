package com.patsnap.automation.report;

import com.patsnap.automation.entity.Checkpoint;
import com.patsnap.automation.entity.Iteration;
import com.patsnap.automation.log.LogItem;

import com.aventstack.extentreports.Status;

import com.patsnap.automation.log.LogLevel;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * Created by  Liuyikai (Alex) on 2017/10/9.
 */
@Data
public class MemoryReporter implements Reporter {
    
    private Iteration iteration;
    
    protected LogLevel logLevel;
    
    
    @Override
    public void logEvent(LogLevel level, String content) {
        
        if (level.getLevel() >= logLevel.getLevel()) {
            LogItem logItem = new LogItem();
            logItem.setTimestamp(LocalDateTime.now());
            logItem.setContent(content);
            logItem.setLevel(level);
    
            if (iteration != null) {
                iteration.getLogItemList().add(logItem);
            }
        }

    }
    
    @Override
    public void logError(Throwable t) {
        if (logLevel.getLevel() <= LogLevel.ERROR.getLevel()) {
            LogItem logItem = new LogItem();
            logItem.setTimestamp(LocalDateTime.now());
            String errName = t.getClass().getSimpleName();
            String errMessage = !StringUtils.isEmpty(t.getMessage())? t.getMessage(): "No err message.";
            logItem.setContent(errName + ": " + errMessage );
            logItem.setLevel(LogLevel.ERROR);
    
            if (iteration != null) {
                iteration.getLogItemList().add(logItem);
            }
        }
 
    }
    
    @Override
    public void logFatal(Throwable t) {
        
        LogItem logItem = new LogItem();
        logItem.setTimestamp(LocalDateTime.now());
        String errName = t.getClass().getSimpleName();
        String errMessage = !StringUtils.isEmpty(t.getMessage())? t.getMessage(): "No err message.";
        logItem.setContent(errName + ": " + errMessage );
        logItem.setLevel(LogLevel.FATAL);
    
        if (iteration != null) {
            iteration.getLogItemList().add(logItem);
        }
    
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
}
