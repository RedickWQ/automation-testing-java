package com.patsnap.automation.utils;

import org.apache.commons.io.FileUtils;
import org.apache.metamodel.*;
import org.apache.metamodel.create.ColumnCreationBuilder;
import org.apache.metamodel.create.TableCreationBuilder;
import org.apache.metamodel.csv.CsvConfiguration;
import org.apache.metamodel.data.DataSet;
import org.apache.metamodel.data.DataSetTableModel;
import org.apache.metamodel.factory.DataContextFactoryRegistryImpl;
import org.apache.metamodel.factory.DataContextPropertiesImpl;
import org.apache.metamodel.insert.RowInsertionBuilder;
import org.apache.metamodel.schema.ColumnType;
import org.apache.metamodel.schema.Schema;
import org.apache.metamodel.schema.Table;



import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuyikai on 2017/9/6.
 */
public class ExcelUtil {
    
    
    public static ArrayList<HashMap<String,String>> loadDataFromExcel(File file, String sheetName){
 
        //load config from excel File
        DataContext dataContext = DataContextFactory.createExcelDataContext(file);
        
        DataSet dataSet =  dataContext.query()
                .from(sheetName)
                .select("*")
                .execute();
        
        DataSetTableModel resultTable = new DataSetTableModel(dataSet);
        return convert(resultTable);
    }
    
    
    public static ArrayList<HashMap<String,String>> loadDataFromCsv(File file, char seperator){

        CsvConfiguration csvConfiguration = new CsvConfiguration(1, "UTF-8", seperator, '"', '\\');
        
        DataContext dataContext = DataContextFactory.createCsvDataContext(file, csvConfiguration);
        
        
        DataSet dataSet = dataContext.query().from(dataContext.getDefaultSchema()
                .getTable(0))
                .select("*")
                .execute();
        
        DataSetTableModel resultTable = new DataSetTableModel(dataSet);
        return convert(resultTable);
    }
    
    
    public static ArrayList<HashMap<String,String>> convert(DataSetTableModel resultTable){
        ArrayList<HashMap<String,String>> result = new ArrayList<HashMap<String,String>> ();
        for (int i = 0; i <resultTable.getRowCount() ; i++) {
            
            HashMap<String,String> singleLine = new HashMap<String,String>();
            for (int j = 0; j < resultTable.getColumnCount() ; j++) {
                String key = resultTable.getColumnName(j);
                Object cell = resultTable.getValueAt(i,j);
                String value = cell != null ? cell.toString(): "";
                singleLine.put(key,value);
            }
            
            result.add(singleLine);
        }
        
        return result;
        
    }
    
    
    /**
     * 1, drop legacy table; 2, create new table with new column; 3, insert new rows
     * @param file
     * @param sheet
     * @param data
     */
    public static synchronized void UpdateExcel(File file, String sheet, ArrayList<HashMap<String,String>> data) throws IOException {
        
        //back up file
        String sourceFilePath = file.getAbsolutePath();
        String targetFilePath = sourceFilePath + "bak";
        File targetFile = new File(targetFilePath);
        FileUtils.copyFile(file, targetFile);
        
        //try update
        try {
            tryUpdateExcel(file, sheet, data);
        } catch (Exception e) {
            
            FileUtils.copyFile(targetFile, file);
            throw e;
        } finally {
            targetFile.delete();
        }
        
        
    }
    
