package main.java.dao;

import main.java.model.Ingredient;
import main.java.model.PurchaseInvoice;
import main.java.model.PurchaseInvoiceDetail;
import main.java.model.Supplier;
import main.java.model.enums.PurchaseInvoiceStatus;
import main.java.util.JDBCConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PurchaseInvoiceDetailDAO {
    public static List<PurchaseInvoiceDetail> getDetail(String invoiceId) {
        String sql = "";
        if (invoiceId.equals("all")) {
            sql = "SELECT dpi.*, pi.id AS purchase_invoice_id, pi.supplier_id, pi.inventory_clerk_id, pi.total_amount, pi.created_at, " +
                    "i.id AS ingredient_id, i.name AS ingredient_name, i.category, i.status AS ingredient_status, i.unit, i.delete_at AS ingredient_delete_at, " +
                    "s.id AS supplier_id, s.name AS supplier_name, s.address, s.phone " +
                    "FROM purchase_invoice_detail AS dpi " +
                    "JOIN ingredient AS i ON dpi.actual_ingredient = i.id OR dpi.expected_ingredient = i.id " +
                    "JOIN purchase_invoice AS pi ON dpi.purchase_invoice_id = pi.id " +
                    "JOIN supplier AS s ON pi.supplier_id = s.id";
        } else {
            sql = "SELECT dpi.*, pi.id AS purchase_invoice_id, pi.supplier_id, pi.inventory_clerk_id, pi.total_amount, pi.created_at, " +
                    "i.id AS ingredient_id, i.name AS ingredient_name, i.category, i.status AS ingredient_status, i.unit, i.delete_at AS ingredient_delete_at, " +
                    "s.id AS supplier_id, s.name AS supplier_name, s.address, s.phone " +
                    "FROM purchase_invoice_detail AS dpi " +
                    "JOIN ingredient AS i ON dpi.actual_ingredient = i.id OR dpi.expected_ingredient = i.id " +
                    "JOIN purchase_invoice AS pi ON dpi.purchase_invoice_id = pi.id " +
                    "JOIN supplier AS s ON pi.supplier_id = s.id " +
                    "WHERE dpi.purchase_invoice_id = ?";
        }
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            if (!invoiceId.equals("all")) {
                statement.setString(1, invoiceId);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                List<PurchaseInvoiceDetail> purchaseInvoiceDetails = new ArrayList<>();

                while (resultSet.next()) {
                    Supplier supplier = new Supplier();
                    supplier.setId(resultSet.getInt("supplier_id"));
                    supplier.setName(resultSet.getString("supplier_name"));
                    supplier.setAddress(resultSet.getString("address"));
                    supplier.setPhone(resultSet.getString("phone"));

                    Ingredient ingredient = new Ingredient();
                    ingredient.setId(resultSet.getInt("ingredient_id"));
                    ingredient.setName(resultSet.getString("ingredient_name"));
                    ingredient.setCategory(resultSet.getString("category"));
                    ingredient.setUnit(resultSet.getString("unit"));
                    ingredient.setStatus(resultSet.getBoolean("ingredient_status"));
                    if (resultSet.getObject("ingredient_delete_at", LocalDateTime.class) == null) {
                        ingredient.setDeleteAt(null);
                    } else {
                        ingredient.setDeleteAt(resultSet.getObject("ingredient_delete_at", LocalDateTime.class));
                    }

                    PurchaseInvoice purchaseInvoice = new PurchaseInvoice();
                    purchaseInvoice.setId(resultSet.getString("purchase_invoice_id"));
                    purchaseInvoice.setSupplierId(resultSet.getInt("supplier_id"));
                    purchaseInvoice.setInventoryClerkId(resultSet.getInt("inventory_clerk_id"));
                    purchaseInvoice.setTotalAmount(resultSet.getFloat("total_amount"));
                    purchaseInvoice.setCreatedAt(resultSet.getObject("created_at", LocalDateTime.class));

                    purchaseInvoice.setSupplier(supplier);

                    PurchaseInvoiceDetail purchaseInvoiceDetail = new PurchaseInvoiceDetail();
                    purchaseInvoiceDetail.setId(resultSet.getString("id"));
                    purchaseInvoiceDetail.setPurchaseInvoiceId(resultSet.getString("purchase_invoice_id"));
                    purchaseInvoiceDetail.setExpectedIngredient(resultSet.getInt("expected_ingredient"));
                    purchaseInvoiceDetail.setActualIngredient(resultSet.getInt("actual_ingredient"));
                    purchaseInvoiceDetail.setExpectedQuantity(resultSet.getFloat("expected_quantity"));
                    purchaseInvoiceDetail.setActualQuantity(resultSet.getFloat("actual_quantity"));
                    purchaseInvoiceDetail.setUnitPrice(resultSet.getFloat("unit_price"));
                    purchaseInvoiceDetail.setSubTotal(resultSet.getFloat("sub_total"));
                    purchaseInvoiceDetail.setUpdatedAt(resultSet.getObject("updated_at", LocalDateTime.class));
                    purchaseInvoiceDetail.setUpdatedBy(resultSet.getInt("updated_by"));
                    purchaseInvoiceDetail.setStatus(PurchaseInvoiceStatus.valueOf(resultSet.getString("status").toUpperCase()));
                    purchaseInvoiceDetail.setNote(resultSet.getString("note"));

                    purchaseInvoiceDetail.setIngredient(ingredient);
                    purchaseInvoiceDetail.setPurchaseInvoice(purchaseInvoice);

                    purchaseInvoiceDetails.add(purchaseInvoiceDetail);
                }

                return purchaseInvoiceDetails;
            } catch (Exception e) {
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
    }

    public static boolean addPurchaseInvoiceDetail(PurchaseInvoiceDetail purchaseInvoiceDetail) {
        String sql = "INSERT INTO purchase_invoice_detail (id, purchase_invoice_id, expected_ingredient, actual_ingredient, expected_quantity, actual_quantity, unit_price, sub_total, updated_at, updated_by, status, note) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, purchaseInvoiceDetail.getId());
            statement.setString(2, purchaseInvoiceDetail.getPurchaseInvoiceId());
            statement.setInt(3, purchaseInvoiceDetail.getExpectedIngredient());
            statement.setInt(4, purchaseInvoiceDetail.getActualIngredient());
            statement.setFloat(5, purchaseInvoiceDetail.getExpectedQuantity());
            statement.setFloat(6, purchaseInvoiceDetail.getActualQuantity());
            statement.setFloat(7, purchaseInvoiceDetail.getUnitPrice());
            statement.setFloat(8, purchaseInvoiceDetail.getSubTotal());
            statement.setObject(9, purchaseInvoiceDetail.getUpdatedAt());
            statement.setInt(10, purchaseInvoiceDetail.getUpdatedBy());
            statement.setString(11, purchaseInvoiceDetail.getStatus().name().toLowerCase());
            statement.setString(12, purchaseInvoiceDetail.getNote());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean updatePurchaseInvoiceDetail(PurchaseInvoiceDetail purchaseInvoiceDetail) {
        String sql = "UPDATE purchase_invoice_detail SET expected_ingredient = ?, actual_ingredient = ?, expected_quantity = ?, actual_quantity = ?, unit_price = ?, sub_total = ?, updated_at = ?, updated_by = ?, status = ?, note = ? WHERE id = ?";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, purchaseInvoiceDetail.getExpectedIngredient());
            statement.setInt(2, purchaseInvoiceDetail.getActualIngredient());
            statement.setFloat(3, purchaseInvoiceDetail.getExpectedQuantity());
            statement.setFloat(4, purchaseInvoiceDetail.getActualQuantity());
            statement.setFloat(5, purchaseInvoiceDetail.getUnitPrice());
            statement.setFloat(6, purchaseInvoiceDetail.getSubTotal());
            statement.setObject(7, purchaseInvoiceDetail.getUpdatedAt());
            statement.setInt(8, purchaseInvoiceDetail.getUpdatedBy());
            statement.setString(9, purchaseInvoiceDetail.getStatus().name().toLowerCase());
            statement.setString(10, purchaseInvoiceDetail.getNote());
            statement.setString(11, purchaseInvoiceDetail.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static List<PurchaseInvoiceDetail> search(String query) {
        String sql = "SELECT dpi.*, pi.id AS purchase_invoice_id, pi.supplier_id, pi.inventory_clerk_id, pi.total_amount, pi.created_at, " +
                "i.id AS ingredient_id, i.name AS ingredient_name, i.category, i.status AS ingredient_status, i.unit, i.delete_at AS ingredient_delete_at, " +
                "s.id AS supplier_id, s.name AS supplier_name, s.address, s.phone " +
                "FROM purchase_invoice_detail AS dpi " +
                "JOIN ingredient AS i ON dpi.actual_ingredient = i.id OR dpi.expected_ingredient = i.id " +
                "JOIN purchase_invoice AS pi ON dpi.purchase_invoice_id = pi.id " +
                "JOIN supplier AS s ON pi.supplier_id = s.id " +
                "JOIN member AS m ON dpi.updated_by = m.id " +
                "WHERE i.name COLLATE Latin1_General_CI_AI LIKE ? OR s.name COLLATE Latin1_General_CI_AI LIKE ? OR m.name COLLATE Latin1_General_CI_AI LIKE ? OR CONVERT(VARCHAR(30), pi.created_at, 120) LIKE ? OR CONVERT(VARCHAR(30), dpi.updated_at, 120) LIKE ? OR dpi.status LIKE ? OR dpi.note LIKE ?";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 1; i <= 7; i++) {
                statement.setString(i, "%" + query + "%");
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                List<PurchaseInvoiceDetail> purchaseInvoiceDetails = new ArrayList<>();

                while (resultSet.next()) {
                    Supplier supplier = new Supplier();
                    supplier.setId(resultSet.getInt("supplier_id"));
                    supplier.setName(resultSet.getString("supplier_name"));
                    supplier.setAddress(resultSet.getString("address"));
                    supplier.setPhone(resultSet.getString("phone"));

                    Ingredient ingredient = new Ingredient();
                    ingredient.setId(resultSet.getInt("ingredient_id"));
                    ingredient.setName(resultSet.getString("ingredient_name"));
                    ingredient.setCategory(resultSet.getString("category"));
                    ingredient.setUnit(resultSet.getString("unit"));
                    ingredient.setStatus(resultSet.getBoolean("ingredient_status"));
                    if (resultSet.getObject("ingredient_delete_at", LocalDateTime.class) == null) {
                        ingredient.setDeleteAt(null);
                    } else {
                        ingredient.setDeleteAt(resultSet.getObject("ingredient_delete_at", LocalDateTime.class));
                    }

                    PurchaseInvoice purchaseInvoice = new PurchaseInvoice();
                    purchaseInvoice.setId(resultSet.getString("purchase_invoice_id"));
                    purchaseInvoice.setSupplierId(resultSet.getInt("supplier_id"));
                    purchaseInvoice.setInventoryClerkId(resultSet.getInt("inventory_clerk_id"));
                    purchaseInvoice.setTotalAmount(resultSet.getFloat("total_amount"));
                    purchaseInvoice.setCreatedAt(resultSet.getObject("created_at", LocalDateTime.class));

                    purchaseInvoice.setSupplier(supplier);

                    PurchaseInvoiceDetail purchaseInvoiceDetail = new PurchaseInvoiceDetail();
                    purchaseInvoiceDetail.setId(resultSet.getString("id"));
                    purchaseInvoiceDetail.setPurchaseInvoiceId(resultSet.getString("purchase_invoice_id"));
                    purchaseInvoiceDetail.setExpectedIngredient(resultSet.getInt("expected_ingredient"));
                    purchaseInvoiceDetail.setActualIngredient(resultSet.getInt("actual_ingredient"));
                    purchaseInvoiceDetail.setExpectedQuantity(resultSet.getFloat("expected_quantity"));
                    purchaseInvoiceDetail.setActualQuantity(resultSet.getFloat("actual_quantity"));
                    purchaseInvoiceDetail.setUnitPrice(resultSet.getFloat("unit_price"));
                    purchaseInvoiceDetail.setSubTotal(resultSet.getFloat("sub_total"));
                    purchaseInvoiceDetail.setUpdatedAt(resultSet.getObject("updated_at", LocalDateTime.class));
                    purchaseInvoiceDetail.setUpdatedBy(resultSet.getInt("updated_by"));
                    purchaseInvoiceDetail.setStatus(PurchaseInvoiceStatus.valueOf(resultSet.getString("status").toUpperCase()));
                    purchaseInvoiceDetail.setNote(resultSet.getString("note"));

                    purchaseInvoiceDetail.setIngredient(ingredient);
                    purchaseInvoiceDetail.setPurchaseInvoice(purchaseInvoice);

                    purchaseInvoiceDetails.add(purchaseInvoiceDetail);
                }

                return purchaseInvoiceDetails;
            } catch (Exception e) {
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
    }
}
