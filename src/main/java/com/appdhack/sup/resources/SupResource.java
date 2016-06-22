package com.appdhack.sup.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/sup")
public class SupResource {

    @Path("/ping")
    @GET
    public Response ping() {
        return Response.ok("hi").build();
    }
}
