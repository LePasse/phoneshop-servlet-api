<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>

<tags:master pageTitle="Product Details">

    <h1>${product.description}</h1>
    <h3 class="success"> ${param.modalSuccess}</h3>
    <h3 class="error">${param.modalError}</h3>

    <form method="post" action="${pageContext.servletContext.contextPath}/products/${product.id}">
        <table>
            <tr>
                <td>Image</td>
                <td><img src="${product.imageUrl}"></td>
            </tr>
            <tr>
                <td>Code</td>
                <td class="detail">${product.code}</td>
            </tr>
            <tr>
                <td>Price</td>
                <td class="detail">
                    <fmt:formatNumber value="${product.price}" type="currency"
                                      currencySymbol="${product.currency.symbol}"/>
                </td>
            </tr>
            <tr>
                <td>Stock</td>
                <td class="detail">${product.stock}</td>
            </tr>
            <tr>
                <td>Quantity</td>
                <td>
                    <input name="quantity" class="detail" value="${not empty error? param.quantity : 1}">
                    <p class="error">
                            ${error}
                    </p>
                </td>
            </tr>
        </table>
        <p>
            <button>Add to cart</button>
        </p>
    </form>

    <footer>
        <jsp:include page="footer.jsp"/>
    </footer>
</tags:master>