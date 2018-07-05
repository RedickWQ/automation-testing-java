package com.patsnap.automation.patentApi.testcase.family;

import com.patsnap.automation.annotation.AutomationComponent;
import com.patsnap.automation.annotation.AutomationTestCase;
import com.patsnap.automation.annotation.DataSource;
import com.patsnap.automation.annotation.TestParam;
import com.patsnap.automation.base.BaseTestComponent;
import com.patsnap.automation.patentApi.bean.family.FamilyMap;
import com.patsnap.automation.patentApi.endpoint.family.FamilyEndpoint;
import com.patsnap.automation.entity.TestResult;
import com.patsnap.automation.enums.DataSourceType;
import com.patsnap.automation.enums.TestcaseType;
import com.patsnap.automation.patentApi.response.family.FamilyCountRes;
import com.patsnap.automation.patentApi.response.family.FamilyMapRes;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Created by liuyikai on 2017/9/14.
 */

@Component
@Scope("prototype")
@AutomationComponent
public class FamilyMapTestComponent extends BaseTestComponent {
    
 
    @Autowired
    FamilyEndpoint familyEndpoint;
    
    
    @AutomationTestCase(name ="testGetFamilyMapInfo", author = "Alex", testcaseType = TestcaseType.WEB_API_TEST)
    @DataSource(value = "Family/PatentInfo.xlsx", type = DataSourceType.EXCEL, sheetName = "patentInfo")
    public TestResult<FamilyMapRes> testGetFamilyMapInfo(@TestParam(name="patentId", value="80087ff1-8c34-4610-9119-acc125535848") String patentId) {
    
        familyEndpoint.addCookie("sampleCookie", "sampleCookieValue");
        familyEndpoint.addHeader("myheader","headervalues");
        
        ResponseEntity<FamilyMapRes> resResponseEntity = familyEndpoint.getFamilyMap(patentId);
        FamilyMap actualFamilyMap = resResponseEntity.getBody().getData();
    
        if (CollectionUtils.isNotEmpty(actualFamilyMap.getList())) {
            checkpoint("CheckTotal").assume(actualFamilyMap.getTotal()).that((Integer total) -> total > 0);
            
            // total should equals all country count sum;
            checkpoint("CheckTotalEqualsSum").assume(actualFamilyMap.getTotal().intValue()).that(
                    (Integer t) -> t == actualFamilyMap.getList().stream().flatMap(m -> m.values().stream()).reduce((sum, count) -> sum + count).get().intValue());
            
        } else {
            checkpoint("CheckTotal").assume(actualFamilyMap.getTotal().longValue()).that((Long total) -> total == 0);
           
        }
        checkpoint("CheckStatusCode").assume(resResponseEntity.getStatusCodeValue()).that((Integer c) -> c == 200);
        
       return new TestResult<>(resResponseEntity.getBody());
    }
    
    
    
    @AutomationTestCase(name ="testGetFamilyCount", author = "Alex", testcaseType = TestcaseType.WEB_API_TEST)
    @DataSource(value = "Family/PatentInfo.csv", type = DataSourceType.CSV, separator = ',')
    public TestResult<FamilyCountRes> testGetFamilyCount(@TestParam(name="patentId", value="80087ff1-8c34-4610-9119-acc125535848") String patentId) {
    
        ResponseEntity<FamilyCountRes> resResponseEntity = familyEndpoint.getFamilyCount(patentId);
    
        
        return new TestResult<>(resResponseEntity.getBody());
    }
    

    
}
