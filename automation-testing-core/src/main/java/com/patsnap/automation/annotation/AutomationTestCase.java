package com.patsnap.automation.annotation;

import com.patsnap.automation.enums.TestcaseType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by liuyikai on 2017/9/4.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface AutomationTestCase {
    String name() default  "";
    String author() default "";
    String description() default "";
    TestcaseType testcaseType() default  TestcaseType.WEB_API_TEST;
    String[] tags() default {};
}
