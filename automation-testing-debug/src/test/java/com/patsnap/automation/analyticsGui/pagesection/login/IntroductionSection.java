package com.patsnap.automation.analyticsGui.pagesection.login;

import com.patsnap.automation.gui.base.BasePageSection;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * Created by  Liuyikai (Alex) on 2017/11/7.
 */
@Component
@Lazy
public class IntroductionSection extends BasePageSection{
    
    public String getEntryLinkText() {
        
        String value =  $("EntryLink").highlight(2).getText();
        return value;
        
    }
    
}
