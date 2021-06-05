package com.es.phoneshop.model.cart;


import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DefaultCartService implements CartService {
    public static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";
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
    public synchronized AddResult add(Cart cart, Long productId, int quantity) {
        Optional<Product> product = productDao.getProduct(productId);
        CartItem item = new CartItem(product.get(), quantity);
        List<Product> list = cart.getItems().stream()
                .map(CartItem::getProduct)
                .collect(Collectors.toList());
        int indexOfCartItem = list.indexOf(product.get());
        if (indexOfCartItem == -1) {
            if (quantity > product.get().getStock()) {
                return AddResult.NOT_ENOUGH_STOCK;
            }
            cart.getItems().add(item);
        } else {
            if (quantity + cart.getItems().get(indexOfCartItem).getQuantity() > product.get().getStock()) {
                return AddResult.NOT_ENOUGH_STOCK;
            }
            CartItem temp = cart.getItems().get(indexOfCartItem);
            temp.setQuantity(quantity + temp.getQuantity());
        }
        return AddResult.SUCCESS;
    }
}
