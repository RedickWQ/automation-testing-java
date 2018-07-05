package com.patsnap.automation.base;

import com.patsnap.automation.entity.Checkpoint;
import org.springframework.util.StringUtils;


import java.time.LocalDateTime;
import java.util.function.Consumer;
import java.util.function.Predicate;
import static org.assertj.core.api.Assertions.*;


/**
 * @author liuyikai(Alex)
 * @date 2017/11/20
 */
public class ValidationWrapper {
    
    private Checkpoint checkpoint;
    
    
    public ValidationWrapper(Checkpoint checkpoint){
        this.checkpoint = checkpoint;
    }
    
    
    
    /**
     * cutomized validation rule ,   boolean type
     * @param condition
     * @param <T>
     */
    public <T extends Object> void that(Predicate<T> condition) {
        try {

            this.checkpoint.setTimeStamp(LocalDateTime.now());
            this.checkpoint.setEvaluated(true);
            
            if (condition.test((T)this.checkpoint.getActualValueObj())){
                
                this.checkpoint.setPassed(true);
                
            } else {
                this.checkpoint.setPassed(false);
            }
            
        } catch (Throwable t) {
            this.checkpoint.setErrorMessage(t.getMessage());
            this.checkpoint.setPassed(false);
        }
        
        this.checkpoint.flush();
    }
    
    
    /**
     * customized validation rule, exception type.
     * @param consumer
     * @param <T>
     */
    public <T extends Object> void that(Consumer<T> consumer){
        try {
            this.checkpoint.setTimeStamp(LocalDateTime.now());
            this.checkpoint.setEvaluated(true);
            consumer.accept((T)this.checkpoint.getActualValueObj());
            this.checkpoint.setPassed(true);
            
        } catch (Throwable t) {
            System.out.println("exception  in checkpoint");
            System.out.println(t.getMessage());
            this.checkpoint.setErrorMessage(t.getMessage());
            this.checkpoint.setPassed(false);
        }
        this.checkpoint.flush();
        
    }
    
    public void isEqualTo(Object object){
    
        try {
           assertThat(this.checkpoint.getActualValueObj()).isEqualTo(object);
           this.checkpoint.setPassed(true);
        } catch (Exception ex){
           this.checkpoint.setPassed(false);
        }
        this.checkpoint.setEvaluated(true);
        this.checkpoint.setTimeStamp(LocalDateTime.now());
    
        this.checkpoint.flush();
    
    }
    
    public void isEqualToIgnoringNullFields (Object object) {
        try {
            assertThat(this.checkpoint.getActualValueObj()).isEqualToIgnoringNullFields(object);
            this.checkpoint.setPassed(true);
        } catch (Exception ex){
            this.checkpoint.setPassed(false);
        }
        this.checkpoint.setEvaluated(true);
        this.checkpoint.setTimeStamp(LocalDateTime.now());
    
        this.checkpoint.flush();
        
    }
    
    
    
    public void isEmpty (){
        boolean result = StringUtils.isEmpty(this.checkpoint.getActualValueObj());
        this.checkpoint.setPassed(result);
        this.checkpoint.setEvaluated(true);
        this.checkpoint.setTimeStamp(LocalDateTime.now());
        this.checkpoint.flush();
    }
    
    public void isNotEmpty() {
        boolean result = !StringUtils.isEmpty(this.checkpoint.getActualValueObj());
        this.checkpoint.setPassed(result);
        this.checkpoint.setEvaluated(true);
        this.checkpoint.setTimeStamp(LocalDateTime.now());
        this.checkpoint.flush();
    }
    
    public void isNull () {
        
        boolean result = this.checkpoint.getActualValueObj() == null ? true: false;
        this.checkpoint.setPassed(result);
        this.checkpoint.setEvaluated(true);
        this.checkpoint.setTimeStamp(LocalDateTime.now());
        this.checkpoint.flush();
    }
    
    public void isNotNull (){
    
        boolean result = this.checkpoint.getActualValueObj() == null ? false: true;
        this.checkpoint.setPassed(result);
        this.checkpoint.setEvaluated(true);
        this.checkpoint.setTimeStamp(LocalDateTime.now());
        this.checkpoint.flush();
    }
    
    
    
    
    
    
    
    
    
}
