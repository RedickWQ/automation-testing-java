package com.patsnap.automation.listener;

import com.alibaba.fastjson.JSONObject;
import com.patsnap.automation.endpoint.PlatformCommunicationEndpoint;
import com.patsnap.automation.entity.Slave;
import com.patsnap.automation.entity.Testcase;
import com.patsnap.automation.manager.TestComponentManager;

import com.patsnap.automation.utils.JsonUtil;
import com.patsnap.automation.utils.SpringContextUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liuyikai on 2017/9/6.
 */
@Service
public class RegisterTestServiceListener implements ApplicationListener<ContextRefreshedEvent> {
    
    @Autowired
    TestComponentManager testComponentManager;
    
    
    @Autowired
    PlatformCommunicationEndpoint platformCommunicationEndpoint;
    

    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event)
    {
        //root application context 没有parent，他就是老大.
        if(event.getApplicationContext().getParent() == null)
        {
            // register all existing testcases
            List<Testcase> testcaseList = testComponentManager.getTestCaseList();
    
            System.out.println("===== total cases: =======");
            System.out.println(testcaseList.size());
            
            System.out.println("===== all beans are setting up =======");
            
            
            // register to server
            System.out.println("===== register to server =======");
//          ResponseEntity<String> pingResult =  platformCommunicationEndpoint.ping();
            

            Slave slave = SpringContextUtil.getBean(Slave.class);
            slave.setTestcaseCount(testcaseList.size());
            
            try {
                ResponseEntity<Slave> result = platformCommunicationEndpoint.register(slave);
    
                if (result.getStatusCode().equals(HttpStatus.OK)) {
                    BeanUtils.copyProperties(result.getBody(), slave);

                    System.out.println("===== register to server successful =======");
                } else {
                    //todo
                }
                
            } catch (Exception ex){
            
            }
            
            
            
            
        }
        
    }
}
