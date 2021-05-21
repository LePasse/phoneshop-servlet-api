package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest
{
    private ProductDao productDao;

    @Before
    public void setup() {
        productDao = new ArrayListProductDao();
    }

    @Test
    public void testFindProductsNoResults() {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void testGetProduct(){
        Optional<Product> product = productDao.getProduct(1L);
        assertTrue(product.isPresent());
    }

    @Test
    public void testDeleteProduct(){
        Optional<Product> product = productDao.getProduct(2L);
        assertTrue(product.isPresent());
        productDao.delete(2L);
        product = productDao.getProduct(2L);
        assertFalse(product.isPresent());
    }

    @Test
    public void testSaveProduct(){
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("save test", "Samsung Galaxy SS", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);
        assertTrue(productDao.getProduct(product.getId()).isPresent());
    }

    @Test
    public void testSaveSameProduct(){
        Currency usd = Currency.getInstance("USD");
        Product product = new Product(22L, "save test", "Samsung Galaxy SS", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);
        product.setCode("new test");
        productDao.save(product);
        Optional<Product> copy = productDao.getProduct((productDao.getMaxId()-1));
        assertEquals(copy.get().getCode(),"new test");
    }


    @Test
    public void testPriceNullProduct() {
        Currency usd = Currency.getInstance("USD");
        List<Product> products = productDao.findProducts();
        int sizeBefore = products.size();
        productDao.save(new Product("save test", "Samsung Galaxy SS", null, usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        products = productDao.findProducts();
        int sizeAfter = products.size();
        assertEquals(sizeBefore, sizeAfter);
    }

    @Test
    public void testZeroStockProduct() {
        Currency usd = Currency.getInstance("USD");
        List<Product> products = productDao.findProducts();
        int sizeBefore = products.size();
        productDao.save(new Product("save test", "Samsung Galaxy SS", new BigDecimal(100), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        products = productDao.findProducts();
        int sizeAfter = products.size();
        assertEquals(sizeBefore, sizeAfter);
    }
}
