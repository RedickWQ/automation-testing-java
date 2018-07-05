package com.patsnap.automation.schedule;

import com.patsnap.automation.endpoint.PlatformCommunicationEndpoint;
import com.patsnap.automation.entity.Slave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author liuyikai(Alex)
 * @date 2017/12/29
 */
@Component
public class ScheduleManager {
    
    
    /**
     * heartbeat: ping platform every 1 min
     */
    private static final long pingInterval = 1 * 60 * 1000;
    
    @Autowired
    PlatformCommunicationEndpoint platformCommunicationEndpoint;
    
    @Autowired
    Slave slave;
    
    
    @Scheduled(fixedDelay = pingInterval)
    public void ping() {
        if (slave.getId()!= null) {
            try {
                platformCommunicationEndpoint.ping(slave.getId());
            } catch (Exception ex){
                //todo test platform is not available
            }
            
        }
        
    }
    
    
    
}
