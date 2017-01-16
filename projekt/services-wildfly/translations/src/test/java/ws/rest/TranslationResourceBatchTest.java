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
import ws.utils.LoggingFilter;

import javax.json.Json;
import javax.json.JsonArray;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.net.URL;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(Arquillian.class)
public class TranslationResourceBatchTest {

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
    public void should_return_multiple_translations(@ArquillianResource URL url) throws URISyntaxException {
        final String[] array = {"home", "house", "doctor"};

        Response response = ClientBuilder.newClient()
                .register(new LoggingFilter())
                .target(url.toURI())
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