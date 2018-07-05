package com.patsnap.automation.utils;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Parameter;

/**
 * Created by liuyikai on 2017/9/4.
 */
public class TestDataMappingUtil {

    public static Object convertTestParameterToObject(String value, Parameter p){
    
        Object result;
        Class parameterType = p.getType();
    
        if(parameterType.equals(String.class)){
            return value;
        }
    
        //todo to be modified to enable more parameter types
        result = JSON.parseObject(value, parameterType);
       
        
//
//        switch (parameterType.getName()){
//            case "java.lang.String":
//
//                result =  value;
//                break;
//            case "java.lang.Long":
//            case "long":
//                result =   Long.valueOf(value);
//                break;
//            case "java.lang.Boolean":
//            case "boolean":
//                result =  Boolean.valueOf(value);
//                break;
//            case "java.lang.Integer":
//                result = Integer.valueOf(value);
//                break;
//            default:
//                result = JSON.parseObject(value, parameterType);
//                break;
//        }
        
        return result;
       
    }
    
    
    
    
    
    
}
