package com.patsnap.automation.backOfficeApi.endpoint.company;

import com.alibaba.fastjson.JSONObject;
import com.patsnap.automation.annotation.Endpoint;
import com.patsnap.automation.annotation.EndpointComponent;
import com.patsnap.automation.backOfficeApi.annotation.AuthenticationRequired;
import com.patsnap.automation.backOfficeApi.utils.Constant;
import com.patsnap.automation.base.BaseEndpoint;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.*;

/**
 * Created by liuyikai on 2017/9/27.
 */
@EndpointComponent
public interface CompanyEndpoint extends BaseEndpoint{
    
    
    @Endpoint(hostName = Constant.IDENTITY)
    @Path("/identity/companies/{companyId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<JSONObject> getCompanyById(@PathParam("companyId") String companyId);
    
    
    
}
