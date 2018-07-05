package com.patsnap.automation.patentApi.response.citation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.patsnap.automation.patentApi.bean.citation.CitationInfo;
import com.patsnap.automation.patentApi.response.base.BaseResponse;
import lombok.Data;

import java.util.List;

/**
 * Created by liuyikai on 2017/9/5.
 */
@Data
public class CitationInfoRsp extends BaseResponse {
    
    @JsonProperty("citation")
    private List<CitationInfo> citation;
    
    @JsonProperty("pn")
    private String pn;
    
    @JsonProperty("patent_id")
    private String patent_id;
    
    @JsonProperty("cite_count")
    private Integer cite_count;
    
    @JsonProperty("code")
    private String code;
    
    @JsonProperty("family_count")
    private Integer family_count;

}
