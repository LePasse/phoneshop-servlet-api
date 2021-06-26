<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>

<tags:master pageTitle="Order Overview">
    <h1>Order overview</h1>
    <table>
        <thead>
        <tr>
            <td>Image</td>
            <td>Description</td>
            <td class="price">Price</td>
            <td>Quantity</td>
        </tr>
        </thead>
        <c:forEach var="item" items="${order.items}" varStatus="status">
            <tr>
                <td><img class="product-tile" src="${item.product.imageUrl}"></td>
                <td>
                    <a href="${pageContext.servletContext.contextPath}/products/${item.product.id}">
                            ${item.product.description}
                    </a>
                </td>
                <td class="price">
                    <a href='#'
                       onclick='javascript:window.open("${pageContext.servletContext.contextPath}/priceHistory/${item.product.id}", "_blank", "scrollbars=1,resizable=1,height=300,width=450");'
                       title='Pop Up'>
                        <fmt:formatNumber value="${item.product.price}" type="currency"
                                          currencySymbol="${item.product.currency.symbol}"/>
                    </a>
                </td>
                <td class="detail">${item.quantity}</td>
            </tr>
        </c:forEach>
        <tr>
            <td class="detail">Subtotal cost:</td>
            <td class="price"><fmt:formatNumber value="${order.subtotal}" type="currency"
                                                currencySymbol="${order.items[0].product.currency.symbol}"/></td>
        </tr>
        <tr>
            <td class="detail">Delivery cost:</td>
            <td class="price"><fmt:formatNumber value="${order.deliveryCost}" type="currency"
                                                currencySymbol="${order.items[0].product.currency.symbol}"/></td>
        </tr>
        <tr>
            <td class="detail">Total cost:</td>
            <td class="price"><fmt:formatNumber value="${order.totalCost}" type="currency"
                                                currencySymbol="${order.items[0].product.currency.symbol}"/></td>
        </tr>
    </table>

    <h2>Order details</h2>
    <table>
        <tr>
            <td>First name:</td>
            <td>${order.firstName}</td>
        </tr>
        <tr>
            <td>Last name:</td>
            <td>${order.lastName}</td>
        </tr>
        <tr>
            <td>Phone number:</td>
            <td>${order.phone}</td>
        </tr>
        <tr>
            <td>Delivery date:</td>
            <td>${order.deliveryDate}</td>
        </tr>
        <tr>
            <td>Delivery address:</td>
            <td>${order.deliveryAddress}</td>
        </tr>
        <tr>
            <td>Payment method:</td>
            <td>
                <c:choose>
                    <c:when test="${order.paymentMethod == 'CASH'}">
                        Cash
                    </c:when>
                    <c:when test="${order.paymentMethod == 'CREDIT_CARD'}">
                        Credit card
                    </c:when>
                    <c:otherwise>
                        Not selected.
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </table>

    <footer>
        <jsp:include page="footer.jsp"/>
    </footer>
</tags:master>