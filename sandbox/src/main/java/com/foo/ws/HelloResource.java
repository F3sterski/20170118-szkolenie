package com.foo.ws;

// http://localhost:8080/sandbox/api/hello/{name}

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.ws.rs.*;
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
    @Produces({MediaType.TEXT_PLAIN})
    public HelloMessage sayHello(@PathParam("name") String name) {
        if (name != null || name.isEmpty())
            return new HelloMessage("Hello, " + name);

        throw new IllegalStateException("Name is null!!!");
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/")
    public void consume(HelloMessage message) {
        System.out.println("message = " + message);
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

        public String message;

        public HelloMessage(String msg) {
            this.message = msg;
        }

    }

}
