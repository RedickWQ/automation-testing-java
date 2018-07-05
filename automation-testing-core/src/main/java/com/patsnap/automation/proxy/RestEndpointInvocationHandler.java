package com.patsnap.automation.proxy;

import com.patsnap.automation.annotation.*;
import com.patsnap.automation.base.BaseEndpoint;
import com.patsnap.automation.base.DynamicHeaderHandler;

import com.patsnap.automation.base.DynamicHostAddressHandler;
import com.patsnap.automation.endpoint.SlaveExclusive;
import com.patsnap.automation.infrastructure.StatefulRestTemplate;
import com.patsnap.automation.context.TestContextManager;
import com.patsnap.automation.utils.SpringContextUtil;
import com.patsnap.automation.utils.TestEnvUtil;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.HttpCookie;
import java.net.URLEncoder;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.CookieParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 *
 * @author liuyikai
 * @date 2017/9/22
 */
public class RestEndpointInvocationHandler   implements InvocationHandler {
    
    
    private BaseEndpoint baseEndpoint = new BaseEndpoint() {
    
    };
    
    private Class delegatedClass;
    
    public RestEndpointInvocationHandler (Class delegatedClass){
        this.delegatedClass = delegatedClass;
    }
    
    
    
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //确认该接口的方法是否有EndPoint注解
        //如果为空，该方法会执行原来的逻辑
        Endpoint endpointAnnotation = method.getAnnotation(Endpoint.class);
        if (endpointAnnotation == null ) {
            String methodName = method.getName();
            if("equals".equals(methodName)) {
                return proxy == args[0];
            } else if("hashCode".equals(methodName)) {
                return System.identityHashCode(proxy);
            } else if("toString".equals(methodName)) {
                return proxy.getClass().getName() + "@" +
                        Integer.toHexString(System.identityHashCode(proxy)) +
                        ", with InvocationHandler " + this;
            } else  {
                return method.invoke(baseEndpoint, args);
            }
        }
        
        

        //获取代理对象的接口
        Class<?> proxyIfc = proxy.getClass().getInterfaces()[0];
        //获取方法返回值类型
        Class<?> responseType = method.getReturnType();
    
        
        //resolve method
        //获取request的执行方法
        org.springframework.http.HttpMethod requestMethod = getRequestMethod(method);
    

        //拼接完整URL
        //todo get hostname from configuration
        //根据Endpoint注解，从配置文件读取对应的host
        String hostName = getHostAddrByName(method);
        //获取接口上的path内容
        String pathAnnotatedByInterface = getPathFromInterfaceAnnotation(delegatedClass);
        //获取方法上的path内容
        String pathAnnotatedByMethod = getPathFromMethodAnnotation(method);
        //拼接URL
        String url = mergeUrlPath(hostName, pathAnnotatedByInterface, pathAnnotatedByMethod);
        
        //获取方法参数中的annotation
        Annotation[][] paramAnns = method.getParameterAnnotations();

        Object body = null;
        Type entityType = null;

    
        List<HttpCookie> cookies = new ArrayList<>();
    
    
        HttpHeaders headers = new HttpHeaders();
        HashMap<String, Object> queryParams = new HashMap<>();
        HashMap<String, Object> pathParams = new HashMap<>();
    
        MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<String, String>();
    
        //resolve parameter Annotations
        //遍历参数的annotations
        for (int i = 0; i < paramAnns.length; ++i) {
            Map<Class, Annotation> anns = new HashMap();
            Annotation[] arr$ = paramAnns[i];
            System.out.println(paramAnns[i]);
            int len$ = arr$.length;

            //将parameters 的 annotations 存入Map<Class, Annotation>
            for (int i$ = 0; i$ < len$; ++i$) {
                Annotation ann = arr$[i$];
                anns.put(ann.annotationType(), ann);
            }

            Object value = args[i];
            if (anns.isEmpty()) {
    
                //if parameter is not annotated, deal it as a request body
                entityType = method.getGenericParameterTypes()[i];
                body = value;
            } else {
                //如果参数中有annotation，针对这种情况进行进一步处理
                Annotation ann;
                //如果注解的参数没有传值，则使用默认值
                if (value == null && (ann = (Annotation) anns.get(DefaultValue.class)) != null) {
                    value = ((DefaultValue) ann).value();
                }
                //如果注解的参数有新的赋值
                if (value != null) {
                    if ((ann = (Annotation) anns.get(PathParam.class)) != null) {
                        //url的path的值，用于重新拼接url
                        pathParams.put(((PathParam) ann).value(), value);
    
                    } else if ((ann = (Annotation) anns.get(QueryParam.class)) != null) {
                        //url的参数的值，一般用于get请求后追加查询参数使用
                        queryParams.put(((QueryParam) ann).value(), value);
    
                    } else if ((ann = (Annotation) anns.get(HeaderParam.class)) != null) {
                        //也可以指定header
                        headers.set(((HeaderParam) ann).value(), value.toString());
    
                    } else if ((ann = (Annotation) anns.get(CookieParam.class)) != null) {
                        //设置cookies
                        String name = ((CookieParam) ann).value();
//                        if (!(value instanceof Cookie)) {
//                            c = new Cookie(name, value.toString());
//                        } else {
//                            c = (Cookie)value;
//                            if (!name.equals(((Cookie)value).getName())) {
//                                c = new Cookie(name, c.getValue(), c.getPath(), c.getDomain(), c.getVersion());
//                            }
//                        }
                        HttpCookie c = new HttpCookie(name, value.toString());
                        cookies.add(c);
                    } else if ((ann = (Annotation) anns.get(MatrixParam.class)) != null) {
    
                        //todo this case is not handled yet
    
    
                    } else if ((ann = (Annotation) anns.get(FormParam.class)) != null) {
                        //form表单
                        formParameters.add(((FormParam) ann).value(), value.toString());
                    }
                }
            }
        }
        //接收响应内容的类型
        Produces produces = method.getAnnotation(Produces.class);
        if (produces == null) {
            //如果方法上没有拿到响应类型，再去接口上拿一次
            produces = (Produces) proxyIfc.getAnnotation(Produces.class);
        }
    
