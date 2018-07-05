package com.patsnap.automation.annotation;

import com.patsnap.automation.base.DefaultHostAddressHandler;
import com.patsnap.automation.base.DynamicHostAddressHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by liuyikai on 2017/9/5.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Endpoint {
    String value() default "";
    
    boolean isStateful() default  false;
    
   
    String hostName();
    
    Class<? extends DynamicHostAddressHandler> handler() default DefaultHostAddressHandler.class;
    
    boolean isDynamicHost() default  false;
}
