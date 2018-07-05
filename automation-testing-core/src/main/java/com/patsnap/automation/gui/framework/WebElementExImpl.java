package com.patsnap.automation.gui.framework;

import com.patsnap.automation.log.LogEvent;
import com.patsnap.automation.context.TestContextManager;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by  Liuyikai (Alex) on 2017/10/9.
 */
@Component("WebElementEx")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class WebElementExImpl implements WebElementEx {
    private WebDriver driver;
    private WebElement element;
    private By locator = null;
    private NoSuchElementException noSuchElementException;
    
    private String givenName = null;
    
    
    public WebElementExImpl()
    {
    
    }
    
    public WebElementExImpl(WebElement element) {
        this.driver = ((GuiTestContext) TestContextManager.getContext()).returnWebDriver();
        this.element = element;
    }
    
    public WebElementExImpl(By locator) {
        this.element = null;
        this.driver = ((GuiTestContext) TestContextManager.getContext()).returnWebDriver();
        this.locator = locator;
        this.isPresent(3);
    }
    
    
    @Override
    public void setLocator(By locator){
        this.locator = locator;
    }
    
    @Override
    public WebElementEx setGivenName(String name) {
        this.givenName = name;
        return this;
    }
    
    @Override
    public String getGivenName() {
        return this.givenName;
    }
    
    @Override
    public String getLocatorInfo() {
        return this.locator.toString();
    }
    
    /*
         * it is checking that an element is present on the DOM of a page or not.
         * That does not necessarily mean that the element is visible.
         * ExpectedConditions will return true once the element is found in the DOM.
         */
    @Override
    public boolean isPresent(long seconds){
        if (seconds < 0) {
            return false;
        }
        try	{
            WebElement element = (WebElement) (new WebDriverWait(driver, seconds))
                    .until((Function<? super WebDriver, ? extends Object>) ExpectedConditions.presenceOfElementLocated(locator));
            this.element = element;
            return true;
        }
        catch (NoSuchElementException ex){
            this.noSuchElementException = new NoSuchElementException(locator.toString());;
            return false;
        }
        catch (TimeoutException ex){
            this.noSuchElementException = new NoSuchElementException(locator.toString());
            return false;
        }
    }
    
    @Override
    public boolean isPresent(){
        return isPresent(3); //by default 3 seconds
    }
    
    @Override
    public boolean isPresent(WebElementEx parentWebElementEx, long waitTimeinSeconds){
        if (waitTimeinSeconds < 0) {
            return false;
        }
        
        try {
            WebElement subNode = (WebElement) new WebDriverWait(driver,waitTimeinSeconds)
                    .until(
                            (Function<? super WebDriver, ? extends Object>) ExpectedConditions.presenceOfNestedElementLocatedBy(
                                    parentWebElementEx, locator));
            return true;
        }catch (TimeoutException ex){
            this.noSuchElementException = new NoSuchElementException(locator.toString());
            return false;
        }
        
    }
    
    
    @Override
    public WebElementEx waitToPresent(long seconds){
        try	{
            WebElement element = (WebElement) new WebDriverWait(driver, seconds)
                    .until((Function<? super WebDriver, ? extends Object>) ExpectedConditions.presenceOfElementLocated(locator));
            this.element = element;
            return this;
        }
        catch (NoSuchElementException ex){
            this.noSuchElementException = new NoSuchElementException(locator.toString());;
            return this;
        }
        catch(StaleElementReferenceException ex){
            this.sleep(0,500);
            return waitToPresent(seconds);
        }
        catch (TimeoutException ex){
            this.noSuchElementException = new NoSuchElementException(locator.toString());
            return this;
        }
    }
    
    @Override
    public WebElementEx waitToBeVisible(long waitTimeInSeconds){
        try	{

    
            WebElement element= (WebElement)
            new WebDriverWait(driver, waitTimeInSeconds)
                    .until((Function<? super WebDriver, ? extends Object>) ExpectedConditions.visibilityOfElementLocated(locator));
                   
            
 
            
            this.element = element;
            return this;
        }
        catch (NoSuchElementException ex){
            this.noSuchElementException = new NoSuchElementException(locator.toString());;
            return this;
        }
        catch(StaleElementReferenceException ex){
            this.sleep(0,500);
            return waitToBeVisible(waitTimeInSeconds);
        }
        catch (TimeoutException ex){
            this.noSuchElementException = new NoSuchElementException(locator.toString());
            return this;
        }
        
    }
    
    
    
    @Override
    public WebElementEx waitToPresent(WebElementEx parentWebElementEx, long seconds){
        try	{
    
            WebElement element = (WebElement) new WebDriverWait(driver, seconds)
                    .until((Function<? super WebDriver, ? extends Object>)ExpectedConditions.presenceOfElementLocated(locator));
            this.element = element;
            return this;
        }
        catch (NoSuchElementException ex){
            this.noSuchElementException = new NoSuchElementException(locator.toString());;
            return this;
        }
        catch(StaleElementReferenceException ex){
            this.sleep(0,500);
            return waitToPresent(parentWebElementEx, seconds);
        }
        catch (TimeoutException ex){
            this.noSuchElementException = new NoSuchElementException(locator.toString());
            return this;
        }
    }
    
    
    @Override
    public void setValueByScript(String value){
        if (element == null) {
            throw this.noSuchElementException;
        }
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].removeAttribute('readonly')", element);
        jsExecutor.executeScript("arguments[0].value=arguments[1]",element,value);
        //jsExecutor.executeScript("$(arguments[0]).change();", element);
    }
    
    
    
    
    @Override
    public void mouseHover(){
        if (element == null) {
            throw this.noSuchElementException;
        }
        
        Actions builder = new Actions(driver);
        builder.moveToElement(this.element).build().perform();
        
        
    }
    
    @Override
    public void mouseHover(int seconds) {
        mouseHover();
        sleep(seconds, 0);
    }
    
    
    
    
    @Override
    public WebElementEx sleep(int seconds){
        sleep(seconds, 0);
        return this;
    }
    
    @Override
    public WebElementEx sleep(int seconds, int millisecond){
        if (seconds <= 0) {
            return this;
        }
        try {
            Thread.sleep(seconds*1000 + millisecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
        
    }
    
    
    @Override
    public WebElementEx highlight(){
        if (element == null) {
            throw this.noSuchElementException;
        }
        ((JavascriptExecutor)driver).executeScript("arguments[0].setAttribute('style', arguments[1]);",element, "border: solid 2px red;");
        return this;
    }
    
    
    @Override
    public WebElementEx highlight(int seconds){
        if (element == null) {
            throw this.noSuchElementException;
        }
        if (seconds <= 0) {
            return this;
        }
        
        String originalStyle = element.getAttribute("style");
        
        JavascriptExecutor js = (JavascriptExecutor) driver;
        
        for (int i=0;i<seconds;i++) {
            js.executeScript("arguments[0].setAttribute('style', 'border: 2px solid red;');", element);
            try {
                Thread.sleep(500);
            }
            catch (InterruptedException e) {}
            js.executeScript("arguments[0].setAttribute('style', '" + originalStyle + "');", element);
            try {
                Thread.sleep(500);
            }
            catch (InterruptedException e) {}
        }
        
        System.out.println(this.getClass().toString());
        
        return this;
    }
    
    @Override
    @LogEvent
    public void doubleClick(){
        if (element == null) {
            throw this.noSuchElementException;
        }
        
        Actions action = new Actions(driver);
        action.doubleClick(element).perform();
    }
    
    
    
    @Override
    public WebElementEx findElementEx(By by) {
        WebElementEx temp =  (WebElementEx)findElement(by);
        temp.setLocator(by);
        return temp;
    }
    
    
    @Override
    public boolean isClickable(){
        if (element == null) {
            throw this.noSuchElementException;
        }
        
        try	{
            (new WebDriverWait(driver, 3))
                    .until((Function<? super WebDriver, ? extends Object>) ExpectedConditions.elementToBeClickable(locator));
            return true;
        }
        catch (NoSuchElementException ex){
            return false;
        }
        catch (TimeoutException ex){
            return false;
        }
    }
    
    
    @Override
    public boolean isVisible(){
        //if (element == null)   throw this.noSuchElementException;
        
        try	{
             new WebDriverWait(driver, 1)
                    .until((Function<? super WebDriver, ? extends Object>) ExpectedConditions.visibilityOfElementLocated(locator));
            
            return true;
        }
        catch (NoSuchElementException ex){
            return false;
        }
        catch (TimeoutException ex){
            return false;
        }
        
    }
    
    
    @Override
    public void onBlur() {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].blur();", element);
    }
    
    @Override
    public WebElementEx $(By locator){
        return  this.findElementEx(locator);
    }
    
    
    
    @LogEvent
    @Override
    public void check() {
        if (this.element.isEnabled() && !this.element.isSelected()){
            this.element.click();
        }
    }
    
    @LogEvent
    @Override
    public void unCheck() {
        if (this.element.isEnabled() && this.element.isSelected()){
            this.element.click();
        }
    }
    
    @LogEvent
    @Override
    public void click() {
        if (element == null) {
            throw this.noSuchElementException;
        }
        highlight();
        element.click();
    }
    
    @Override
    public void submit() {
        if (element == null) {
            throw this.noSuchElementException;
        }
        element.submit();
        
    }
    
    @LogEvent
    @Override
    public void sendKeys(CharSequence... keysToSend) {
        if (element == null) {
            throw this.noSuchElementException;
        }
        element.clear();
        element.sendKeys(keysToSend);
    }
    
    @Override
    public void setAttribute(String key, String value)
    {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String stjs = "arguments[0].setAttribute('"+key+"','"+value+"')";
        js.executeScript(stjs, element);
    }
    
    @LogEvent
    @Override
    public void clear() {
        if (element == null) {
            throw this.noSuchElementException;
        }
        element.clear();
    }
    
    @Override
    public String getTagName() {
        if (element == null) {
            throw this.noSuchElementException;
        }
        return element.getTagName();
        
    }
    
    @Override
    public String getAttribute(String name) {
        if (element == null) {
            throw this.noSuchElementException;
        }
        return element.getAttribute(name);
        
        
    }
    
    @Override
    public boolean isSelected() {
        if (element == null) {
            throw this.noSuchElementException;
        }
        return element.isSelected();
        
        
    }
    
    @Override
    public boolean isEnabled() {
        if (element == null) {
            throw this.noSuchElementException;
        }
        return element.isEnabled();
    }
    
    
    @Override
    public String getText() {
        if (element == null) {
            throw this.noSuchElementException;
        }
        return element.getText();
    }
    
    @Override
    public List<WebElement> findElements(By by) {
        if (element == null) {
            throw this.noSuchElementException;
        }
        
        List<WebElement> elements = element.findElements(by);
        return elements;
    }
    
    
    @Override
    public List<WebElementEx> findElementExs(By by) {
        List<WebElement> elements = findElements(by);
        List<WebElementEx> result = new ArrayList<>(elements.size());
        for (WebElement element : elements) {
            result.add(new WebElementExImpl(element));
        }
        return result;
    }
    
    @Override
    public WebElement findElement(By by) {
        if (element == null) {
            throw this.noSuchElementException;
        }
        WebElement temp = element.findElement(by);
        return new WebElementExImpl(temp);
    }
    
    @Override
    public boolean isDisplayed() {
        if (element == null) {
            throw this.noSuchElementException;
        }
        
        return element.isDisplayed();
    }
    
    @Override
    public Point getLocation() {
        if (element == null) {
            throw this.noSuchElementException;
        }
        return element.getLocation();
    }
    
    @Override
    public Dimension getSize() {
        if (element == null) {
            throw this.noSuchElementException;
        }
        return element.getSize();
    }
    
    @Override
    public Rectangle getRect() {
        if (element == null) {
            throw this.noSuchElementException;
        }
        return element.getRect();
    }
    
    @Override
    public String getCssValue(String propertyName) {
        if (element == null) {
            throw this.noSuchElementException;
        }
        return element.getCssValue(propertyName);
    }
    
    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        if (element == null) {
            throw this.noSuchElementException;
        }
        return element.getScreenshotAs(target);
    }
    
    @Override
    public WebElement getElement()
    {
        return element;
    }
    
    @Override
    public WebElement inViewPort()
    {
        if (this.isVisible()){return this;}
        
        Coordinates coodinates = ((Locatable)this.getElement()).getCoordinates();
        coodinates.inViewPort();
        return this;
    }
    
    @LogEvent
    @Override
    public WebElementEx clickByJavaScript() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();",element);
        return this;
    }
    
    
    @Override
    public void checkByJavaScript(){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].checked = true;",element);
        
    }
    
    
}
