package com.patsnap.automation.report;

import com.patsnap.automation.contants.Constant;
import com.patsnap.automation.entity.TestSuite;
import com.patsnap.automation.utils.CommonUtil;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import java.io.IOException;
import java.nio.file.Paths;

import lombok.Data;

/**
 *
 * @author Liuyikai (Alex)
 * @date 2017/10/20
 */
@Data
public class ExtentReportManager {
    
    private ExtentReports extentReport;
    
    private String reportName;
    
    public ExtentReportManager () {
    
    
    }
    
    
    public void initReportInstance(TestSuite testSuite) {
        if (extentReport == null) {
            try {
                ExtentReportUtil.initReportRootDir();
            } catch (IOException e) {
                e.printStackTrace();
            }
    
            String timestamp = CommonUtil.getCurrentTimeStamp();
            reportName = testSuite.getName() + "_" + timestamp ;
    
            String reportPath = Paths.get(Constant.REPORT_ROOT_DIR, reportName + ".html").toString();
    
            ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(reportPath);
            htmlReporter.config().setDocumentTitle(reportName);
            htmlReporter.config().setEncoding("utf-8");
            htmlReporter.config().setReportName(reportName);
    
            extentReport = new ExtentReports();
            extentReport.attachReporter(htmlReporter);
            
            //set env
            extentReport.setSystemInfo("CaseRunModel", testSuite.isCaseParallel()? "Parallel" : "Sequential");
            extentReport.setSystemInfo("IterationRunModel", testSuite.isIterationParallel()? "Parallel" : "Sequential");
            
        }
        
//        ExtentTest demotest =  extentReport.createTest("sampletest");
//        demotest.log(Status.INFO, "This step shows usage of log(status, details)");
        
        
    }
    
    public synchronized ExtentTest startTestcase(String testcaseName, String description){
        
        return this.extentReport.createTest(testcaseName,description);
        
    }
    
    
    
    
    public synchronized void  endTestSuite() throws IOException {
        if (extentReport != null) {
            extentReport.flush();
    
            ExtentReportUtil.replaceStaticResourceReferenceLink(reportName);
            
        }
    }
    
    
    
    
    
}
