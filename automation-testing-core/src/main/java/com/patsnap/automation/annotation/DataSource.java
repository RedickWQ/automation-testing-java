package com.patsnap.automation.annotation;

import com.patsnap.automation.enums.DataSourceType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by liuyikai on 2017/9/6.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface DataSource {
    
    String value();
    
    DataSourceType type() default  DataSourceType.EXCEL;
    
    String sheetName() default "sheet1";
    
    char separator() default ',';
    
}
