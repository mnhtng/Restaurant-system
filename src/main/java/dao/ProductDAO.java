package main.java.dao;

import main.java.model.Product;
import main.java.util.JDBCConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ProductDAO {
    public static int addProduct(Product product) {
        String sql = "INSERT INTO product (type, status, delete_at) VALUES (?, ?, ?)";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, Integer.parseInt(product.getType()));
            statement.setBoolean(2, product.isStatus());
            statement.setObject(3, product.getDeleteAt());

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }

            return 0;
        } catch (SQLException e) {
            return 0;
        }
    }

    public static boolean updateProduct(Product product) {
        String sql = "UPDATE product SET type = ?, status = ?, delete_at = ? WHERE id = ?";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, Integer.parseInt(product.getType()));
            statement.setBoolean(2, product.isStatus());
            statement.setObject(3, product.getDeleteAt());
            statement.setInt(4, product.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean deleteProduct(int id) {
        String sql = "UPDATE product SET delete_at = ? WHERE id = ?";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, LocalDateTime.now());
            statement.setInt(2, id);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }
}
