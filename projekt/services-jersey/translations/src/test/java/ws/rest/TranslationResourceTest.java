package ws.rest;

import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.StringReader;
import java.net.URISyntaxException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class TranslationResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new TranslationApplication();
    }

    @Test
    public void should_return_dict_translations() throws URISyntaxException {
        final String word = "home";

        WebTarget target = target().path("translate").path(word);
        String jsonString = target.request().header("X-Dictionary", "dict").get(String.class);

        JsonReader reader = Json.createReader(new StringReader(jsonString));
        JsonArray json = reader.readArray();

        assertThat(json.size(), equalTo(24));
    }

    @Test
    public void should_return_babla_translations() throws URISyntaxException {
        final String word = "house";

        WebTarget target = target().path("translate").path(word);
        String jsonString = target.request().header("X-Dictionary", "babla").get(String.class);

        JsonReader reader = Json.createReader(new StringReader(jsonString));
        JsonArray json = reader.readArray();

        assertThat(json.size(), equalTo(8));
    }

    @Test
    public void should_return_dict_first_translations() throws URISyntaxException {
        final String word = "house";

        WebTarget target = target().path("translate").path(word).path("first");
        String jsonString = target.request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header("X-Dictionary", "dict").get(String.class);

        JsonReader reader = Json.createReader(new StringReader(jsonString));
        JsonObject json = reader.readObject();

        assertThat(json.getString("pW"), equalTo("izba"));
    }

    @Test
    public void should_return_not_found_for_missing_word() throws URISyntaxException {
        final String word = "yadayada";

        Response response = target()
                .path("translate").path(word).path("first")
                .request()
                .header("X-Dictionary", "dict").get();

        assertThat(response.getStatus(), equalTo(404));
    }
}