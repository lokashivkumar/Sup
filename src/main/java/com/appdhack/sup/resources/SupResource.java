package com.appdhack.sup.resources;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/sup")
public class SupResource {

    @Path("/auth")
    @POST
    public void authUser() {
        System.out.println("received team");
        System.out.println("received ");
        System.out.println();
        System.out.println();
    }

    @Path("/ping")
    @GET
    public Response ping() {
        return Response.ok("hi").build();
    }

    @Path("/howto")
    @GET
    public Response howTo() {
        String instructions = "This application integrated with Slack will help schedule and manage your stand ups.";
        return Response.ok(instructions).build();
    }

    @Path("/support")
    @GET
    public Response support() {
        String support = "For support please contact Shashank Devan or John Lee or Shiv Loka.";
        return Response.ok(support).build();
    }
}
