package com.patsnap.automation.patentApi.testcase.citation;

import com.alibaba.fastjson.JSON;
import com.patsnap.automation.annotation.*;
import com.patsnap.automation.base.BaseTestComponent;
import com.patsnap.automation.patentApi.endpoint.citation.CitationEndpoint;
import com.patsnap.automation.entity.TestResult;
import com.patsnap.automation.enums.DataSourceType;
import com.patsnap.automation.enums.TestcaseType;
import com.patsnap.automation.patentApi.response.citation.CitationInfoRsp;
import com.patsnap.automation.patentApi.testcase.BasePatentApiTestComponent;
import com.patsnap.automation.utils.GlobalVariableUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import static org.assertj.core.api.Assertions.*;
/**
 * Created by liuyikai on 2017/9/5.
 */

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
@AutomationComponent
public class CitationOriginalTestComponent extends BasePatentApiTestComponent {

    
    @Autowired
    CitationEndpoint citationEndpoint;
    
    
    @DataSource(value = "Citation/CitationOriginalInfo.xlsx", type = DataSourceType.EXCEL, sheetName = "patentInfo")
    @AutomationTestCase(name ="TestGetCitationOriginalInfo", author = "Alex", testcaseType = TestcaseType.WEB_API_TEST, tags={"demo", "smoke"},
                        description = "this is a demo test case for get citation original info")
    public TestResult<CitationInfoRsp> testGetCitationOriginalInfoRsp(@TestParam(name="patentId", value="80087ff1-8c34-4610-9119-acc125535848") String patentId,
                                                                     @TestParam(name="sorts", value="PN") String sorts,
                                                                     @TestParam(name="start", value="0") String start,
                                                                     @TestParam(name="rows", value="5") String rows,
                                                                     @TestParam(name="lang", value="EN") String lang) {
        
        ResponseEntity<CitationInfoRsp> rspResponseEntity
                = citationEndpoint.getCitationOriginalRsp(patentId, sorts, start, rows, lang);

        CitationInfoRsp citationInfoRsp = rspResponseEntity.getBody();

        checkpoint("checkCiteCount")
                .assume(citationInfoRsp.getCite_count())
                .that((Integer c) -> c > 0);


        String patentIdInResult = citationInfoRsp.getPatent_id();
       
        checkpoint("check_PatentId")
                .assume(patentIdInResult)
                .that((id) -> {
                    assertThat(id).isEqualTo(patentId);
                });

        checkpoint("isEqualTo").assume(citationInfoRsp).isEqualTo(citationInfoRsp);
        
        checkpoint("isEmpty").assume("").isEmpty();
        
        checkpoint("isNull").assume(null).isNull();
        
        
        
        checkpoint("simplpass2").isPassed(true);
        
        checkpoint("isfieldsequal").assume(citationInfoRsp).isEqualToIgnoringNullFields(citationInfoRsp);
        
        return new TestResult<CitationInfoRsp>(citationInfoRsp);
    }
    
    
    
    
    
    
}