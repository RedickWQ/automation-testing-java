package com.patsnap.automation.gui.framework;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

/**
 * Created by liuyikai on 2017/10/9.
 */
public class RemoteScreenShotWebDriver extends RemoteWebDriver implements TakesScreenshot {
    
    public RemoteScreenShotWebDriver(URL url, DesiredCapabilities dc) {
        super(url, dc);
    }
    
    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        
//        if ((Boolean) getCapabilities().getCapability(CapabilityType.TAKES_SCREENSHOT)) {
//            return target.convertFromBase64Png(execute(DriverCommand.SCREENSHOT).getValue().toString());
//        }
//        return null;
    
        
        return target.convertFromBase64Png(execute(DriverCommand.SCREENSHOT).getValue().toString());
    }
    
}
