<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<tags:master pageTitle="Cart">
    <form method="post">
    <table>
        <thead>
          <tr>
            <td>Image</td>
            <td>
                Description
            </td>
            <td class="price">
                Price
            </td>
            <td>
                Quantity
            </td>
          </tr>
        </thead>
        <c:forEach var="item" items="${cart.items}">
                <tr>
                      <td><img class="product-tile" src="${item.product.imageUrl}"></td>
                      <td>
                          <a href="${pageContext.servletContext.contextPath}/products/${item.product.id}">
                             ${item.product.description}
                          </a>
                      </td>
                      <td class="price">
                          <a <a href='#' onclick='javascript:window.open("${pageContext.servletContext.contextPath}/priceHistory/${item.product.id}", "_blank", "scrollbars=1,resizable=1,height=300,width=450");' title='Pop Up'>
                             <fmt:formatNumber value="${item.product.price}" type="currency" currencySymbol="${item.product.currency.symbol}"/>
                          </a>
                      </td>
                      <td class="detail">
                        <fmt:formatNumber value="${item.quantity}" var="quantity"/>
                        <input name="quantity" value=${quantity} class="quantity"/>
                        <input name="productId" type="hidden" value="${item.product.id}"/>
                      </td>
                </tr>
        </c:forEach>
    </table>
    <p>
        <button>Update cart</button>
    </p>
    </form>

    <footer>
        <jsp:include page="footer.jsp"/>
    </footer>
</tags:master>