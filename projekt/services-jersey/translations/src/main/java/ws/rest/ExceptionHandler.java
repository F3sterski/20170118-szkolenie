package ws.rest;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.NoSuchElementException;

public class ExceptionHandler implements ExceptionMapper<NoSuchElementException> {

    @Override
    public Response toResponse(NoSuchElementException e) {
        JsonObject json = Json.createObjectBuilder()
                .add("message", e.getLocalizedMessage())
                .build();

        return Response.status(Response.Status.NOT_FOUND)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .entity(json.toString())
                .build();
    }

}
