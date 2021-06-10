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
import java.util.ArrayList;
import java.util.Optional;
import java.util.Queue;

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

    }

    private Long parseProductID(HttpServletRequest request) {
        String productId = request.getPathInfo().substring(1);
        return Long.valueOf(productId);
    }
}
