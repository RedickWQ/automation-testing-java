package com.patsnap.automation.base;

import com.patsnap.automation.context.TestContextManager;
import com.patsnap.automation.entity.Checkpoint;
import com.patsnap.automation.report.ScreenShotUtil;
import com.patsnap.automation.utils.JsonUtil;

import java.time.LocalDateTime;

/**
 * Created by liuyikai on 2017/9/8.
 */
public class CheckpointWrapper {
    
    private Checkpoint checkpoint;
    
//    private Object actualValueObject;
    
    
    CheckpointWrapper(String name) {
        this.checkpoint = new Checkpoint();
        checkpoint.setName(name);
        
        if (null != TestContextManager.getContext() && null != TestContextManager.getContext().getIteration()){
            TestContextManager.getContext().getIteration().getCheckpointList().add(checkpoint);
        }
    }
    public void pass(){
        this.checkpoint.setPassed(true);
        this.checkpoint.setEvaluated(true);
        this.checkpoint.setTimeStamp(LocalDateTime.now());
        this.checkpoint.flush();
    }
    
    public void fail(){
        this.checkpoint.setPassed(false);
        this.checkpoint.setEvaluated(true);
        this.checkpoint.setTimeStamp(LocalDateTime.now());
        this.checkpoint.flush();
    }
    
    public void isPassed(boolean result){
        this.checkpoint.setPassed(result);
        this.checkpoint.setEvaluated(true);
        this.checkpoint.setTimeStamp(LocalDateTime.now());
        this.checkpoint.flush();
    }
  
    public CheckpointWrapper takeScreenshot() {
        this.checkpoint.setScreenshotUrl(ScreenShotUtil.takeScreenShot(this.checkpoint.getName()));
        return this;
    }
    
    public void flush(){
        
        this.checkpoint.setActualValue("");
        this.checkpoint.setPassed(true);
        this.checkpoint.setEvaluated(true);
        this.checkpoint.setTimeStamp(LocalDateTime.now());
        this.checkpoint.flush();
    }
    
    
    public <T extends Object> ValidationWrapper assume(T actual){
        this.checkpoint.setActualValueObj(actual);
        this.checkpoint.setActualValue(parseToString(actual));
        
        return new ValidationWrapper(this.checkpoint);
    }
    
    
    private <T extends Object> String parseToString(T actual) {
        
        if (actual == null) {
            return "null";
        } else  if (actual instanceof  String){
            return (String) actual;
        }else  if (actual.getClass().isPrimitive()) {
            return String.valueOf(actual);
        } else  {
            return JsonUtil.toJsonString(actual);
        }
    }
    
    
    
    
    
    

    
}
