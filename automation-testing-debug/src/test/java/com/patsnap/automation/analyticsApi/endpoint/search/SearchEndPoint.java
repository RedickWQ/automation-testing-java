package com.patsnap.automation.analyticsApi.endpoint.search;

import com.alibaba.fastjson.JSONObject;
import com.patsnap.automation.annotation.Endpoint;
import com.patsnap.automation.annotation.EndpointComponent;
import com.patsnap.automation.base.BaseEndpoint;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 *
 * @author liuyikai
 * @date 2017/9/26
 */

@EndpointComponent
public interface SearchEndPoint  {
    
    
    @Endpoint(isStateful = true, hostName = "analytics")
    @GET
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @Path("/patent/query/result")
    ResponseEntity<JSONObject> query(@QueryParam("_type") String type,
                                     @QueryParam("q") String q,
                                     @QueryParam("fq") String fq,
                                     @QueryParam("viewtype") String viewType,
                                     @QueryParam("page") String page,
                                     @QueryParam("sort") String sort,
                                     @QueryParam("_") String timeStamp);
    
    
    
    
    
}
