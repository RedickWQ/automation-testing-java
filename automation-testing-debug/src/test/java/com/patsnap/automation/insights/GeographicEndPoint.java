package com.patsnap.automation.insights;

import com.alibaba.fastjson.JSONObject;
import com.patsnap.automation.annotation.*;
import com.patsnap.automation.base.BaseEndpoint;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@EndpointComponent
public interface GeographicEndPoint extends BaseEndpoint {
    @Endpoint(hostName = "backend")
    @POST
    @Path("/2.1/report/competitive/geographic_distribution_map")
    @ContentType(MediaType.APPLICATION_JSON_VALUE)
    @Headers(value = {
            @Header(headerName = "USER_ID", headerValue = "dfe390bba2474de29acd0922ae0069a7"),
            @Header(headerName = "X-PatSnap-From", headerValue = "insights")

    })
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<JSONObject> getDistributionMap(JSONObject body);


    @Endpoint(hostName = "insights")
    @POST
    @Path("/xxxx")
    @ContentType(MediaType.APPLICATION_JSON_VALUE)
    @Headers(value = {
            @Header(headerName = "USER_ID", headerValue = "dfe390bba2474de29acd0922ae0069a7"),
            @Header(headerName = "X-PatSnap-From", headerValue = "insights")

    })
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<JSONObject> getTest(JSONObject body);


}
