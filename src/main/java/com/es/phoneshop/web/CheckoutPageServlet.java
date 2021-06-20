package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.order.DefaultOrderService;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.OrderService;
import com.es.phoneshop.model.order.PaymentMethod;
import com.es.phoneshop.model.product.RecentlyViewed;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class CheckoutPageServlet extends HttpServlet {

    public final String CHECKOUT_JSP = "/WEB-INF/pages/checkout.jsp";
    private CartService cartService;
    private OrderService orderService;
    private RecentlyViewed recentlyViewed;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
        orderService = DefaultOrderService.getInstance();
        recentlyViewed = RecentlyViewed.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("order", orderService.getOrder(cartService.getCart(request)));
        request.setAttribute("paymentMethods", orderService.getPaymentMethods());
        request.setAttribute("recentlyViewed", recentlyViewed.getQueue(request));
        request.getRequestDispatcher(CHECKOUT_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Order order = orderService.getOrder(cartService.getCart(request));
        Map<String, String> errors = new HashMap<>();
        setRequiredParameters(request, response, errors, order);
    }

    private void setRequiredParameters(HttpServletRequest request, HttpServletResponse response, Map<String, String> errors, Order order) throws ServletException, IOException {
        String firstName = request.getParameter("firstName");
        if (!verifyName(firstName)) {
            errors.put("firstName", "First name must contain only letters.");
        }
        String lastName = request.getParameter("lastName");
        if (!verifyName(lastName)) {
            errors.put("lestName", "Last name must contain only letters.");
        }
        String phoneNumber = request.getParameter("phoneNumber");
        if (!verifyPhoneNumber(phoneNumber)) {
            errors.put("phoneNumber", "Phone number must contain digits spaces or dashes.");
        }
        String deliveryDateString = request.getParameter("deliveryDate");
        if (!verifyDeliveryDate(deliveryDateString)) {
            errors.put("deliveryDate", "Delivery date must be after now.");
        }
        String deliveryAddress = request.getParameter("deliveryAddress");
        if (!verifyAddress(deliveryAddress)) {
            errors.put("deliveryAddress", "Address must not be empty.");
        }
        String paymentMethodString = request.getParameter("paymentMethod");
        if (!verifyPaymentMethod(paymentMethodString)) {
            errors.put("paymentMethod", "Payment method must be one of the suggested.");
        }
        if (errors.isEmpty()) {
            order.setFirstName(firstName);
            order.setLastName(lastName);
            order.setPhone(phoneNumber);
            order.setDeliveryDate(LocalDate.parse(deliveryDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            order.setDeliveryAddress(deliveryAddress);
            order.setPaymentMethod(PaymentMethod.fromString(paymentMethodString));

            orderService.placeOrder(order);
            cartService.clearCart(cartService.getCart(request), order);
            response.sendRedirect(request.getContextPath() + "/order/overview/" + order.getSecureId());
        } else {
            request.setAttribute("errors", errors);
            doGet(request, response);
        }
    }

    private boolean verifyWord(String word) {
        return word != null && word.matches("[A-Za-z]+");
    }

    private boolean verifyNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    private boolean verifyName(String name) {
        return verifyWord(name);
    }

    private boolean verifyPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("[0-9 -]+");
    }

    private boolean verifyDeliveryDate(String deliveryDateStr) {
        try {
            return deliveryDateStr != null
                    && LocalDate.parse(deliveryDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    .isAfter(LocalDate.now());
        } catch (DateTimeException e) {
            return false;
        }
    }

    private boolean verifyAddress(String address) {
        return verifyNotEmpty(address);
    }

    private boolean verifyPaymentMethod(String paymentMethod) {
        return paymentMethod != null && (paymentMethod.equals("CASH") || paymentMethod.equals("CREDIT_CARD"));
    }
}
