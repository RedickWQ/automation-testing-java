package com.patsnap.automation.patentApi.bean.citation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by liuyikai on 2017/9/5.
 */
@Data
public class CitationInfo {
    @JsonProperty("patent_id")
    private String patent_id;
    
    @JsonProperty("pn")
    private String pn;
    
    @JsonProperty("apno_count")
    private Integer apno_count;
    
    @JsonProperty("apno")
    private String apno;
    
    @JsonProperty("cite_count")
    private Integer cite_count;
    
    @JsonProperty("pbdt")
    private Integer pbdt;
    
    @JsonProperty("firstAssignee")
    private String firstAssignee;
    
    @JsonProperty("assignees")
    private List<String> assignees;
    
    @JsonProperty("isOriginal")
    private String isOriginal;
    
    @JsonProperty("firstNormalizeAssignee")
    private String firstNormalizeAssignee;
    
    @JsonProperty("normalizeAssignees")
    private List<String> normalizeAssignees;
    
    @JsonProperty("firstInventor")
    private String firstInventor;
    
    @JsonProperty("inventors")
    private List<String> inventors;
    
    @JsonProperty("firstIPC")
    private String firstIpc;
    
    @JsonProperty("ipcs")
    private List<String> ipcs;
}
