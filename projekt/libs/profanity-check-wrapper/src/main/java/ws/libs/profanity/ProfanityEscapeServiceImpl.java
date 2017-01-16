package ws.libs.profanity;

import ws.libs.profanity.rest.LoggedRestClient;
import ws.libs.profanity.rest.RestClient;

import java.net.URLEncoder;

class ProfanityEscapeServiceImpl implements ProfanityEscapeService {

    private final String baseUrl = "http://www.purgomalum.com/service/plain?text=%s";
    private final RestClient rest;

    public ProfanityEscapeServiceImpl() {
        this.rest = new LoggedRestClient();
    }

    public ProfanityEscapeServiceImpl(RestClient restClient) {
        this.rest = restClient;
    }

    @Override
    public String escape(String input) {
        String url = String.format(baseUrl, URLEncoder.encode(input));
        return rest.getForString(url);
    }

}
