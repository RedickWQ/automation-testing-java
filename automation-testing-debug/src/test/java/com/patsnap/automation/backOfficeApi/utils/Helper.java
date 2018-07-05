package com.patsnap.automation.backOfficeApi.utils;

import com.alibaba.fastjson.JSONObject;
import com.patsnap.automation.backOfficeApi.endpoint.authentication.AuthenticationEndpoint;
import com.patsnap.automation.utils.SpringContextUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Created by liuyikai on 2017/9/27.
 */
public class Helper {
    
    public static String getAccessToken(){
    
        AuthenticationEndpoint authenticationEndpoint
                = SpringContextUtil.getBean(AuthenticationEndpoint.class);
    
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
        params.add("grant_type", "client_credentials");
    
        authenticationEndpoint.addHeader("Authorization", "Basic MTA6YWJj");
        ResponseEntity<JSONObject> authResult = authenticationEndpoint.authenticate(params);
        
        String token = "Bearer " + authResult.getBody().getString("access_token");
        System.out.println("token: " + token);
        return  token;
    }
    
    
    
}
