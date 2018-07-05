package com.patsnap.automation.log;

import com.alibaba.fastjson.annotation.JSONField;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * Created by  Liuyikai (Alex) on 2017/10/9.
 */
@Data
public class LogItem {
    
    @JSONField(format = "HH:mm:ss")
    private LocalDateTime timestamp;
    
    private String content;
    
    private LogLevel level;
    
    
    
}
