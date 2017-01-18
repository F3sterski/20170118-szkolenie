package ws.rest;

import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import ws.libs.dictionary.DictionaryWord;
import ws.rest.utils.CustomObjectMapper;
import ws.rest.utils.DictionaryWordModule;
import ws.rest.utils.DictionaryWordReader;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class TranslationResourceMappingTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new TranslationApplication();
    }


    @Test
    public void should_return_single_translation() throws URISyntaxException {
        final String word = "computer";

        DictionaryWord dictionaryWord = target().path("translate").path(word).path("first")
                //TODO: Register appropriate MessageBodyReader with .register() function
                .request().header("X-Dictionary", "dict").get(DictionaryWord.class);

        assertThat(dictionaryWord.englishWord, equalTo("computer"));
        assertThat(dictionaryWord.polishWord, equalTo("komputer"));
    }

    @Test
    public void should_return_not_found_for_missing_word() throws URISyntaxException {
        final String word = "yadayada";

        Response response = target().path("translate").path(word).path("first")
                .request().header("X-Dictionary", "dict").get();

        assertThat(response.getStatus(), equalTo(404));
    }

    @Test
    public void should_return_objects_not_json_string() throws URISyntaxException {
        final String word = "home";

        List<DictionaryWord> dictionaryWords = target().path("translate").path(word)
                //TODO: Register appropriate MessageBodyReader with .register() function
                .request().header("X-Dictionary", "dict").get(new GenericType<List<DictionaryWord>>() {
                });

        assertThat(dictionaryWords.size(), equalTo(24));
    }

}