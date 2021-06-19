<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>

<tags:master pageTitle="Cart">
    <p>
    <c:if test="${not empty param.modalSuccess}">
        <h3 class="success"> ${param.modalSuccess}</h3>
    </c:if>
    <c:if test="${not empty errors}">
        <h3 class="error">There were some errors updating cart</h3>
    </c:if>
    </p>
    <c:if test="${cart.getItems().size() > 0}">
        <form method="post" action="${pageContext.servletContext.contextPath}/cart">
            <table>
                <thead>
                <tr>
                    <td>Image</td>
                    <td>Description</td>
                    <td class="price"> Price</td>
                    <td>Quantity</td>
                </tr>
                </thead>

                <c:forEach var="item" items="${cart.items}" varStatus="status">
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
                        <td class="detail">
                            <fmt:formatNumber value="${item.quantity}" var="quantity"/>
                            <c:set var="error" value="${errors[item.product.id]}"/>
                            <input name="quantity"
                                   value=${not empty error? paramValues['quantity'][status.index] : item.quantity} class="quantity"/>

                            <c:if test="${not empty error}">
                                <p class="error">${error}</p>
                            </c:if>
                            <input name="productId" type="hidden" value="${item.product.id}"/>
                        </td>
                        <td>
                            <button form="deleteCartItem"
                                    formaction="${pageContext.servletContext.contextPath}/cart/deleteCartItem/${item.product.id}">
                                Delete
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                <tr>
                    <td>Total quantity</td>
                    <td>${cart.totalQuantity}</td>
                </tr>
                <tr>
                    <td>Total cost</td>
                    <td><fmt:formatNumber value="${cart.totalCost}" type="currency"
                                          currencySymbol="${cart.items[0].product.currency.symbol}"/></td>

                </tr>
            </table>
            <p>
                <button>Update cart</button>
            </p>
        </form>
    </c:if>

    <form id="deleteCartItem" method="post">
    </form>

    <c:if test="${cart.getItems().size() == 0}">
        <h1>
            Your cart is empty.
        </h1>
        <a href="${pageContext.servletContext.contextPath}/products"/>
        -> to main page
        </a>
    </c:if>

    <footer>
        <jsp:include page="footer.jsp"/>
    </footer>
</tags:master>