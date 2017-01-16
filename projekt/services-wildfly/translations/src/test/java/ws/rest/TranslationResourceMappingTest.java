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
import ws.libs.dictionary.DictionaryWord;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(Arquillian.class)
public class TranslationResourceMappingTest {

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
    public void should_return_single_translation(@ArquillianResource URL url) throws URISyntaxException {
        final String word = "computer";

        DictionaryWord dictionaryWord = ClientBuilder.newClient()
                .target(url.toURI()).path("translate").path(word).path("first")
                .request().header("X-Dictionary", "dict").get(DictionaryWord.class);

        assertThat(dictionaryWord.englishWord, equalTo("computer"));
        assertThat(dictionaryWord.polishWord, equalTo("komputer"));
    }

    @Test
    @RunAsClient
    public void should_return_not_found_for_missing_word(@ArquillianResource URL url) throws URISyntaxException {
        final String word = "yadayada";

        Response response = ClientBuilder.newClient()
                .target(url.toURI()).path("translate").path(word).path("first")
                .request().header("X-Dictionary", "dict").get();

        assertThat(response.getStatus(), equalTo(404));
    }

    @Test
    @RunAsClient
    public void should_return_objects_not_json_string(@ArquillianResource URL url) throws URISyntaxException {
        final String word = "home";

        List<DictionaryWord> dictionaryWords = ClientBuilder.newClient()
                .target(url.toURI()).path("translate").path(word)
                .request().header("X-Dictionary", "dict").get(new GenericType<List<DictionaryWord>>() {});

        assertThat(dictionaryWords.size(), equalTo(24));
    }

}