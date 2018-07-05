package com.patsnap.automation.analyticsApi.endpoint.authentication;

import com.patsnap.automation.annotation.ContentType;
import com.patsnap.automation.annotation.Endpoint;
import com.patsnap.automation.annotation.EndpointComponent;
import com.patsnap.automation.base.BaseEndpoint;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;


/**
 * Created by liuyikai on 2017/9/25.
 */

@EndpointComponent
public interface LoginEndpoint  extends BaseEndpoint{
    
    @POST
    @Endpoint(isStateful = true, hostName = "passport")
    @Path("/loginsubmit")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @ContentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ResponseEntity<Void> login(MultiValueMap<String, Object> params);
    
}
