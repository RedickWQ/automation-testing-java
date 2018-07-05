package com.patsnap.automation.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by liuyikai on 2017/9/4.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER})
public @interface TestParam {
    String name();
    
    
    String value() default  "";
    
}
