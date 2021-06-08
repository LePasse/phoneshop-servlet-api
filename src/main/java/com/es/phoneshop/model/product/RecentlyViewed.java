package com.es.phoneshop.model.product;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.DefaultCartService;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class RecentlyViewed {
    public Queue<Product> queue;
    public static final String RECENTLY_VIEWED_SESSION_ATTRIBUTE = RecentlyViewed.class.getName() + ".recentlyViewed";

    private static RecentlyViewed instance;

    public static synchronized RecentlyViewed getInstance() {
        if (instance == null) {
            instance = new RecentlyViewed();
        }
        return instance;
    }

    public RecentlyViewed() {
    }

    public Queue<Product> getQueue(HttpServletRequest request) {
        queue = (Queue<Product>) request.getSession().getAttribute(RECENTLY_VIEWED_SESSION_ATTRIBUTE);
        if (queue == null) {
            queue = new LinkedList<>();
            request.getSession().setAttribute(RECENTLY_VIEWED_SESSION_ATTRIBUTE, queue);
        }
        return queue;
    }

    public void add(Queue<Product> queue, Product product) {
        if (!queue.contains(product)) {
            if (queue.size() == 3) {
                queue.remove();
            }
            queue.add(product);
        }
    }
}
