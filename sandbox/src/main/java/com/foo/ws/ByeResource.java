package com.foo.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/bye")
public class ByeResource {

    @GET
    @Path("/")
    public String bye() {
        return "Bye!";
    }

}
