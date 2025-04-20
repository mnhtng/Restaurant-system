package main.java.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author MnhTng
 * @Package main.java.util
 * @date 4/17/2025 4:18 PM
 * @Copyright tùng
 */

public class JDBCConnection {
    private static JDBCConnection instance;
    private static final String url = "jdbc:sqlserver://localhost:1433;database=RestaurantSystem;encrypt=true;trustServerCertificate=true;";
    private String user = "sa";
    private String password = "tunghpvn123";

    Connection conn;

    private JDBCConnection() {
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static class BillPughSingleton {
        private static final JDBCConnection INSTANCE = new JDBCConnection();
    }

    public static JDBCConnection getInstance() {
        return BillPughSingleton.INSTANCE;
    }

    public Connection getConnection() {
        return conn;
    }

    public static void closeConnection() {
        try {
            if (getInstance().conn != null) {
                getInstance().conn.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void connectInfo() {
        try {
            if (getInstance().conn != null) {
                System.out.println(getInstance().conn.getMetaData().getDatabaseProductName());
                System.out.println(getInstance().conn.getMetaData().getDatabaseProductVersion());
            } else {
                System.out.println("Connection is null");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
