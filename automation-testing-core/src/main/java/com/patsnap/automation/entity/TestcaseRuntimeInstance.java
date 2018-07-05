package com.patsnap.automation.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.aventstack.extentreports.ExtentTest;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import com.patsnap.automation.log.LogLevel;
import lombok.Builder;
import lombok.Data;

/**
 * Created by liuyikai on 2017/9/6.
 */
@Data
@Builder
public class TestcaseRuntimeInstance {
    
    
    private Testcase testcase;
    
    @JSONField(serialize = false)
    private Object bean;
    
    @JSONField(serialize = false)
    private Method method;
    
    @JSONField(serialize = false)
    private Class clazz;
    
    @JSONField(serialize = false)
    private ExtentTest extentTest;
    
    
    /**
     * by default the level is info
     */
    private LogLevel logLevel = LogLevel.INFO;
    
    
    private List<Iteration> iterationList;
    
  
    //todo overall status
    private String status;
    
    /**
     * 记录当前run是由哪个前端请求触发的
     */
    private String sessionId;
    
    
    private Duration totalDuration;
    
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    
    
    
    
}
