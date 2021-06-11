package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.AddResult;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.product.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import static com.es.phoneshop.web.tools.RequestTools.parseIntegerUsingLocale;

public class CartPageServlet extends HttpServlet {

    public final String CART_JSP = "/WEB-INF/pages/cart.jsp";
    private ProductDao productDao;
    private CartService cartService;
    private RecentlyViewed recentlyViewed;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
        recentlyViewed = RecentlyViewed.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("cart", cartService.getCart(request));
        request.setAttribute("recentlyViewed", recentlyViewed.getQueue(request));
        request.getRequestDispatcher(CART_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] productIds = request.getParameterValues("productId");
        String[] quantities = request.getParameterValues("quantity");

        Map<Long, String> errors = new HashMap<>();
        for (int i = 0; i < productIds.length; i++) {
            Long productId = Long.valueOf(productIds[i]);
            int quantity = 0;
            try {
                quantity = parseIntegerUsingLocale(request, quantities[i]);
                Cart cart = cartService.getCart(request);
                AddResult result = cartService.update(cart, productId, quantity);
                switch (result) {
                    case NOT_ENOUGH_STOCK:
                        errors.put(productId, "Not enough stock, max available " + productDao.getProduct(productId).get().getStock());
                        break;
                    case PRODUCT_NOT_FOUND:
                        errors.put(productId, "Product not found");
                }
            } catch (ParseException e) {
                errors.put(productId, "Quantity is not a valid number");
            }
        }
        if (errors.isEmpty()){
            response.sendRedirect(request.getContextPath() + "/cart?modalSuccess=Cart updated successfully");
        } else {
            request.setAttribute("errors", errors);
            doGet(request, response);
        }
    }
}
