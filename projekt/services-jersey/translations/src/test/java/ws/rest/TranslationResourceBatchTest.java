package ws.rest;

import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.json.Json;
import javax.json.JsonArray;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.io.StringReader;
import java.net.URISyntaxException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class TranslationResourceBatchTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new TranslationApplication();
    }


    @Test
    public void should_return_multiple_translations() throws URISyntaxException {
        final String[] array = {"home", "house", "doctor"};

        Response response = target()
                .path("translate")
                .request()
                .header("X-Dictionary", "dict")
                .post(Entity.json(array));

        assertThat(response.getStatus(), equalTo(200));

        String jsonArray = response.readEntity(String.class);
        System.out.println("jsonArray = " + jsonArray);

        JsonArray respJsonArray = Json.createReader(new StringReader(jsonArray)).readArray();
        assertThat(respJsonArray.size(), equalTo(3));
    }

}