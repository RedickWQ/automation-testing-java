package com.patsnap.automation.gui.annotation;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by  Liuyikai (Alex) on 2017/11/13.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Lazy
public @interface WebPage {
    
    String route() default  "/";
    
    String hostName();
    
    
}
