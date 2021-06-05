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
import java.util.Optional;

import static com.es.phoneshop.web.tools.RequestTools.parseIntegerUsingLocale;

public class ProductDetailPageServlet extends HttpServlet {

    private ProductDao productDao;
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("product", productDao.getProduct(parseProductID(request)));
        } catch (IllegalArgumentException e) {
            response.sendError(404);
            return;
        }

        //if (sessionHasAttributes(request)) {
        //    setRequestAttributes(request);
        //}

        Long productID = parseProductID(request);
        Optional<Product> product = productDao.getProduct(productID);
        if (product.isPresent()) {
            request.setAttribute("product", product.get());
            request.setAttribute("cart", cartService.getCart(request));
            request.getRequestDispatcher("/WEB-INF/pages/product.jsp").forward(request, response);
        } else {
            request.setAttribute("message", "Product " + productID + " not found");
            request.getRequestDispatcher("/WEB-INF/pages/errorProductNotFound.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String quantityString = request.getParameter("quantity");
        Long productID = parseProductID(request);
        int quantity;
        try {
            quantity = parseIntegerUsingLocale(request, quantityString);
        } catch (ParseException e) {
            request.setAttribute("error", "Quantity is not a valid number");
            request.setAttribute("modalError", "Error adding to cart");
            doGet(request, response);
            return;
        }
        Cart cart = cartService.getCart(request);
        AddResult result = cartService.add(cart, productID, quantity);
        switch (result) {
            case SUCCESS:
                response.sendRedirect(request.getContextPath() + "/products/" + productID + "?modalSuccess=Added to cart successfully");
                return;
            case NOT_ENOUGH_STOCK:
                response.sendRedirect(request.getContextPath() + "/products/" + productID + "?modalError=Not enough stock");
                return;
        }
    }

    private Long parseProductID(HttpServletRequest request) {
        String productId = request.getPathInfo().substring(1);
        return Long.valueOf(productId);
    }
}
