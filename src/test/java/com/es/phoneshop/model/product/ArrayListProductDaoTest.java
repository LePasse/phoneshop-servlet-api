package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest {
    private ProductDao productDao;

    @Before
    public void setup() {
        productDao = ArrayListProductDao.getInstance();
    }

    @Test
    public void testFindProductsNoResults() {
        assertFalse(productDao.findProducts(null, null, null).isEmpty());
    }

    @Test
    public void testFindProductWithQuery() {
        int size = productDao.findProducts("Samsung", SortField.PRICE, SortOrder.DESC).size();
        assertNotEquals(0, size);
    }

    @Test
    public void testGetProduct() {
        Optional<Product> product = productDao.getProduct(1L);
        assertTrue(product.isPresent());
    }

    @Test
    public void testDeleteProduct() {
        Optional<Product> product = productDao.getProduct(2L);
        assertTrue(product.isPresent());
        productDao.delete(2L);
        product = productDao.getProduct(2L);
        assertFalse(product.isPresent());
    }

    @Test
    public void testSaveProduct() {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("save test", "Samsung Galaxy SS", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg", new PriceHistory(new Date(2021, 2, 21), "100$"));
        productDao.save(product);
        assertTrue(productDao.getProduct(product.getId()).isPresent());
    }

    @Test
    public void testSaveExistingProduct() {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product(22L, "save test", "Samsung Galaxy SS", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg", new PriceHistory(new Date(2021, 2, 21), "100$"));
        productDao.save(product);
        product.setCode("new test");
        productDao.save(product);
        Optional<Product> copy = productDao.getProduct((productDao.getMaxId() - 1));
        assertEquals(copy.get().getCode(), "new test");
    }

    @Test
    public void testFindPriceNullProduct() {
        Currency usd = Currency.getInstance("USD");
        List<Product> products = productDao.findProducts(null, null, null);
        int sizeBefore = products.size();
        productDao.save(new Product("save test", "Samsung Galaxy SS", null, usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg", new PriceHistory(new Date(2021, 2, 21), "100$")));
        products = productDao.findProducts(null, null, null);
        int sizeAfter = products.size();
        assertEquals(sizeBefore, sizeAfter);
    }

    @Test
    public void testFindZeroStockProduct() {
        Currency usd = Currency.getInstance("USD");
        List<Product> products = productDao.findProducts(null, null, null);
        int sizeBefore = products.size();
        productDao.save(new Product("save test", "Samsung Galaxy SS", new BigDecimal(100), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg", new PriceHistory(new Date(2021, 2, 21), "100$")));
        products = productDao.findProducts(null, null, null);
        int sizeAfter = products.size();
        assertEquals(sizeBefore, sizeAfter);
    }

    @Test
    public void searchByNegativeMinPrice() {
        List<Product> products = productDao.searchProducts("", Long.valueOf(-100), null, SearchType.ANY_WORD);
        assertFalse(products.isEmpty());
    }

    @Test
    public void searchByNegativeMaxPrice() {
        List<Product> products = productDao.searchProducts("", Long.valueOf(0), Long.valueOf(-100), SearchType.ANY_WORD);
        assertTrue(products.isEmpty());
    }

    @Test
    public void searchBYNullMinPrice() {
        List<Product> products = productDao.searchProducts("", null, Long.valueOf(1000), SearchType.ANY_WORD);
        assertFalse(products.isEmpty());
    }

    @Test
    public void searchByNullMaxPrice() {
        List<Product> products = productDao.searchProducts("", Long.valueOf(0), null, SearchType.ANY_WORD);
        assertFalse(products.isEmpty());
    }

    @Test
    public void searchByEmptyQuery() {
        List<Product> products = productDao.searchProducts("", Long.valueOf(0), Long.valueOf(1000), SearchType.ANY_WORD);
        assertFalse(products.isEmpty());
    }
}
