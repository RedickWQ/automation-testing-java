package com.patsnap.automation.gui.base;

import com.patsnap.automation.gui.annotation.WebPage;
import com.patsnap.automation.gui.framework.GuiTestContext;
import com.patsnap.automation.gui.framework.WebElementEx;
import com.patsnap.automation.gui.framework.WebElementExImpl;
import com.patsnap.automation.log.LogLevel;
import com.patsnap.automation.report.FakeReporter;
import com.patsnap.automation.report.Reporter;
import com.patsnap.automation.context.TestContextManager;

import com.patsnap.automation.utils.TestEnvUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by  Liuyikai (Alex) on 2017/10/9.
 */
public abstract class BasePage {
    
    
    
    protected String mainUrl;
    
    public BasePage (){
    
    }
    
    
    private WebDriver getDriver(){
        
        return ((GuiTestContext) TestContextManager.getContext()).returnWebDriver();
    }
    
    
    
    public void open() {
        WebPage pageInfo = this.getClass().getAnnotation(WebPage.class);
        if (pageInfo != null) {
           navigate(pageInfo.route());
        } else  {
           navigate("/");
        }
   
    }
    
    
    
    protected  String getMainUrl(){
        WebPage pageInfo = this.getClass().getAnnotation(WebPage.class);
        if (pageInfo != null) {
            String hostname = pageInfo.hostName();
            return TestEnvUtil.getHostAddressByName(hostname);
        } else  {
            throw new RuntimeException("Annotation @WebPage is not found");
        }
        
    }
    
    
    private Reporter getReporter(){
        Reporter reporter = TestContextManager.getContext().getReporter();
        return  reporter != null ? reporter: new FakeReporter();
    }
    
    
    
    
    public void navigateToAbsoluteUrl(String absoluteUrl){
        WebDriver driver = getDriver();
        Reporter reporter = getReporter();
        reporter.logEvent(LogLevel.INFO,"navigate to: " + absoluteUrl);
        
        
        driver.get(absoluteUrl);
    
        FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver);
        wait.withTimeout(5, TimeUnit.SECONDS);
        wait.pollingEvery(250, TimeUnit.MILLISECONDS);
        wait.ignoring(NoSuchElementException.class);
    
        Function<WebDriver, Boolean> f = new Function<WebDriver, Boolean>() {
        
            @Override
            public Boolean apply(WebDriver dr) {
                return ((JavascriptExecutor) driver).executeScript(
                        "return document.readyState"
                ).equals("complete");
            }
        };
    
