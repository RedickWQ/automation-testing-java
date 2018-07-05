package com.patsnap.automation.utils;

import com.patsnap.automation.contants.Constant;
import com.patsnap.automation.context.TestContextManager;
import com.patsnap.automation.entity.EnvironmentVariable;

import com.alibaba.fastjson.JSONReader;

import org.apache.commons.io.FileUtils;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 *
 * @author liuyikai
 * @date 2017/9/25
 */
public class TestEnvUtil {
    
    
    static {
        try {
            FileUtils.forceMkdir(new File(Constant.ENVIRONMENT_VARIABLES_ROOT_DIR));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * spring framework env
     */
    private static Environment env;
    
    
    /**
     * system env
     */
    private static Map<String, String> systemEnv;
    
    
    /**
     * application env list
     */
    private static List<EnvironmentVariable> envList;
    
    private static final String ENV_ROOT_FOLDER = "Environments";
    
    static {
        try {
            loadSystemEnvs();
            loadApplicationEnvsFromConfig();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    
    public static String getHostAddressByName(String hostName){
        //TODO 获取环境的变量
        EnvironmentVariable env = TestContextManager.getContext().getEnvironmentVariable();
        
        String hostUrl =  env.get(hostName, "address");
        
        if (StringUtils.isEmpty(hostUrl)){
            throw new RuntimeException("The 'address' of [" + hostName + "] is not configured for [" + env.getEnvName() + "]");
        }
        return hostUrl;
    }
    
    /**
     * get configuration from json file
     * @param applicationName
     * @param key
     * @return
     */
    public static String getEnvironmentVariable(String applicationName, String key){
        EnvironmentVariable env = TestContextManager.getContext().getEnvironmentVariable();
        if (null != env){
            return env.get(applicationName, key);
        } else  {
            throw new RuntimeException("Environment Variable for [" + applicationName + "] is not configured");
        }
        
    }
    
    public static void setEnvironmentVariable(String applicationName, String key, String value) {
        EnvironmentVariable env = TestContextManager.getContext().getEnvironmentVariable();
        if (null != env){
             env.set(applicationName, key, value);
        } else  {
            throw new RuntimeException("Environment Variable for [" + applicationName + "] is not configured");
        }
    }
    
    
    /**
     * read from application.properties  or  test.properties
     * @param key
     * @return
     */
    public static String getPropertyConfigValueByKey(String key){
        if (env == null) {
            env = SpringContextUtil.getBean(Environment.class);
        }
        return env.getProperty(key);
    }
    
    
    /**
     * read from current system env
     * @param key
     * @return
     */
    public static String getSystemEnv(String key){
        return systemEnv.get(key);
    }
    
    
    
    public static void loadSystemEnvs(){
        //load sys env properties
        systemEnv = new HashMap<>();

        Map<String, String> envs = System.getenv();
        Iterator it = envs.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry) it.next();
            systemEnv.put(entry.getKey(), entry.getValue());

        }

        Properties properties = System.getProperties();
        Iterator i =  properties.entrySet().iterator();
        while(i.hasNext())
        {
            Map.Entry entry = (Map.Entry)i.next();
            systemEnv.put(entry.getKey().toString(), entry.getValue().toString());
        }

    
    }
    
    
    /**
     * Read from json file
     * @throws FileNotFoundException
     */
    private static void loadApplicationEnvsFromConfig() throws FileNotFoundException {
    
    
//        ClassLoader classLoader = TestEnvUtil.class.getClassLoader();
//        String path =  classLoader.getResource(ENV_ROOT_FOLDER).getPath();
        
        
        
        
        File envRootDir  = new File(Constant.ENVIRONMENT_VARIABLES_ROOT_DIR);
        File [] subFileList = envRootDir.listFiles();
        envList = new ArrayList<>();
        for (File file: subFileList){
            if (file.isFile() && "json".equals(getFileExtension(file))){
                String fileName = getFileNameWithoutExtension(file);
                
                
                
                JSONReader reader = new JSONReader(new FileReader(file));
                Map<String, Map<String,String>> applicationVariables = reader.readObject(HashMap.class);
                EnvironmentVariable environmentVariable = new EnvironmentVariable(fileName);
            
                environmentVariable.setApplicationVariables(applicationVariables);
                envList.add(environmentVariable);
            }
        }
        
    }
    
    
 
    private  static String getFileExtension(File file) {
        String name = file.getName();
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }
    
    private static String getFileNameWithoutExtension(File file){
        String name = file.getName();
        try {
            return name.substring(0, name.lastIndexOf("."));
        } catch (Exception e) {
            return name;
        }
    }
    
    public static EnvironmentVariable getEnvironmentVariableByName(String envName){
        
        return envList.stream().filter(env -> env.getEnvName().equals(envName)).findAny().orElse(null);
        
    }
    
    /**
     * for debugging purpose
     */
    public static void setEnvironmentNameToCurrentTestContext(String envName){
        EnvironmentVariable env = getEnvironmentVariableByName(envName);
        
        if(env == null) {
            throw new RuntimeException("Environment [" + envName+"] is not well configured.");
        }
        
        TestContextManager.getContext().setEnvironmentVariable(env);
        
    }
    
    
    public static List<EnvironmentVariable> getApplicationEnvList(){
        return  envList;
    }
    
    
    
    
    
}
