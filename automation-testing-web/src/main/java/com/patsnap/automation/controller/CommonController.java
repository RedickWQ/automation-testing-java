package com.patsnap.automation.controller;

import com.patsnap.automation.utils.TestEnvUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.env.Environment;
/**
 * Created by liuyikai on 2017/9/4.
 */

@RestController
@RequestMapping("/data")
public class CommonController {
    
    
    @RequestMapping("/ping")
    @ResponseBody
    public String ping() {
        return "success";
    }
    
    
    @Autowired
    Environment environment;
    
    @RequestMapping("/getPort")
    @ResponseBody
    public String getPort(){
   
        
        String port = environment.getProperty("local.server.port");
        return port;
    }
    
}
