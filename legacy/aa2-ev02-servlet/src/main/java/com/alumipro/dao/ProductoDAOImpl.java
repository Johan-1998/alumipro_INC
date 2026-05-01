/* archivo java productodaoimpl */
package com.alumipro.dao;

import com.alumipro.config.DatabaseConnection;
import com.alumipro.model.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAOImpl implements ProductoDAO {

    private final Connection conexion;

    public ProductoDAOImpl() {
        this.conexion = DatabaseConnection.getConnection();
    }

    @Override
    public List<Producto> listar() {
        String sql = "SELECT * FROM productos";
        List<Producto> lista = new ArrayList<>();

        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Producto p = new Producto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getInt("stock")
                );
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}
