package ws.rest;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import ws.libs.profanity.ProfanityCheckClient;
import ws.utils.jersey.EndpointLoggingListener;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/")
public class ProfanityCheckApplication extends ResourceConfig {

    public ProfanityCheckApplication() {
        setApplicationName("profanity-check-application");
        register(new Binder());
        register(new EndpointLoggingListener());
        packages("ws.rest");
    }

    public static class Binder extends AbstractBinder {

        @Override
        protected void configure() {
            bind(ProfanityCheckClient.class).to(ProfanityCheckClient.class);
        }
    }

}