package com.patsnap.automation.backOfficeApi.endpoint.authentication;

import com.alibaba.fastjson.JSONObject;
import com.patsnap.automation.annotation.ContentType;
import com.patsnap.automation.annotation.Endpoint;
import com.patsnap.automation.annotation.EndpointComponent;
import com.patsnap.automation.backOfficeApi.utils.Constant;
import com.patsnap.automation.base.BaseEndpoint;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by liuyikai on 2017/9/26.
 */
@EndpointComponent
public interface AuthenticationEndpoint  extends BaseEndpoint{

    
    @Endpoint(hostName = Constant.IDENTITY)
    @POST
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @ContentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @Path("/identity/oauth/token")
    ResponseEntity<JSONObject> authenticate(MultiValueMap<String, Object> params);
    


}
