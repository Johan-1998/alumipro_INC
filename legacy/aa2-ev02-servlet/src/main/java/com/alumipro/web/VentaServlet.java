/* archivo java ventaservlet */
package com.alumipro.web;

import com.alumipro.dao.*;
import com.alumipro.model.Cliente;
import com.alumipro.model.DetalleVenta;
import com.alumipro.model.Producto;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/ventas")
public class VentaServlet extends HttpServlet {

    private final ClienteDAO clienteDAO = new ClienteDAOImpl();
    private final ProductoDAO productoDAO = new ProductoDAOImpl();
    private final VentaDAO ventaDAO = new VentaDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Cliente> clientes = clienteDAO.listar();
        List<Producto> productos = productoDAO.listar();

        request.setAttribute("clientes", clientes);
        request.setAttribute("productos", productos);

        request.getRequestDispatcher("/venta-form.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            int clienteId = Integer.parseInt(request.getParameter("clienteId"));

            int producto1 = Integer.parseInt(request.getParameter("producto1"));
            int cantidad1 = Integer.parseInt(request.getParameter("cantidad1"));

            int producto2 = Integer.parseInt(request.getParameter("producto2"));
            int cantidad2 = Integer.parseInt(request.getParameter("cantidad2"));

            List<DetalleVenta> detalles = new ArrayList<>();
            detalles.add(new DetalleVenta(0, producto1, cantidad1, 0));
            detalles.add(new DetalleVenta(0, producto2, cantidad2, 0));

            int ventaId = ventaDAO.registrarVenta(clienteId, detalles);

            request.setAttribute("ventaId", ventaId);
            request.getRequestDispatcher("/venta-result.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/venta-result.jsp").forward(request, response);
        }
    }
}
