package main.java.dao;

import main.java.model.PurchaseInvoiceDetail;
import main.java.model.Warehouse;
import main.java.util.JDBCConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WarehouseDAO {
    public static List<Warehouse> getAllInventory() {
        String sql = "SELECT * FROM warehouse";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            List<Warehouse> warehouses = new ArrayList<>();

            while (resultSet.next()) {
                Warehouse warehouse = new Warehouse();

                warehouse.setId(resultSet.getInt("id"));
                warehouse.setIngredientId(resultSet.getInt("ingredient_id"));
                warehouse.setQuantity(resultSet.getFloat("quantity"));
                warehouse.setReservedQuantity(resultSet.getFloat("reserved_quantity"));
                warehouse.setImportDate(resultSet.getDate("import_date").toLocalDate());
                warehouse.setExpire(resultSet.getDate("expire").toLocalDate());

                warehouses.add(warehouse);
            }

            return warehouses;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Warehouse getInventoryById(int inventoryId) {
        String sql = "SELECT * FROM warehouse WHERE id = ?";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, inventoryId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Warehouse warehouse = new Warehouse();

                    warehouse.setId(resultSet.getInt("id"));
                    warehouse.setIngredientId(resultSet.getInt("ingredient_id"));
                    warehouse.setQuantity(resultSet.getFloat("quantity"));
                    warehouse.setReservedQuantity(resultSet.getFloat("reserved_quantity"));
                    warehouse.setImportDate(resultSet.getDate("import_date").toLocalDate());
                    warehouse.setExpire(resultSet.getDate("expire").toLocalDate());

                    return warehouse;
                }
            } catch (SQLException e) {
                return null;
            }
        } catch (SQLException e) {
            return null;
        }

        return null;
    }

    public static boolean importInventory(PurchaseInvoiceDetail purchaseInvoiceDetail) {
        String sql = "INSERT INTO warehouse (ingredient_id, quantity, reserved_quantity, import_date, expire) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = JDBCConnection.getInstance().getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, purchaseInvoiceDetail.getActualIngredient());
            preparedStatement.setFloat(2, purchaseInvoiceDetail.getActualQuantity());
            preparedStatement.setFloat(3, 0);
            preparedStatement.setDate(4, Date.valueOf(LocalDate.now()));
            preparedStatement.setDate(5, Date.valueOf(LocalDate.now().plusDays(7)));

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean updateInventory(Warehouse updateWarehouse) {
        String sql = "UPDATE warehouse SET ingredient_id = ?, quantity = ?, reserved_quantity = ?, import_date = ?, expire = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = JDBCConnection.getInstance().getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, updateWarehouse.getIngredientId());
            preparedStatement.setFloat(2, updateWarehouse.getQuantity());
            preparedStatement.setFloat(3, updateWarehouse.getReservedQuantity());
            preparedStatement.setDate(4, Date.valueOf(updateWarehouse.getImportDate()));
            preparedStatement.setDate(5, Date.valueOf(updateWarehouse.getExpire()));
            preparedStatement.setInt(6, updateWarehouse.getId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean deleteInventory(int inventoryId) {
        String sql = "DELETE FROM warehouse WHERE id = ?";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, inventoryId);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static List<Warehouse> search(String query) {
        String sql;
        if (query.matches("\\d+(\\.\\d+)?")) {
            sql = "SELECT * FROM warehouse JOIN ingredient ON warehouse.ingredient_id = ingredient.id " +
                    "WHERE ingredient.name COLLATE Latin1_General_CI_AI LIKE ? OR CONVERT(VARCHAR(30), warehouse.import_date, 120) LIKE ? OR CONVERT(VARCHAR(30), warehouse.expire, 120) LIKE ? OR warehouse.quantity = ?";
        } else {
            sql = "SELECT * FROM warehouse JOIN ingredient ON warehouse.ingredient_id = ingredient.id " +
                    "WHERE ingredient.name COLLATE Latin1_General_CI_AI LIKE ? OR CONVERT(VARCHAR(30), warehouse.import_date, 120) LIKE ? OR CONVERT(VARCHAR(30), warehouse.expire, 120) LIKE ?";
        }
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + query + "%");
            statement.setString(2, "%" + query + "%");
            statement.setString(3, "%" + query + "%");
            if (query.matches("\\d+(\\.\\d+)?")) {
                statement.setFloat(4, Float.parseFloat(query));
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                List<Warehouse> warehouses = new ArrayList<>();

                while (resultSet.next()) {
                    Warehouse warehouse = new Warehouse();

                    warehouse.setId(resultSet.getInt("id"));
                    warehouse.setIngredientId(resultSet.getInt("ingredient_id"));
                    warehouse.setQuantity(resultSet.getFloat("quantity"));
                    warehouse.setReservedQuantity(resultSet.getFloat("reserved_quantity"));
                    warehouse.setImportDate(resultSet.getDate("import_date").toLocalDate());
                    warehouse.setExpire(resultSet.getDate("expire").toLocalDate());

                    warehouses.add(warehouse);
                }

                return warehouses;
            } catch (SQLException e) {
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
    }
}
