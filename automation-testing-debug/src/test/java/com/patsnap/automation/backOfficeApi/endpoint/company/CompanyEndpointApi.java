package com.patsnap.automation.backOfficeApi.endpoint.company;

import com.alibaba.fastjson.JSONObject;
import com.patsnap.automation.backOfficeApi.annotation.AuthenticationRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Created by  Liuyikai (Alex) on 2017/10/17.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CompanyEndpointApi {
    
    @Autowired
    CompanyEndpoint companyEndpoint;
    
    @AuthenticationRequired
    public JSONObject getCompanyById(String companyById){
    
        
        ResponseEntity<JSONObject> company = companyEndpoint.getCompanyById(companyById);
        return company.getBody();
    }
    
    
    
}
