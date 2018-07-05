package com.patsnap.automation.context;

import com.patsnap.automation.entity.TestcaseRuntimeInstance;
import com.patsnap.automation.report.Reporter;
import com.patsnap.automation.entity.EnvironmentVariable;
import com.patsnap.automation.entity.Iteration;

import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

import lombok.Data;

/**
 *
 * @author liuyikai
 * @date 2017/9/7
 */

@Data
public class TestContext {

    protected TestcaseRuntimeInstance testcaseRuntimeInstance;

    protected Iteration iteration;
    
    private RestTemplate restTemplate;
    
    protected EnvironmentVariable environmentVariable;
    
    protected Reporter reporter;
    
    protected HashMap<String, Object> globalVariables = new HashMap<>();
    
    public void endContext(){
    
    }
    
}