        String[] accepts = produces == null ? null : produces.value();
    
        if (ArrayUtils.isNotEmpty(accepts)) {
            List<MediaType> acceptMediaTypes = new ArrayList<>();
            for (int a = 0; a < accepts.length; a++) {
                MediaType mediaType = MediaType.valueOf(accepts[a]);
                acceptMediaTypes.add(mediaType);
            }
            //header中设置响应类型
            headers.setAccept(acceptMediaTypes);
        }
    
        //设置请求类型
        String contentType = null;
        if (body != null) {

            ContentType c = method.getAnnotation(ContentType.class);
            if (c != null) {
                contentType = c.value();
            }
        }

        //如果body为空则表示，这个请求是以form表单提交的
        if (body == null && !formParameters.isEmpty()) {
            body = formParameters;
            contentType = "application/x-www-form-urlencoded";
        } else {
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
        }
    
        headers.setContentType(MediaType.valueOf(contentType));
    
        // todo to move it to request interceptor
        headers.set("X-PatSnap-From", "AutomationApiTest");
        
        
    
        

    
        if (proxy instanceof BaseEndpoint){
            HttpHeaders customizedHeaders = baseEndpoint.getHeaders();
            if (customizedHeaders.size() > 0) {
               for(Map.Entry<String, List<String>> entry : customizedHeaders.entrySet()){
                   headers.put(entry.getKey(), entry.getValue());
               }
            }
            
            cookies.addAll(baseEndpoint.getCookies());
        }
    
        // process user defined headers
        Headers customHeaders = method.getAnnotation(Headers.class);
        if (customHeaders != null) {
            Header [] headerList = customHeaders.value();
            for (Header h: headerList) {
                String headerName = h.headerName();
                String headerValue;
                if (h.isDynamic()) {
                    Class headerHandlerClass = h.handler();
                    DynamicHeaderHandler handler = (DynamicHeaderHandler)SpringContextUtil.getBean(headerHandlerClass);
                    headerValue = handler.getValue();
                } else {
                    headerValue = h.headerValue();
                }
                headers.set(headerName, headerValue);
            }
        }
        
       
        //get restTemplate and deal with cookies
        RestTemplate template = getRestTemplate(method);
        if (template instanceof StatefulRestTemplate) {
            ((StatefulRestTemplate) template).appendCookies(cookies);
        } else  {
            
            if (!CollectionUtils.isEmpty(cookies))
            {
                StringBuilder sb = new StringBuilder();
                cookies.stream().forEach(c -> {
                    sb.append(c.getName()).append("=").append(c.getValue()).append(";");
                });
                headers.add("Cookie", sb.toString());
            }
        }
        

        HttpEntity httpEntity;
        if (body == null) {
            httpEntity = new HttpEntity(headers);
        } else {
    
            //todo check if required
//            if (entityType instanceof ParameterizedType) {
//                body = new GenericEntity(body, entityType);
//            }
    
            httpEntity = new HttpEntity(body, headers);
        }
    
    
        //resolve url parameters
        url = resolveUrlWithPathParams(url, pathParams);
        url = resolveUrlWithQueryParams(url, queryParams);
    
        
        System.out.println("url in proxy handler: " + url);
    
