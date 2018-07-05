package com.patsnap.automation.annotation;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by liuyikai on 2017/9/4.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Component
@Lazy
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public @interface AutomationComponent {
    String value() default "";
}
