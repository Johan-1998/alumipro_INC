/* archivo java ventadaoimpl */
package com.alumipro.dao;

import com.alumipro.config.DatabaseConnection;
import com.alumipro.model.DetalleVenta;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public class VentaDAOImpl implements VentaDAO {

    private final Connection conexion;

    public VentaDAOImpl() {
        this.conexion = DatabaseConnection.getConnection();
    }

    @Override
    public int registrarVenta(int clienteId, List<DetalleVenta> detalles) {

        String sqlInsertVenta = "INSERT INTO ventas (fecha, cliente_id, total) VALUES (?, ?, ?)";
        String sqlInsertDetalle = "INSERT INTO detalle_venta (venta_id, producto_id, cantidad, subtotal) VALUES (?, ?, ?, ?)";
        String sqlPrecioProducto = "SELECT precio, stock FROM productos WHERE id = ?";
        String sqlActualizarStock = "UPDATE productos SET stock = stock - ? WHERE id = ?";

        int ventaIdGenerada = -1;
        double totalVenta = 0;

        try {

            conexion.setAutoCommit(false);


            for (DetalleVenta d : detalles) {

                try (PreparedStatement ps = conexion.prepareStatement(sqlPrecioProducto)) {
                    ps.setInt(1, d.getProductoId());
                    ResultSet rs = ps.executeQuery();

                    if (!rs.next()) {
                        throw new SQLException("Producto no existe: id=" + d.getProductoId());
                    }

                    double precio = rs.getDouble("precio");
                    int stock = rs.getInt("stock");

                    if (d.getCantidad() <= 0) {
                        throw new SQLException("Cantidad inválida para producto id=" + d.getProductoId());
                    }

                    if (stock < d.getCantidad()) {
                        throw new SQLException("Stock insuficiente para producto id=" + d.getProductoId() + ". Stock=" + stock);
                    }

                    double subtotal = precio * d.getCantidad();
                    d.setSubtotal(subtotal);
                    totalVenta += subtotal;
                }
            }


            try (PreparedStatement psVenta = conexion.prepareStatement(sqlInsertVenta, Statement.RETURN_GENERATED_KEYS)) {
                psVenta.setDate(1, Date.valueOf(LocalDate.now()));
                psVenta.setInt(2, clienteId);
                psVenta.setDouble(3, totalVenta);
                psVenta.executeUpdate();

                ResultSet keys = psVenta.getGeneratedKeys();
                if (keys.next()) {
                    ventaIdGenerada = keys.getInt(1);
                } else {
                    throw new SQLException("No se pudo obtener ID de la venta.");
                }
            }


            for (DetalleVenta d : detalles) {


                try (PreparedStatement psDet = conexion.prepareStatement(sqlInsertDetalle)) {
                    psDet.setInt(1, ventaIdGenerada);
                    psDet.setInt(2, d.getProductoId());
                    psDet.setInt(3, d.getCantidad());
                    psDet.setDouble(4, d.getSubtotal());
                    psDet.executeUpdate();
                }


                try (PreparedStatement psStock = conexion.prepareStatement(sqlActualizarStock)) {
                    psStock.setInt(1, d.getCantidad());
                    psStock.setInt(2, d.getProductoId());
                    psStock.executeUpdate();
                }
            }


            conexion.commit();
            conexion.setAutoCommit(true);

            System.out.println("Venta registrada con éxito. ID venta: " + ventaIdGenerada + " | Total: " + totalVenta);
            return ventaIdGenerada;

        } catch (SQLException e) {
            try {
                conexion.rollback();
                conexion.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.err.println("Error registrando venta. Se hizo rollback.");
            e.printStackTrace();
            return -1;
        }
    }
}
