package com.example.cb;

import com.example.cb.stocks.DelayedStockQuotesClient;
import com.example.cb.stocks.FailableStockQuotesClient;
import com.example.cb.stocks.StockQuotesClient;
import com.jasongoodwin.monads.Try;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

public class App {

    private final static int PORT = 9000;
    private static Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws URISyntaxException, IOException {
        // configuration
        Spark.port(PORT);

        // GetQuote host configuration
        final Properties props = new Properties();
        props.load(Files.newBufferedReader(Paths.get(App.class.getResource("/hosts.properties").toURI())));
        final String url = props.getProperty("stocks.url");

        // stock client
        final StockQuotesClient client = new DelayedStockQuotesClient(
                new FailableStockQuotesClient(url)
        );

        // routes
        Spark.get("/stocks/:name", (req, resp) -> {
            String name = req.params("name");

            Try<JSONObject> quote = client.getStockData(name);
            resp.header("Content-type", "application/json");
            String responseBody = quote.map(json -> json.toString()).recover(t -> {
                resp.status(404);
                return new JSONObject().put("error", t.getMessage()).toString();
            });

            log.info("Response: " + responseBody);
            return responseBody;
        });
    }

}
