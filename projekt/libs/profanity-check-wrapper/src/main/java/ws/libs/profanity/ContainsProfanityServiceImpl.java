package ws.libs.profanity;

import ws.libs.profanity.rest.LoggedRestClient;
import ws.libs.profanity.rest.RestClient;

import java.net.URLEncoder;

class ContainsProfanityServiceImpl implements ContainsProfanityService {

    private final String baseUrl = "http://www.purgomalum.com/service/containsprofanity?text=%s";
    private final RestClient rest;

    public ContainsProfanityServiceImpl() {
        this.rest = new LoggedRestClient();
    }

    public ContainsProfanityServiceImpl(RestClient rest) {
        this.rest = rest;
    }

    @Override
    public boolean test(String input) {
        String url = String.format(baseUrl, URLEncoder.encode(input));
        String response = rest.getForString(url);
        return Boolean.valueOf(response);
    }


}
