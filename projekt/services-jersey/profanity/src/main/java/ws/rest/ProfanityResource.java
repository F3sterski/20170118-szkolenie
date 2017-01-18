package ws.rest;

import ws.libs.profanity.ProfanityCheckClient;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/profanity/{input}")
public class ProfanityResource {

    @Context
    ProfanityCheckClient client;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response check(@PathParam("input") String input) {
        return Response.ok(client.profanityCheck(input)).build();
    }
}
