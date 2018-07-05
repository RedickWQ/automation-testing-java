package com.patsnap.automation.gui.framework;

import com.patsnap.automation.context.TestContext;
import com.patsnap.automation.log.LogLevel;
import com.patsnap.automation.report.Reporter;
import com.patsnap.automation.utils.TestEnvUtil;

import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;

import lombok.Data;

/**
 *
 * @author Liuyikai (Alex)
 * @date 2017/10/9
 */
@Data
public class GuiTestContext extends TestContext {
    
    private WebDriver driver;
    
    
    
    
    public WebDriver returnWebDriver() {
        if (this.driver == null){
            initializeDriver();
        }
        
        return this.driver;
    }
    
    private void initializeDriver(){
    
        Reporter reporter = this.getReporter();
        
        String browserType = environmentVariable.get("common","BrowserType");
        if (null == browserType){
            browserType = "chrome";
        }
        
        
        
        try {
            if ((environmentVariable.get("common", "WebDriverType").toString().toUpperCase()).equals(WebDriverFactory.DriverType.REMOTE)) {
                String hubAddress = TestEnvUtil.getPropertyConfigValueByKey("webdriver.remote.hub");
                reporter.logEvent(LogLevel.INFO, "Start to init remote webdriver for [" + browserType + "] at: " + hubAddress );
                this.driver = WebDriverFactory.createWebDriver(browserType, hubAddress);
                
                
            } else {
                reporter.logEvent(LogLevel.INFO, "Start to init local webdriver for [" + browserType + "]");
                this.driver = WebDriverFactory.createWebDriver(browserType);
                
            }
            String implicitlyWaitTime = TestEnvUtil.getPropertyConfigValueByKey("webdriver.implicitlyWaitTime");
            Long waitTime = implicitlyWaitTime != null ? Long.valueOf(implicitlyWaitTime) : 3;
            
            driver.manage().timeouts().implicitlyWait(waitTime, TimeUnit.SECONDS);
            
            if (!browserType.toLowerCase().equals("edge") && !browserType.toLowerCase().equals("eg") ) {
                driver.manage().deleteAllCookies();
                driver.manage().window().maximize();
            }
            
            
            
            
            reporter.logEvent(LogLevel.INFO,"Driver initialized");
    
    
        } catch (Exception ex){
//            reporter.logError(ex);
            throw ex;
        }
        
    }
    @Override
    public void endContext(){
        terminateDriver();
    }
    
    
    
    public void terminateDriver() {
        try {
            if (driver != null){
                this.driver.close();
                this.driver.quit();
            }
            
        } catch (Exception e){
            e.printStackTrace();
        }
        
    }
    
    
}