    private static  void tryUpdateExcel(File file, String sheet, ArrayList<HashMap<String,String>> data) {
        
        
        UpdateableDataContext context =  DataContextFactory.createExcelDataContext(file);
        Schema schema = context.getDefaultSchema();
        Table table = schema.getTableByName(sheet);
        
        final Table[] newTable = {null};
        context.executeUpdate(new UpdateScript() {
            @Override
            public void run(UpdateCallback updateCallback) {
                //updateCallback.deleteFrom(table).execute();
                updateCallback.dropTable(table).execute();
                
                
                HashMap<String,String> row = data.get(0);
                
                try {
                    
                    TableCreationBuilder tableCreationBuilder =  updateCallback.createTable(context.getDefaultSchema(),table.getName());
                    
                    //tableCreationBuilder.withColumn("123").ofType(ColumnType.VARCHAR);
                    
                    Method withColumn = tableCreationBuilder.getClass().getMethod("withColumn", String.class);
                    Method ofType = ColumnCreationBuilder.class.getMethod("ofType", ColumnType.class);
                    ColumnCreationBuilder columnCreationBuilder = null ;
                    for (String columnName: row.keySet()) {
                        columnCreationBuilder = (ColumnCreationBuilder) withColumn.invoke(tableCreationBuilder, columnName);
                        columnCreationBuilder = (ColumnCreationBuilder) ofType.invoke(columnCreationBuilder, ColumnType.VARCHAR);
                        
                    }
                    if (columnCreationBuilder != null) {
                        newTable[0] = columnCreationBuilder.execute();
                    }
                    
                    
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                
                
            }
        });
        
        
        data.forEach(row -> {
            
            context.executeUpdate(new UpdateScript() {
                @Override
                public void run(UpdateCallback updateCallback) {
                    
                    RowInsertionBuilder rb =  updateCallback.insertInto(newTable[0]);
                    try {
                        Method valueMethod = rb.getClass().getMethod("value",String.class, Object.class)   ;
                        for (Map.Entry<String, String> columnValue : row.entrySet()){
                            
                            rb = (RowInsertionBuilder)valueMethod.invoke(rb, columnValue.getKey(), columnValue.getValue());
                        }
                        rb.execute();
                        
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    
                }
            });
            
        });
        
        
    }
    
    
    
    /**
     * 1, delete legacy file ; 2, create new file with new column; 3, insert new rows
     * @param file
     * @param seperateor
     * @param data
     */
    public static synchronized void updateCsv(File file, char seperateor,  ArrayList<HashMap<String,String>> data) throws IOException {
        
        
        //back up file
        String sourceFilePath = file.getAbsolutePath();
        String targetFilePath = sourceFilePath + "bak";
        File targetFile = new File(targetFilePath);
        FileUtils.copyFile(file, targetFile);
        
        //try update
        try {
            tryUpdateCsv(file, seperateor, data);
        } catch (Exception e) {
            
            FileUtils.copyFile(targetFile, file);
            throw e;
        } finally {
            targetFile.delete();
        }
        
        
    }
    
    private static void tryUpdateCsv(File file, char seperator,  ArrayList<HashMap<String,String>> data){
        String fileName = file.getName();
        String filePath = file.getAbsolutePath();
        file.delete();
        
        final DataContextPropertiesImpl properties = new DataContextPropertiesImpl();
        properties.put("type", "csv");
        properties.put("resource", filePath);
        properties.put("encoding", "UTF-8");
        properties.put("column-name-line-number", 1);
        properties.put("separator-char", seperator);
        properties.put("multiline-values", true);
        
        DataContext dataContext = DataContextFactoryRegistryImpl.getDefaultInstance().createDataContext(properties);
        
        UpdateableDataContext context = (UpdateableDataContext) dataContext;
        final Table[] newTable = {null};
        context.executeUpdate(new UpdateScript() {
            @Override
            public void run(UpdateCallback updateCallback) {
                //updateCallback.deleteFrom(table).execute();
                
                HashMap<String,String> row = data.get(0);
                
                try {
                    
                    TableCreationBuilder  tableCreationBuilder =  updateCallback.createTable(context.getDefaultSchema(),fileName);
                    
                    //tableCreationBuilder.withColumn("123").ofType(ColumnType.VARCHAR);
                    
                    Method withColumn = tableCreationBuilder.getClass().getMethod("withColumn", String.class);
                    Method ofType = ColumnCreationBuilder.class.getMethod("ofType", ColumnType.class);
                    ColumnCreationBuilder columnCreationBuilder = null ;
                    for (String columnName: row.keySet()) {
                        columnCreationBuilder = (ColumnCreationBuilder) withColumn.invoke(tableCreationBuilder, columnName);
                        columnCreationBuilder = (ColumnCreationBuilder) ofType.invoke(columnCreationBuilder, ColumnType.VARCHAR);
                        
                    }
                    if (columnCreationBuilder != null) {
                        newTable[0] = columnCreationBuilder.execute();
                    }
                    
                    
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                
                
            }
        });
        
        data.forEach(row -> {
            
            context.executeUpdate(new UpdateScript() {
                @Override
                public void run(UpdateCallback updateCallback) {
                    
                    RowInsertionBuilder rb =  updateCallback.insertInto(newTable[0]);
                    try {
                        Method valueMethod = rb.getClass().getMethod("value",String.class, Object.class)   ;
                        for (Map.Entry<String, String> columnValue : row.entrySet()){
                            
                            rb = (RowInsertionBuilder)valueMethod.invoke(rb, columnValue.getKey(), columnValue.getValue());
                        }
                        rb.execute();
                        
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    
                }
            });
            
        });
    }

}
