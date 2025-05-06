package main.java.dao;

import main.java.model.Dish;
import main.java.model.DishIngredient;
import main.java.model.Ingredient;
import main.java.model.enums.DishCategory;
import main.java.model.enums.DishStatus;
import main.java.util.JDBCConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DishIngredientDAO {
    public static List<DishIngredient> getAllDishIngredient() {
        String sql = "SELECT d.*, di.id AS dish_ingredient_id, di.quantity, di.unit, " +
                "i.id AS ingredient_id, i.name AS ingredient_name, i.category AS ingredient_category, i.unit AS ingredient_unit, i.status AS ingredient_status " +
                "FROM dish AS d " +
                "JOIN product AS p ON d.id = p.id " +
                "JOIN dish_ingredient AS di ON d.id = di.dish_id " +
                "JOIN ingredient AS i ON di.ingredient_id = i.id " +
                "WHERE p.type = 0 AND p.delete_at IS NULL";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            List<DishIngredient> dishes = new ArrayList<>();

            while (resultSet.next()) {
                Dish dish = new Dish();
                dish.setId(resultSet.getInt("id"));
                dish.setName(resultSet.getString("name"));
                dish.setCategory(DishCategory.valueOf(resultSet.getString("category").toUpperCase()));
                dish.setPortionSize(resultSet.getString("portion_size"));
                dish.setPrice(resultSet.getFloat("price"));
                dish.setStatus(DishStatus.valueOf(resultSet.getString("status").toUpperCase()));
                dish.setDescription(resultSet.getString("description"));

                Ingredient ingredient = new Ingredient();
                ingredient.setId(resultSet.getInt("ingredient_id"));
                ingredient.setName(resultSet.getString("ingredient_name"));
                ingredient.setCategory(resultSet.getString("ingredient_category"));
                ingredient.setUnit(resultSet.getString("ingredient_unit"));
                ingredient.setStatus(resultSet.getBoolean("ingredient_status"));

                DishIngredient dishIngredient = new DishIngredient();
                dishIngredient.setId(resultSet.getInt("dish_ingredient_id"));
                dishIngredient.setDishId(resultSet.getInt("id"));
                dishIngredient.setIngredientId(resultSet.getInt("ingredient_id"));
                dishIngredient.setQuantity(resultSet.getFloat("quantity"));
                dishIngredient.setUnit(resultSet.getString("unit"));

                dishIngredient.setDish(dish);
                dishIngredient.setIngredient(ingredient);

                dishes.add(dishIngredient);
            }

            return dishes;
        } catch (SQLException e) {
            return null;
        }
    }

    public static List<DishIngredient> getIngredientByDish(int id) {
        String sql = "SELECT d.*, di.id AS dish_ingredient_id, di.quantity, di.unit, " +
                "i.id AS ingredient_id, i.name AS ingredient_name, i.category AS ingredient_category, i.unit AS ingredient_unit, i.status AS ingredient_status " +
                "FROM dish AS d " +
                "JOIN product AS p ON d.id = p.id " +
                "JOIN dish_ingredient AS di ON d.id = di.dish_id " +
                "JOIN ingredient AS i ON di.ingredient_id = i.id " +
                "WHERE p.type = 0 AND p.delete_at IS NULL AND d.id = ?";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<DishIngredient> dishes = new ArrayList<>();

                while (resultSet.next()) {
                    Dish dish = new Dish();
                    dish.setId(resultSet.getInt("id"));
                    dish.setName(resultSet.getString("name"));
                    dish.setCategory(DishCategory.valueOf(resultSet.getString("category").toUpperCase()));
                    dish.setPortionSize(resultSet.getString("portion_size"));
                    dish.setPrice(resultSet.getFloat("price"));
                    dish.setStatus(DishStatus.valueOf(resultSet.getString("status").toUpperCase()));
                    dish.setDescription(resultSet.getString("description"));

                    Ingredient ingredient = new Ingredient();
                    ingredient.setId(resultSet.getInt("ingredient_id"));
                    ingredient.setName(resultSet.getString("ingredient_name"));
                    ingredient.setCategory(resultSet.getString("ingredient_category"));
                    ingredient.setUnit(resultSet.getString("ingredient_unit"));
                    ingredient.setStatus(resultSet.getBoolean("ingredient_status"));

                    DishIngredient dishIngredient = new DishIngredient();
                    dishIngredient.setId(resultSet.getInt("dish_ingredient_id"));
                    dishIngredient.setDishId(resultSet.getInt("id"));
                    dishIngredient.setIngredientId(resultSet.getInt("ingredient_id"));
                    dishIngredient.setQuantity(resultSet.getFloat("quantity"));
                    dishIngredient.setUnit(resultSet.getString("unit"));

                    dishIngredient.setDish(dish);
                    dishIngredient.setIngredient(ingredient);

                    dishes.add(dishIngredient);
                }

                return dishes;
            } catch (SQLException e) {
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
    }

    public static boolean addIngredient(int dishId, Integer ingredientId, Float quantity, String unit) {
        String sql = "INSERT INTO dish_ingredient (dish_id, ingredient_id, quantity, unit) VALUES (?, ?, ?, ?)";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, dishId);
            statement.setInt(2, ingredientId);
            statement.setFloat(3, quantity);
            statement.setString(4, unit);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean deleteDishIngredient(int dishId) {
        String sql = "DELETE FROM dish_ingredient WHERE dish_id = ?";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, dishId);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }
}