        //Wait until using Predicate interface of com.google.common.base package
        wait.until(f);
        
    }
    
    
    public void navigate(String url, int waitSeconds){
        
        WebDriver driver = getDriver();
        
        String fullUrl = getMainUrl() + url;
        
        Reporter reporter = getReporter();
        reporter.logEvent(LogLevel.INFO,"navigate to: " + fullUrl);

        driver.get(fullUrl);

        FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver);
        wait.withTimeout(waitSeconds, TimeUnit.SECONDS);
        wait.pollingEvery(250, TimeUnit.MILLISECONDS);
        wait.ignoring(NoSuchElementException.class);
    
        Function<WebDriver, Boolean> f = new Function<WebDriver, Boolean>() {
        
            @Override
            public Boolean apply(WebDriver dr) {
                return ((JavascriptExecutor) driver).executeScript(
                        "return document.readyState"
                ).equals("complete");
            }
        };
    
        //Wait until using Predicate interface of com.google.common.base package
        wait.until(f);
    
    }
    
    protected void navigate(String url){
        navigate(url,5);
    }
    
    
    protected void navigate(String url, By by){
        navigate(url, by, 5);
    }
    
    
    
    protected void navigate(String url, By by, int waitSeconds){
        WebDriver driver = getDriver();
        driver.get(getMainUrl() + url);
        FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver);
        wait.withTimeout(waitSeconds, TimeUnit.SECONDS);
        wait.pollingEvery(250, TimeUnit.MILLISECONDS);
        wait.ignoring(NoSuchElementException.class);
    
        Function<WebDriver, Boolean> f = new Function<WebDriver, Boolean>() {
        
            @Override
            public Boolean apply(WebDriver dr) {
                WebElementEx elementEx = new WebElementExImpl(by);
                return elementEx.isPresent();
            }
        };
    
        //Wait until using Predicate interface of com.google.common.base package
        wait.until(f);
    }
    
    protected void switchToLatestWindow(){
        WebDriver driver = getDriver();
        wait(1);
        String lastwinHandle ="";
        Iterator<String> windleHandles = driver.getWindowHandles().iterator();
        while (windleHandles.hasNext())   {
            lastwinHandle = windleHandles.next();
        }
        driver.switchTo().window(lastwinHandle);
    }
    
    protected  void switchToDefaultContent(){
        WebDriver driver = getDriver();
        driver.switchTo().defaultContent();
    }
    
    protected  void switchToFrame(WebElementEx frame){
        WebDriver driver = getDriver();
        driver.switchTo().frame(frame.getElement());
    }
    
    
    protected void closeCurrentWindowAndSwitchToPrevious(){
        WebDriver driver = getDriver();
        
        driver.close();
        switchToLatestWindow();
        driver.navigate().refresh();
    
        FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver);
        wait.withTimeout(10, TimeUnit.SECONDS);
        wait.pollingEvery(250, TimeUnit.MILLISECONDS);
        wait.ignoring(NoSuchElementException.class);
    
        Function<WebDriver, Boolean> f = new Function<WebDriver, Boolean>() {
        
            @Override
            public Boolean apply(WebDriver dr) {
                return ((JavascriptExecutor) driver).executeScript(
                        "return document.readyState"
                ).equals("complete");
            }
        };
    
        
        wait.until(f);
        
    }
    
    public void wait(int seconds){
        Reporter reporter = getReporter();
        if (null != reporter) {
            reporter.logEvent(LogLevel.INFO,"wait  " + seconds + " s");
        }
    
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public void waitOnLoading(int seconds){
    
        LocalDateTime begin = LocalDateTime.now();
        
        
        Reporter reporter = getReporter();
        reporter.logEvent(LogLevel.INFO,"wait on loading for " + seconds + " s");
        
    
        WebDriver driver = getDriver();
        FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver);
        wait.withTimeout(seconds, TimeUnit.SECONDS);
        wait.pollingEvery(250, TimeUnit.MILLISECONDS);
        wait.ignoring(NoSuchElementException.class);
    
        Function<WebDriver, Boolean> f = new Function<WebDriver, Boolean>() {
        
            @Override
            public Boolean apply(WebDriver dr) {
                return ((JavascriptExecutor) driver).executeScript(
                        "return document.readyState"
                ).equals("complete");
            }
        };
        
        wait.until(f);
    
    
        LocalDateTime end = LocalDateTime.now();
        
        Duration d =  Duration.between(begin, end);
        System.out.println("duration of page load waiting: "+d.toMillis());
        
    }
    
    protected void waitOnLoading(int seconds, WebElementEx elementEx){
        
        LocalDateTime begin = LocalDateTime.now();
        
        Reporter reporter = getReporter();
        reporter.logEvent(LogLevel.INFO,"begin to wait " + seconds + "s for page loading");
        
        
        WebDriver driver = getDriver();
        FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver);
        wait.withTimeout(seconds, TimeUnit.SECONDS);
        wait.pollingEvery(1000, TimeUnit.MILLISECONDS);
        wait.ignoring(NoSuchElementException.class);
        
        Function<WebDriver, Boolean> f = new Function<WebDriver, Boolean>() {
            
            @Override
            public Boolean apply(WebDriver dr) {
                try {
                    return elementEx.isPresent(1);
                } catch (NoSuchElementException E) {
                    return false;
                }
            }
        };
        
        wait.until(f);
        
        LocalDateTime end = LocalDateTime.now();
        Duration d =  Duration.between(begin, end);
        reporter.logEvent(LogLevel.INFO,"actual duration of page loading: " + d.toMillis()+ "ms" );
        
    }
    
    
}