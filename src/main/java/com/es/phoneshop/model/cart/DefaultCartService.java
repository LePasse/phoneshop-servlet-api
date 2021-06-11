package com.es.phoneshop.model.cart;


import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Collectors;

public class DefaultCartService implements CartService {
    public static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";
    private final ProductDao productDao;
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
    public synchronized Cart getCart(HttpServletRequest request) {
        Cart cart = (Cart) request.getSession().getAttribute(CART_SESSION_ATTRIBUTE);
        if (cart == null) {
            cart = new Cart();
            request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, cart);
        }
        return cart;
    }

    @Override
    public synchronized CartResult add(Cart cart, Long productId, int quantity) {
        Optional<Product> product = productDao.getProduct(productId);
        if (!product.isPresent()) return CartResult.PRODUCT_NOT_FOUND;
        Optional<CartItem> item = cart.getItems()
                .stream()
                .filter(cartItem -> cartItem.getProduct().equals(product.get()))
                .findAny();
        if (item.isPresent()) {
            if (quantity + item.get().getQuantity() > product.get().getStock()) {
                return CartResult.NOT_ENOUGH_STOCK;
            }
            item.get().setQuantity(quantity + item.get().getQuantity());
        } else {
            if (quantity > product.get().getStock()) {
                return CartResult.NOT_ENOUGH_STOCK;
            }
            cart.getItems().add(new CartItem(product.get(), quantity));
        }
        recalculateCart(cart);
        return CartResult.SUCCESS;
    }

    @Override
    public CartResult update(Cart cart, Long productId, int quantity) {
        Optional<Product> product = productDao.getProduct(productId);
        if (!product.isPresent()) return CartResult.PRODUCT_NOT_FOUND;
        Optional<CartItem> item = cart.getItems()
                .stream()
                .filter(cartItem -> cartItem.getProduct().equals(product.get()))
                .findAny();
        if (quantity > product.get().getStock()) {
            return CartResult.NOT_ENOUGH_STOCK;
        }
        item.get().setQuantity(quantity);
        recalculateCart(cart);
        return CartResult.SUCCESS;
    }
}
