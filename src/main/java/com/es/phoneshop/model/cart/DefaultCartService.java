package com.es.phoneshop.model.cart;


import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import java.util.List;
import java.util.Optional;

public class DefaultCartService implements CartService {
    private Cart cart = new Cart();
    private ProductDao productDao;
    private static DefaultCartService instance;

    public static synchronized DefaultCartService getInstance() {
        if (instance == null) {
            instance = new DefaultCartService();
        }
        return instance;
    }

    public DefaultCartService() {
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    public Cart getCart() {
        return cart;
    }

    @Override
    public AddResult add(Long productId, int quantity) {
        Optional<Product> product = productDao.getProduct(productId);
        if (product.isPresent()){
            if (product.get().getStock() >= quantity) {
                cart.getItems().add(new CartItem(product.get(),quantity));
                return AddResult.SUCCESS;
            } else return AddResult.NOT_ENOUGH_STOCK;
        } else return AddResult.PRODUCT_NOT_FOUND;
    }
}
