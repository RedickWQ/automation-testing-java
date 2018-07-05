package com.patsnap.automation.innoApi.endpoint;

import com.alibaba.fastjson.JSONObject;
import com.patsnap.automation.annotation.ContentType;
import com.patsnap.automation.annotation.Endpoint;
import com.patsnap.automation.annotation.EndpointComponent;
import com.patsnap.automation.base.BaseEndpoint;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.PATCH;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 *
 * @author Liuyikai (Alex)
 * @date 2017/10/23
 */
@EndpointComponent
public interface SuperModuleEndpoint extends BaseEndpoint {
    
    @PATCH
    @Path("/super-module-objects/{object}")
    @ContentType(MediaType.APPLICATION_JSON_VALUE)
    @Endpoint(hostName = "innosnap")
    ResponseEntity<JSONObject> PATCH_SuperModuleObject(@PathParam("object") String object, JSONObject body);


}
