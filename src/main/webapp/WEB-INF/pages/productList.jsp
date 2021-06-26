<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>

<tags:master pageTitle="Product List">

    <p>Welcome to Expert-Soft training!</p>
    <p>
        <c:if test="${not empty param.modalSuccess}">
            <h3 class="success">${param.modalSuccess}</h3>
        </c:if>
        <c:if test="${not empty param.modalError}">
            <h3 class="error">${param.modalError}</h3>
        </c:if>
    </p>
    <form>
        <input name="query" value="${param.query}">
        <button>Search</button>
    </form>

    <table>
        <thead>
        <tr>
            <td>Image</td>
            <td>
                Description
                <tags:sortLink sort="DESCRIPTION" order="ASC"/>
                <tags:sortLink sort="DESCRIPTION" order="DESC"/>
            </td>
            <td class="price">
                Price
                <tags:sortLink sort="PRICE" order="ASC"/>
                <tags:sortLink sort="PRICE" order="DESC"/>
            </td>
        </tr>
        </thead>
        <c:forEach var="product" items="${products}">
            <tr>
                <td>
                    <img class="product-tile" src="${product.imageUrl}">
                </td>
                <td>
                    <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                            ${product.description}
                    </a>
                    <c:if test="${not empty error && productId == product.id}">
                        <p class="error">${error}</p>
                    </c:if>
                </td>
                <td class="price">
                    <a href='#'
                       onclick='javascript:window.open("${pageContext.servletContext.contextPath}/priceHistory/${product.id}",
                               "_blank", "scrollbars=1,resizable=1,height=300,width=450");' title='Pop Up'>
                        <fmt:formatNumber value="${product.price}" type="currency"
                                          currencySymbol="${product.currency.symbol}"/>
                    </a>
                </td>
                <td>
                    <button form="addCartItem"
                            formaction="${pageContext.servletContext.contextPath}/cart/addCartItem/${product.id}">
                        Add to cart
                    </button>
                </td>
            </tr>
        </c:forEach>
    </table>
    <form id="addCartItem" method="post">
    </form>

    <footer>
        <jsp:include page="footer.jsp"/>
    </footer>

</tags:master>