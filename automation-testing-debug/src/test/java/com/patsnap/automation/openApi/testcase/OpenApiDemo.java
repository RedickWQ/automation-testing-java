package com.patsnap.automation.openApi.testcase;

import com.alibaba.fastjson.JSONObject;
import com.patsnap.automation.annotation.AutomationComponent;
import com.patsnap.automation.annotation.AutomationTestCase;
import com.patsnap.automation.base.BaseTestComponent;
import com.patsnap.automation.log.Logger;
import com.patsnap.automation.entity.TestResult;
import com.patsnap.automation.enums.TestcaseType;
import com.patsnap.automation.openApi.endpoint.AuthEndpoint;
import com.patsnap.automation.openApi.endpoint.Cloud2GEndpoint;

import com.patsnap.automation.openApi.endpoint.CloudPatentEndpoint;
import com.patsnap.automation.utils.LoggerUtil;
import com.patsnap.automation.utils.TestEnvUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.Charset;
import java.util.Base64;

/**
 * @author liuyikai(Alex)
 * @date 2017/12/5
 */
@AutomationComponent
public class OpenApiDemo extends BaseTestComponent{
    
    
    private Logger logger = LoggerUtil.getLogger();
    
    
    @Autowired
    AuthEndpoint authEndpoint;
    
    @Autowired
    Cloud2GEndpoint cloud2GEndpoint;
    
    @AutomationTestCase(testcaseType = TestcaseType.WEB_API_TEST)
    public TestResult<JSONObject> authentication(){
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
        
        
        return new TestResult<>(result.getBody());
    }
    
    
    @AutomationTestCase(testcaseType = TestcaseType.WEB_API_TEST, description = "中文描述")
    public TestResult<JSONObject> getPatentsDemo2() {
        String enName = "HUAWEI";
        
        ResponseEntity<JSONObject> result = cloud2GEndpoint.getPatents(enName);
    

    
        return new TestResult<>(result.getBody());
    }
    
    @AutomationTestCase(testcaseType = TestcaseType.WEB_API_TEST, description = "中文描述")
    public TestResult<JSONObject> getPatentsDemo3() {
        String enName = "HUAWEI";
        
        ResponseEntity<JSONObject> result = cloud2GEndpoint.getPatents(enName);
        
        logger.debug("debug log");
        logger.info("info log");
        logger.warning("warning log");
        logger.error("error log");
        logger.fatal("fatal log");
        
        
        return new TestResult<>(result.getBody());
    }
    
    @AutomationTestCase(testcaseType = TestcaseType.WEB_API_TEST, description = "中文描述")
    public TestResult<JSONObject> getPatentsDemo4() {
        String enName = "HUAWEI";
        
        ResponseEntity<JSONObject> result = cloud2GEndpoint.getPatents(enName);
 
        logger.fatal("fatal log");
        
        
        return new TestResult<>(result.getBody());
    }

    
    @Autowired
    CloudPatentEndpoint cloudPatentEndpoint;
    @AutomationTestCase(testcaseType = TestcaseType.WEB_API_TEST, description = "中文描述")
    public TestResult<JSONObject> getDescriptionIdRsp() {
        String [] patentIdArr = new String [1];
        patentIdArr[0] = "59cdfcc19dc315e915a03adae85007373a739e44072907758de3f2549a0a5729d4e4f2ff85e3d8790490debc20cd6f3f";
        
        ResponseEntity<JSONObject> result = cloudPatentEndpoint.getDescriptionIdRsp(patentIdArr);
        
      
        
        
        return new TestResult<>(result.getBody());
    }
    
    
    
    
    
}
