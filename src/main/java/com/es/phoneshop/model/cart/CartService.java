package com.es.phoneshop.model.cart;

public interface CartService {
    Cart getCart();
    AddResult add(Long productId, int quantity);
}
