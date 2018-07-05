package com.patsnap.automation.gui.framework;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 *
 * @author Liuyikai (Alex)
 * @date 2017/10/9
 */
public interface WebElementEx extends WebElement {
    
    void setLocator(By locator);
    String getLocatorInfo();
    
    WebElementEx setGivenName(String name);
    
    String getGivenName();
    
    public void setAttribute(String key, String value);
    
    
    public boolean isPresent(long seconds);
    
    
    public boolean isPresent();
    
    public WebElementEx waitToPresent(long seconds);
    
    public WebElementEx waitToPresent(WebElementEx parentWebElementEx, long seconds);
    
    public void setValueByScript(String value);
    
    public void mouseHover();
    
    public void mouseHover(int seconds);
    
    public WebElementEx highlight(int seconds);
    
    public WebElementEx highlight();
    
    public WebElementEx sleep(int seconds);
    
    public WebElementEx sleep(int seconds, int millisecond);
    
    public void doubleClick();
    
    
    
    public WebElementEx findElementEx(By by);
    
    public List<WebElementEx> findElementExs(By by);
    
    
    public boolean isClickable();
    
    public boolean isVisible();
    
    public WebElementEx waitToBeVisible(long waitTimeInSeconds);
    
    public boolean isPresent(WebElementEx parentWebElementEx, long waitTimeinSeconds);
    
    public WebElementEx $(By locator);
    
    
    void check();
    void unCheck();
    
    //================js events==================
    public void onBlur();
    
    
    
    WebElement getElement();
    WebElement inViewPort();
    
    
    
    //by javascript
    public WebElementEx clickByJavaScript();
    public void checkByJavaScript();
}