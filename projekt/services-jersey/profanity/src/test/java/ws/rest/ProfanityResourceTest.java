package ws.rest;

import org.glassfish.jersey.test.JerseyTest;
import org.json.JSONObject;
import org.junit.Test;

import javax.ws.rs.core.Application;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ProfanityResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ProfanityCheckApplication();
    }

    @Test
    public void should_return_escaped_phrase() {
        final String phrase = "This is shit";

        final String result = target("profanity").path(phrase).request().get(String.class);
        JSONObject object = new JSONObject(result);

        assertThat(object.getBoolean("containsProfanity"), equalTo(true));
        assertThat(object.getString("input"), equalTo(phrase));
        assertThat(object.getString("output"), equalTo("This is ****"));
    }

    @Test
    public void should_return_only_input() {
        final String phrase = "This is ok";

        final String result = target("profanity").path(phrase).request().get(String.class);
        JSONObject object = new JSONObject(result);

        assertThat(object.getBoolean("containsProfanity"), equalTo(false));
        assertThat(object.getString("input"), equalTo(phrase));

//        TODO: uncomment this and make the test pass
//        assertThat(object.has("output"), equalTo(false));
    }

}