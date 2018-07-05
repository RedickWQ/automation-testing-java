package com.patsnap.automation.configuration;

/**
 * Created by liuyikai on 2017/9/5.
 */

import com.patsnap.automation.interceptor.RequestInterceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * @author liuyikai
 */
@Configuration
public class InterceptorConfiguration extends WebMvcConfigurerAdapter {
    
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new RequestInterceptor()).addPathPatterns("/**");
    }
}
