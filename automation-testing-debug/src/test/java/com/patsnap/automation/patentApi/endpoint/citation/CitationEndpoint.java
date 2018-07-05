package com.patsnap.automation.patentApi.endpoint.citation;

import com.patsnap.automation.annotation.Endpoint;
import com.patsnap.automation.annotation.EndpointComponent;
import com.patsnap.automation.base.BaseEndpoint;
import com.patsnap.automation.patentApi.response.citation.CitationInfoRsp;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.*;

/**
 * Created by liuyikai on 2017/9/26.
 */

@EndpointComponent
@Path("/citation")
public interface CitationEndpoint extends BaseEndpoint{
    
    
    @Endpoint(hostName = "patentapi" )
    @GET
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @Path("/original/patent_id/{patent_id}")
    ResponseEntity<CitationInfoRsp> getCitationOriginalRsp(@PathParam("patent_id") String pid,
                                                           @QueryParam("sortType") String sort,
                                                           @QueryParam("start") String start,
                                                           @QueryParam("rows") String rows,
                                                           @QueryParam("lang") String lang);
    
    
    
}
