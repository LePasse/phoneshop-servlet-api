package com.es.phoneshop.model.product;

import java.util.Date;
import java.util.HashMap;

public class PriceHistory {
    private HashMap<Date, String> history;

    public PriceHistory(Date date, String price){
        this.history = new HashMap<>();
        add(date, price);
    }

    public void add(Date date, String price){
        this.history.put(date,price);
    }

    public HashMap<Date, String> get() {
        return this.history;
    }

    public HashMap<Date, String> getHistory() {
        return history;
    }
}
