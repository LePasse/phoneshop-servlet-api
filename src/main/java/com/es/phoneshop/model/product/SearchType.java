package com.es.phoneshop.model.product;

import com.es.phoneshop.model.order.PaymentMethod;

import java.util.Arrays;
import java.util.List;

public enum SearchType {
    ALL_WORDS, ANY_WORD;

    public static List<SearchType> getSearchTypes() {
        return Arrays.asList(SearchType.values());
    }
}
