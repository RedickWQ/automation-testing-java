package com.patsnap.automation.patentApi.bean.family;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by liuyikai on 2017/9/14.
 */
@Data
public class FamilyMap {
    @JsonProperty("total")
    private Integer total;
    
    @JsonProperty("patent_id")
    private String patent_id;
    
    @JsonProperty("list")
    private List<Map<String, Integer>> list;
    
}
