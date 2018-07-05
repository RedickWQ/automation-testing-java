package com.patsnap.automation.enums;

/**
 *
 * @author liuyikai
 * @date 2017/9/6
 */
public enum TestcaseType {
    
    /**
     *
     */
    
    WEB_API_TEST(2, "WebApiTest"),
    WEB_GUI_TEST(3, "WebGuiTest");
    
    
    private int code;
    private String description;
    
    TestcaseType(int code, String description){
        this.code = code;
        this.description = description;
        
    }
    
    public int getCode(){
        return this.code;
    }
    
    @Override
    public String toString(){
        return this.description;
    }
    
    
}
