package com.patsnap.automation.gui.framework;

import com.patsnap.automation.utils.TestEnvUtil;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.safari.SafariDriver;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by liuyikai on 2017/10/9.
 */
public class WebDriverFactory {
    
    public static class DriverType {
        public static final String REMOTE = "REMOTE";
        public static final String LOCAL = "LOCAL";
    }
    
    
    //for local webdriver
    public static WebDriver createWebDriver(String browserType){
        
        WebDriver driver = null;
        DesiredCapabilities capabilities = null;
        switch (browserType.toLowerCase()){
            case "ch":
            case "chrome":
//                System.setProperty("webdriver.chrome.driver",
//                        TestEnvUtil.getPropertyConfigValueByKey("webdriver.chrome.driver"));
                
                driver = new ChromeDriver();
                break;
            case "ff":
            case "firefox":
//                System.setProperty("webdriver.gecko.driver",
//                        TestEnvUtil.getPropertyConfigValueByKey("webdriver.gecko.driver"));

                driver = new FirefoxDriver();
                break;
                
            case "ie":
            case "internetexplorer":
                System.setProperty("webdriver.ie.driver", TestEnvUtil.getPropertyConfigValueByKey("webdriver.ie.driver"));
    
                capabilities = DesiredCapabilities.internetExplorer();
                capabilities.setCapability("ignoreZoomSetting", true);
                capabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
                driver = new InternetExplorerDriver(capabilities);
                break;
            case "sf":
            case "safari":
                //May have some settings for safari here
                driver = new SafariDriver();
                break;
    
            case "edge":
            case "eg":
                driver = new EdgeDriver();
                break;
            default:
                driver = new FirefoxDriver();
                break;
        }
        
       
        return driver;
        
    }
    
    
    //for remote webdriver
    public static WebDriver createWebDriver(String browserType, String url){
        
 
        DesiredCapabilities capability = null;
        
        switch (browserType.toLowerCase()){
            case "ch":
            case "chrome":
                capability =  DesiredCapabilities.chrome();
                break;
            case "ff":
            case "firefox":
                capability =  DesiredCapabilities.firefox();
                break;
            case "ie":
            case "internetexplorer":
                capability = DesiredCapabilities.internetExplorer();
                capability.setCapability("ignoreZoomSetting", true);
                capability.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
                break;
            case "sf":
            case "safari":
                capability = DesiredCapabilities.safari();
                
                break;
            case "edge":
            case "eg":
                capability = DesiredCapabilities.edge();
                break;
            default:
                capability =  DesiredCapabilities.chrome();
                break;
        }
        
        try {
            WebDriver driver = new RemoteScreenShotWebDriver(new URL(url),capability);
           
            
            return driver;
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnreachableBrowserException e){
            e.printStackTrace();
        }
        
        return null;
    }
    
    
}
