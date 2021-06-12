package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartResult;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AddCartItemServlet extends HttpServlet {
    private ProductDao productDao;
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long productId = parseProductID(request);
        Cart cart = cartService.getCart(request);
        CartResult result = cartService.add(cart, productId, 1);
        switch (result) {
            case SUCCESS:
                response.sendRedirect(request.getContextPath() + "/products?modalSuccess=Product added to cart successfully");
                return;
            case NOT_ENOUGH_STOCK:
                request.setAttribute("productId", productId);
                request.setAttribute("error", "Not enough stock, max available " + productDao.getProduct(productId).get().getStock());
                response.sendRedirect(request.getContextPath() + "/products?modalError=There was an error adding product to cart");
                return;
            case PRODUCT_NOT_FOUND:
                request.setAttribute("message", "Product " + productId + " not found");
                request.getRequestDispatcher("/WEB-INF/pages/errorProductNotFound.jsp").forward(request, response);
                return;
        }
    }

    private Long parseProductID(HttpServletRequest request) {
        String productId = request.getPathInfo().substring(1);
        return Long.valueOf(productId);
    }
}