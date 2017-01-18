package com.example.cb;

import net.jodah.failsafe.CircuitBreaker;
import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class StocksClient {

    static CircuitBreaker BREAKER = new CircuitBreaker()
            .withFailureThreshold(3, 5)
            .withSuccessThreshold(1)
            .withDelay(5, TimeUnit.SECONDS);

    public static void main(String[] args) {

        System.out.println("Try looking you stock quote like: IBM, AAPL, TWTR, LNKD, MSFT\n");

        String str = getStockName();
        while (str != null) {
            List<String> stockNames = Arrays.stream(str.split("[ \\.,;:]"))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());

            GetQuotes tasks = new GetQuotes(stockNames);

            ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors()*10);
            String results = pool.invoke(tasks);

            System.out.println(results);
            str = getStockName();
        }
    }

    private static String getStockName() {
        System.out.print("Enter quote symbol>> ");
        return new Scanner(System.in).nextLine();
    }

    public static class GetQuotes extends RecursiveTask<String> {

        final List<String> symbols;

        public GetQuotes(List<String> symbols) {
            Objects.nonNull(symbols);
            this.symbols = symbols;
        }

        @Override
        protected String compute() {
            if (this.symbols.isEmpty()) {
                return new String();
            } else if (this.symbols.size() == 1) {
                return readDataWithExceptionHandling(symbols.get(0));
            } else {
                GetQuotes leftTask = new GetQuotes(symbols.subList(0, symbols.size() / 2));
                leftTask.fork();
                GetQuotes rightTask = new GetQuotes(symbols.subList(symbols.size() / 2, symbols.size()));

                String right = rightTask.compute();
                String left = leftTask.join();
                return new StringBuffer().append(left).
                        append(System.lineSeparator()).
                        append(right).
                        append(System.lineSeparator()).toString();
            }
        }

        /**
         * Adjust this method for better retry policies
         */
        private String readDataWithExceptionHandling(String stockSymbol ) {
            RetryPolicy retryPolicy = new RetryPolicy()
                    .retryOn(Exception.class)
                    .withBackoff(2, 30, TimeUnit.SECONDS);

            return Failsafe.with(BREAKER).with(retryPolicy)
                    .withFallback("Failed to fetch stocks")
                    .onFailedAttempt(f -> {
                        System.out.println("Couldn't process task " + stockSymbol + ". Retrying");
                        System.out.println("breaker = " + BREAKER.getState());
                    })
                    .get(() -> readExternalData(stockSymbol));

//            try {
//                return readExternalData(stockSymbol);
//            } catch (Exception e) {
//                System.out.println("Couldn't process task " + stockSymbol + ". Retrying");
//                return compute();
//            }
        }

        /**
         * Do not change this method
         */
        private String readExternalData(String name) {
            try {
                final URL url = new URL("http://localhost:9000/stocks/" + name);
                final URLConnection con = url.openConnection();
                con.setReadTimeout(1500);

                JSONTokener tokener = new JSONTokener(con.getInputStream());
                JSONObject jsonObject = new JSONObject(tokener);

                return String.format("Current for %s (%s) is %s",
                        jsonObject.getJSONObject("QuoteData").get("StockSymbol"),
                        jsonObject.getJSONObject("QuoteData").get("CompanyName"),
                        jsonObject.getJSONObject("QuoteData").get("LastTradeAmount"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
