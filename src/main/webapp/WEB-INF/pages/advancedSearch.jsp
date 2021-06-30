<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>

<tags:master pageTitle="Search Page">

    <form method="get">
        <h1>Advanced Search</h1>
        <p>
           <tags:advancedSearchField name="query" label="Description"></tags:advancedSearchField>

            <select name="searchType">
                <c:forEach var="item" items="${searchTypes}">
                    <option value="${item}" ${param.searchType == item.toString()? "selected" : ""}>${item}</option>
                </c:forEach>
            </select>
            <c:if test="${not empty errors.get('searchType')}">
                <p class="error">${errors.get('searchType')}</p>
            </c:if>
        </p>
        <tags:advancedSearchField name="minPrice" label="Min price"></tags:advancedSearchField>
        <tags:advancedSearchField name="maxPrice" label="Max price"></tags:advancedSearchField>
        <button>Search</button>
    </form>

    <table>
        <thead>
        <tr>
            <td>Image</td>
            <td>Description</td>
            <td class="price">Price</td>
        </tr>
        </thead>
        <c:forEach var="product" items="${products}">
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
</tags:master>
