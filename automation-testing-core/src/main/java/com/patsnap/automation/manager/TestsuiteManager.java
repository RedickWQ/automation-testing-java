package com.patsnap.automation.manager;

import com.patsnap.automation.entity.TestSuite;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by  Liuyikai (Alex) on 2017/10/16.
 */
public interface TestsuiteManager {
    
    TestSuite invokeTestSuite(TestSuite testSuite) throws Exception;
    
    Future<TestSuite> asyncRunTestSuite(TestSuite testSuite) throws  Exception;
    
    
    TestSuite saveTestSuite(TestSuite testSuite) throws Exception;
    
    List<TestSuite> getTestSuiteList() throws Exception;
    
    void deleteSuiteById(String id) throws Exception;
    
}
