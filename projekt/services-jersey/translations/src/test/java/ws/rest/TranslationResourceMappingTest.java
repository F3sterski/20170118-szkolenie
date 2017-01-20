package ws.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import ws.libs.dictionary.DictionaryWord;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
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

        DictionaryWord dictionaryWord = target()
                .register(DictionaryWordMapper.class)
                //TODO: Register appropriate MessageBodyReader with .register() function
                .path("translate").path(word).path("first")
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header("X-Dictionary", "dict").get(DictionaryWord.class);

        assertThat(dictionaryWord.englishWord, equalTo("computer"));
        assertThat(dictionaryWord.polishWord, equalTo("komputer"));
    }

//    public static class Dictionary2 extends DictionaryWord {
//
//        public Dictionary2() {
//            super("", "");
//        }
//
//        public Dictionary2(String englishWord, String polishWord) {
//            super(englishWord, polishWord);
//        }
//    }

    @Test
    public void should_return_objects_not_json_string() throws URISyntaxException {
        final String word = "home";

        List<DictionaryWord> dictionaryWords = target().path("translate").path(word)
                //TODO: Register appropriate MessageBodyReader with .register() function
                .request().header("X-Dictionary", "dict").get(new GenericType<List<DictionaryWord>>() {
                });

        assertThat(dictionaryWords.size(), equalTo(24));
    }

    public static class DictionaryWordMapper implements ContextResolver<ObjectMapper> {

        final ObjectMapper mapper;

        public DictionaryWordMapper() {
            this.mapper = new ObjectMapper();
            this.mapper.addMixIn(DictionaryWord.class, DictionaryWordMixIn.class);
        }

        @Override
        public ObjectMapper getContext(Class<?> aClass) {
            return this.mapper;
        }

        public static class DictionaryWordMixIn {

            public DictionaryWordMixIn(@JsonProperty("englishWord") String englishWord,
                                       @JsonProperty("polishWord") String polishWord) {
            }
        }
    }


}