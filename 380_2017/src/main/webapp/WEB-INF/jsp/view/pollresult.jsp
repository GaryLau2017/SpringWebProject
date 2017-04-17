<!DOCTYPE html>
<html>
    <head>
        <title>Poll result</title>
    </head>
    <body>
         <h2>Poll #${pollId}: <c:out value="${poll.pollSubject}" /></h2>
         The total number of poll : <c:out value="${total}"/><br/>
           <c:forEach items="${poll.map}" var="entry">
               Item${entry.key} : ${entry.value} <br>
            </c:forEach>
               
            <a href="<c:url value="/post" />">Return to main page</a>
    </body>
</html>
