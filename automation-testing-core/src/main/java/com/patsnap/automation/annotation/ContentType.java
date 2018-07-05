package com.patsnap.automation.annotation;

/**
 * Created by liuyikai on 2017/9/27.
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ContentType {
    
    String value();
    
}
