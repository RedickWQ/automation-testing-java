package com.patsnap.automation.patentApi.response.family;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.patsnap.automation.patentApi.bean.family.FamilyCount;
import com.patsnap.automation.patentApi.response.base.BaseResponse;
import lombok.Data;

import java.util.List;

/**
 * Created by  Liuyikai (Alex) on 2017/11/6.
 */
@Data
public class FamilyCountRes  extends BaseResponse {
    
    @JsonProperty("result")
    private List<FamilyCount> result;
    
}
