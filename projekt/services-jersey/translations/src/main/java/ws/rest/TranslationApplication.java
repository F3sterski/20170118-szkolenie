package ws.rest;

import jersey.repackaged.com.google.common.collect.Sets;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScope;
import org.glassfish.jersey.process.internal.RequestScoped;
import ws.libs.dictionary.BablaDictionaryClient;
import ws.libs.dictionary.DictDictionaryClient;
import ws.libs.dictionary.DictionaryClient;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import java.util.Set;

@ApplicationPath("/")
public class TranslationApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        return Sets.newHashSet(
                ExceptionHandler.class,
                InjectionFeature.class, TranslationResource.class);
    }

    public static class InjectionFeature implements Feature {

        @Override
        public boolean configure(FeatureContext ctx) {
            ctx.register(new AbstractBinder() {
                @Override
                protected void configure() {
                    bindFactory(DictionaryFactory.class)
                            .to(DictionaryClient.class);
                }
            });

            return true;
        }
    }

    public static class DictionaryFactory implements Factory<DictionaryClient> {

        @HeaderParam("X-Dictionary")
        String type;

        @Override
        public DictionaryClient provide() {

            switch (type) {
                case "dict":
                    return new DictDictionaryClient();
                case "babla":
                    return new BablaDictionaryClient();
                default:
                    throw new IllegalStateException("Unknown X-Dictionary type");
            }
        }

        @Override
        public void dispose(DictionaryClient dictionaryClient) {
        }
    }

}
