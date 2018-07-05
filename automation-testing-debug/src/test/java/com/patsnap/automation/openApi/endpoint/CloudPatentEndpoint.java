package com.patsnap.automation.openApi.endpoint;

import com.alibaba.fastjson.JSONObject;
import com.patsnap.automation.annotation.Endpoint;
import com.patsnap.automation.annotation.EndpointComponent;
import com.patsnap.automation.annotation.Header;
import com.patsnap.automation.annotation.Headers;
import com.patsnap.automation.base.BaseEndpoint;
import com.patsnap.automation.openApi.util.OpenApiAuthenticationHandler;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 * @author liuyikai(Alex)
 * @date 2017/12/23
 */
@EndpointComponent
public interface CloudPatentEndpoint extends BaseEndpoint {
    
    @Endpoint(hostName = "cloud_patent")
    @GET
    @Path("/patent/description")
    @Headers({
            @Header(headerName = "Authorization",handler = OpenApiAuthenticationHandler.class,isDynamic = true),
            @Header(headerName = "X-PatSnap-Version",isDynamic = false,headerValue = "1.0.0"),
           
    })
    ResponseEntity<JSONObject> getDescriptionIdRsp(@QueryParam("patent_id") String[] patentIdArr);
    
}
