package com.patsnap.automation.enums;

/**
 * @author liuyikai(Alex)
 * @date 2017/12/5
 */
public enum AuthType {
    BASIC("Basic"),
    BEARER("Bearer");
    
    
    private String prefix;
    
    private AuthType (String prefix){
        this.prefix = prefix;
    }
    
    public String getPrefix(){
        return this.prefix;
    }
    
}
