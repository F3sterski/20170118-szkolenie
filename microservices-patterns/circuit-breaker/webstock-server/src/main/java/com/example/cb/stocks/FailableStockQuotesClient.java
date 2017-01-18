package com.example.cb.stocks;

import com.example.cb.App;
import com.jasongoodwin.monads.Try;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class FailableStockQuotesClient implements StockQuotesClient {

    private static Logger log = LoggerFactory.getLogger(FailableStockQuotesClient.class);
    private final ConcurrentHashMap<String, JSONObject> quotes = new ConcurrentHashMap<>();
    private final String urlTemplate;

    public FailableStockQuotesClient(String url) {
        this.urlTemplate = url;
        log.info("StockQuotesClient: {}", urlTemplate);
    }

    @Override
    public Try<JSONObject> getStockData(String name) {
        return Try.ofFailable(() ->
                quotes.computeIfAbsent(name, this::getStockDataDirect));
    }

    private JSONObject getStockDataDirect(String name) {
        try {
            log.info("External call -> GetQuote({})", name);
            HttpURLConnection con = (HttpURLConnection) new URL(String.format(urlTemplate, name)).openConnection();
            con.setConnectTimeout(1000);
            con.connect();

            if (con.getResponseCode() != 200) {
                String errorResponse = new Scanner(con.getErrorStream()).useDelimiter("\\A").next();
                throw new RuntimeException(
                        String.format("External error response: %s (%d). %s",
                                con.getResponseMessage(),
                                con.getResponseCode(),
                                errorResponse));
            }

            String responseBody = new Scanner(con.getInputStream()).useDelimiter("\\A").next();
            JSONObject jsonObject = XML.toJSONObject(responseBody);

            if (jsonObject.getJSONObject("QuoteData").getBoolean("QuoteError")) {
                throw new RuntimeException("External error response: " +
                        jsonObject.getJSONObject("QuoteData").get("CompanyName")
                );
            }

            return jsonObject;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
