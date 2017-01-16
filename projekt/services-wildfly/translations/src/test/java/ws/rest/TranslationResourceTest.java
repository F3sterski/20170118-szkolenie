package ws.rest;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.net.URL;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(Arquillian.class)
public class TranslationResourceTest {

    @Deployment(testable = false)
    public static WebArchive create() throws IOException {
        File[] files = Maven.resolver()
                .loadPomFromFile("pom.xml")
                .resolve("com.example.webservices.libs:translation-service:1.0-SNAPSHOT")
                .withTransitivity().asFile();

        return ShrinkWrap.create(WebArchive.class)
                .addAsLibraries(files)
                .addPackage(TranslationApplication.class.getPackage());
    }

    @Test
    @RunAsClient
    public void should_return_dict_translations(@ArquillianResource URL url) throws URISyntaxException {
        final String word = "home";

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url.toURI()).path("translate").path(word);
        String jsonString = target.request().header("X-Dictionary", "dict").get(String.class);

        JsonReader reader = Json.createReader(new StringReader(jsonString));
        JsonArray json = reader.readArray();

        assertThat(json.size(), equalTo(24));
    }

    @Test
    @RunAsClient
    public void should_return_babla_translations(@ArquillianResource URL url) throws URISyntaxException {
        final String word = "house";

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url.toURI()).path("translate").path(word);
        String jsonString = target.request().header("X-Dictionary", "babla").get(String.class);

        JsonReader reader = Json.createReader(new StringReader(jsonString));
        JsonArray json = reader.readArray();

        assertThat(json.size(), equalTo(8));
    }

}