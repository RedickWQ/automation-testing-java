package com.patsnap.automation.common;

import java.io.PrintWriter;
import java.io.StringWriter;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author liuyikai
 * @date 2017/9/4
 */

public class AjaxResult {

    
    @Getter @Setter
    private boolean success;
    
    @Getter @Setter
    private Object result;
    
    @Getter @Setter
    private String errMsg;
    
    @Getter @Setter
    private String stackTrace;
    
    
    public void setSuccessResult(Object result){
        this.success = true;
        this.result = result;
    }
    
    public void setError(Exception e){
        this.setSuccess(false);
        this.setErrMsg(e.getMessage()!= null? e.getMessage():"系统错误");
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        this.setStackTrace(sw.toString());
    }
    

}
