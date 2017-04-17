<!DOCTYPE html>
<html>
  <head>
    <title>Customer Support</title>
  </head>
  <body>

    <h2>All Post</h2>
    <security:authorize access="isAuthenticated()">
      <c:url var="logoutUrl" value="/logout"/>
      <form action="${logoutUrl}" method="post">
        <input type="submit" value="Log out" />
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      </form>
    </security:authorize> 
    <security:authorize access="hasRole('ADMIN')">    
      <a href="<c:url value="/user" />">Manage User Accounts</a><br /><br />
    </security:authorize>
    <security:authorize access="isAuthenticated()">
      <a href="<c:url value="/post/create" />">New Post</a>  
    </security:authorize>       
    <a href="<c:url value="/post/listlecture" />">lecture</a>
    <a href="<c:url value="/post/listlab" />">lab</a>
    <a href="<c:url value="/post/listother" />">other</a>
    <a href="<c:url value="/post/list" />">all</a>
    <security:authorize access="isAnonymous()">
      <a href="<c:url value="/login" />">login</a>
    </security:authorize>  
    <br /><br />
    <c:choose>
      <c:when test="${fn:length(postDatabase) == 0}">
        <i>0 Post.</i>
      </c:when>
      <c:otherwise>
        <c:forEach items="${postDatabase}" var="entry3">
          <c:if test="${entry3.categories == 'lab'}" >
            Post #${entry3.id}:
            <a href="<c:url value="/post/view/${entry3.id}" />"> 
              <c:out value="${entry3.subject}" /></a>
            (Post by: <c:out value="${entry3.customerName}" />)     
            <security:authorize access="isAuthenticated()">
              <security:authorize access="hasRole('ADMIN') or principal.username=='${entry3.customerName}'">            
                [<a href="<c:url value="/post/edit/${entry3.id}" />">Edit</a>]
              </security:authorize>
            </security:authorize>  
            <security:authorize access="hasRole('ADMIN')">            
              [<a href="<c:url value="/post/delete/${entry3.id}" />">Delete</a>]
            </security:authorize>
            <hr>
          </c:if>  
        </c:forEach>
      </c:otherwise>
    </c:choose>
    <a href="<c:url value="/post" />">Return to list posts</a>
  </body>
</html>