package com.patsnap.automation.gui.base;

import com.patsnap.automation.gui.framework.ObjectRepository;
import com.patsnap.automation.gui.framework.WebElementEx;
import com.patsnap.automation.gui.framework.WebElementExImpl;
import com.patsnap.automation.utils.SpringContextUtil;

import org.openqa.selenium.By;

import java.io.InputStream;

/**
 *
 * @author Liuyikai (Alex)
 * @date 2017/10/9
 */
public abstract class BasePageSection {
    
    
    protected ObjectRepository objectRepository;
    
    public BasePageSection() {
        
        
        InputStream in = this.getClass().getResourceAsStream(this.getClass().getSimpleName() + ".yaml");
        this.objectRepository = new ObjectRepository(in);
        
        //append object repository for parent page recursively
        Class self = this.getClass();
        System.out.println("init ObjectRepository for " + self.getSimpleName());
        
        while  (self.getSuperclass() != BasePageSection.class ){
            Class parent = self.getSuperclass();
            //System.out.println("parent class name: " + parent.getSimpleName());
            in = parent.getResourceAsStream(parent.getSimpleName() + ".yaml");
            this.objectRepository.merge(in);
            self = parent;
        }
        
    }
    
    
    public WebElementEx $(String name){
        By by = objectRepository.getLocatorByName(name);
        //return new WebElementExImpl(by);
        return $(by).setGivenName(name);
    }
    
    public WebElementEx $(String name, String...expression){
        By by = objectRepository.getLocatorByName(name, expression);
        //return new WebElementExImpl(by);
        return $(by).setGivenName(name);
    }
    
    public WebElementEx $(By by){
        //return new WebElementExImpl(by);
        return (WebElementEx) SpringContextUtil.getBean("WebElementEx", by);
    }
    
    
    public WebElementEx $(Class<? extends WebElementExImpl> clazz, String name){
        By by = objectRepository.getLocatorByName(name);
        return  ((WebElementEx) SpringContextUtil.getBean(clazz, by)).setGivenName(name);
    }
    
    public WebElementEx $(Class<? extends WebElementExImpl> clazz, String name, String ... expression){
        By by = objectRepository.getLocatorByName(name,expression);
        return  (WebElementEx) SpringContextUtil.getBean(clazz,by).setGivenName(name);
    }
    
    
    
    
}