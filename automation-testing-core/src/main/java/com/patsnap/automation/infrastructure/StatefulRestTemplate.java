package com.patsnap.automation.infrastructure;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by liuyikai on 2017/9/20.
 */
@Component
public class StatefulRestTemplate extends RestTemplate {
    
    @Getter
    @Setter
    private final List<HttpCookie> cookies = new ArrayList<>();
    
//    @Getter
//    private HttpHeaders headers;
    
    
    
    public StatefulRestTemplate() {
    }
    
    public StatefulRestTemplate(ClientHttpRequestFactory requestFactory) {
        super(requestFactory);
    }
    
    public synchronized List<HttpCookie> getCookies() {
        return cookies;
    }
    
    public synchronized void appendCookies( List<HttpCookie> cookies){
        this.cookies.addAll(cookies);
    }
    
    public synchronized void resetCoookies() {
        cookies.clear();
    }
    
    private void processHeaders(HttpHeaders headers) {
        
//        this.headers = headers;
//        Map<String, List<String>> headerEntrySet = (Map<String, List<String>>) headers.entrySet();
        
        
        //请求返回时处理新获取的cookie
        final List<String> cooks = headers.get("Set-Cookie");
        if (cooks != null && !cooks.isEmpty()) {
            cooks.stream().map((c) -> HttpCookie.parse(c)).forEachOrdered((cook) -> {
                cook.forEach((a) -> {
                    HttpCookie cookieExists = cookies.stream().filter(x -> a.getName().equals(x.getName())).findAny().orElse(null);
                    if (cookieExists != null) {
                        cookies.remove(cookieExists);
                    }
                    cookies.add(a);
                });
            });
        }
    }
    
    @Override
    protected <T extends Object> T doExecute(URI url, HttpMethod method, final RequestCallback requestCallback, final ResponseExtractor<T> responseExtractor) throws RestClientException {
        final List<HttpCookie> cookies = getCookies();
        
        return super.doExecute(url, method, new RequestCallback() {
            @Override
            public void doWithRequest(ClientHttpRequest chr) throws IOException {
                if (!CollectionUtils.isEmpty(cookies)) {
                    StringBuilder sb = new StringBuilder();
                    for (HttpCookie cookie : cookies) {
                        // sb.append(cookie.getName()).append(cookie.getValue()).append(";");
                        sb.append(cookie.getName()).append("=").append(cookie.getValue()).append(";");
                    }
                    chr.getHeaders().add("Cookie", sb.toString());
                }
                requestCallback.doWithRequest(chr);
            }
            
        }, new ResponseExtractor<T>() {
            @Override
            public T extractData(ClientHttpResponse chr) throws IOException {
                processHeaders(chr.getHeaders());
                return responseExtractor.extractData(chr);
            }
        });
    }
    
}