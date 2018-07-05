package com.patsnap.automation.controller;

import com.patsnap.automation.common.AjaxResult;
import com.patsnap.automation.utils.JsonUtil;
import com.patsnap.automation.utils.TestEnvUtil;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by liuyikai on 2017/9/28.
 */
@RestController
@RequestMapping("/api/environment")
public class EnviromentController {
    
    @RequestMapping(value = "/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public AjaxResult getApplicationEnvironmentList(){
        AjaxResult result = new AjaxResult();
        
        try {
            result.setSuccessResult(TestEnvUtil.getApplicationEnvList());
        } catch (Exception e) {
            result.setError(e);
        }
        return result;
    }
    

    
}
