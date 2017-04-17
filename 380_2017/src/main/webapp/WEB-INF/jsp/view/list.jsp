<!DOCTYPE html>
<html>
    <head>
        <title>Customer Support</title>
    </head>
    <body>
      
      
        <h2>All Post</h2>

        <security:authorize access="hasRole('ADMIN')">    
            <a href="<c:url value="/user" />">Manage User Accounts</a><br /><br />
            
        </security:authorize>
        <security:authorize access="hasRole('ADMIN')"> 
        <a href="<c:url value="/poll/create" />">Create new poll</a>
        </security:authorize>
        <security:authorize access="isAuthenticated()">
        <a href="<c:url value="/post/create" />">New Post</a>  
        </security:authorize>        
        </br>
        <a href="<c:url value="/post/listlecture" />">lecture</a>
        <a href="<c:url value="/post/listlab" />">lab</a>
        <a href="<c:url value="/post/listother" />">other</a>
        <br/>
        <security:authorize access="isAnonymous()">
        <a href="<c:url value="/login" />">login</a>
         <a href="<c:url value="/create" />">Create a User</a>
        </security:authorize>  
         <security:authorize access="isAuthenticated()">
        <c:url var="logoutUrl" value="/logout"/>
        <form action="${logoutUrl}" method="post">
            <input type="submit" value="Log out" />
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>
      </security:authorize>
        <br /><br />
        <c:choose>
            <c:when test="${fn:length(postDatabase) == 0}">
                <i>0 Post.</i>
            </c:when>
            <c:otherwise>
                <c:forEach items="${postDatabase}" var="entry">
                    Post #${entry.id}:
                    <a href="<c:url value="/post/view/${entry.id}" />">
                        <c:out value="${entry.subject}" /></a>
                    (Post by: <c:out value="${entry.customerName}" />)
                    (categories: <c:out value="${entry.categories}" />)  
                    <security:authorize access="isAuthenticated()">
                    <security:authorize access="hasRole('ADMIN') or principal.username=='${entry.customerName}'">            
                        [<a href="<c:url value="/post/edit/${entry.id}" />">Edit</a>]
                    </security:authorize>
                    </security:authorize>
                    <security:authorize access="hasRole('ADMIN')">            
                        [<a href="<c:url value="/post/delete/${entry.id}" />">Delete</a>]
                    </security:authorize>
                    <hr>    
                </c:forEach>
            </c:otherwise>
        </c:choose>
        
        <br>      
        <c:choose>
            <c:when test="${fn:length(pollPostDatabase) == 0}">
                <i>0 Poll.</i>
            </c:when>
                
            <c:otherwise>
                <c:forEach items="${pollPostDatabase}" var="entry5">
                    Poll #${entry5.key}: 
                    <security:authorize access="isAuthenticated()"><a href="<c:url value="/poll/view/${entry5.key}" />"> 
                        <c:out value="${entry5.value.pollSubject}" /></a>
                    </security:authorize>
                    <security:authorize access="isAnonymous()"><a href="<c:url value="/poll/viewresult/${entry5.key}" />"> 
                        <c:out value="${entry5.value.pollSubject}" /></a>
                    </security:authorize>

                    <br>
                </c:forEach>
            </c:otherwise>  
        </c:choose>
                    
    </body>
</html>