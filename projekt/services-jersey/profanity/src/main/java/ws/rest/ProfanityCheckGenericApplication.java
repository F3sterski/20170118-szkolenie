package ws.rest;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import ws.libs.profanity.ProfanityCheckClient;
import ws.utils.jersey.EndpointLoggingListener;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.ContextResolver;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/jaxrs")
public class ProfanityCheckGenericApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> s = new HashSet<>();
        s.add(InjectionFeature.class);
        s.add(EndpointLoggingListener.class);
        s.add(ProfanityResource.class);
        return s;
    }

    public static class InjectionFeature implements Feature {
        @Override
        public boolean configure(FeatureContext ctx) {
            ctx.register(new Binder());
            return true;
        }
    }

    public static class Binder extends AbstractBinder {

        @Override
        protected void configure() {
            bind(ProfanityCheckClient.class).to(ProfanityCheckClient.class);
        }
    }



}