<!DOCTYPE html>
<html>
    <head>
        <title>Create poll</title>
    </head>
    <body>
        
        <h2>New Poll</h2>
        <c:url var="logoutUrl" value="/logout"/>
        <form action="${logoutUrl}" method="post">
            <input type="submit" value="Log out" />
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>

        <button onclick="myFunction()">add poll item</button> 
        <form method="POST" id="f" action="create">
            <input type="submit" value="Submit poll"/><br>
            Poll Subject:<br><input type="text" name="subject" ><br>
            Items:<br><input type="text" name="item0" ><br>
            <input type="text" name="item1" ><br>
            <input type="hidden" id="count" name="count" value="2">        
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>

        <script>
            var p = 2;
            function myFunction() {
                if(p<4){
                  var f = document.getElementById("f");
                  var br = document.createElement("br");
                  var x = document.createElement("INPUT");
                  x.setAttribute("type", "text");
                  x.setAttribute("name", "item" + p);
                  p = p + 1;
                  document.getElementById("count").value = p;
                  f.appendChild(x);
                  f.appendChild(br);
              }
            }
        </script>
    </body>
</html>