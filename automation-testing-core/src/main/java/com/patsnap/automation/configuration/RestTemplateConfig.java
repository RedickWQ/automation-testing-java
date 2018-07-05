package com.patsnap.automation.configuration;

import com.patsnap.automation.infrastructure.StatefulRestTemplate;
import com.patsnap.automation.context.TestContextManager;
import com.patsnap.automation.log.LogLevel;
import com.patsnap.automation.report.Reporter;

import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.protocol.HttpContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by liuyikai on 2017/9/5.
 */
@Configuration
public class RestTemplateConfig {

    private static final  int READ_TIME_OUT = 20000;
    private static final  int CONNECTION_TIME_OUT = 15000;

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
    
        //        HttpClient httpClient = HttpClientBuilder.create()
//                .setRedirectStrategy(new EnhancedRedirectStrategy())
//
//                .build();
//        requestFactory.setHttpClient(httpClient);
        
        CloseableHttpClient httpClient = HttpClientUtils.acceptsUntrustedCertsHttpClientBuilder()
                .setRedirectStrategy(new EnhancedRedirectStrategy())
                .build();
        
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(READ_TIME_OUT);
        requestFactory.setConnectTimeout(CONNECTION_TIME_OUT);
        
        BufferingClientHttpRequestFactory bufferingClientHttpRequestFactory = new BufferingClientHttpRequestFactory(requestFactory);

        return bufferingClientHttpRequestFactory;
    }

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory clientHttpRequestFactory) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        //todo customize settings, e.g timeout
        
        RestTemplate template = new RestTemplate(clientHttpRequestFactory);
        template.getInterceptors().add(new RequestInterceptor());
        return template;
    }


    @Bean
    public StatefulRestTemplate statefulRestTemplate(ClientHttpRequestFactory clientHttpRequestFactory){

        StatefulRestTemplate statefulRestTemplate = new StatefulRestTemplate(clientHttpRequestFactory);
        statefulRestTemplate.getInterceptors().add(new RequestInterceptor());
        return statefulRestTemplate;
    }
    
    
    @Bean(name = "slaveRestTemplate")
    public RestTemplate slaveRestTemplate(ClientHttpRequestFactory clientHttpRequestFactory) {
        RestTemplate template = new RestTemplate(clientHttpRequestFactory);
        //todo: interceptor for slave platform communication
        template.getInterceptors().add(new LoggingClientHttpRequestInterceptor());
        return template;
    }

    

    private static class RequestInterceptor implements ClientHttpRequestInterceptor {
        @Override
        public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] body, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
            HttpRequestWrapper requestWrapper = new HttpRequestWrapper(httpRequest);

            HttpHeaders headers = requestWrapper.getHeaders();

            System.out.println("Request interceptor: " + httpRequest.getURI());


            Reporter reporter = TestContextManager.getContext().getReporter();

            //todo to format in rich html
            String logContent = "Url: " + httpRequest.getURI() + "; Method: " + httpRequest.getMethod().name();
            reporter.logEvent(LogLevel.INFO, logContent);
            ClientHttpResponse response = clientHttpRequestExecution.execute(requestWrapper, body);
            reporter.logEvent(LogLevel.INFO,"Status code: " + response.getStatusCode().value());

            return response;

        }
    }


    private static class EnhancedRedirectStrategy extends  LaxRedirectStrategy {

        @Override
        public boolean isRedirected(org.apache.http.HttpRequest request, HttpResponse response,
                                    HttpContext context) throws ProtocolException {

//            System.out.println(response);

            // If redirect intercept intermediate response.
            if (super.isRedirected( request, response, context)){

                // int statusCode  = response.getStatusLine().getStatusCode();
                String redirectURL = response.getFirstHeader("Location").getValue();
                System.out.println("redirectURL: " + redirectURL);
                return true;
            }
            return false;
        }
    }




}
