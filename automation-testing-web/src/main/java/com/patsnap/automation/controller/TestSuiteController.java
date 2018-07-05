package com.patsnap.automation.controller;

import com.patsnap.automation.common.AjaxResult;
import com.patsnap.automation.entity.TestSuite;
import com.patsnap.automation.manager.TestsuiteManager;
import com.patsnap.automation.utils.JsonUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.ws.rs.QueryParam;

/**
 * Created by  Liuyikai (Alex) on 2017/10/13.
 */

@RestController
@RequestMapping("/api/testsuite")
public class TestSuiteController {
    
    
    @Autowired
    TestsuiteManager testsuiteManager;
    
    
    @RequestMapping(value = {"/run"},
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public AjaxResult run(HttpSession httpSession,
                      @RequestBody TestSuite testSuite ) {
    
        AjaxResult ajaxResult = new AjaxResult();
        try {
    
            TestSuite legacyRunningSuite = getRunningSuite(httpSession);
            if (legacyRunningSuite != null && legacyRunningSuite.isRunning()){
                throw new RuntimeException("Previous Suite is running! Please wait until it is finished");
            }
            
            String sessionId =  httpSession.getId();
            testSuite.setSessionId(sessionId);
            httpSession.setAttribute("RunningSuite", testSuite);
    
//            testsuiteManager.invokeTestSuite(testSuite);
            
            
            testsuiteManager.asyncRunTestSuite(testSuite);
            
            ajaxResult.setSuccessResult(testSuite);
    
        } catch (Exception e){
            ajaxResult.setError(e);
        }
    
        return ajaxResult;
    }
    
    
    private TestSuite getRunningSuite(HttpSession httpSession){
        Object testsuite =  httpSession.getAttribute("RunningSuite");
        return testsuite != null ? (TestSuite)testsuite : null;
    }
    
    
    @RequestMapping(value = {"/getExecutionResult"} , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public AjaxResult getExecutionResult(HttpSession httpSession){
        AjaxResult ajaxResult = new AjaxResult();
        
        try {
            
            TestSuite testSuite = getRunningSuite(httpSession);
            if (null != testSuite) {
                ajaxResult.setSuccessResult(testSuite);
            } else  {
                ajaxResult.setError(new RuntimeException("No suite is running"));
            }
            
        } catch (Exception e) {
            ajaxResult.setError(e);
        }
    
        return ajaxResult;
        
        
        
    }
    
    @RequestMapping(value = {"/save"},
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public AjaxResult saveTestSuite(@RequestBody TestSuite testSuite) {
        AjaxResult ajaxResult = new AjaxResult();
    
        try {
           TestSuite suite =  testsuiteManager.saveTestSuite(testSuite);
           ajaxResult.setSuccessResult(suite);
        
        } catch (Exception e) {
            ajaxResult.setError(e);
        }
    
        return ajaxResult;
        
    }
    
    @RequestMapping(value = {"/list"} ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public AjaxResult list() {
        AjaxResult ajaxResult = new AjaxResult();
    
        try {
            List<TestSuite> testSuiteList =  testsuiteManager.getTestSuiteList();
            ajaxResult.setSuccessResult(testSuiteList);
        
        } catch (Exception e) {
            ajaxResult.setError(e);
        }
    
        return ajaxResult;
    }
    
    @RequestMapping(value = {"/delete"},
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public AjaxResult delete(@QueryParam("id") String id){
    
        AjaxResult ajaxResult = new AjaxResult();
    
        try {
            testsuiteManager.deleteSuiteById(id);
            ajaxResult.setSuccessResult("success");
        } catch (Exception e) {
            ajaxResult.setError(e);
        }
    
        return ajaxResult;
        
        
    }
    
    
}
