package com.patsnap.automation.entity;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author liuyikai(Alex)
 * @date 2017/12/27
 */
@Data
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class Slave {

    private Long id;
    
    @Value("${com.patsnap.automation.slave.name}")
    private String name;
    
    private String description;
    
    @Value("${server.port}")
    private String port;
    
    private String address;
    
    private Integer testcaseCount;
    
//    private String status;

//    private LocalDateTime createTime;

//    private LocalDateTime updateTime;
}
