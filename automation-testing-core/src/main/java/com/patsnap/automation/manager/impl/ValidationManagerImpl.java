package com.patsnap.automation.manager.impl;

import com.patsnap.automation.annotation.ValidationConfig;
import com.patsnap.automation.entity.Checkpoint;
import com.patsnap.automation.entity.Iteration;
import com.patsnap.automation.entity.TestResult;
import com.patsnap.automation.enums.RunStatus;
import com.patsnap.automation.enums.ValidationRule;
import com.patsnap.automation.manager.ValidationManager;
import com.patsnap.automation.utils.ExcelUtil;
import com.patsnap.automation.utils.ResourceUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by liuyikai on 2017/9/7.
 */
@Service
public class ValidationManagerImpl  implements ValidationManager {
    
    
    @Override
    public List<Checkpoint> getCheckpointList(Method method ) throws Exception {
        List<Checkpoint> checkpointList = new ArrayList<>();
        
        try {
            ValidationConfig validationRule = method.getAnnotation(ValidationConfig.class);
            if (null != validationRule) {
        
                File checkpointConfigFile = ResourceUtil.LoadResourceAsFile(method.getDeclaringClass(), validationRule.value());
                ArrayList<HashMap<String, String>> ruleRawInfoList
                        = ExcelUtil.loadDataFromExcel(checkpointConfigFile, validationRule.sheetName());
                
                if (!CollectionUtils.isEmpty(ruleRawInfoList)){
                    for (HashMap<String, String> ruleInfo: ruleRawInfoList){
                        Checkpoint checkpoint = new Checkpoint();
                        checkpoint.setName(ruleInfo.get("name"));
                        ValidationRule rule = ValidationRule.getRuleByExpression(ruleInfo.get("rule"));
                        checkpoint.setRuleType(rule.getCode());
//                        checkpoint.setRuleDescription(rule.getDescription());
                        checkpoint.setJsonPath(ruleInfo.get("jsonPath"));
                        
                        checkpointList.add(checkpoint);
                    }
                }
            }
            
        } catch (Exception e) {
            //todo handel exception
            e.printStackTrace();
        }
        
        return checkpointList;
        
    }
    
   
  
    @Override
    public void validate(Iteration iteration) {
        
        iteration.getCheckpointList().stream()
                .filter(cp -> cp.isNotEvaluated()).forEach(cp -> validateSingleCheckpoint(cp, iteration.getTestcaseResult()));
        
        evaluateOverallStatus(iteration);
        
    }
    
    
    private void evaluateOverallStatus(Iteration iteration) {
        
        if (RunStatus.ERROR.getDesc().equals(iteration.getStatus())){
            return;
        }
        
        if (CollectionUtils.isEmpty(iteration.getCheckpointList())){
            iteration.setStatus(RunStatus.WARNING.getDesc());
        } else  {
            Checkpoint failedCheckpoint
                    = iteration.getCheckpointList().stream()
                    .filter(cp -> !cp.isPassed()).findAny().orElse(null);
            if (null != failedCheckpoint) {
                iteration.setStatus(RunStatus.FAILED.getDesc());
            } else {
                iteration.setStatus(RunStatus.PASSED.getDesc());
            }
        }
    }
    
    
    
    
    @Deprecated
    private  void validateSingleCheckpoint(Checkpoint checkpoint, TestResult testResult) {
    
        //todo
        Object actualValue;
        try {
            // 如果返回值是null， 则没有校验的必要
            if (null == testResult.getTestCaseReturnObject()){
                checkpoint.setPassed(false);
                checkpoint.setActualValue("Null");
        
            } else  {
                Object result = testResult.getTestCaseReturnObject();
   
                Class clazz = result.getClass();
                if (clazz.isPrimitive()){
                    actualValue = result;
                    checkpoint.setActualValue(actualValue.toString());
                } else {
                    actualValue = JSONPath.eval(result, checkpoint.getJsonPath());
                    checkpoint.setActualValue(JSON.toJSONString(actualValue));
                }
        
                ValidationRule rule = ValidationRule.getRuleByCode(checkpoint.getRuleType());
//                boolean isValid = validate(checkpoint.getExpectedValue(),actualValue,rule);
//                checkpoint.setPassed(isValid);
 
            }
        } catch (Exception e){
            checkpoint.setErrorMessage(e.getClass().getName() + ": " + e.getMessage());
            checkpoint.setPassed(false);
        } finally {
            checkpoint.setEvaluated(true);
            checkpoint.setTimeStamp(LocalDateTime.now());
        }
        
 
    }
    
    
    @Deprecated
    private boolean validate(String expectedValue, Object actualValue, ValidationRule rule){
        
//        switch (rule) {
//
//
//
//        }
        return false;
    }

    
    
}
