package com.patsnap.automation.enums;

/**
 * Created by liuyikai on 2017/9/8.
 */
public enum ValidationRule {

    OBJECT_EQUALS(0, "ObjectEquals", "equals"),
    REG_MATCHES(1, "RegMatches", "matches"),
    CONTAINS(2, "Contains", "contains"),
    NUMBER_EQUALS(3, "NumberEquals", "=="),
    NUMBER_NOT_EQUALS(4, "NumberNotEquals", "!="),
    GREATER_THAN(5, "GreaterThan", ">"),
    GREATER_THAN_OR_EQUALS(6, "GreaterThanOrEquals", ">="),
    LESS_THAN(7, "LessThan", "<"),
    LESS_THAN_OR_EQUALS(8, "LessThanOrEquals", "<=");
    
    
    private int code;
    
    private String description;
    
    private String operatorExpression;
    
    ValidationRule(int code, String description, String operatorExpression){
        this.code = code;
        this.description = description;
        this.operatorExpression = operatorExpression;
    }

    public int getCode() {
        return this.code;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public String getOperatorExpression(){
        return this.operatorExpression;
    }
    
    public static ValidationRule getRuleByExpression(String operatorExpression){
    
        ValidationRule[] allRules = ValidationRule.class.getEnumConstants();
        for (ValidationRule rule : allRules){
            if (rule.operatorExpression.equals(operatorExpression)){
                return rule;
            }
            
        }
        
        throw new RuntimeException("Validation rule cannot be found by operation : [" + operatorExpression + "]");
//        return ValidationRule.OBJECT_EQUALS;
    }
    
    public static ValidationRule getRuleByCode(int code) {
        ValidationRule[] allRules = ValidationRule.class.getEnumConstants();
        for (ValidationRule rule : allRules){
            if (rule.code == code){
                return rule;
            }
        
        }
        //todo ,  if not found, set it as objectEqual
        throw new RuntimeException("Validation rule cannot be found by code: [" + code+ "]");
    }
    
    
}
