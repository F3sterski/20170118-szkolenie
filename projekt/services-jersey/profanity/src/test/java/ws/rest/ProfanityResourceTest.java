package ws.rest;

import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import java.io.StringReader;
import java.net.URISyntaxException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ProfanityResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ProfanityCheckApplication();
    }

    @Test
    public void should_return_escaped_phrase() {
        final String phrase = "This is shit";

        final String result = target("profanity").path(phrase).request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get(String.class);

        JsonReader reader = Json.createReader(new StringReader(result));
        JsonObject object = reader.readObject();

        assertThat(object.getBoolean("containsProfanity"), equalTo(true));
        assertThat(object.getString("input"), equalTo(phrase));
        assertThat(object.getString("output"), equalTo("This is ****"));
    }

    @Test
    public void should_return_only_input() {
        final String phrase = "This is ok";

        final String result = target("profanity").path(phrase).request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get(String.class);
        JsonReader reader = Json.createReader(new StringReader(result));
        JsonObject object = reader.readObject();

        assertThat(object.getBoolean("containsProfanity"), equalTo(false));
        assertThat(object.getString("input"), equalTo(phrase));
        assertThat(object.containsKey("output"), equalTo(false));
    }

    @Test
    public void should_return_escaped_xml_phrase() throws URISyntaxException {
        final String input = "This is fucked";

        WebTarget target = target().path("profanity").path(input);
        String xmlString = target.request()
                .accept(MediaType.APPLICATION_XML_TYPE)
                .get(String.class);

        assertTrue(xmlString.contains("<containsProfanity>true</containsProfanity>"));
        assertTrue(xmlString.contains("<output>This is ******</output>"));
    }

}