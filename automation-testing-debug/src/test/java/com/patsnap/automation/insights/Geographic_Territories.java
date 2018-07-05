package com.patsnap.automation.insights;

import com.alibaba.fastjson.JSONObject;
import com.patsnap.automation.annotation.AutomationComponent;
import com.patsnap.automation.annotation.AutomationTestCase;
import com.patsnap.automation.annotation.DataSource;
import com.patsnap.automation.annotation.TestParam;
import com.patsnap.automation.base.BaseTestComponent;
import com.patsnap.automation.enums.DataSourceType;
import com.patsnap.automation.enums.TestcaseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import javax.xml.ws.Response;

@AutomationComponent
public class Geographic_Territories  extends BaseTestComponent {
    @Autowired
    GeographicEndPoint geographicEndPoint;

    @AutomationTestCase(name="testGeographic_Markets", author = "WQ",testcaseType = TestcaseType.WEB_API_TEST)
    @DataSource(type = DataSourceType.EXCEL, value = "competitiveTestData.xlsx", sheetName = "Geographic_Markets")
    public void testGeographic_Markets (@TestParam(name="Geographic_Markets") JSONObject bodyInsights,
                                        @TestParam(name ="Geographic_Markets_SearchApi") JSONObject bodySearchAPI){
        ResponseEntity<JSONObject> response = geographicEndPoint.getDistributionMap(bodyInsights);
        checkpoint("pass").pass();
    }
}
