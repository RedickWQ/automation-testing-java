package com.patsnap.automation.analyticsGui.pagesection.login;

import com.patsnap.automation.gui.base.BasePageSection;
import com.patsnap.automation.utils.TestEnvUtil;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author liuyikai
 * @date 2017/9/30
 */
@Component
@Lazy
public class LoginSection extends BasePageSection{
    
    public void login(String username, String password){
        
        $("username").sendKeys(username);
        $("password").sendKeys(password);
        $("loginButton").click();
        
    }
    
}
