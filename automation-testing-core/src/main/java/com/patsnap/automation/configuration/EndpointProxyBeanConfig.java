package com.patsnap.automation.configuration;

import com.patsnap.automation.annotation.EnableEndpoint;
import com.patsnap.automation.proxy.EndpointProxyFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by liuyikai on 2017/9/22.
 */

@Configuration
@EnableEndpoint({
        
        "com.patsnap.automation"
})

public class EndpointProxyBeanConfig {
    
    @Bean(name = "EndpointProxyFactory" )
    public EndpointProxyFactory endpointProxyFactory(){
        return new EndpointProxyFactory();
    }
    
    
}
