package com.patsnap.automation.gui.annotation;


import org.springframework.context.annotation.Lazy;

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
@Lazy
public @interface PageSection {
}
