package com.patsnap.automation.entity;

import com.patsnap.automation.enums.RunStatus;
import com.patsnap.automation.log.LogItem;

import com.alibaba.fastjson.annotation.JSONField;

import org.springframework.util.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by liuyikai on 2017/9/6.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Iteration {

    
    private List<TestParameter> testParameterList;
    
    
    // current exec passed
    private String status;
    
    
    private List<Checkpoint> checkpointList = new ArrayList<>();
    
    
    private List<LogItem> logItemList = new ArrayList<>();
    
    //return from testcase method
    private TestResult testcaseResult;
    
    private String stackTrace;
    
    private String errMessage;
    
    private Long threadId;
    
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    
    private Duration duration;
    
 
    public void setError(Throwable e){
        this.setErrMessage(!StringUtils.isEmpty(e.getMessage())? e.getMessage(): "No err message.");
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        this.setStackTrace(sw.toString());
        this.status = RunStatus.ERROR.getDesc();
    }
    
    
    
    
    
}
