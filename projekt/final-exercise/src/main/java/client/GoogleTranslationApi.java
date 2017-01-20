package client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.io.StringReader;

public class GoogleTranslationApi {

    private final String apikey;

    public GoogleTranslationApi(String apikey) {
        this.apikey = apikey;
    }

    //https://www.youtube.com/watch?v=f2d6q2oUJeY
    final String[] phrase = {
            "The quick brown fox jumped over the lazy dog",
            "Live, Universe and Everything"
    };

    public String translate() {
        return translate(phrase);
    }

    public String translate(String phrase) {
        return translate(new String[] {phrase});
    }


    public String translate(String[] phrase) {

//        {
//            'q': 'The quick brown fox jumped over the lazy dog.',
//                'source': 'en',
//                'target': 'es',
//                'format': 'text'
//        }

        Client client = ClientBuilder.newClient();

//        JsonObject jsonObject = Json.createObjectBuilder()
//                .add("q", phrase)
//                .add("source", "en")
//                .add("target", "pl")
//                .add("format", "text")
//                .build();

        GoogleTranslateRequest request = GoogleTranslateRequest
                .englishToPolishTranslation(phrase);

        Response r = client.target("https://translation.googleapis.com/language/translate/v2")
                .register(JacksonJsonProvider.class)
                .queryParam("key", apikey)
                .request()
                .post(Entity.json(request));


        System.out.println("r.getStatus() = " + r.getStatus());

        String s = r.readEntity(String.class);
        System.out.println("r.readEntity() = " + s);


        JsonReader reader = Json.createReader(new StringReader(s));
        JsonObject object = reader.readObject();

        String string = object
                .getJsonObject("data")
                .getJsonArray("translations")
                    .getJsonObject(0).getString("translatedText");

        return string;
    }

    public static class GoogleTranslateRequest {
        @JsonProperty("q")
        public String[] query;
        public String source;
        public String target;
        public String format;

        public GoogleTranslateRequest() {
        }

        public static GoogleTranslateRequest englishToPolishTranslation(String query) {
            GoogleTranslateRequest r = new GoogleTranslateRequest();
            r.query = new String[]{query};
            r.source = "en";
            r.target = "pl";
            r.format = "text";

            return r;
        }

        public static GoogleTranslateRequest englishToPolishTranslation(String[] query) {
            GoogleTranslateRequest r = new GoogleTranslateRequest();
            r.query = query;
            r.source = "en";
            r.target = "pl";
            r.format = "text";

            return r;
        }

    }

}
