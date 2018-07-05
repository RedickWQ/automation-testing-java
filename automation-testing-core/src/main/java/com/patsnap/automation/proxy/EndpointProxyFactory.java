package com.patsnap.automation.proxy;

import java.lang.reflect.Proxy;

/**
 * Created by liuyikai on 2017/9/22.
 */
public class EndpointProxyFactory {
    public EndpointProxyFactory(){
        System.out.println("===============EndpointProxyFactory===================");
    }
    

    //这边代理的都是接口
    public  <T> T getProxyObject(ClassLoader classLoader, Class<T> clazz){
        
        
        
        return (T) Proxy.newProxyInstance(classLoader,new Class[]{clazz}, new RestEndpointInvocationHandler(clazz));
    }
}
