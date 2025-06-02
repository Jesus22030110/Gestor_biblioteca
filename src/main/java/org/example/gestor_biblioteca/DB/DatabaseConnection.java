package org.example.gestor_biblioteca.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instancia;
    private Connection connection;
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_biblioteca";
    private static final String USER = "bibliotecario";
    private static final String PASSWORD = "biblioteca@123";

    private DatabaseConnection() {}

    public static DatabaseConnection getInstance() {
        if (instancia == null) {
            instancia = new DatabaseConnection();
        }
        return instancia;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Para MySQL 8.0+
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Error al cargar el driver JDBC de MySQL", e);
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (instancia != null && instancia.connection != null) {
            try {
                instancia.connection.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}