/* archivo java main */
package com.alumipro.main;

import com.alumipro.dao.*;
import com.alumipro.model.Cliente;
import com.alumipro.model.DetalleVenta;
import com.alumipro.model.Producto;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {


        ClienteDAO clienteDAO = new ClienteDAOImpl();
        ProductoDAO productoDAO = new ProductoDAOImpl();
        VentaDAO ventaDAO = new VentaDAOImpl();


        List<Cliente> clientes = clienteDAO.listar();
        if (clientes.isEmpty()) {
            clienteDAO.insertar(new Cliente("Cliente Venta", "3009998888", "Dirección Venta", "venta@correo.com"));
            clientes = clienteDAO.listar();
        }
        int clienteId = clientes.get(0).getId();
        System.out.println("Usando clienteId = " + clienteId);


        List<Producto> productos = productoDAO.listar();
        if (productos.size() < 2) {
            productoDAO.insertar(new Producto("Aluminio perfil 2m", "Perfil aluminio para ventanería", 95000.00, 30));
            productoDAO.insertar(new Producto("Vidrio templado 8mm", "Vidrio templado para fachada", 210000.00, 15));
            productos = productoDAO.listar();
        }


        Producto p1 = productos.get(0);
        Producto p2 = productos.get(1);

        System.out.println("Producto 1: id=" + p1.getId() + " stock=" + p1.getStock());
        System.out.println("Producto 2: id=" + p2.getId() + " stock=" + p2.getStock());


        List<DetalleVenta> detalles = new ArrayList<>();
        detalles.add(new DetalleVenta(0, p1.getId(), 2, 0));
        detalles.add(new DetalleVenta(0, p2.getId(), 1, 0));


        int ventaId = ventaDAO.registrarVenta(clienteId, detalles);

        if (ventaId > 0) {
            System.out.println("Venta creada correctamente. ID = " + ventaId);
        } else {
            System.out.println("No se pudo crear la venta.");
        }

        System.out.println("\nProceso finalizado.");
    }
}
