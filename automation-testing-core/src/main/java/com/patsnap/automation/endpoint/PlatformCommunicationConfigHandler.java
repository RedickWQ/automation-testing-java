package com.patsnap.automation.endpoint;

import com.patsnap.automation.base.DynamicHostAddressHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author liuyikai(Alex)
 * @date 2017/12/27
 */
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class PlatformCommunicationConfigHandler implements DynamicHostAddressHandler{
    
    @Value("${com.patsnap.automation.platform.address}")
    private String address;
    
    @Override
    public String getAddress() {
        return address;
    }
    
    
    
}
