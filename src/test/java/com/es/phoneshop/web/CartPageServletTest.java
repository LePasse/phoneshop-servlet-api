package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.product.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Currency;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private HttpSession session;
    @Mock
    private ServletConfig config;

    private CartPageServlet servlet = new CartPageServlet();
    private ProductDao productDao = ArrayListProductDao.getInstance();
    private CartService cartService = DefaultCartService.getInstance();

    private Cart cart = new Cart();
    private String[] productIds = {"1"};
    private String[] quantities = {"5"};

    @Before
    public void setup() throws ServletException, ParseException {
        servlet.init(config);
        Currency usd = Currency.getInstance("USD");
        productDao.save(new Product(1L, "test", "Samsung Galaxy SS", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg", new PriceHistory(new Date(2021, 2, 21), "100$")));
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(RecentlyViewed.class.getName() + ".recentlyViewed")).thenReturn(new LinkedList<>());
        when(session.getAttribute(DefaultCartService.class.getName() + ".cart")).thenReturn(cart);
        when(request.getLocale()).thenReturn(Locale.forLanguageTag("en_US"));
    }

    @After
    public void complete() {
        productDao.delete(1L);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);
        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute("cart", cart);
        verify(request).setAttribute("recentlyViewed", new LinkedList<>());
        verify(request, Mockito.atLeast(2)).getSession();
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        when(request.getParameterValues("productId")).thenReturn(productIds);
        when(request.getParameterValues("quantity")).thenReturn(quantities);

        cartService.add(cart, 1L, 1);

        servlet.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }
}
