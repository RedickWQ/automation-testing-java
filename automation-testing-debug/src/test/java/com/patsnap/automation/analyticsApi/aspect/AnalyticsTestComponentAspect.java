package com.patsnap.automation.analyticsApi.aspect;

import com.patsnap.automation.analyticsApi.endpoint.authentication.LoginEndpoint;
import com.patsnap.automation.context.TestContextManager;
import com.patsnap.automation.infrastructure.StatefulRestTemplate;


import com.patsnap.automation.utils.TestEnvUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Created by liuyikai on 2017/9/26.
 */
@Aspect
@Component
@Order(2)
public class AnalyticsTestComponentAspect {
    
    @Autowired
    LoginEndpoint loginEndpoint;

    
    @Pointcut("@annotation(com.patsnap.automation.annotation.AutomationTestCase) " +
            "&& execution(* com.patsnap.automation.analyticsApi..*.*(..))")
    public void pointCut(){
    }
    
    
    @Around("pointCut()")
    public Object precondition(ProceedingJoinPoint joinPoint) throws Throwable {
        
        System.out.println("====before AnalyticsTestComponentAspect===");
    
        StatefulRestTemplate template = (StatefulRestTemplate) TestContextManager.getContext().getRestTemplate();
        if (null == template) {
    
            String redirectUrl = TestEnvUtil.getPropertyConfigValueByKey("redirectUrl");
            
            MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
            params.add("from", "analytics");
            params.add("redirect_uri", "http://dev-analytics.zhihuiya.com/initSession");
            
            //todo
            
            
            params.add("client_id", "10");
            params.add("username", "baijing@patsnap.com");
            params.add("password", "1111111b");
            params.add("rememberMe", "0");
            
            loginEndpoint.login(params);
    
        }
        
        Object result =  joinPoint.proceed();
    
        System.out.println("====after AnalyticsTestComponentAspect===");
        
        return result;
    }
}
