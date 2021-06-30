<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="label" required="true" %>

<p>
  ${label}
  <input name="${name}" value="${param[name]}">
  <c:if test="${not empty errors.get(name)}">
      <p class="error">${errors.get(name)}</p>
  </c:if>
</p>