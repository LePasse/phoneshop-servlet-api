package com.es.phoneshop.web;

import com.es.phoneshop.model.order.ArrayListOrderDao;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.OrderDao;
import com.es.phoneshop.model.product.RecentlyViewed;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class OrderOverviewPageServlet extends HttpServlet {

    public final String ORDER_OVERVIEW_JSP = "/WEB-INF/pages/orderOverview.jsp";
    private OrderDao orderDao;
    private RecentlyViewed recentlyViewed;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        orderDao = ArrayListOrderDao.getInstance();
        recentlyViewed = RecentlyViewed.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String secureOrderId = parseOrderID(request);
        Optional<Order> order = orderDao.getOrderBySecureId(secureOrderId);
        if (order.isPresent()) {
            request.setAttribute("order", order.get());
            request.setAttribute("recentlyViewed", recentlyViewed.getQueue(request));
            request.getRequestDispatcher(ORDER_OVERVIEW_JSP).forward(request, response);
        } else {
            request.setAttribute("message", "Order " + secureOrderId + " not found");
            request.getRequestDispatcher("/WEB-INF/pages/errorProductNotFound.jsp").forward(request, response);
        }
    }

    private String parseOrderID(HttpServletRequest request) {
        return request.getPathInfo().substring(1);
    }
}
