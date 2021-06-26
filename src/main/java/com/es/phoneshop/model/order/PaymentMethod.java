package com.es.phoneshop.model.order;

public enum PaymentMethod {
    CASH, CREDIT_CARD;

    public static PaymentMethod fromString(String str) {
        for (PaymentMethod value :
                PaymentMethod.values()) {
            if (value.toString().equalsIgnoreCase(str)) {
                return value;
            }
        }
        return null;
    }
}
