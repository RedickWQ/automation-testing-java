package com.patsnap.automation.analyticsGui.pagesection.simple;

import com.patsnap.automation.gui.base.BasePageSection;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author Liuyikai (Alex)
 * @date 2017/10/9
 */
@Component
@Lazy
//@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SearchSection extends BasePageSection{
    
    public void search(String keyword){
        
        $("keyword").sendKeys(keyword);
        $("searchButton").click();
        
    }
    
    
    
    
}
