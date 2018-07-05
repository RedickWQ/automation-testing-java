package com.patsnap.automation.analyticsGui.testcase.common;

import com.patsnap.automation.analyticsGui.page.LoginPage;
import com.patsnap.automation.analyticsGui.page.simple.SimpleSearchPage;
import com.patsnap.automation.annotation.AutomationComponent;
import com.patsnap.automation.annotation.AutomationTestCase;
import com.patsnap.automation.annotation.TestParam;
import com.patsnap.automation.base.BaseTestComponent;
import com.patsnap.automation.entity.Checkpoint;
import com.patsnap.automation.entity.TestResult;
import com.patsnap.automation.enums.TestcaseType;
import com.patsnap.automation.utils.TestEnvUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author liuyikai
 * @date 2017/9/30
 */

@AutomationComponent
public class AnalyticsDemoTestComponent extends BaseTestComponent {
    
    public AnalyticsDemoTestComponent(){
        System.out.println("constructor is called");
    }
    
    
    @Autowired
    LoginPage loginPage;
    @Autowired
    SimpleSearchPage simpleSearchPage;
    
    
    @AutomationTestCase(name ="AnalyticsLoginTest", author = "Alex",
            testcaseType = TestcaseType.WEB_GUI_TEST,
            tags = "Smoke")
    public TestResult<Void> loginTest(@TestParam(name="username", value="liuyikai@patsnap.com") String username,
                                      @TestParam(name="password", value="111111Aa") String password){
        
        loginPage.open();
        loginPage.login(username, password);
        
        return new TestResult<>(null);
        
    }
    
    @AutomationTestCase(name ="AnalyticsSimpleSearchTest", author = "Alex",
            testcaseType = TestcaseType.WEB_GUI_TEST,
            tags = {"Smoke", "Regression"})
    public TestResult<Void> simpleSearchTest(@TestParam(name="keyword",value = "apple") String keyword){
        
        loginPage.open();
        loginPage.login();
        
        simpleSearchPage.search(keyword);
        checkpoint("searchResult").takeScreenshot().flush();
        simpleSearchPage.logout();
        
        return new TestResult<>(null);
    }
    
    
    @AutomationTestCase(name ="validateEntryText", author = "Alex",
            testcaseType = TestcaseType.WEB_GUI_TEST,
            tags = {"Smoke", "Regression"})
    public TestResult<String> validateEntryText(@TestParam(name="text",value = "") String text){
        
        loginPage.open();
        String actual = loginPage.getEntryText();
        System.out.println(actual);
        checkpoint("checkEntryText").assume(actual).that(t-> {return t.equals(text);});
        
        
        
      
        return new TestResult<>(actual);
    }
    
}
