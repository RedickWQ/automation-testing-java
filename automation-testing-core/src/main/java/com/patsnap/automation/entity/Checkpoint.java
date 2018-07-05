package com.patsnap.automation.entity;

import com.patsnap.automation.report.Reporter;
import com.patsnap.automation.context.TestContextManager;
import com.patsnap.automation.exception.ExecutionInterruptException;

import com.alibaba.fastjson.annotation.JSONField;
import com.aventstack.extentreports.Status;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.Setter;

/**
 *
 * @author liuyikai
 * @date 2017/9/6
 */
@Data
public class Checkpoint implements  Cloneable{

    private String name;
    
    private String jsonPath;
    
    private int ruleType;
    
//    private String ruleDescription;
    
//    private String expectedValue;
    
    
    
    
    private String actualValue;
    
    
    @JSONField(serialize = false)
    private Object actualValueObj;
    
    
    //in case
    private String errorMessage;
    
    @Setter
    private boolean isEvaluated = false;
    
    /**
     * pass of fail
     * pass: true,
     * fail: false
     */
    @Setter
    private boolean isPassed;
    
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timeStamp;
    
    
    
    private String screenshotUrl;
    
    
    
    public boolean isNotEvaluated(){
        return !isEvaluated;
    }
    
    
    @Override
    public Checkpoint clone() throws  CloneNotSupportedException{
        return (Checkpoint) super.clone();
    }
    
    
    public void flush(){
        ////todo to be optimized for detail report content
        Reporter reporter = TestContextManager.getContext().getReporter();
        

        
        
        reporter.logCheckpoint(this);
        
        if (!this.isPassed && true ) {
            throw new ExecutionInterruptException("Checkpoint [" + name + "] failed and stop execution");
        }
    }
    
    
    
    
}
