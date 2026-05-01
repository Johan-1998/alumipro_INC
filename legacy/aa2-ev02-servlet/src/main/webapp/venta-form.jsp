<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.alumipro.model.Cliente" %>
<%@ page import="com.alumipro.model.Producto" %>

<%
    List<Cliente> clientes = (List<Cliente>) request.getAttribute("clientes");
    List<Producto> productos = (List<Producto>) request.getAttribute("productos");
%>

<html>
<head>
    <title>Registrar Venta</title>
</head>
<body>

<h2>Registrar Venta - Alumipro</h2>

<form action="ventas" method="post">

    <label>Cliente:</label>
    <select name="clienteId" required>
        <% for (Cliente c : clientes) { %>
        <option value="<%= c.getId() %>"><%= c.getNombre() %></option>
        <% } %>
    </select>

    <hr>

    <h3>Producto 1</h3>
    <label>Producto:</label>
    <select name="producto1" required>
        <% for (Producto p : productos) { %>
        <option value="<%= p.getId() %>"><%= p.getNombre() %> - $<%= p.getPrecio() %> (stock: <%= p.getStock() %>)</option>
        <% } %>
    </select>
    <label>Cantidad:</label>
    <input type="number" name="cantidad1" min="1" value="1" required>

    <h3>Producto 2</h3>
    <label>Producto:</label>
    <select name="producto2" required>
        <% for (Producto p : productos) { %>
        <option value="<%= p.getId() %>"><%= p.getNombre() %> - $<%= p.getPrecio() %> (stock: <%= p.getStock() %>)</option>
        <% } %>
    </select>
    <label>Cantidad:</label>
    <input type="number" name="cantidad2" min="1" value="1" required>

    <br><br>
    <button type="submit">Registrar Venta</button>

</form>

<br>
<a href="index.jsp">Volver</a>

</body>
</html>