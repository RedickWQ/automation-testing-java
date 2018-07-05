package com.patsnap.automation.openApi.util;

import com.alibaba.fastjson.JSONObject;
import com.patsnap.automation.base.DynamicHeaderHandler;

import com.patsnap.automation.openApi.endpoint.AuthEndpoint;
import com.patsnap.automation.utils.TestEnvUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.Charset;
import java.util.Base64;

/**
 * @author liuyikai(Alex)
 * @date 2017/12/5
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class OpenApiAuthenticationHandler implements  DynamicHeaderHandler{
    
    @Autowired
    AuthEndpoint authEndpoint;
    
    
    @Override
    public String getValue() {
        String username = TestEnvUtil.getEnvironmentVariable("connector", "username");
        String password = TestEnvUtil.getEnvironmentVariable("connector", "password");
    
        String plainCreds = username + ":" + password;
        System.out.println(plainCreds);
        byte[] plainCredsBytes = plainCreds.getBytes(Charset.forName("UTF-8"));
        String base64Creds = Base64.getUrlEncoder().encodeToString(plainCredsBytes);
    
        authEndpoint.addHeader("Authorization", "Basic " + base64Creds);
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
        params.add("grant_type", "client_credentials");
    
        ResponseEntity<JSONObject> result = authEndpoint.authenticate(params);
    
        return "Bearer " + result.getBody().getString("access_token");
    }
}
