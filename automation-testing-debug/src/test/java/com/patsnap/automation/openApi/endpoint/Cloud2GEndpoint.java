package com.patsnap.automation.openApi.endpoint;

import com.alibaba.fastjson.JSONObject;
import com.patsnap.automation.annotation.*;
import com.patsnap.automation.base.BaseEndpoint;

import com.patsnap.automation.openApi.util.OpenApiAuthenticationHandler;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 * @author liuyikai(Alex)
 * @date 2017/12/5
 */
@EndpointComponent
public interface Cloud2GEndpoint extends BaseEndpoint {
    
    @Endpoint(hostName = "openapi")
    @GET
    @Path("/statistic/enterprises/patents/value")
    @Headers({
            @Header(headerName = "Authorization", handler = OpenApiAuthenticationHandler.class, isDynamic = true ),
            @Header(headerName = "Accept", isDynamic = false,
                    headerValue = "application/vnd.zhihuiya.v1.0.0+json;profile=\"https://dev.zhihuiya.com/documents/statistic/enterprises/patents/value-schema.json\";")
    })
    ResponseEntity<JSONObject> getPatents(@QueryParam("enName") String enName);
    
}
