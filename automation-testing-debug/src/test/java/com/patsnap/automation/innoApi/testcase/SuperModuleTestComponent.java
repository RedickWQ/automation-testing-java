package com.patsnap.automation.innoApi.testcase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.patsnap.automation.annotation.AutomationComponent;
import com.patsnap.automation.annotation.AutomationTestCase;
import com.patsnap.automation.base.BaseTestComponent;
import com.patsnap.automation.entity.TestResult;
import com.patsnap.automation.enums.TestcaseType;
import com.patsnap.automation.innoApi.endpoint.SuperModuleEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Created by  Liuyikai (Alex) on 2017/10/23.
 */
@Component
@Scope("prototype")
@AutomationComponent
public class SuperModuleTestComponent extends BaseTestComponent{
    
    
    @Autowired
    SuperModuleEndpoint superModuleEndpoint;
    
    
    @AutomationTestCase( testcaseType = TestcaseType.WEB_API_TEST)
    public TestResult<JSONObject> testPatchMethod() {
        
        String object = "1508750289821";
        JSONObject body = JSON.parseObject("{ \"label\" : \"1508750301644_patched\"}");
    
        String authToken = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiI2NWQ5MDM0N2JmMzg0ZTM4YWU3NmQxMmRlOGE1MjM3OSIsImlhdCI6MTUwODc1MDI5MCwiZXhwIjoxNTA4ODM2NjkwLCJjbGllbnRfdHlwZSI6MSwiYXBwX2tleSI6bnVsbCwiZXh0cmEiOnsibGFzdF9sb2dpbl9pcCI6IjEyNy4wLjAuMSIsImxhc3RfbG9naW5fdGltZSI6MTUwODc1MDI5MCwibG9naW5fbnVtIjozMzYwMSwiYWNjb3VudCI6ImF1dG9AcGF0c25hcC5jb20iLCJ1c2VyX2lkIjoxLCJzdGF0dXMiOjAsImF2YXRhciI6W10sInRlbmFudF9pZCI6MjR9fQ.wIQJojmwrEAsPAqXJJNLAxy3J3_DNxuh6fHxSw21JCo";
        String x_app_key = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiI2NWQ5MDM0N2JmMzg0ZTM4YWU3NmQxMmRlOGE1MjM3OSIsImlhdCI6MTQ5ODgwNDEzMiwiZXhwIjo0NjUyNDA0MTMyLCJjbGllbnRfdHlwZSI6IjMiLCJhcHBfa2V5IjoiZjEyZjhhMmFjMDNlNzUzM2JkZjE3YTczMTY1OWQ1ZGIiLCJleHRyYSI6W119.g-YfuBPxykANv9iawfqAdWcnJR5IOfCn_b5Lb5kCjpM";
        String x_api_token = "36e516b6371cf8f530d70de60d14da97e07f3779";
        
        superModuleEndpoint.addHeader("Authorization",authToken);
        superModuleEndpoint.addHeader("X-APP-KEY", x_app_key);
        superModuleEndpoint.addHeader("X-API-Token", x_api_token);
    
    
        ResponseEntity<JSONObject> result = superModuleEndpoint.PATCH_SuperModuleObject(object, body);
        
        return new TestResult<>(result.getBody());
    }
    
}
