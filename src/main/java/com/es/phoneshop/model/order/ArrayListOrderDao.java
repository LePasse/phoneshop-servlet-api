package com.es.phoneshop.model.order;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ArrayListOrderDao implements OrderDao {
    private static OrderDao instance;

    public static synchronized OrderDao getInstance() {
        if (instance == null) {
            instance = new ArrayListOrderDao();
        }
        return instance;
    }

    private final List<Order> orders;
    private long maxId;
    private static final ReentrantReadWriteLock locker = new ReentrantReadWriteLock(true);

    private ArrayListOrderDao() {
        this.orders = new ArrayList<>();
        maxId = 0;
    }

    @Override
    public synchronized Optional<Order> getOrder(Long id) {
        return orders.stream()
                .filter(order -> id.equals(order.getId()))
                .findAny();
    }

    @Override
    public synchronized Optional<Order> getOrderBySecureId(String secureId) {
        return orders.stream()
                .filter(order -> secureId.equals(order.getSecureId()))
                .findAny();
    }

    @Override
    public void save(Order order) {
        locker.readLock().lock();
        try {
            order.setId(maxId++);
            orders.add(order);
        } finally {
            locker.readLock().unlock();
        }
    }
}
