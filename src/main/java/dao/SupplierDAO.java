package main.java.dao;

import main.java.model.Supplier;
import main.java.util.JDBCConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {
    public static Supplier getSupplierById(int suppilerId) {
        String sql = "SELECT * FROM supplier WHERE id = ? AND delete_at IS NULL";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, suppilerId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Supplier supplier = new Supplier();
                    supplier.setId(resultSet.getInt("id"));
                    supplier.setName(resultSet.getString("name"));
                    supplier.setAddress(resultSet.getString("address"));
                    supplier.setPhone(resultSet.getString("phone"));
                    if (resultSet.getObject("delete_at", LocalDateTime.class) == null) {
                        supplier.setDeleteAt(null);
                    } else {
                        supplier.setDeleteAt(resultSet.getObject("delete_at", LocalDateTime.class));
                    }

                    return supplier;
                }
            } catch (SQLException e) {
                return null;
            }
        } catch (SQLException e) {
            return null;
        }

        return null;
    }

    public static boolean addSupplier(Supplier supplier) {
        String sql = "INSERT INTO supplier (name, address, phone, delete_at) VALUES (?, ?, ?, ?)";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, supplier.getName());
            statement.setString(2, supplier.getAddress());
            statement.setString(3, supplier.getPhone());
            statement.setObject(4, null);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean updateSupplier(Supplier supplier) {
        String sql = "UPDATE supplier SET name = ?, address = ?, phone = ? WHERE id = ?";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, supplier.getName());
            statement.setString(2, supplier.getAddress());
            statement.setString(3, supplier.getPhone());
            statement.setInt(4, supplier.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean deleteSupplier(int supplierId) {
        String sql = "UPDATE supplier SET delete_at = ? WHERE id = ?";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, LocalDateTime.now());
            statement.setInt(2, supplierId);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static List<Supplier> search(String query) {
        String sql = "SELECT * FROM supplier WHERE (name COLLATE Latin1_General_CI_AI LIKE ? OR address COLLATE Latin1_General_CI_AI LIKE ? OR phone LIKE ?) AND delete_at IS NULL";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + query + "%");
            statement.setString(2, "%" + query + "%");
            statement.setString(3, "%" + query + "%");

            try (ResultSet resultSet = statement.executeQuery()) {
                List<Supplier> suppliers = new ArrayList<>();

                while (resultSet.next()) {
                    Supplier supplier = new Supplier();
                    supplier.setId(resultSet.getInt("id"));
                    supplier.setName(resultSet.getString("name"));
                    supplier.setAddress(resultSet.getString("address"));
                    supplier.setPhone(resultSet.getString("phone"));
                    if (resultSet.getObject("delete_at", LocalDateTime.class) == null) {
                        supplier.setDeleteAt(null);
                    } else {
                        supplier.setDeleteAt(resultSet.getObject("delete_at", LocalDateTime.class));
                    }

                    suppliers.add(supplier);
                }

                return suppliers;
            }
        } catch (SQLException e) {
            return null;
        }
    }
}
