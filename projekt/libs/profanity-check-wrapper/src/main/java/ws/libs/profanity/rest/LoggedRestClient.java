package ws.libs.profanity.rest;

import java.util.logging.Logger;

public class LoggedRestClient implements RestClient {

    private static Logger log = Logger.getLogger("RestClient");
    private final RestClient target;

    public LoggedRestClient() {
        this.target = new RestClientImpl();
    }

    @Override
    public String getForString(String url) {
        log.info("Calling: " + url);
        return target.getForString(url);
    }
}
