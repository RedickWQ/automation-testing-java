package com.patsnap.automation.analyticsGui.pagesection.common;

import com.patsnap.automation.gui.base.BasePageSection;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author Liuyikai (Alex)
 * @date 2017/10/10
 */

@Component
@Lazy

public class UserProfileSection extends BasePageSection{
    
    public void logout(){
        $("userPannel").waitToPresent(2).highlight(2).mouseHover(1);
        $("logoutLink").click();
    
    }
    
}
