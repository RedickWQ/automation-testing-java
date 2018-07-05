package com.patsnap.automation.openApi.endpoint;

import com.alibaba.fastjson.JSONObject;
import com.patsnap.automation.annotation.ContentType;
import com.patsnap.automation.annotation.Endpoint;
import com.patsnap.automation.annotation.EndpointComponent;
import com.patsnap.automation.base.BaseEndpoint;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * @author liuyikai(Alex)
 * @date 2017/12/5
 */
@EndpointComponent
public interface AuthEndpoint extends BaseEndpoint{
    
    
    @Endpoint(hostName = "connector")
    @POST
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @ContentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @Path("/connector/oauth/token")
    ResponseEntity<JSONObject> authenticate(MultiValueMap<String, Object> params);
    
}
