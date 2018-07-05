package com.patsnap.automation.base;

import org.springframework.http.HttpHeaders;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author liuyikai
 * @date 2017/9/25
 */
public interface BaseEndpoint {
    
 
    List<HttpCookie> COOKIES = new ArrayList<>();
    
    HttpHeaders HEADERS = new HttpHeaders();
    
    /**
     * @param name
     * @param value
     */
    default void addCookie(String name, String value){
        COOKIES.add(new HttpCookie(name, value));
    }
    
    
    /**
     * 获取当前 threadlocal 里 StatefulTemplate中的cookie
     * @return
     */
    default  List<HttpCookie> getCookies(){
        return COOKIES;
    }
    
    
    /**
     * @param name
     * @param value
     */
    default  void addHeader(String name, String value){
        HEADERS.set(name, value);
    }
    
    /**
     * @return currentRequestHeaders
     */
    default HttpHeaders getHeaders(){
        return HEADERS;
    }
    
    
 
}
