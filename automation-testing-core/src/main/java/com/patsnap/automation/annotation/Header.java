package com.patsnap.automation.annotation;

import com.patsnap.automation.base.DefaultHeaderHandler;
import com.patsnap.automation.base.DynamicHeaderHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author liuyikai(Alex)
 * @date 2017/12/6
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Header {
    
    String headerName();
    
    String headerValue() default "";
    
    Class<? extends DynamicHeaderHandler> handler() default DefaultHeaderHandler.class;
    
    boolean isDynamic() default false;
    
    
}
