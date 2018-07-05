package com.patsnap.automation.patentApi.bean.family;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by  Liuyikai (Alex) on 2017/11/6.
 */
@Data
public class FamilyCount {
    
    @JsonProperty("patent_id")
    private String patent_id;
    
    @JsonProperty("count")
    private Integer count;
    
}
