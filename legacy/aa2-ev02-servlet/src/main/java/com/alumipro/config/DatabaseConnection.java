/* archivo java databaseconnection */
package com.alumipro.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {


    private static final String URL =
            "jdbc:mysql://localhost:3306/alumipro_db" +
                    "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static Connection connection;

    private DatabaseConnection() {}

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {

                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("[DB] Conexión OK");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("[DB] No se encontró el driver JDBC.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("[DB] Error conectando a la BD. Revisa URL/USER/PASS y que MySQL esté activo.");
            e.printStackTrace();
        }
        return connection;
    }
}
