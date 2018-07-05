package com.patsnap.automation.backOfficeApi.testcase.company;

import com.alibaba.fastjson.JSONObject;
import com.patsnap.automation.annotation.AutomationComponent;
import com.patsnap.automation.annotation.AutomationTestCase;
import com.patsnap.automation.annotation.TestParam;
import com.patsnap.automation.backOfficeApi.endpoint.company.CompanyEndpoint;
import com.patsnap.automation.backOfficeApi.endpoint.company.CompanyEndpointApi;
import com.patsnap.automation.base.BaseTestComponent;
import com.patsnap.automation.entity.TestResult;
import com.patsnap.automation.enums.TestcaseType;
import com.patsnap.automation.utils.TestEnvUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by  Liuyikai (Alex) on 2017/10/17.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@AutomationComponent
public class CompanyTestComponent extends BaseTestComponent {
    
    
    @Autowired
    CompanyEndpointApi companyEndpointApi;
    
    @AutomationTestCase(name ="BackOfficeCompanyTest", author = "Alex", testcaseType = TestcaseType.WEB_API_TEST)
    public TestResult<JSONObject> backOfficeCompanyTest(@TestParam(name = "companyId", value = "dbb75e6494bf4c099b3d04d141590ff6") String companyId) {
        
        JSONObject company = companyEndpointApi.getCompanyById(companyId);
        return  new TestResult<>(company);
    
        
        
        
    }
    
    
}