        Type t = responseType.getGenericSuperclass();
        if (responseType.equals(Void.TYPE)){
//             Object r = template.exchange(url, requestMethod, httpEntity, Object.class);
            template.postForLocation(url, body);
            return null;
        }else if (ParameterizedType.class.isAssignableFrom(t.getClass()) && t.equals(ResponseEntity.class.getGenericSuperclass())) {
            Class requestReturnType = getRequestReturnType(method);
            
            return template.exchange(url, requestMethod, httpEntity, requestReturnType);
        } else {
            
            return template.exchange(url, requestMethod, httpEntity, responseType).getBody();
        }
    

        
    }
    
    private static String getHttpMethodName(AnnotatedElement ae) {
        HttpMethod a = (HttpMethod)ae.getAnnotation(HttpMethod.class);
        return a == null ? null : a.value();
    }
    
    
    private static String getPathFromMethodAnnotation(Method method){
        Path p = method.getAnnotation(Path.class);
        if (p != null) {
            return p.value();
        }
        return "";
    }
    
    private static String getPathFromInterfaceAnnotation(Class delegatedClass) {
        Path p = (Path) delegatedClass.getAnnotation(Path.class);
        if (p != null) {
            return p.value();
        }
        return "";
    }
    
    
    
    private static  org.springframework.http.HttpMethod getRequestMethod(Method method){
        String httpMethod = getHttpMethodName(method);
    
        if (httpMethod == null) {
            Annotation[] arr$ = method.getAnnotations();
            int len$ = arr$.length;
        
            for(int i$ = 0; i$ < len$; ++i$) {
                Annotation ann = arr$[i$];
                httpMethod = getHttpMethodName(ann.annotationType());
                if (httpMethod != null) {
                    break;
                }
            }
        }
        
        org.springframework.http.HttpMethod requestMethod = org.springframework.http.HttpMethod.resolve(httpMethod);
        if (requestMethod != null) {
            return requestMethod;
        } else {
            return  org.springframework.http.HttpMethod.GET;
        }
       
    }
    
    
    private static Class getRequestReturnType(Method method){
        return (Class)((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0];
    }
    

    private static RestTemplate getRestTemplate(Method method){
        
    
        SlaveExclusive slaveExclusive = method.getAnnotation(SlaveExclusive.class);
        if (slaveExclusive != null) {
            return SpringContextUtil.getBean("slaveRestTemplate");
        }
    
        RestTemplate template;
        boolean isStateful;
        Endpoint endpoint = method.getAnnotation(Endpoint.class);
        if (endpoint == null ) {
            isStateful = false;
        } else  {
            isStateful = endpoint.isStateful();
        }
    
        if (isStateful) {
            //todo get stateful  template and auto login
            template = TestContextManager.getContext().getRestTemplate();
            if (template == null) {
               template =   SpringContextUtil.getBean("statefulRestTemplate");
               TestContextManager.getContext().setRestTemplate(template);
            }
            
        } else  {
            template  =  SpringContextUtil.getBean("restTemplate");
        
        }
        return template;
        
    }
    
    private static String resolveUrlWithPathParams(String url, HashMap<String, Object> pathParams) throws UnsupportedEncodingException {
        
        if (CollectionUtils.isEmpty(pathParams)){
           return url;
        }
        
        for (Map.Entry<String, Object> param: pathParams.entrySet()) {
            String name =  param.getKey();
            Object value = param.getValue();
            
            if (StringUtils.isEmpty(name) ) {
                throw new RuntimeException("Name of UrlPathParameter is empty");
            }
            
            if (value == null) {
                throw new NullPointerException("Value of UrlPathParameter ["+ name + "] is null ");
            }
            
            //todo handel list Object?
            
            String v = URLEncoder.encode(value.toString(), "UTF-8");
            
            //先简单实现吧
            name = "{" + name + "}";
            url = url.replace(name, v);
            
        }
        
        return url;
    }
    
    
    private static String resolveUrlWithQueryParams(String url, HashMap<String, Object> queryParams) throws UnsupportedEncodingException {
    
        if (CollectionUtils.isEmpty(queryParams)){
            return url;
        }
        
        url += "?";
        
        for (Map.Entry<String, Object> param: queryParams.entrySet()){
            String name = param.getKey();
            Object value = param.getValue();
            if (value instanceof String[]) {
                String [] vals = (String[]) value;
                for (int i = 0; i < vals.length ; i++) {
                    String v =  URLEncoder.encode(vals[i], "UTF-8");
                    url = url + name + "=" + v + "&";
                }
   
            } else  {
                String v = URLEncoder.encode(value.toString(), "UTF-8");
                url = url + name + "=" + v + "&";
            }
            
        }
        
        // trim end "&"
        url = url.substring(0,url.length() - 1);
        
        return url;
    }
    
    private static String  getHostAddrByName(Method method) {
    
        Endpoint endpoint = method.getAnnotation(Endpoint.class);
        
        if (endpoint.isDynamicHost()) {
            DynamicHostAddressHandler handler =  (DynamicHostAddressHandler)SpringContextUtil.getBean(endpoint.handler());
            return handler.getAddress();
        }
        
        
        String hostName = endpoint.hostName();
        return TestEnvUtil.getHostAddressByName(hostName);

    }
    
    
    private static String mergeUrlPath(String hostName, String...subPaths)  {
        String url = hostName;
        for (String subpath : subPaths){
            if (!StringUtils.isEmpty(subpath)){
                if (url.charAt(url.length() - 1) == '/') {
                    url = StringUtils.trimTrailingCharacter(url, '/');
                }
    
                if (!subpath.startsWith("/")){
                    url = url + "/" + subpath;
                } else  {
                    url = url + subpath;
                }
                
            }
        }
        
        
        return url;
    }
    
    
}
