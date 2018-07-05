package com.patsnap.automation.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by  Liuyikai (Alex) on 2017/10/20.
 */
public class CommonUtil {
    
    public static String getCurrentTimeStamp(String format)
    {
        return new SimpleDateFormat(format).format(new Date());
        
    }
    
    public static String getCurrentTimeStamp(){
        return getCurrentTimeStamp("yyyy-MM-dd-HH-mm-ss");
    }
    
    public static String getGuid() {
        
        return UUID.randomUUID().toString();
    
    }
    
    
}
