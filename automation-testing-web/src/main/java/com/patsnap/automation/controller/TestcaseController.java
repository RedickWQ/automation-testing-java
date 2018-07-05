package com.patsnap.automation.controller;

import com.patsnap.automation.common.AjaxResult;
import com.patsnap.automation.entity.Testcase;
import com.patsnap.automation.entity.TestcaseRuntimeInstance;
import com.patsnap.automation.manager.TestComponentManager;
import com.patsnap.automation.utils.JsonUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import net.minidev.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.servlet.http.HttpSession;

/**
 * Created by liuyikai on 2017/9/5.
 */
@RestController
@RequestMapping("/api/testcase")
public class TestcaseController {
    
    
    
    @Autowired
    TestComponentManager testComponentManager;
    
    @RequestMapping(value = "/list",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public AjaxResult getTestCaseList(){
        AjaxResult ajaxResult = new AjaxResult();
        
        try {
            List<Testcase> testcaseList = testComponentManager.getTestCaseList();
            ajaxResult.setSuccessResult(testcaseList);
            
        } catch (Exception e) {
            ajaxResult.setError(e);
        }
        
        return ajaxResult;
    }
    
    @RequestMapping(value = "/detail", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public AjaxResult getTestCaseDetail(@RequestParam("componentName") String componentName,
                                    @RequestParam("entryPoint") String entryPoint){
        AjaxResult ajaxResult = new AjaxResult();
        try {
            Testcase testcase = testComponentManager.getTestCase(componentName, entryPoint);
            ajaxResult.setSuccessResult(testcase);
        } catch (Exception e) {
            ajaxResult.setError(e);
        }
    
        return ajaxResult;
        
    }
    
    
    
    @RequestMapping(value = {"/run"},
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public AjaxResult run(HttpSession httpSession,
                      @RequestParam(name= "isParallel", required = false, defaultValue = "true") boolean isParallel,
                      @RequestParam(name="logLevel", required = false, defaultValue = "1") int logLevel,
                      @RequestBody Testcase testcase ) {
        AjaxResult ajaxResult = new AjaxResult();
        try {
            
            String sessionId =  httpSession.getId();
            TestcaseRuntimeInstance testcaseRuntimeInstance = testComponentManager.invokeTestCase(testcase, isParallel, logLevel);
            String key = testcase.getComponentName() + "-" + testcase.getEntryPoint();
            
            httpSession.setAttribute(key, testcaseRuntimeInstance);
            
            ajaxResult.setSuccessResult(testcaseRuntimeInstance);
        
        } catch (Exception e){
            ajaxResult.setError(e);
        }
    
        return ajaxResult;
        
    }
    
    
    @RequestMapping(value = {"/getExecutionResult"} ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public AjaxResult getExecutionResult(HttpSession httpSession,
                                     @RequestParam("componentName") String componentName,
                                     @RequestParam("entryPoint") String entryPoint) {
    
        AjaxResult ajaxResult = new AjaxResult();
        try {
    
            String key = componentName + "-" + entryPoint;
            TestcaseRuntimeInstance testcaseRuntimeInstance = (TestcaseRuntimeInstance) httpSession.getAttribute(key);
            ajaxResult.setSuccessResult(testcaseRuntimeInstance);
        } catch (Exception e) {
            ajaxResult.setError(e);
        }
    
        return ajaxResult;

    }
    
    
    @RequestMapping(value = {"/search"}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public AjaxResult search(@RequestParam("keyword") String keyword){
        
        AjaxResult ajaxResult = new AjaxResult();
        try {
        
            List<Testcase> testcaseList = testComponentManager.search(keyword);
            ajaxResult.setSuccessResult(testcaseList);
        } catch (Exception e){
            ajaxResult.setError(e);
        }
    
        return ajaxResult;
        
    }
    
    @RequestMapping(value = {"/saveData"},
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public AjaxResult saveData(@RequestBody Testcase testcase){
    
        AjaxResult ajaxResult = new AjaxResult();
        try {
            testComponentManager.saveTestData(testcase);
            ajaxResult.setSuccessResult("Success");
        } catch (Exception e){
            ajaxResult.setError(e);
        }
    
        return ajaxResult;
        
    }
    
    
    
    
}
