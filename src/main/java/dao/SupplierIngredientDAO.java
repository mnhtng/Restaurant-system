package main.java.dao;

import main.java.model.Ingredient;
import main.java.model.Supplier;
import main.java.model.SupplierIngredient;
import main.java.util.JDBCConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SupplierIngredientDAO {
    public static List<Supplier> getAllSupplier() {
        String sql = "SELECT * FROM supplier WHERE delete_at IS NULL";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
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
        } catch (SQLException e) {
            return null;
        }
    }

    public static List<SupplierIngredient> getIngredientBySupplier(int supplierId) {
        String sql = "SELECT si.*, s.name AS supplier_name, s.address, s.phone, s.delete_at AS supplier_delete_at, " +
                "i.name AS ingredient_name, i.category, i.status, i.unit, i.delete_at AS ingredient_delete_at " +
                "FROM supplier_ingredient AS si " +
                "JOIN supplier AS s ON si.supplier_id = s.id " +
                "JOIN ingredient AS i ON si.ingredient_id = i.id " +
                "WHERE si.supplier_id = ? AND s.delete_at IS NULL AND i.delete_at IS NULL";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, supplierId);

            try (ResultSet resultSet = statement.executeQuery()) {
                List<SupplierIngredient> supplierIngredients = new ArrayList<>();

                while (resultSet.next()) {
                    Supplier supplier = new Supplier();
                    supplier.setId(resultSet.getInt("supplier_id"));
                    supplier.setName(resultSet.getString("supplier_name"));
                    supplier.setAddress(resultSet.getString("address"));
                    supplier.setPhone(resultSet.getString("phone"));
                    if (resultSet.getObject("supplier_delete_at", LocalDateTime.class) == null) {
                        supplier.setDeleteAt(null);
                    } else {
                        supplier.setDeleteAt(resultSet.getObject("supplier_delete_at", LocalDateTime.class));
                    }

                    Ingredient ingredient = new Ingredient();
                    ingredient.setId(resultSet.getInt("ingredient_id"));
                    ingredient.setName(resultSet.getString("ingredient_name"));
                    ingredient.setCategory(resultSet.getString("category"));
                    ingredient.setUnit(resultSet.getString("unit"));
                    ingredient.setStatus(resultSet.getBoolean("status"));
                    if (resultSet.getObject("ingredient_delete_at", LocalDateTime.class) == null) {
                        ingredient.setDeleteAt(null);
                    } else {
                        ingredient.setDeleteAt(resultSet.getObject("ingredient_delete_at", LocalDateTime.class));
                    }

                    SupplierIngredient supplierIngredient = new SupplierIngredient();
                    supplierIngredient.setSupplierId(resultSet.getInt("supplier_id"));
                    supplierIngredient.setIngredientId(resultSet.getInt("ingredient_id"));
                    supplierIngredient.setDefaultPrice(resultSet.getFloat("default_price"));
                    supplierIngredient.setPrimary(resultSet.getInt("is_primary") != 0);
                    supplierIngredient.setSupplier(supplier);
                    supplierIngredient.setIngredient(ingredient);

                    supplierIngredients.add(supplierIngredient);
                }

                return supplierIngredients;
            } catch (Exception e) {
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
    }

    public static List<SupplierIngredient> getSupplierIngredientById(int id) {
        String sql = "SELECT * FROM supplier_ingredient WHERE supplier_id = ?";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                List<SupplierIngredient> supplierIngredients = new ArrayList<>();

                while (resultSet.next()) {
                    SupplierIngredient supplierIngredient = new SupplierIngredient();
                    supplierIngredient.setSupplierId(resultSet.getInt("supplier_id"));
                    supplierIngredient.setIngredientId(resultSet.getInt("ingredient_id"));
                    supplierIngredient.setDefaultPrice(resultSet.getFloat("default_price"));
                    supplierIngredient.setPrimary(resultSet.getInt("is_primary") != 0);

                    supplierIngredients.add(supplierIngredient);
                }

                return supplierIngredients;
            } catch (SQLException e) {
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
    }

    public static boolean addSupplierIngredient(int supplierId, Integer ingredientId, Float price) {
        String sql = "INSERT INTO supplier_ingredient (supplier_id, ingredient_id, default_price, is_primary) VALUES (?, ?, ?, ?)";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, supplierId);
            statement.setInt(2, ingredientId);
            statement.setFloat(3, price);
            statement.setInt(4, 0);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean deleteSupplierIngredient(int id) {
        String sql = "DELETE FROM supplier_ingredient WHERE supplier_id = ?";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean resetMainSupplier(int supplierId) {
        String sql = "UPDATE supplier_ingredient SET is_primary = 0 WHERE supplier_id = ?";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, supplierId);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean setMainSupplier(int supplierId, int ingredientId) {
        String sql = "UPDATE supplier_ingredient SET is_primary = 1 WHERE supplier_id = ? AND ingredient_id = ?";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, supplierId);
            statement.setInt(2, ingredientId);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static List<Supplier> search(String query) {
        String sql = "SELECT DISTINCT s.* " +
                "FROM supplier_ingredient AS si JOIN supplier AS s ON si.supplier_id = s.id JOIN ingredient AS i ON si.ingredient_id = i.id " +
                "WHERE (s.name COLLATE Latin1_General_CI_AI LIKE ? OR s.address COLLATE Latin1_General_CI_AI LIKE ? OR s.phone LIKE ? OR i.name COLLATE Latin1_General_CI_AI LIKE ?) AND s.delete_at IS NULL";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            String searchQuery = "%" + query + "%";
            statement.setString(1, searchQuery);
            statement.setString(2, searchQuery);
            statement.setString(3, searchQuery);
            statement.setString(4, searchQuery);

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
            } catch (Exception e) {
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
    }
}
