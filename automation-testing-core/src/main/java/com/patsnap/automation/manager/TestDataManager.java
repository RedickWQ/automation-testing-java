package com.patsnap.automation.manager;

import com.patsnap.automation.entity.Iteration;

import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by liuyikai on 2017/9/6.
 */
public interface TestDataManager {
    
    
    
     List<Iteration> getIterationList(Method method) throws URISyntaxException;
     
     void saveIterationList(Method method, List<Iteration> iterationList) throws  Exception;
    
}
