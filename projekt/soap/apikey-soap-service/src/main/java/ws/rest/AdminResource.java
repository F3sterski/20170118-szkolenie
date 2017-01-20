package ws.rest;

import repository.ApiKeys;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/admin")
public class AdminResource {

    final List<String> keys = ApiKeys.keys();

    @GET
    @Path("/keys")
    @Produces(MediaType.APPLICATION_JSON)
    public Response apiKeysList() {
        GenericEntity<List<String>> entity = new GenericEntity<List<String>>(keys) {
        };
        return Response.ok(entity).build();
    }

    @POST
    @Path("/keys")
    public Response addApikey(String key) {
        keys.add(key);
        return Response.status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("/keys/{key}")
    public Response deleteApikey(@PathParam("key") String key) {
        keys.remove(key);
        return Response.noContent().build();
    }

}
