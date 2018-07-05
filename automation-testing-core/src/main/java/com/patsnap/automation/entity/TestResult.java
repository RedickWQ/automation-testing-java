package com.patsnap.automation.entity;

import java.io.PrintWriter;
import java.io.StringWriter;

import lombok.Data;

/**
 * Created by liuyikai on 2017/9/6.
 */

/**
 * one test result for each iteration
 * @param <T>
 */
@Data
public class TestResult<T> {
    public TestResult() {
    
    }
    
    public TestResult(T obj){
        this.testCaseReturnObject = obj;
        
    }
    
    public TestResult(T obj, boolean success){
        this.testCaseReturnObject = obj;
        this.success = success;
    }

    
    public void setResult(T obj){
        this.testCaseReturnObject = obj;
    }
    
    private T testCaseReturnObject;
    
    private boolean success = true;
    
    private String stackTrace;
    
    private String errorMessage;
    
    
    
    public void setError(Exception e) {
        this.setErrorMessage(e.getMessage());
        this.setSuccess(false);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        this.setStackTrace(sw.toString());
    }
}
