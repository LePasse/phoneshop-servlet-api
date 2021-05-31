<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Price History">
  <h1>
    ${product.description}
  </h1>
  <table>
       <c:forEach var="pair" items="${product.history.getHistory().entrySet()}">
        <tr>
          <td>
              ${pair.getKey()}
          </td>
          <td class="price">
             ${pair.getValue()}
          </td>
        </tr>
      </c:forEach>
    </table>
</tags:master>