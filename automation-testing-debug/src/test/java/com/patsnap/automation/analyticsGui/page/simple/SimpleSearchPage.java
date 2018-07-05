package com.patsnap.automation.analyticsGui.page.simple;

import com.patsnap.automation.analyticsGui.base.BaseAnalyticsPage;
import com.patsnap.automation.analyticsGui.pagesection.common.UserProfileSection;
import com.patsnap.automation.analyticsGui.pagesection.simple.SearchSection;
import com.patsnap.automation.gui.annotation.WebPage;
import com.patsnap.automation.gui.base.BasePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author Liuyikai (Alex)
 * @date 2017/10/9
 */


@WebPage(hostName = "analytics" , route = "/simple")
public class SimpleSearchPage extends BaseAnalyticsPage {
    
    @Autowired
    SearchSection searchSection;
    
    @Autowired
    UserProfileSection userProfileSection;
    
    public void search(String keyword){
        searchSection.search(keyword);
    }
    
    public void logout(){
        userProfileSection.logout();
    }
    

}
