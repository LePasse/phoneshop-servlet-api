package com.es.phoneshop.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutPageServletTest {
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

    private CheckoutPageServlet servlet = new CheckoutPageServlet();
    private String string = "a";
    private String phone = "80292326723";

    @Before
    public void setup() throws ServletException, ParseException {
        servlet.init(config);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getLocale()).thenReturn(Locale.forLanguageTag("en_US"));
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        when(request.getParameter("firstName")).thenReturn(string);
        when(request.getParameter("lastName")).thenReturn(string);
        when(request.getParameter("phoneNumber")).thenReturn(phone);
        when(request.getParameter("deliveryAddress")).thenReturn(string);
        when(request.getParameter("deliveryDate")).thenReturn("21-12-2222");
        when(request.getParameter("paymentMethod")).thenReturn("Cash");

        servlet.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }
}
