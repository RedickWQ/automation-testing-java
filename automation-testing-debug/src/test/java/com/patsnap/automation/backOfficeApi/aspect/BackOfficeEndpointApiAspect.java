package com.patsnap.automation.backOfficeApi.aspect;

import com.alibaba.fastjson.JSONObject;
import com.patsnap.automation.backOfficeApi.endpoint.authentication.AuthenticationEndpoint;
import com.patsnap.automation.base.BaseEndpoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Field;

/**
 * Created by  Liuyikai (Alex) on 2017/10/17.
 */
@Aspect
@Component
public class BackOfficeEndpointApiAspect {
    
    
    
    @Autowired
    AuthenticationEndpoint authenticationEndpoint;
    
    
    @Pointcut("@annotation(com.patsnap.automation.backOfficeApi.annotation.AuthenticationRequired) "+
            "&& execution(* com.patsnap.automation.backOfficeApi.endpoint..*.*(..))")
    public void pointCut(){
    }
    
    
    
    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
    
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
        params.add("grant_type", "client_credentials");
    
        authenticationEndpoint.addHeader("Authorization", "Basic MTA6YWJj");
        ResponseEntity<JSONObject> authResult = authenticationEndpoint.authenticate(params);
    
        String token = "Bearer " + authResult.getBody().getString("access_token");
        System.out.println("token: " + token);
    
        Object targetObject = joinPoint.getTarget();
        
        Field[] fields =  targetObject.getClass().getDeclaredFields();
        
        for (Field field: fields) {
            field.setAccessible(true);
            Object fieldValue = field.get(targetObject);
            if (fieldValue instanceof BaseEndpoint){
                BaseEndpoint endpoint = (BaseEndpoint) fieldValue;
                endpoint.addHeader("Authorization",  token);
            }
        }
        
        Object result =  joinPoint.proceed();
   
        
        
        return result;
        
    }
    
    
}
