package org.example.gestor_biblioteca.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static DatabaseConnection instancia;

    private static Connection connection;

    private static String URL;
    private static String USER;
    private static String PASSWORD;

    /**
     * Constructor privado para implementar SINGLETON
     */
    private DatabaseConnection() {
        this.URL = "jdbc:mysql://localhost:3306/gestion_biblioteca";
        this.USER = "bibliotecario";
        this.PASSWORD = "biblioteca@123";

    }

    /**
     * Metodo estatico para obtener una unica instancia
     *
     * @return instancia
     */
    public static DatabaseConnection getInstance() {
        if (instancia == null) {
            instancia = new DatabaseConnection();
        }
        return instancia;
    }


    public static String getURL() {
        return URL;
    }

    public static void setURL(String URL) {
        DatabaseConnection.URL = URL;
    }

    public static String getUSER() {
        return USER;
    }

    public static void setUSER(String USER) {
        DatabaseConnection.USER = USER;
    }

    public static String getPASSWORD() {
        return PASSWORD;
    }

    public static void setPASSWORD(String PASSWORD) {
        DatabaseConnection.PASSWORD = PASSWORD;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver MySQL no encontrado", e);
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}