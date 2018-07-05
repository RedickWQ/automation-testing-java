package com.patsnap.automation.annotation;

/**
 * Created by liuyikai on 2017/9/22.
 */

import com.patsnap.automation.configuration.EndpointProxyBeansRegistrar;

import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({
        EndpointProxyBeansRegistrar.class
})
public @interface EnableEndpoint {
    @AliasFor("basePackages")
    String[] value() default {};
    
    @AliasFor("value")
    String[] basePackages() default {};
}
