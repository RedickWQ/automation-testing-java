package com.patsnap.automation.endpoint;

import com.patsnap.automation.annotation.ContentType;
import com.patsnap.automation.annotation.Endpoint;
import com.patsnap.automation.annotation.EndpointComponent;
import com.patsnap.automation.base.BaseEndpoint;
import com.patsnap.automation.entity.Slave;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.*;

/**
 * @author liuyikai(Alex)
 * @date 2017/12/27
 */
@EndpointComponent
@Path("/slave")
public interface PlatformCommunicationEndpoint extends BaseEndpoint{
    
    @SlaveExclusive
    @Endpoint(hostName = "", isDynamicHost = true, handler = PlatformCommunicationConfigHandler.class)
    @Path("/ping/{slaveId}")
    @GET
    ResponseEntity<String> ping(@PathParam("slaveId") Long slaveId);
    
    
    @SlaveExclusive
    @Endpoint(hostName = "", isDynamicHost = true, handler = PlatformCommunicationConfigHandler.class)
    @Path("/register")
    @POST
    @ContentType(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Slave> register(Slave slave);
    
    
    
}
