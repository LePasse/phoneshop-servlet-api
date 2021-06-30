package com.es.phoneshop.model.product;


import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {
    private static ProductDao instance;

    public static synchronized ProductDao getInstance() {
        if (instance == null) {
            instance = new ArrayListProductDao();
        }
        return instance;
    }

    private final List<Product> products;
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

    private List<Product> findProducts(String query, SearchType searchType) {
        locker.writeLock().lock();
        try {
            if (query != null) {
                String[] keywords = query.toLowerCase().split(" ");
                ToIntFunction<Product> getNumberOfMatches = product -> (int) Arrays.stream(keywords)
                        .filter(product.getDescription().toLowerCase()::contains)
                        .count();
                int keywordsCount = keywords.length;
                if (searchType.equals(SearchType.ALL_WORDS)) {
                    return products.stream()
                            .filter(product -> product.getPrice() != null)
                            .filter(product -> product.getStock() > 0)
                            .filter(product -> (keywordsCount == getNumberOfMatches.applyAsInt(product)))
                            .sorted(Comparator.comparingInt(getNumberOfMatches))
                            .collect(Collectors.toList());
                } else {
                    return products.stream()
                            .filter(product -> product.getPrice() != null)
                            .filter(product -> product.getStock() > 0)
                            .filter(product -> (getNumberOfMatches.applyAsInt(product) > 0))
                            .sorted(Comparator.comparingInt(getNumberOfMatches))
                            .collect(Collectors.toList());
                }
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
            List<Product> result = findProducts(query, SearchType.ALL_WORDS);
            if (sortField == null) {
                return result;
            }
            Comparator<Product> comparator = Comparator.comparing(product -> {
                if (SortField.PRICE == sortField) {
                    return (Comparable) product.getPrice();
                } else {
                    return (Comparable) product.getDescription();
                }
            });
            comparator = SortOrder.DESC == sortOrder ? comparator.reversed() : comparator;
            return result.stream()
                    .sorted(comparator)
                    .collect(Collectors.toList());

        } finally {
            locker.writeLock().unlock();
        }
    }

    @Override
    public List<Product> searchProducts(String query, Long minPrice, Long maxPrice, SearchType searchType) {
        locker.writeLock().lock();
        try {
            List<Product> result = findProducts(query, searchType);
            if (minPrice != null) {
                result = result.stream().filter(product -> product.getPrice().intValue() >= minPrice).collect(Collectors.toList());
            }
            if (maxPrice != null) {
                result = result.stream().filter(product -> product.getPrice().intValue() <= maxPrice).collect(Collectors.toList());
            }
            return result;
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
