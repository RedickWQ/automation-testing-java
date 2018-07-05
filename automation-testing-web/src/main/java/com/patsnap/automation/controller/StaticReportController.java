package com.patsnap.automation.controller;

import com.patsnap.automation.common.AjaxResult;
import com.patsnap.automation.report.ExtentReportUtil;
import com.patsnap.automation.report.HtmlReportInfo;
import com.patsnap.automation.utils.JsonUtil;

import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * Created by  Liuyikai (Alex) on 2017/10/18.
 */
@RestController
@RequestMapping("/api/report")
public class StaticReportController {
    
    @RequestMapping(value = "/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public AjaxResult staticReportDemo() throws IOException {
        AjaxResult ajaxResult = new AjaxResult();
        List<HtmlReportInfo> reportList = ExtentReportUtil.getReportList();
        if (!CollectionUtils.isEmpty(reportList)){
            ajaxResult.setSuccessResult(ExtentReportUtil.getReportList());
        } else  {
            ajaxResult.setSuccess(false);
        }
        
        return ajaxResult;
    }
    
}
