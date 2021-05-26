package com.es.phoneshop.model.product;


import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {

    private List<Product> products;
    private long maxId;
    private static final ReentrantReadWriteLock locker = new ReentrantReadWriteLock(true);

    public ArrayListProductDao() {
        this.products = new ArrayList<>();
        maxId = 0;

        getSampleProducts();
    }

    @Override
    public synchronized Optional<Product> getProduct(Long id) {
        return products.stream()
                .filter(product -> id.equals(product.getId()))
                .findAny();
    }

    @Override
    public List<Product> findProducts(String query) {
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
                return frequency.entrySet()
                        .stream()
                        .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
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

    public void getSampleProducts() {
        Currency usd = Currency.getInstance("USD");
        save(new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        save(new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 1, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg"));
        save(new Product("sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"));
        save(new Product("iphone", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg"));
        save(new Product("iphone6", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg"));
        save(new Product("htces4g", "HTC EVO Shift 4G", new BigDecimal(320), usd, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg"));
        save(new Product("sec901", "Sony Ericsson C901", new BigDecimal(420), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg"));
        save(new Product("xperiaxz", "Sony Xperia XZ", new BigDecimal(120), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg"));
        save(new Product("nokia3310", "Nokia 3310", new BigDecimal(70), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg"));
        save(new Product("palmp", "Palm Pixi", new BigDecimal(170), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg"));
        save(new Product("simc56", "Siemens C56", new BigDecimal(70), usd, 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg"));
        save(new Product("simc61", "Siemens C61", new BigDecimal(80), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg"));
        save(new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg"));
    }

    public long getMaxId() {
        return this.maxId;
    }
}
