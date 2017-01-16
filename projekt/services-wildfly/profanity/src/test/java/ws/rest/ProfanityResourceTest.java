package ws.rest;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.modules.maven.ArtifactCoordinates;
import org.jboss.modules.maven.MavenResolver;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import ws.libs.profanity.IsSwearWord;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBElement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.net.URL;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class ProfanityResourceTest {

    @Deployment(testable = false)
    public static WebArchive create() throws IOException {

        File file = MavenResolver.createDefaultResolver().resolveJarArtifact(new ArtifactCoordinates(
                "com.example.webservices.libs", "profanity-check-wrapper", "1.0-SNAPSHOT"));

        return ShrinkWrap.create(WebArchive.class)
                .addAsLibraries(file)
                .addPackage(RestApplication.class.getPackage());
    }

    @Test
    @RunAsClient
    public void should_return_escaped_phrase(@ArquillianResource URL url) throws URISyntaxException {
        final String input = "This is shit";

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url.toURI()).path("profanity").path(input);
        String jsonString = target.request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get(String.class);

        JsonReader reader = Json.createReader(new StringReader(jsonString));
        JsonObject json = reader.readObject();

        assertThat(json.getBoolean("containsProfanity"), equalTo(true));
        assertThat(json.getString("input"), equalTo(input));
        assertThat(json.getString("output"), equalTo("This is ****"));
    }

    @Test
    @RunAsClient
    public void should_return_escaped_xml_phrase(@ArquillianResource URL url) throws URISyntaxException {
        final String input = "This is fucked";

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url.toURI()).path("profanity").path(input);
        String xmlString = target.request()
                .accept(MediaType.APPLICATION_XML_TYPE)
                .get(String.class);

        assertTrue(xmlString.contains("<containsProfanity>true</containsProfanity>"));
        assertTrue(xmlString.contains("<output>This is ******</output>"));
    }

    @Test
    @RunAsClient
    public void should_return_only_input(@ArquillianResource URL url) throws URISyntaxException {
        final String phrase = "This is ok";

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url.toURI()).path("profanity").path(phrase);
        String jsonString = target.request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get(String.class);

        JsonReader reader = Json.createReader(new StringReader(jsonString));
        JsonObject json = reader.readObject();

        assertThat(json.getBoolean("containsProfanity"), equalTo(false));
        assertThat(json.getString("input"), equalTo(phrase));

//        TODO: uncomment this and make the test pass
//        IsSwearWord isSwearWord = target.request().get(IsSwearWord.class);
//
//        assertThat(isSwearWord.containsProfanity, equalTo(false));
//        assertThat(isSwearWord.input, equalTo(phrase));
//        assertThat(object.has("output"), equalTo(false));
    }
}