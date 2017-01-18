package com.example.cb.stocks;

import com.jasongoodwin.monads.Try;
import org.json.JSONObject;

public interface StockQuotesClient {
    Try<JSONObject> getStockData(String name);
}
