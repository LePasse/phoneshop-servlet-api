package com.es.phoneshop.model.order;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.PriceHistory;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;

import static org.junit.Assert.*;

public class DefaultOrderServiceTest {
    public static final int QUANTITY_ONE = 1;
    public static final int QUANTITY_TWO = 2;
    private final CartService cartService = DefaultCartService.getInstance();
    private final OrderService orderService = DefaultOrderService.getInstance();
    private final ProductDao productDao = ArrayListProductDao.getInstance();
    private Cart cart;
    private Order order;
    Currency usd = Currency.getInstance("USD");
    private final Product product1 = new Product(1L,"product1", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg", new PriceHistory(new Date(2021, 2, 21), "100$"));;
    private final Product product2 = new Product(2L,"product2", "Samsung Galaxy SS", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg", new PriceHistory(new Date(2021, 2, 21), "100$"));;

    @Before
    public void setup() {
        cart = new Cart();
        productDao.save(product1);
        productDao.save(product2);
        cartService.add(cart, product1.getId(), QUANTITY_ONE);
        cartService.add(cart, product2.getId(), QUANTITY_TWO);
        order = new Order();
    }

    @Test
    public void testGetOrderDeliveryCostIsSet() {
        order.setDeliveryCost(null);
        Order updatedOrder = orderService.getOrder(cart);

        assertNotNull(updatedOrder.getDeliveryCost());
    }

    @Test
    public void testGetOrderSubtotalIsEqualToCartTotalCost() {
        Order order1 = orderService.getOrder(cart);
        assertEquals(order1.getSubtotal(), cart.getTotalCost());
    }

    @Test
    public void testGetOrderTotalCostIsEqualToSubtotalAndDeliveryCostSum() {
        Order order1 = orderService.getOrder(cart);
        assertEquals(order1.getTotalCost(), order1.getSubtotal().add(order1.getDeliveryCost()));
    }

    @Test
    public void testGetOrderCartIsCloned() {
        Order order1 = orderService.getOrder(cart);
        assertNotSame(cart.getItems(), order1.getItems());
    }

    @Test
    public void testPlaceOrderSecureIdIsCreated() {
        order.setSecureId(null);
        orderService.placeOrder(order);

        assertNotNull(order.getSecureId());
    }

    @Test
    public void testClearCartDecreasesCart() {
        Cart cart = new Cart();
        cartService.add(cart, 1L, QUANTITY_ONE);
        assertFalse(cart.getItems().isEmpty());
        Order order = orderService.getOrder(cart);
        orderService.placeOrder(order);
        cartService.clearCart(cart, order);
        assertTrue(cart.getItems().isEmpty());
    }
}
