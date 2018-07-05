package com.patsnap.automation.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by liuyikai on 2017/9/4.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestParameter {
    
    private String name;
    
    private String type;
    
    private String value;
    
}
