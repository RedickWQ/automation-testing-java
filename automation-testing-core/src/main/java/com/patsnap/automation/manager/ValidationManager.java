package com.patsnap.automation.manager;

import com.patsnap.automation.entity.Checkpoint;
import com.patsnap.automation.entity.Iteration;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by liuyikai on 2017/9/7.
 */
public interface ValidationManager {
    
    void validate(Iteration iteration);
    
    List<Checkpoint> getCheckpointList(Method method) throws Exception;
    
    
}
