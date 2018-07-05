package com.patsnap.automation.patentApi.endpoint;

import com.alibaba.fastjson.JSONObject;
import com.patsnap.automation.annotation.Endpoint;
import com.patsnap.automation.annotation.EndpointComponent;
import com.patsnap.automation.base.BaseEndpoint;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 * Created by  Liuyikai (Alex) on 2017/11/16.
 */
@EndpointComponent
public interface AgencyEndpoint extends BaseEndpoint {
    
    
    @Endpoint(hostName = "openapi")
    @GET
    @Path("/agency/agency_id")
    ResponseEntity<JSONObject> getAgencyId(@QueryParam("agency_number") String agencyNumber);
    
    
    
}
