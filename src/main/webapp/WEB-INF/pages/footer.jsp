<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="recentlyViewed" type="java.util.LinkedList" scope="request"/>

<c:if test="${recentlyViewed.size() > 0}">
    <h3>Recently viewed</h3>
    <table>
        <thead>
        <tr>
            <td>Image</td>
            <td>Description</td>
            <td class="price"> Price</td>
        </tr>
        </thead>
        <c:forEach var="product" items="${recentlyViewed}">
            <tr>
                <td><img class="product-tile" src="${product.imageUrl}"></td>
                <td>
                    <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                            ${product.description}
                    </a>
                </td>
                <td class="price">
                    <a href='#'
                       onclick='javascript:window.open("${pageContext.servletContext.contextPath}/priceHistory/${product.id}",
                               "_blank", "scrollbars=1,resizable=1,height=300,width=450");' title='Pop Up'>
                        <fmt:formatNumber value="${product.price}" type="currency"
                                          currencySymbol="${product.currency.symbol}"/>
                    </a>
                </td>
            </tr>
        </c:forEach>
    </table>
</c:if>
<p><h3>PhoneShop, Pavel Lebedzinski 2021</h3></p>