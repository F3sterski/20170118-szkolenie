package com.example.cb.stocks;

import com.example.cb.App;
import com.jasongoodwin.monads.Try;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

public class DelayedStockQuotesClient implements StockQuotesClient {

    private static Logger log = LoggerFactory.getLogger(DelayedStockQuotesClient.class);
    private static AtomicLong DELAY = new AtomicLong(0);
    final StockQuotesClient target;

    public DelayedStockQuotesClient(StockQuotesClient target) {
        this.target = target;
    }

    @Override
    public Try<JSONObject> getStockData(String name) {
        log.info("Quotes for {}. Maximum delay {} ms", name, DELAY.incrementAndGet() * 1000);

        long start = System.currentTimeMillis();
        Try<JSONObject> data = target.getStockData(name);
        long end = System.currentTimeMillis();

        delay(end - start);
        return data;
    }

    private void delay(long elapsedTime) {
        long waitTime = DELAY.get() * 1000 - elapsedTime;
        waitTime = (waitTime > 0) ? waitTime : 0;
        log.info("API call took {}ms. Waiting another {}ms.", elapsedTime, waitTime);
        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DELAY.decrementAndGet();
    }
}
