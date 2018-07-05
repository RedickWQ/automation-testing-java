package com.patsnap.automation.enums;

/**
 * Created by liuyikai on 2017/9/7.
 */

/**
 * run passed of single iteration
 * @author liuyikai
 */
public enum RunStatus {
    
    /**
     *
     */
    PASSED(0, "Passed"),
    FAILED(1, "Failed"),
    WARNING(2, "Warning"),
    ERROR(3, "Error"),
    RUNNING(4, "Running");
    
    
    private int code;
    private String description;
    
    RunStatus(int code, String description){
        this.code = code;
        this.description = description;
    }
    
    public int getCode(){
        return this.code;
    }
    
    public String getDesc(){
        return this.description;
    }
    
    
}
