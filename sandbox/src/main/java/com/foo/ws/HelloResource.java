package com.foo.ws;

// http://localhost:8080/sandbox/api/hello/{name}

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/hello")
public class HelloResource {

    @GET
    @Path("/")
    public String sayHello() {
        return "Hello, stranger!";
    }

    @GET
    @Path("/{name}")
    public String sayHello(@PathParam("name") String name) {
        if (name != null || name.isEmpty())
            return "Hello, " + name;

        throw new IllegalStateException("Name is null!!!");
    }

}
