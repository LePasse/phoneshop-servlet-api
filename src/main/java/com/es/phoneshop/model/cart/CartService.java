package com.es.phoneshop.model.cart;

import javax.servlet.http.HttpServletRequest;

public interface CartService {
    Cart getCart(HttpServletRequest request);

    AddResult add(Cart cart, Long productId, int quantity);
}
