<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>

    <div class="minicart">
        Cart: ${cart.totalQuantity} products with total cost
        <fmt:formatNumber value="${cart.totalCost}" type="currency" currencySymbol="${cart.items[0].product.currency.symbol}"/>
    </div</p>