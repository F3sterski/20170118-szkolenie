package com.foo.ws;

// http://localhost:8080/sandbox/api/hello/{name}

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.OutputStream;

@Path("/hello")
public class HelloResource {

    @GET
    @Path("/")
    public String sayHello() {
        return "Hello, stranger!";
    }

    @GET
    @Path("/{name}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public HelloMessage sayHello(@PathParam("name") String name) {
        if (name != null || name.isEmpty())
            return new HelloMessage("Hello, " + name);

        throw new IllegalStateException("Name is null!!!");
    }

    @GET
    @Path("/secured")
    @Produces(MediaType.TEXT_PLAIN)
    public Response method() {

        return Response.noContent()
                .header("X-ContentType", "application/pdf")
                .build();
    }


    public static class HelloMessage {

        public final String message;

        public HelloMessage(String msg) {
            this.message = msg;
        }

    }

}
