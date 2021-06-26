package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.PriceHistory;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;
import java.util.Queue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DefaultCartServiceTest {
    public static final int QUANTITY_ONE = 1;
    public static final int QUANTITY_TWO = 2;
    private final CartService cartService = DefaultCartService.getInstance();
    private final ProductDao productDao = ArrayListProductDao.getInstance();
    private final Cart cart = new Cart();
    Currency usd = Currency.getInstance("USD");
    private final Product product1 = new Product("product1", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg", new PriceHistory(new Date(2021, 2, 21), "100$"));;
    private final Product product2 = new Product("product2", "Samsung Galaxy SS", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg", new PriceHistory(new Date(2021, 2, 21), "100$"));;

    @Before
    public void setup() {
        productDao.save(product1);
        productDao.save(product2);
    }

    @Test
    public void testAddToEmptyCart() {
        cart.getItems().add(new CartItem(product1, QUANTITY_ONE));
        assertEquals(cart.getItems().size(), 1);
    }

    @Test
    public void testAddTwoProducts() {
        cart.getItems().add(new CartItem(product1, QUANTITY_ONE));
        cart.getItems().add(new CartItem(product2, QUANTITY_TWO));
        assertEquals(cart.getItems().size(), 2);
    }

    @Test
    public void testDeleteFromCart() {
        cart.getItems().add(new CartItem(product1, QUANTITY_ONE));
        cartService.delete(cart, product1.getId());
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    public void testDeleteNotExistingProductMakesNoChange() {
        cartService.delete(cart, product2.getId());
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    public void testUpdateOnExistingProductChangesQuantity() {
        cart.getItems().add(new CartItem(product1, QUANTITY_ONE));
        cartService.update(cart, product1.getId(), QUANTITY_TWO);
        assertEquals(1L, cart.getItems().stream()
                .filter(cartItem -> cartItem.getProduct().equals(product1))
                .count()
        );
        assertEquals(QUANTITY_TWO, cart.getItems().stream()
                .filter(cartItem -> cartItem.getProduct().equals(product1))
                .findAny().get().getQuantity()
        );
    }

    @Test
    public void testUpdateRecalculatesTotalCost() {
        cart.getItems().add(new CartItem(product1, QUANTITY_ONE));
        cart.setTotalCost(product1.getPrice());

        cartService.update(cart, product1.getId(), QUANTITY_TWO);

        assertEquals(product1.getPrice().multiply(BigDecimal.valueOf(QUANTITY_TWO)), cart.getTotalCost());
    }

    @Test
    public void testDeleteRecalculatesTotalCost() {
        cart.getItems().add(new CartItem(product1, QUANTITY_ONE));
        cart.setTotalCost(product1.getPrice());

        cartService.delete(cart, product1.getId());

        assertEquals(BigDecimal.ZERO, cart.getTotalCost());
    }

    @Test
    public void testUpdateRecalculatesTotalQuantity() {
        cart.getItems().add(new CartItem(product1, QUANTITY_ONE));
        cart.setTotalQuantity(QUANTITY_ONE);

        cartService.update(cart, product1.getId(), QUANTITY_TWO);
        assertEquals(QUANTITY_TWO, cart.getTotalQuantity());
    }

    @Test
    public void testDeleteRecalculatesTotalQuantity() {
        cart.getItems().add(new CartItem(product1, QUANTITY_ONE));
        cart.setTotalQuantity(QUANTITY_ONE);

        cartService.delete(cart, product1.getId());

        assertEquals(0, cart.getTotalQuantity());
    }

    @Test
    public void testAddTwoProductsSumTotalQuantity() {
        cartService.add(cart, product1.getId(), QUANTITY_ONE);
        cartService.add(cart, product2.getId(), QUANTITY_TWO);
        assertEquals(cart.getTotalQuantity(), QUANTITY_ONE + QUANTITY_TWO);
    }

    @Test
    public void testAddTwoProductsSumTotalCost() {
        cartService.add(cart, product1.getId(), QUANTITY_ONE);
        cartService.add(cart, product2.getId(), QUANTITY_TWO);
        BigDecimal cost1 = product1.getPrice().multiply(new BigDecimal(QUANTITY_ONE));
        BigDecimal cost2 = product2.getPrice().multiply(new BigDecimal(QUANTITY_TWO));
        assertEquals(cart.getTotalCost(), cost1.add(cost2));
    }
}
