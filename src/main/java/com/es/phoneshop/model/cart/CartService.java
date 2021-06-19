package com.es.phoneshop.model.cart;

import javax.servlet.http.HttpServletRequest;

public interface CartService {
    Cart getCart(HttpServletRequest request);

    CartResult add(Cart cart, Long productId, int quantity);

    CartResult update(Cart cart, Long productId, int quantity);

    void delete(Cart cart, Long productId);
}
