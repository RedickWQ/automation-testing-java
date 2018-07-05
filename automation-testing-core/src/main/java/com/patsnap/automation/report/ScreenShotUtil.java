package com.patsnap.automation.report;

import com.patsnap.automation.contants.Constant;
import com.patsnap.automation.context.TestContext;
import com.patsnap.automation.context.TestContextManager;
import com.patsnap.automation.gui.framework.GuiTestContext;
import com.patsnap.automation.utils.CommonUtil;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

/**
 *
 * @author Liuyikai (Alex)
 * @date 2017/11/8
 */
public class ScreenShotUtil {
    
    static {
        try {
            FileUtils.forceMkdir(new File(Constant.SCREENSHOT_DIR_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public static String takeScreenShot(String checkpointName){
    
        TestContext context = TestContextManager.getContext();
        if (context instanceof GuiTestContext){
            
            GuiTestContext guiTestContext = (GuiTestContext)context;
            WebDriver driver = guiTestContext.getDriver();
            if (driver != null) {
                
                try {
                    
                    String browserName = ((RemoteWebDriver)driver).getCapabilities().getBrowserName();
                    
                    TakesScreenshot screenshotDriver = (TakesScreenshot)driver;
                   
                    Object ss = screenshotDriver.getScreenshotAs(OutputType.BASE64);
                    Base64 decoder = new Base64();
                    byte[] imgBytes;
                    String filename = checkpointName + "-"+ browserName + "-" + CommonUtil.getCurrentTimeStamp() + CommonUtil.getGuid() + ".png";
                    imgBytes = (byte[]) decoder.decode(ss);
                    FileOutputStream osf = new FileOutputStream(new File(Constant.SCREENSHOT_DIR_PATH, filename));
                    osf.write(imgBytes);
                    osf.flush();
                    
                    return Paths.get("/AutomationReport/" + Constant.SCREENSHOT_DIR , filename).toString();
                    
                } catch (DecoderException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    //driver is not initialized in case of api testing
                    e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                }
                return null;
                
            } else {
                return  null;
            }
            
        } else {
            return null;
        }

    }

    
}
