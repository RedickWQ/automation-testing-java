package com.patsnap.automation.report;

import com.alibaba.fastjson.annotation.JSONField;

import java.nio.file.attribute.FileTime;

import lombok.Builder;
import lombok.Data;

/**
 * Created by  Liuyikai (Alex) on 2017/10/23.
 */
@Data
@Builder
public class HtmlReportInfo {
    
    private String name;
    
    @JSONField(serialize = false)
    private FileTime createTime;
    
}
