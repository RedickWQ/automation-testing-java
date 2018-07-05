package com.patsnap.automation.analyticsApi.testcase.expandsearch;

import com.alibaba.fastjson.JSONObject;
import com.patsnap.automation.analyticsApi.endpoint.search.SearchEndPoint;
import com.patsnap.automation.annotation.AutomationComponent;
import com.patsnap.automation.annotation.AutomationTestCase;
import com.patsnap.automation.annotation.DataSource;
import com.patsnap.automation.annotation.TestParam;
import com.patsnap.automation.base.BaseTestComponent;
import com.patsnap.automation.entity.TestResult;
import com.patsnap.automation.enums.DataSourceType;
import com.patsnap.automation.enums.TestcaseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

/**
 * Created by liuyikai on 2017/9/21.
 */


@Component
@Scope("prototype")
@AutomationComponent
public class ExpandSearchTestComponent extends BaseTestComponent {
    
    
  
    
    
    @Autowired
    SearchEndPoint searchEndPoint;
    
    
    
    @DataSource(value = "ExpandSearch.xlsx", type = DataSourceType.EXCEL, sheetName = "expandQuery")
    @AutomationTestCase(name ="ExpandSearchResultTest", author = "Alex", testcaseType = TestcaseType.WEB_API_TEST)
    public TestResult<JSONObject> expandSearchResultTest(@TestParam(name="expandQuery", value = "car") String expandQuery) {
    
        TestResult<JSONObject>  testResult = new TestResult<>();
        try {
//            ResponseEntity<JSONObject>  responseEntity = searchEndpoint_bak.query(expandQuery);
    
            ResponseEntity<JSONObject>  responseEntity = searchEndPoint.query("query",
                    expandQuery, "", "tablelist", "1", "cdesc", "1480906202546");
            
            testResult.setResult(responseEntity.getBody());
            
            
            
        } catch (ResourceAccessException e) {
            testResult.setError(e);
        } catch (Exception e) {
            testResult.setError(e);
        }
    
    
        return testResult;
        
        
    }
    
    
    
    
}
