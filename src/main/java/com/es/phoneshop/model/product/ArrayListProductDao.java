package com.es.phoneshop.model.product;


import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {
    private static ProductDao instance;

    public static synchronized ProductDao getInstance() {
        if (instance == null) {
            instance = new ArrayListProductDao();
        }
        return instance;
    }

    private List<Product> products;
    private long maxId;
    private static final ReentrantReadWriteLock locker = new ReentrantReadWriteLock(true);

    private ArrayListProductDao() {
        this.products = new ArrayList<>();
        maxId = 0;
    }

    @Override
    public synchronized Optional<Product> getProduct(Long id) {
        return products.stream()
                .filter(product -> id.equals(product.getId()))
                .findAny();
    }

    private double countPercentage(int count, String description) {
        return (double) count / description.split(" ").length;
    }

    private synchronized HashMap<Product, Integer> clearFrequency(HashMap<Product, Integer> frequency, int count) {
        frequency.forEach((k, v) -> {
            if (v < count) {
                frequency.remove(k);
            }
        });
        return frequency;
    }

    private List<Product> findByQuery(String query) {
        locker.writeLock().lock();
        try {
            if (query != null) {
                String[] keys = query.split(" ");
                HashMap<Product, Integer> frequency = new HashMap<>();
                for (String key : keys) {
                    products.stream()
                            .filter(product -> product.getDescription().matches(".*\\b" + key + "\\b.*"))
                            .filter(product -> product.getPrice() != null)
                            .filter(product -> product.getStock() > 0)
                            .forEach(product -> {
                                int count = frequency.getOrDefault(product, 0);
                                frequency.put(product, count + 1);
                            });
                }
                frequency.entrySet().removeIf(e -> (e.getValue() < keys.length));
                return frequency
                        .entrySet()
                        .stream()
                        .sorted(Comparator.comparingInt(e -> e.getKey().getDescription().split(" ").length))
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());

            } else {
                return products.stream()
                        .filter(product -> product.getPrice() != null)
                        .filter(product -> product.getStock() > 0)
                        .collect(Collectors.toList());
            }
        } finally {
            locker.writeLock().unlock();
        }
    }

    @Override
    public List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        locker.writeLock().lock();
        try {
            List<Product> result = findByQuery(query);
            if (sortField != null || sortOrder != null) {
                Comparator<Product> comparator = Comparator.comparing(product -> {
                    if (SortField.price == sortField) {
                        return (Comparable) product.getPrice();
                    } else {
                        return (Comparable) product.getDescription();
                    }
                });
                comparator = SortOrder.desc == sortOrder ? comparator.reversed() : comparator;
                return result.stream()
                        .filter(product -> product.getPrice() != null)
                        .filter(product -> product.getStock() > 0)
                        .sorted(comparator)
                        .collect(Collectors.toList());
            } else {
                return result.stream()
                        .filter(product -> product.getPrice() != null)
                        .filter(product -> product.getStock() > 0)
                        .collect(Collectors.toList());
            }

        } finally {
            locker.writeLock().unlock();
        }
    }

    @Override
    public void save(Product product) {
        locker.readLock().lock();
        try {
            if (product.getId() == null) {
                product.setId(maxId++);
                products.add(product);
            } else {
                Optional<Product> productCopy = getProduct(product.getId());
                if (productCopy.isPresent()) {
                    int index = products.indexOf(productCopy.get());
                    products.set(index, product);
                } else {
                    product.setId(maxId++);
                    products.add(product);
                }
            }
        } finally {
            locker.readLock().unlock();
        }
    }

    @Override
    public void delete(Long id) {
        locker.readLock().lock();
        try {
            Optional<Product> product = getProduct(id);
            product.ifPresent(value -> products.remove(value));
        } finally {
            locker.readLock().unlock();
        }

    }

    public long getMaxId() {
        return this.maxId;
    }
}
