package com.patsnap.automation.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 *
 * @author liuyikai
 * @date 2017/9/5
 */
@Aspect
@Component
@Order(1)
public class TestcaseAspect {
    
    @Pointcut("@annotation(com.patsnap.automation.annotation.AutomationTestCase)")
    
    public void pointCut(){
    }
    
    @Around("pointCut()")
    public Object loadResourceTemplate(ProceedingJoinPoint joinPoint) throws Throwable {
        
        //todo to log down more information
        String componentName = joinPoint.getTarget().getClass().getSimpleName();
        Method method = getPointExecuteMethod(joinPoint);
        System.out.println("before case run");
        System.out.println(componentName + "." + method.getName() );
        
        Object result =  joinPoint.proceed();
        return  result;
        
    }
    
    
    private Method getPointExecuteMethod(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        return methodSignature.getMethod();
    }

}
