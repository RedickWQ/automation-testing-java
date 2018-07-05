package com.patsnap.automation.gui.framework;

import org.ho.yaml.Yaml;
import org.openqa.selenium.By;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuyikai on 2017/10/9.
 */
public class ObjectRepository {
    
    private HashMap<String, HashMap<String, String>> locators;
    
    private InputStream in;
    
    public ObjectRepository(InputStream in){
        if (in != null){
            this.in = in;
            this.init();
        }
    }
    
    
    public void init(){
        try
        {
            this.locators = Yaml.loadType(in, HashMap.class);
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Object Repository file is not found");
        }
        catch(RuntimeException ex){
            ex.printStackTrace();
            System.err.println("Object Repository file is not found");
        }
        
        
        //print();
    }
    
    
    public void merge(InputStream in){
        HashMap<String, HashMap<String, String>> tempLocators = null;
        try
        {
            tempLocators = Yaml.loadType(in, HashMap.class);
            
            //merge to existing locators, but only for new locators
            for( Map.Entry<String,  HashMap<String, String>> entry : tempLocators.entrySet()){
                if ( !this.locators.containsKey(entry.getKey())){
                    this.locators.put(entry.getKey(),entry.getValue());
                }
            }
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Object Repository file is not found");
        }
        catch(RuntimeException ex){
            ex.printStackTrace();
            System.err.println("Object Repository file is not found");
        }
        
    }
    
    
    
    
    public void print(){
        for(Map.Entry<String,  HashMap<String, String>> entry : locators.entrySet()){
            String output = entry.getKey() + " locator: " + entry.getValue();
            System.out.println(output);
        }
    }
    
    
    public By getLocatorByName(String name){
        
        return getLocatorByName(name,null);
    }
    
    public By getLocatorByName(String name, String...expression){
        if (this.locators != null && this.locators.containsKey(name)){
            HashMap<String,String> locator = this.locators.get(name);
            String type = locator.get("type").trim().toLowerCase();
            String value = locator.get("value");
            if (expression!=null) {
                value = String.format(value, expression);
            }
            
            return getLocator(type,value);
        }else{
            throw new RuntimeException(MessageFormat.format("Locator {0} is not defined in yaml file", name));
        }
    }
    
    
    public By getLocator(String type, String value)  {
        switch (type) {
            case "id":
                return By.id(value);
            
            case "xpath":
                return By.xpath(value);
            
            case "classname":
                return By.className(value);
            
            case "tagname":
                return By.tagName(value);
            
            case "cssselector":
                return By.cssSelector(value);
            
            case "linktext":
                return By.linkText(value);
            
            case "name":
                return By.name(value);
            
            case "partiallinktext":
                return By.partialLinkText(value);
            
            default:
                return null;
        }
    }
}

