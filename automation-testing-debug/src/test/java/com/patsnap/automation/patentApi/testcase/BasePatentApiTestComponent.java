package com.patsnap.automation.patentApi.testcase;

import com.patsnap.automation.annotation.AfterTestcase;
import com.patsnap.automation.annotation.BeforeTestcase;
import com.patsnap.automation.base.BaseTestComponent;

/**
 * Created by  Liuyikai (Alex) on 2017/10/23.
 */
public abstract class BasePatentApiTestComponent extends BaseTestComponent {
    
    @BeforeTestcase
    public void beforeTestcase(){
        System.out.println("*****this is before testcase******");
    }
    
    @AfterTestcase(alwaysRun = true)
    public void afterTestcase() {
        System.out.println("*****this is after testcase******");
    }
    
    
    
}
