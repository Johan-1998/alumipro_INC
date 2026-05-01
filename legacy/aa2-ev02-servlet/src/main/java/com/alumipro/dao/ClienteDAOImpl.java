/* archivo java clientedaoimpl */
package com.alumipro.dao;

import com.alumipro.config.DatabaseConnection;
import com.alumipro.model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAOImpl implements ClienteDAO {

    private final Connection conexion;

    public ClienteDAOImpl() {
        this.conexion = DatabaseConnection.getConnection();
    }

    @Override
    public List<Cliente> listar() {
        String sql = "SELECT * FROM clientes";
        List<Cliente> lista = new ArrayList<>();

        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cliente c = new Cliente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rs.getString("email")
                );
                lista.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}
