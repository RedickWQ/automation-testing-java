package com.patsnap.automation.analyticsGui.base;

import com.patsnap.automation.gui.base.BasePage;
import com.patsnap.automation.utils.TestEnvUtil;

/**
 * Created by liuyikai on 2017/9/30.
 */
public abstract class BaseAnalyticsPage extends BasePage {
    
    public BaseAnalyticsPage () {
        super();
        
    }
    
//    @Override
//    protected String getMainUrl(){
//        if (null == this.mainUrl) {
//            this.mainUrl = TestEnvUtil.getHostAddressByName("analytics");
//        }
//        return mainUrl;
//    }
}
