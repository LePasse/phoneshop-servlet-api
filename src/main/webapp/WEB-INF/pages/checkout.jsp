<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>

<tags:master pageTitle="Checkout">
    <p>
    <c:if test="${not empty param.modalSuccess}">
        <h3 class="success"> ${param.modalSuccess}</h3>
    </c:if>
    <c:if test="${not empty errors}">
        <h3 class="error">There were some errors confirming order</h3>
    </c:if>
    </p>
    <form method="post" action="${pageContext.servletContext.contextPath}/checkout">
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
    </form>

    <h2>Order details</h2>
    <form method="post" action="${pageContext.servletContext.contextPath}/checkout">
        <table>
            <tags:orderFormRow name="firstName" label="First name"></tags:orderFormRow>
            <tags:orderFormRow name="lastName" label="Last name"></tags:orderFormRow>
            <tags:orderFormRow name="phoneNumber" label="Phone number"></tags:orderFormRow>
            <tags:orderFormRow name="deliveryDate" label="Delivery date" type="date"></tags:orderFormRow>
            <tags:orderFormRow name="deliveryAddress" label="Delivery address"></tags:orderFormRow>

            <tr>
                <td>Payment method:<span style="color:red">*</span></td>
                <td>
                    <select name="paymentMethod">
                        <c:forEach var="item" items="${paymentMethods}">
                            <option value="${item}" ${param.paymentMethod == item? "selected" : ""}>${item}</option>
                        </c:forEach>
                    </select>
                    <c:if test="${not empty errors.get('paymentMethod')}">
                        <p class="error">${errors.get('paymentMethod')}</p>
                    </c:if>
                </td>
            </tr>
        </table>

        <c:if test="${not empty order.items}">
            <p><button type="submit">Place order</button></p>
        </c:if>
    </form>

    <footer>
        <jsp:include page="footer.jsp"/>
    </footer>
</tags:master>