package com.patsnap.automation.manager.impl;

import com.patsnap.automation.annotation.DataSource;
import com.patsnap.automation.contants.Constant;
import com.patsnap.automation.entity.Iteration;
import com.patsnap.automation.entity.TestParameter;
import com.patsnap.automation.enums.DataSourceType;
import com.patsnap.automation.manager.TestDataManager;
import com.patsnap.automation.utils.ExcelUtil;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuyikai on 2017/9/6.
 */
@Service
public class TestDataManagerImpl implements TestDataManager {
    
    
    private ArrayList<HashMap<String, String>> loadTestDataFromCsv() {
        
        return null;
    }
    
    public TestDataManagerImpl () throws IOException {
        FileUtils.forceMkdir(new File(Constant.TEST_DATA_ROOT_DIR));
    }
    
    @Override
    public List<Iteration> getIterationList(Method method) throws URISyntaxException {
        
        //todo handel configuration exceptions
        
        try {
            //init parameter
            ArrayList<HashMap<String, String>> rawData;
            DataSource dataSource = (DataSource) method.getAnnotation(DataSource.class);
            
            String filePath = Paths.get(Constant.TEST_DATA_ROOT_DIR, dataSource.value()).toString();
            File file = new File( filePath);
            
            if (dataSource.type().equals(DataSourceType.EXCEL)) {
                String sheetName = dataSource.sheetName();
                rawData = ExcelUtil.loadDataFromExcel(file, sheetName);
            } else if (dataSource.type().equals(DataSourceType.CSV)){
                rawData = ExcelUtil.loadDataFromCsv(file, dataSource.separator());
            } else  {
                rawData = new ArrayList<HashMap<String, String>>();
            }
    
            List<Iteration> iterationList =  getIterationListFromRawData(rawData);
            return iterationList;
            
        } catch (Exception e ){
            return new ArrayList<>();
        }
        
    }
    
    private List<Iteration> getIterationListFromRawData(ArrayList<HashMap<String, String>> rawData){
        
        List<Iteration> iterationList = new ArrayList<>();
        
        for (HashMap<String, String> row: rawData){
            List<TestParameter> testParameterList = new ArrayList<>();
            
            for (Map.Entry<String,String> entry: row.entrySet()){
                TestParameter testParameter = TestParameter.builder()
                        .name(entry.getKey())
                        .value(entry.getValue())
                        .build();
                testParameterList.add(testParameter);
            }
            
            Iteration iteration = Iteration.builder().testParameterList(testParameterList).build();
            iterationList.add(iteration);
        }
        
        return iterationList;
    }

    @Override
    public void saveIterationList(Method method, List<Iteration> iterationList) throws  Exception{
        DataSource dataSource = method.getAnnotation(DataSource.class);
        if (dataSource == null) {
            throw new RuntimeException("@DataSource is not configured.");
        }
    
        String filePath = Paths.get(Constant.TEST_DATA_ROOT_DIR, dataSource.value()).toString();
        File file = new File( filePath);
        
        ArrayList<HashMap<String, String>> rawData = getRawDataFromIterationList(iterationList);
        
        if (dataSource.type().equals(DataSourceType.EXCEL)) {
            String sheetName = dataSource.sheetName();
            ExcelUtil.UpdateExcel(file, sheetName, rawData);
        } else if (dataSource.type().equals(DataSourceType.CSV)){
            char separator = dataSource.separator();
            ExcelUtil.updateCsv(file, separator, rawData);
        } else  {
           throw  new RuntimeException(dataSource.type().name() + "is not supported yet");
        }
        
    }
    
    private ArrayList<HashMap<String, String>>  getRawDataFromIterationList(List<Iteration> iterationList) {
        ArrayList<HashMap<String, String>> rawData = new ArrayList<>();
        
        for (Iteration iteration: iterationList) {
            HashMap<String, String> row = new HashMap<>();
            
            for (TestParameter parameter: iteration.getTestParameterList()) {
                row.put(parameter.getName(), parameter.getValue());
            }
            
            rawData.add(row);
        }
        
        return rawData;
    }
    
    
    
}
