package com.patsnap.automation.entity;

import com.patsnap.automation.report.ExtentReportManager;

import com.alibaba.fastjson.annotation.JSONField;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Liuyikai (Alex)
 * @date 2017/10/13
 */


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestSuite {
    
    /**
     * UUID for local storage
     */
    private String id;
    
    
    private String name;
    
    /**
     * 单个用户一次批量执行一个测试集
     */
    private String sessionId;
    
    /**
     * 执行前的基础信息
     */
    private List<Testcase> testcaseList;
    
    /**
     * 执行时的结果信息
     */
    private List<TestcaseRuntimeInstance> testcaseRuntimeInstanceList;
    
    /**
     * 作用于所有testcase
     */
    private EnvironmentVariable environmentVariable;
    
    /**
     * 当前suite中的case是否并发运行
     */
    @JSONField(name="isCaseParallel")
    private boolean isCaseParallel;
    
    /**
     * 每个case中的多个iteration是否并发运行
     */
    @JSONField(name="isIterationParallel")
    private boolean isIterationParallel;
    
    @JSONField(name="isRunning")
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
