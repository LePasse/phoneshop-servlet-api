<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="type" required="false" %>

<tr>
  <td>${label}:<span style="color:red">*</span></td>
  <td>
    <input type="${type}" name=${name} value="${param[name]}"/>
    <c:if test="${not empty errors.get(name)}">
      <p class="error">${errors.get(name)}</p>
    </c:if>
  </td>
</tr>