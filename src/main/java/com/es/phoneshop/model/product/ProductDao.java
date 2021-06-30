package com.es.phoneshop.model.product;

import com.es.phoneshop.model.order.PaymentMethod;

import java.util.List;
import java.util.Optional;

public interface ProductDao {
    Optional<Product> getProduct(Long id);

    List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder);

    List<Product> searchProducts(String query, Long minPrice, Long maxPrice, SearchType searchType);

    void save(Product product);

    void delete(Long id);

    long getMaxId();
}
