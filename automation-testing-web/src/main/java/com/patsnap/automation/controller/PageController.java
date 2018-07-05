package com.patsnap.automation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author liuyikai
 * @date 2017/9/4
 */


@Controller
public class PageController {
    
    
    @RequestMapping(value= {"/"}, method = RequestMethod.GET )
    public String index(){
        return "redirect:/testcaseList";
    }
    
    @RequestMapping(value= {
            "/testcaseList" ,
            "/testSuiteList",
            "/testcaseDetail/**",
            "/testsuiteExecutionResult",
            "/testReportList"
            }, method = RequestMethod.GET )
    public String testcaseList(){
        return "index";
    }
  
    
}
