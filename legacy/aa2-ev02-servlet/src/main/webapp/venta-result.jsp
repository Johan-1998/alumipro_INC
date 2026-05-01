<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Resultado Venta</title>
</head>
<body>

<h2>Resultado</h2>

<%
    Object error = request.getAttribute("error");
    Integer ventaId = (Integer) request.getAttribute("ventaId");
%>

<% if (error != null) { %>
<p style="color:red;">Error: <%= error %></p>
<% } else if (ventaId != null && ventaId > 0) { %>
<p style="color:green;">Venta registrada con éxito. ID: <%= ventaId %></p>
<% } else { %>
<p style="color:red;">No se pudo registrar la venta.</p>
<% } %>

<br>
<a href="ventas">Registrar otra venta</a>
<br>
<a href="index.jsp">Volver al inicio</a>

</body>
</html>
