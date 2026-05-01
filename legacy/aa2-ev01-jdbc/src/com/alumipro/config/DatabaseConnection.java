/* archivo java databaseconnection */
package com.alumipro.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/alumipro_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static Connection connection;

    private DatabaseConnection() {
    }

    public static Connection getConnection() {

        if (connection == null) {

            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Conexión exitosa a la base de datos.");

            } catch (SQLException e) {
                System.err.println("Error al conectar con la base de datos.");
                e.printStackTrace();
            }
        }

        return connection;
    }
}
