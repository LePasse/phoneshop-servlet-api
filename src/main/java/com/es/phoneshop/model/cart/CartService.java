package com.es.phoneshop.model.cart;

public interface CartService {
    Cart getCart(HttpServletRequest request);

    AddResult add(Cart cart, Long productId, int quantity);
}
