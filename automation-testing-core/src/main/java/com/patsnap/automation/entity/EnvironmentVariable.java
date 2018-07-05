package com.patsnap.automation.entity;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author liuyikai
 * @date 2017/9/20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnvironmentVariable {
    
    
    private String envName;
    
    
    /**
     * for application specific settings
     */
    @Setter
    private Map<String, Map<String,String>> applicationVariables;
    
    
    public EnvironmentVariable(String name) {
        this.envName = name;
        this.applicationVariables =  new HashMap<>();
    }
    
    public String get(String appName, String key){
        
        Map<String, String> variables = applicationVariables.get(appName);
        
        if (variables != null && variables.entrySet().size() > 0 ) {
            return variables.get(key);
        }
        
        return null;
        
    }
    
    public void set(String appName, String key, String val){
        Map<String, String> variables = applicationVariables.get(appName);
        
        if (variables != null){
            variables.put(key, val);
        } else   {
            variables = new HashMap<>();
            variables.put(key, val);
            applicationVariables.put(appName,variables);
        }
        
    }
    
    
    
    
    
    
    
}
