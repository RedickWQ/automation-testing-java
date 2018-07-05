package com.patsnap.automation.gui.framework;

import com.patsnap.automation.context.TestContextManager;
import com.patsnap.automation.log.LogLevel;
import com.patsnap.automation.report.Reporter;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created by  Liuyikai (Alex) on 2017/10/9.
 */

@Aspect
@Component
public class WebElementExAspect {
    
    
//    @Pointcut("@annotation(com.patsnap.automation.log.LogEvent) && execution(* com.patsnap.automation.gui.framework.WebElementExImpl.*(..))")
    @Pointcut("@annotation(com.patsnap.automation.log.LogEvent)")
    public void cutPoint() {
    
    }
    
    private Method getPointExecuteMethod(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        return methodSignature.getMethod();
        
    }
    
    @Around("cutPoint()")
    public Object handlerWithReturnValue(ProceedingJoinPoint joinPoint) throws Throwable{
        
        Reporter reporter = TestContextManager.getContext().getReporter();
        if (null != reporter) {
            reporter.logEvent( LogLevel.INFO, getLogEventDesc(joinPoint));
        }
    
        Object result =  joinPoint.proceed();
        
        return result;
    }
    
    
    @AfterThrowing(throwing="ex"
            , pointcut="cutPoint()")
    public void handlerException(Throwable ex){
//        Reporter reporter = TestContextManager.getContext().getReporter();
//        if (null != reporter) {
//            reporter.logError(ex);
//        }
    }
    
    private String getArgumentInfo(ProceedingJoinPoint joinPoint){
        
        String[] argumentValues  = new String[joinPoint.getArgs().length];
        for (int i = 0; i < joinPoint.getArgs().length; i++){
            
            String argType = joinPoint.getArgs()[i].getClass().getSimpleName();
            if (argType.equals("CharSequence[]")){
                CharSequence[] charSequences = (CharSequence[])joinPoint.getArgs()[i];
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < charSequences.length ; j++) {
                    sb.append(charSequences[i].toString());
                }
                argumentValues[i] = "'" + sb.toString() + "'";
            }
            else if(argType.equals("String[]")){
                argumentValues[i] = "'" + String.join("/",(String[])joinPoint.getArgs()[i]) + "'";
            }
            
            else{
                argumentValues[i]  = "'" + joinPoint.getArgs()[i].toString() + "'";
            }
        }
        
        return String.join(",", argumentValues);
        
    }
    
    private String getLocatorInfo(ProceedingJoinPoint joinPoint){
        WebElementEx elementEx = (WebElementEx)joinPoint.getTarget();
        return elementEx.getLocatorInfo();
    }
    
    private String getGivenName(ProceedingJoinPoint joinPoint){
        WebElementEx elementEx = (WebElementEx)joinPoint.getTarget();
        return elementEx.getGivenName();
    }
    
    private String getLogEventDesc(ProceedingJoinPoint joinPoint){
        
        String givenName = getGivenName(joinPoint);
        givenName = givenName!=null? givenName: "-";
        String locatorInfo = getLocatorInfo(joinPoint);
        String methodName = getPointExecuteMethod(joinPoint).getName();
        String argumentInfo = getArgumentInfo(joinPoint);
        return String.format("['%s'].%s(%s)", givenName!= null ?  givenName : locatorInfo, methodName, argumentInfo);
    }
    
}
