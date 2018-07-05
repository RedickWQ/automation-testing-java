package com.patsnap.automation.patentApi.testcase;

import com.alibaba.fastjson.JSONObject;
import com.patsnap.automation.annotation.AutomationComponent;
import com.patsnap.automation.annotation.AutomationTestCase;
import com.patsnap.automation.annotation.TestParam;
import com.patsnap.automation.base.BaseTestComponent;
import com.patsnap.automation.entity.TestResult;
import com.patsnap.automation.enums.TestcaseType;
import com.patsnap.automation.patentApi.endpoint.AgencyEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

/**
 * Created by  Liuyikai (Alex) on 2017/11/16.
 */
@AutomationComponent
public class SampleTestComponent  extends BaseTestComponent{
    
    @Autowired
    AgencyEndpoint agencyEndpoint;
    
    @AutomationTestCase(testcaseType = TestcaseType.WEB_API_TEST)
    public TestResult<JSONObject> getAgencyId(@TestParam(name = "agencyId", value = "42236") String agencyId){
    
        ResponseEntity<JSONObject> result =  agencyEndpoint.getAgencyId(agencyId);
        
        return new TestResult<JSONObject>(result.getBody());
        
    }
    
    
    
    
    
    
}
