/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pizzaexpresstycoon.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author braya
 */
public class DataBaseConnection {
    public static final String DB_HOST     = "localhost";
    public static final String DB_PORT     = "3306";
    public static final String DB_NAME     = "pizza_express_tycoon";
    public static final String DB_USER     = "root";
    public static final String DB_PASSWORD = "br4y4n;c4nch3";  

    private static final String JDBC_URL =
        "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME
        + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

  
    private static DataBaseConnection instance;

    
    private Connection connection;

    
    private DataBaseConnection() {
        connect();
    }

   
     
    public static DataBaseConnection getInstance() {
        if (instance == null) {
            instance = new DataBaseConnection();
        }
        return instance;
    }

   
    private void connect() {
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
            System.out.println("[DB] Conexión exitosa: " + DB_NAME + "@" + DB_HOST);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                "[DB] Driver JDBC no encontrado. Verifica que mysql-connector-j esté en el classpath.\n"
                + e.getMessage(), e
            );
        } catch (SQLException e) {
            throw new RuntimeException(
                "[DB] Error SQL al conectar con " + DB_NAME + ": " + e.getMessage(), e
            );
        }
    }

    
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed() || !connection.isValid(2)) {
                System.out.println("[DB] Conexión inválida. Reconectando...");
                connect();
            }
        } catch (SQLException e) {
            System.err.println("[DB] Error al validar la conexión: " + e.getMessage());
            connect(); // Intentar reconectar
        }
        return connection;
    }

    
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("[DB] Conexión cerrada correctamente.");
            } catch (SQLException e) {
                System.err.println("[DB] Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}
