package com.patsnap.automation.utils;

import com.patsnap.automation.context.TestContextManager;

/**
 *
 * @author Liuyikai (Alex)
 * @date 2017/10/27
 */
public class GlobalVariableUtil {
    
    
    public static Object get(String key){
        return TestContextManager.getContext().getGlobalVariables().get(key);
    }
    
    
    public static void set(String key, Object value){
         TestContextManager.getContext().getGlobalVariables().put(key, value);
    }
    
    
    
}
