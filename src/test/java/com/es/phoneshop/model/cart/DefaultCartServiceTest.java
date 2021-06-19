package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.product.ArrayListProductDao;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class DefaultCartServiceTest {
    private DefaultCartService defaultCartService;

    @Before
    public void setup() {
        defaultCartService = DefaultCartService.getInstance();
    }

    @Test
    public void testAddZeroQuantityProduct() {

    }

    @Test
    public void testAddProductWithNotEnoughStock() {

    }
}
