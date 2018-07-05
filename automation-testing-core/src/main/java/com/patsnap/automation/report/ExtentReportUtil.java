package com.patsnap.automation.report;

import com.patsnap.automation.contants.Constant;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Liuyikai (Alex)
 * @date 2017/10/20
 */
public class ExtentReportUtil {
    
    
    
    
    public static void initReportRootDir() throws IOException {
        FileUtils.forceMkdir(new File(Constant.REPORT_ROOT_DIR));
    }
    
    public static void replaceStaticResourceReferenceLink(String reportName) throws IOException {
        
        //init file
        String reportpath = Paths.get(Constant.REPORT_ROOT_DIR ,reportName + ".html").toString();
        File file = new File(reportpath);
        
        String reportContent =  FileUtils.readFileToString(file);
        
        //replace resourceReferenceLink
        String localResourceRoot = "/testreport";
        String cssExpression = "<link href='http(.*)extent.css' type='text/css' rel='stylesheet' />";
        String targetCssReference = "<link href='" + localResourceRoot + "/css/extent.css' type='text/css' rel='stylesheet' />";
        String jsExpression = "<script src='http(.*)extent.js' type='text/javascript'></script>";
        String targetJsReference = "<script src='" + localResourceRoot + "/js/extent.js' type='text/javascript'></script>";
        
        //remove junk report content
        String brandLogoExpression = "<a href=\"#!\" class=\"brand-logo blue darken-3\">Extent</a>";
        String targetBrandLogo = "<a href=\"#!\" class=\"brand-logo blue darken-3\">Patsnap</a>";
        String versionExpression = "<span class='label blue darken-3'>v3.0.7</span>";
        String targetVersion = "";
        
        reportContent = reportContent.replaceAll(cssExpression, targetCssReference);
        reportContent = reportContent.replaceAll(jsExpression, targetJsReference);
        reportContent = reportContent.replaceAll(brandLogoExpression, targetBrandLogo);
        reportContent = reportContent.replaceAll(versionExpression, targetVersion);
//        System.out.println(reportContent);
        
        //save back to file
        FileUtils.write(file, reportContent, false);
        
    }
    
    public static List<HtmlReportInfo> getReportList() throws IOException {
        
       //todo sort by creation time
        
        File rootDir = new File(Constant.REPORT_ROOT_DIR);
        if (rootDir.exists() && rootDir.isDirectory()){
            List<HtmlReportInfo> reportList = new ArrayList<>();
            File[] files = rootDir.listFiles();
    
            for (int i = 0;i < files.length;i ++) {
                File file = files[i];
                if (file.isFile()){
                    
                    Path path = Paths.get(file.getPath());
                    BasicFileAttributes attributes =
                            Files.readAttributes(path, BasicFileAttributes.class);
                    FileTime creationTime = attributes.creationTime();
    
                    HtmlReportInfo reportInfo = HtmlReportInfo.builder().name(file.getName())
                            .createTime(creationTime)
                            .build();
                    reportList.add(reportInfo);
                }
            }
            
            //sort by creation time desc
            return reportList.stream().sorted(new Comparator<HtmlReportInfo>() {
                @Override
                public int compare(HtmlReportInfo o1, HtmlReportInfo o2) {
                    return o1.getCreateTime().toMillis() > o2.getCreateTime().toMillis()? 1 : -1;
                }
            }.reversed()).collect(Collectors.toList());
        } else {
            return null;
        }
    }
    
    
    
}
