package ws.libs.profanity.rest;

import ws.libs.profanity.rest.RestClient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

class RestClientImpl implements RestClient {

    @Override
    public String getForString(String url) {
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.connect();

            if (con.getResponseCode() != 200) {
                String errorResponse = new Scanner(con.getErrorStream()).useDelimiter("\\A").next();
                throw new RuntimeException(
                        String.format("External error response: %s (%d). %s",
                                con.getResponseMessage(),
                                con.getResponseCode(),
                                errorResponse));
            }

            return new Scanner(con.getInputStream()).useDelimiter("\\A").next();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
