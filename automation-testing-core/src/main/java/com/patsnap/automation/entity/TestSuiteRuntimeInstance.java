package com.patsnap.automation.entity;

import com.patsnap.automation.report.ExtentReportManager;

import com.alibaba.fastjson.annotation.JSONField;

import java.time.Duration;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by  Liuyikai (Alex) on 2017/10/25.
 */

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor

//todo refactoring???
public class TestSuiteRuntimeInstance {
    
    
    private String sessionId;
    
    private TestSuite testSuite;
    
    
    private boolean isRunning;
    
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    
    private Duration duration;
    
    private String status;
    
    @JSONField(serialize = false)
    private ExtentReportManager extentReportManager;
    
    
    
    
    
}
