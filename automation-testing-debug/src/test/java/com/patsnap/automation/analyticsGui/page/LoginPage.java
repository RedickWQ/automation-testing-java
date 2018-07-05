package com.patsnap.automation.analyticsGui.page;

import com.patsnap.automation.analyticsGui.base.BaseAnalyticsPage;
import com.patsnap.automation.analyticsGui.pagesection.login.IntroductionSection;
import com.patsnap.automation.analyticsGui.pagesection.login.LoginSection;
import com.patsnap.automation.gui.annotation.WebPage;
import com.patsnap.automation.utils.TestEnvUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 *
 * @author liuyikai
 * @date 2017/9/30
 */

@WebPage(hostName = "analytics")
public class LoginPage extends BaseAnalyticsPage{
    
    @Autowired
    LoginSection loginSection;

    @Autowired
    IntroductionSection introductionSection;
    
    @Override
    public void open(){
        navigate("/");
    }
    
    
    
    
    
    
    public void login(String username, String password){
        loginSection.login(username, password);
    }
    
    public void login () {
    
        String username = TestEnvUtil.getEnvironmentVariable("analytics", "username");
        String password = TestEnvUtil.getEnvironmentVariable("analytics", "password");
        
        login(username, password);
        wait(2);
    }
    
    
    public String getEntryText(){
       return introductionSection.getEntryLinkText();
    }
}
