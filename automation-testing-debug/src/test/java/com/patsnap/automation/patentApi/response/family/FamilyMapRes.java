package com.patsnap.automation.patentApi.response.family;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.patsnap.automation.patentApi.bean.family.FamilyMap;
import lombok.Data;

/**
 * Created by liuyikai on 2017/9/14.
 */
@Data
public class FamilyMapRes {
    @JsonProperty("data")
    private FamilyMap data;
}
