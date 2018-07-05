package com.patsnap.automation.report;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.MediaEntityModelProvider;
import com.patsnap.automation.contants.Constant;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import com.patsnap.automation.entity.Checkpoint;
import com.patsnap.automation.log.LogLevel;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * Created by  Liuyikai (Alex) on 2017/10/23.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ExtentReporterEx extends MemoryReporter implements Reporter {
    
    private ExtentTest iterationNode;
    
    public ExtentReporterEx(ExtentTest parentNode){
        this.iterationNode = parentNode.createNode("Iteration-" + Thread.currentThread().getId(), "");
    }
    
    @Async(Constant.REPORT_THREAD_POOL_NAME)
    @Override
    public void logEvent(LogLevel level, String content){
        
        super.logEvent(level, content);
    
        if (level.getLevel() >= logLevel.getLevel()) {
    
            switch (level) {
                case DEBUG:
                    iterationNode.debug(content);
                    break;
                case INFO:
                    iterationNode.info(content);
                    break;
                case WARNING:
                    iterationNode.warning(content);
                    break;
                case ERROR:
                    iterationNode.error(content);
                    break;
                case FATAL:
                    iterationNode.fatal(content);
                default:
                    iterationNode.info(content);
                    break;
            }
    
        }
    }
    
    @Async(Constant.REPORT_THREAD_POOL_NAME)
    @Override
    public void logError(Throwable t ){
        super.logError(t);
        if (logLevel.getLevel() <= LogLevel.ERROR.getLevel()) {
            iterationNode.error(t);
        }
    }
    
    @Async(Constant.REPORT_THREAD_POOL_NAME)
    @Override
    public void logFatal(Throwable t ){
        super.logFatal(t);
        iterationNode.fatal(t);
    }
    
    
    
    @Deprecated
    @Async(Constant.REPORT_THREAD_POOL_NAME)
    @Override
    public void logCheckpoint(Status status, String content) {
        iterationNode.log(status, content);
    }
    
    @Deprecated
    @Async(Constant.REPORT_THREAD_POOL_NAME)
    @Override
    public void logCheckpoint(Status status, Throwable t) {
        iterationNode.log(status, t);
    }
    
    
    
    @Async(Constant.REPORT_THREAD_POOL_NAME)
    @Override
    public void logCheckpoint(Checkpoint checkpoint) {
        Status  status = checkpoint.isPassed()? Status.PASS : Status.FAIL;
        String content =  checkpoint.getName() + ":  "+ checkpoint.getActualValue();
        if (checkpoint.getScreenshotUrl() != null) {
            try {
                MediaEntityModelProvider mediaModel = MediaEntityBuilder.createScreenCaptureFromPath(checkpoint.getScreenshotUrl()).build();
                iterationNode.log(status, content, mediaModel);
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        } else {
            iterationNode.log(status, content);
        }
        
    }
    
    
}
