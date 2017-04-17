<!DOCTYPE html>
<html>
    <head>
        <title>Customer Support</title>
    </head>
    <body>
        
        <h2>Users</h2>
       
        <a href="<c:url value="/user/create" />">Create a User</a><br /><br />
        
        <c:choose>
            <c:when test="${fn:length(postUsers) == 0}">
                <i>There are no users in the system.</i>
            </c:when>
            <c:otherwise>
                <table>
                    <tr>
                        <th>Username</th>
                        <th>Password</th>
                        <th>Roles</th>
                        <th>Action</th>
                    </tr>
                    <c:forEach items="${postUsers}" var="user">
                        <tr>
                            <td>${user.username}</td>
                            <td>${user.password}</td>
                            <td>
                                <c:forEach items="${user.roles}" var="role" varStatus="status">
                                    <c:if test="${!status.first}">, </c:if>
                                    ${role}
                                </c:forEach>
                            </td>
                            <td>[<a href="<c:url value="/user/edit/${user.username}" />">Set Role(Admin)</a>]</td>
                            <td>[<a href="<c:url value="/user/edit_user/${user.username}" />">Set Role(User)</a>]</td>
                            <td>[<a href="<c:url value="/user/delete/${user.username}" />">Delete</a>]</td>
                        </tr>
                    </c:forEach>
                </table>
            </c:otherwise>
        </c:choose>
                <a href="<c:url value="/post" />">Return to list posts</a>
                
    </body>
</html>