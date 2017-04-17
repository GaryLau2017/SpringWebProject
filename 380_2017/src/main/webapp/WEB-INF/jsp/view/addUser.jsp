<!DOCTYPE html>
<html>
  <head>
    <title>Customer Support</title>
  </head>
  <body>
    
    <c:if test="${message != null}">
          <c:out value="${message}" />
        </c:if>
    <h2>Create a User</h2>
    <security:authorize access="isAuthenticated()">
      <c:url var="logoutUrl" value="/logout"/>
      <form action="${logoutUrl}" method="post">
        <input type="submit" value="Log out" />
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      </form>
    </security:authorize>
    <form:form method="POST" enctype="multipart/form-data"
               modelAttribute="postUser">
      <form:label path="username">Username</form:label><br/>
      <form:input type="text" path="username" /><br/><br/>
      <form:label path="password">Password</form:label><br/>
      <form:input type="text" path="password" /><br/><br/>
      <form:label path="roles">Roles</form:label><br/>
      <form:radiobutton path="roles" value="ROLE_USER" checked="checked" />ROLE_USER
      <form:radiobutton path="roles" value="ROLE_ADMIN" />ROLE_ADMIN
      <br /><br />
      <input type="submit" value="Add User"/>
    </form:form>
  </body>
</html>