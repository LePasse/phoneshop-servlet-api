package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

public class DeleteCartItemServlet extends HttpServlet {

    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productIdString = request.getPathInfo().substring(1);
        Long productId = parseProductID(request);
        if (productId >= 0) {
            Cart cart = cartService.getCart(request);
            cartService.delete(cart, productId);
            response.sendRedirect(request.getContextPath() + "/cart?modalSuccess=Cart item deleted successfully");
        } else {
            response.sendRedirect(request.getContextPath() + "/cart?modalError=Wrong item id");
        }
    }

    private Long parseProductID(HttpServletRequest request) {
        String productIdString = request.getPathInfo().substring(1);
        try {
            return Long.parseLong(productIdString);
        } catch (NumberFormatException e) {
            return (long) -1;
        }
    }
}
