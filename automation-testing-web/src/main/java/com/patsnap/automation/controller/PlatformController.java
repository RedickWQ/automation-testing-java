package com.patsnap.automation.controller;

import com.patsnap.automation.common.AjaxResult;
import com.patsnap.automation.entity.Testcase;
import com.patsnap.automation.manager.TestComponentManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author liuyikai(Alex)
 * @date 2018/1/3
 */
@RestController
@RequestMapping("/platform/testcase")
public class PlatformController {
    
    @Autowired
    TestComponentManager testComponentManager;
    
    @RequestMapping(value = {"/search"}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<Testcase> search(@RequestParam("keyword") String keyword) throws Exception {
        
        
            List<Testcase> testcaseList = testComponentManager.search(keyword);
       return testcaseList;
       
    }
    
    
}
