package com.patsnap.automation.entity;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by liuyikai on 2017/9/4.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Testcase {
    
    
    
    private String name;
    
    private String componentName;
    
    private String description;
    
    
    private String entryPoint;
    
    private String author;
    
    private int caseType;
    
//    private String category;
    
    private List<String> tags;
    
    @JSONField(name="isDatasourceConfigured")
    private boolean isDatasourceConfigured = false;
    
    
    /**
     * 返回值的数据类型描述
     */
    private String returnValueType;
    
    
    
    private List<Iteration> iterationList;
    
    
//    /**
//     * 仅用作页面展示， 执行时会被set到每个iteration上
//     */
////    private List<Checkpoint> checkpointList;
    
    
    private EnvironmentVariable environmentVariable;
    
}
