package main.java.dao;

import main.java.model.PurchaseInvoice;
import main.java.util.JDBCConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class PurchaseInvoiceDAO {
    public static PurchaseInvoice getPurchaseInvoiceById(String invoiceId) {
        String sql = "SELECT * FROM purchase_invoice WHERE id = ?";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, invoiceId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    PurchaseInvoice purchaseInvoice = new PurchaseInvoice();

                    purchaseInvoice.setId(resultSet.getString("id"));
                    purchaseInvoice.setSupplierId(resultSet.getInt("supplier_id"));
                    purchaseInvoice.setInventoryClerkId(resultSet.getInt("inventory_clerk_id"));
                    purchaseInvoice.setTotalAmount(resultSet.getFloat("total_amount"));
                    purchaseInvoice.setCreatedAt(resultSet.getObject("created_at", LocalDateTime.class));

                    return purchaseInvoice;
                }
            } catch (Exception e) {
                return null;
            }
        } catch (SQLException e) {
            return null;
        }

        return null;
    }

    public static boolean addPurchaseInvoice(PurchaseInvoice purchaseInvoice) {
        String sql = "INSERT INTO purchase_invoice (id, supplier_id, inventory_clerk_id, total_amount, created_at) VALUES (?, ?, ?, ?, ?)";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, purchaseInvoice.getId());
            statement.setInt(2, purchaseInvoice.getSupplierId());
            statement.setInt(3, purchaseInvoice.getInventoryClerkId());
            statement.setFloat(4, purchaseInvoice.getTotalAmount());
            statement.setObject(5, purchaseInvoice.getCreatedAt());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean updatePurchaseInvoice(PurchaseInvoice purchaseInvoice) {
        String sql = "UPDATE purchase_invoice SET supplier_id = ?, inventory_clerk_id = ?, total_amount = ?, created_at = ? WHERE id = ?";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, purchaseInvoice.getSupplierId());
            statement.setInt(2, purchaseInvoice.getInventoryClerkId());
            statement.setFloat(3, purchaseInvoice.getTotalAmount());
            statement.setObject(4, purchaseInvoice.getCreatedAt());
            statement.setString(5, purchaseInvoice.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }
}
