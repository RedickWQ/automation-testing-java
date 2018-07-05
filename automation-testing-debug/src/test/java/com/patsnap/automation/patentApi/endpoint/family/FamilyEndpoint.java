package com.patsnap.automation.patentApi.endpoint.family;

import com.patsnap.automation.annotation.Endpoint;
import com.patsnap.automation.annotation.EndpointComponent;
import com.patsnap.automation.base.BaseEndpoint;
import com.patsnap.automation.patentApi.response.family.FamilyCountRes;
import com.patsnap.automation.patentApi.response.family.FamilyMapRes;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by liuyikai on 2017/9/22.
 */

@EndpointComponent
@Path("/family")
public interface FamilyEndpoint  extends BaseEndpoint{
    
    
    @Endpoint(isStateful = false, hostName = "patentapi" )
    @GET
    @Path("/map")
    @Produces(MediaType.APPLICATION_JSON)
    ResponseEntity<FamilyMapRes> getFamilyMap(@QueryParam("patent_id") String pid);
    
    
    @Endpoint(isStateful = false, hostName = "patentapi" )
    @GET
    @Path("/extendcount/patent_id/{patent_id}")
    @Produces(MediaType.APPLICATION_JSON)
    ResponseEntity<FamilyCountRes> getFamilyCount(@PathParam("patent_id") String patentId);
    
    
    
    
}
