<!DOCTYPE html>
<html>
    <head>
        <title>Customer Support</title>
    </head>
    <body>
       

        <h2>Post #${postId}</h2>
         <c:url var="logoutUrl" value="/logout"/>
        <form action="${logoutUrl}" method="post">
            <input type="submit" value="Log out" />
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>
        <form:form method="POST" enctype="multipart/form-data"
                   modelAttribute="postForm">   
            <form:label path="subject">Title</form:label><br/>
            <form:input type="text" path="subject" /><br/><br/>
            Categories: <c:out value="${post.categories}"/><br /><br />
            <form:label path="body">Contents</form:label><br/>
            <form:textarea path="body" rows="5" cols="30" /><br/><br/>
            <c:if test="${post.numberOfAttachments > 0}">
                <b>Files</b><br/>
                <ul>
                    <c:forEach items="${post.attachments}" var="attachment">
                        <li>
                            <c:out value="${attachment.name}" />
                            [<a href="<c:url value="/post/${postId}/delete/${attachment.name}" />">Delete</a>]
                        </li>
                    </c:forEach>
                </ul>
            </c:if>
            <b>Add Files</b><br />
            <input type="file" name="attachments" multiple="multiple"/><br/><br/>
            <input type="submit" value="Save"/><br/><br/>
        </form:form>
        <a href="<c:url value="/post" />">Return to main page</a>
    </body>
</html>