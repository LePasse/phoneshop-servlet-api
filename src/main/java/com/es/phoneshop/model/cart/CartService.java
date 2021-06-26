package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.order.Order;

import javax.servlet.http.HttpServletRequest;

public interface CartService {
    Cart getCart(HttpServletRequest request);

    CartResult add(Cart cart, Long productId, int quantity);

    CartResult update(Cart cart, Long productId, int quantity);

    void delete(Cart cart, Long productId);

    void clearCart(Cart cart, Order order);
}
