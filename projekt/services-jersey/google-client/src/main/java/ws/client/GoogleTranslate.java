package ws.client;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.StringReader;

public class GoogleTranslate {

    private final String API_KEY;

    public GoogleTranslate(String api_key) {
        API_KEY = api_key;
    }

    public String translationFor(String input) {

//        {
//            'q': 'The quick brown fox jumped over the lazy dog.',
//                'source': 'en',
//                'target': 'es',
//                'format': 'text'
//        }

        throw new IllegalStateException("Not implemented");
    }

}
