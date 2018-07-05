package com.patsnap.automation.log;

/**
 * @author liuyikai(Alex)
 * @date 2017/12/6
 */
public enum LogLevel {
    
    DEBUG(0),
    INFO(1),
    WARNING(2),
    ERROR(3),
    FATAL(4);
    
    
    
    private int level;
    
    private LogLevel(int level){
        this.level = level;
    }
    
    public int getLevel(){
        return  this.level;
    }
    
    public static LogLevel getLogLevel(int i){
        
        LogLevel[] allLevels = LogLevel.class.getEnumConstants();
        for (LogLevel logLevel: allLevels) {
            if (logLevel.getLevel() == i) {
                return logLevel;
            }
        }
        
        // if i is not valid, then return info as  default level
        return LogLevel.INFO;
    }
    
}
