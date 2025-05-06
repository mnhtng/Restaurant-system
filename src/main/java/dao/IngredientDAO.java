package main.java.dao;

import main.java.model.Ingredient;
import main.java.util.JDBCConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class IngredientDAO {
    public static List<Ingredient> getAllIngredient() {
        String sql = "SELECT * FROM ingredient WHERE delete_at IS NULL";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            List<Ingredient> ingredients = new ArrayList<>();

            while (resultSet.next()) {
                Ingredient ingredient = new Ingredient();

                ingredient.setId(resultSet.getInt("id"));
                ingredient.setName(resultSet.getString("name"));
                ingredient.setCategory(resultSet.getString("category"));
                ingredient.setUnit(resultSet.getString("unit"));
                ingredient.setStatus(resultSet.getBoolean("status"));
                if (resultSet.getObject("delete_at", LocalDateTime.class) == null) {
                    ingredient.setDeleteAt(null);
                } else {
                    ingredient.setDeleteAt(resultSet.getObject("delete_at", LocalDateTime.class));
                }

                ingredients.add(ingredient);
            }

            return ingredients;
        } catch (SQLException e) {
            return null;
        }
    }

    public static Ingredient findIngredient(int id) {
        String sql = "SELECT * FROM ingredient WHERE id = ? AND delete_at IS NULL";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Ingredient ingredient = new Ingredient();

                    ingredient.setId(resultSet.getInt("id"));
                    ingredient.setName(resultSet.getString("name"));
                    ingredient.setCategory(resultSet.getString("category"));
                    ingredient.setUnit(resultSet.getString("unit"));
                    ingredient.setStatus(resultSet.getBoolean("status"));
                    if (resultSet.getObject("delete_at", LocalDateTime.class) == null) {
                        ingredient.setDeleteAt(null);
                    } else {
                        ingredient.setDeleteAt(resultSet.getObject("delete_at", LocalDateTime.class));
                    }

                    return ingredient;
                }
            } catch (Exception e) {
                return null;
            }
        } catch (SQLException e) {
            return null;
        }

        return null;
    }

    public static boolean addIngredient(Ingredient ingredient) {
        String sql = "INSERT INTO ingredient (name, category, unit, status, delete_at) VALUES (?, ?, ?, ?, ?)";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, ingredient.getName());
            statement.setString(2, ingredient.getCategory());
            statement.setString(3, ingredient.getUnit());
            statement.setBoolean(4, ingredient.isStatus());
            statement.setObject(5, ingredient.getDeleteAt());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean updateIngredient(Ingredient updateIngredient) {
        String sql = "UPDATE ingredient SET name = ?, category = ?, unit = ?, status = ? WHERE id = ?";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, updateIngredient.getName());
            statement.setString(2, updateIngredient.getCategory());
            statement.setString(3, updateIngredient.getUnit());
            statement.setBoolean(4, updateIngredient.isStatus());
            statement.setInt(5, updateIngredient.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean deleteIngredient(int ingredientId) {
        String sql = "UPDATE ingredient SET delete_at = ? WHERE id = ?";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, LocalDateTime.now());
            statement.setInt(2, ingredientId);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static List<Ingredient> search(String query, String category) {
        String sql;
        if (query.matches("\\d+")) {
            sql = "SELECT * FROM ingredient WHERE delete_at IS NULL AND (name COLLATE Latin1_General_CI_AI LIKE ? OR category COLLATE Latin1_General_CI_AI LIKE ? OR name COLLATE Latin1_General_CI_AI LIKE ? OR category COLLATE Latin1_General_CI_AI LIKE ? or status = ?)";
        } else {
            sql = "SELECT * FROM ingredient WHERE delete_at IS NULL AND (name COLLATE Latin1_General_CI_AI LIKE ? OR category COLLATE Latin1_General_CI_AI LIKE ? OR name COLLATE Latin1_General_CI_AI LIKE ? OR category COLLATE Latin1_General_CI_AI LIKE ?)";
        }
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + query + "%");
            statement.setString(2, "%" + query + "%");
            statement.setString(3, "%" + category + "%");
            statement.setString(4, "%" + category + "%");
            if (query.matches("\\d+")) {
                statement.setInt(5, Integer.parseInt(query));
            }

            ResultSet resultSet = statement.executeQuery();
            List<Ingredient> ingredients = new ArrayList<>();

            while (resultSet.next()) {
                Ingredient ingredient = new Ingredient();

                ingredient.setId(resultSet.getInt("id"));
                ingredient.setName(resultSet.getString("name"));
                ingredient.setCategory(resultSet.getString("category"));
                ingredient.setUnit(resultSet.getString("unit"));
                ingredient.setStatus(resultSet.getBoolean("status"));
                if (resultSet.getObject("delete_at", LocalDateTime.class) == null) {
                    ingredient.setDeleteAt(null);
                } else {
                    ingredient.setDeleteAt(resultSet.getObject("delete_at", LocalDateTime.class));
                }

                ingredients.add(ingredient);
            }

            return ingredients;
        } catch (SQLException e) {
            return null;
        }
    }
}
