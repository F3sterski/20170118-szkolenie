package ws.rest;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import ws.libs.profanity.IsSwearWord;
import ws.libs.profanity.ProfanityCheckClient;
import ws.utils.jersey.EndpointLoggingListener;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.ext.ContextResolver;

@ApplicationPath("/")
public class ProfanityCheckApplication extends ResourceConfig {

    public ProfanityCheckApplication() {
        setApplicationName("profanity-check-application");
        register(new Binder());
        register(new EndpointLoggingListener());
        register(DictionaryWordMapper.class);
        packages("ws.rest");
    }

    public static class Binder extends AbstractBinder {

        @Override
        protected void configure() {
            bind(ProfanityCheckClient.class).to(ProfanityCheckClient.class);
        }
    }

    public static class DictionaryWordMapper implements ContextResolver<ObjectMapper> {

        final ObjectMapper mapper;

        public DictionaryWordMapper() {
            this.mapper = new ObjectMapper();
            this.mapper.addMixIn(IsSwearWord.class, IsSwearWordMixIn.class);
        }

        @Override
        public ObjectMapper getContext(Class<?> aClass) {
            return this.mapper;
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
        public static class IsSwearWordMixIn {

            public IsSwearWordMixIn(@JsonProperty("containsProfanity") boolean containsProfanity,
                                    @JsonProperty("input") String input,
                                       @JsonProperty("output") String output) {
            }
        }
    }

}