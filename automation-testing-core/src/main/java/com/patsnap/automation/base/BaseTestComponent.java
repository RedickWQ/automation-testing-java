package com.patsnap.automation.base;

/**
 * Created by liuyikai on 2017/9/7.
 */

public abstract  class BaseTestComponent {


    protected CheckpointWrapper checkpoint(String name){
    
        CheckpointWrapper checkpointWrapper = new CheckpointWrapper(name);
        
        return checkpointWrapper;
    }
    

}
