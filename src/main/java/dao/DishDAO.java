package main.java.dao;

import main.java.model.Dish;
import main.java.model.Product;
import main.java.model.enums.DishCategory;
import main.java.model.enums.DishStatus;
import main.java.util.JDBCConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DishDAO {
    public static List<Dish> getAllDish() {
        String sql = "SELECT dish.*, product.id, product.type, product.status AS product_status FROM dish JOIN product ON dish.id = product.id WHERE product.type = 0 AND product.delete_at IS NULL";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            List<Dish> dishes = new ArrayList<>();

            while (resultSet.next()) {
                Product product = new Product();
                product.setId(resultSet.getInt("id"));
                product.setType(resultSet.getString("type"));
                product.setStatus(resultSet.getBoolean("product_status"));

                Dish dish = new Dish();
                dish.setId(resultSet.getInt("id"));
                dish.setName(resultSet.getString("name"));
                dish.setCategory(DishCategory.valueOf(resultSet.getString("category").toUpperCase()));
                dish.setPortionSize(resultSet.getString("portion_size"));
                dish.setPrice(resultSet.getFloat("price"));
                dish.setStatus(DishStatus.valueOf(resultSet.getString("status").toUpperCase()));
                dish.setDescription(resultSet.getString("description"));

                dish.setProduct(product);

                dishes.add(dish);
            }

            return dishes;
        } catch (SQLException e) {
            return null;
        }
    }

    public static Dish getDishById(int id) {
        String sql = "SELECT dish.*, product.id, product.type, product.status AS product_status FROM dish JOIN product ON dish.id = product.id WHERE dish.id = ? AND product.delete_at IS NULL AND product.type = 0";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Product product = new Product();
                    product.setId(resultSet.getInt("id"));
                    product.setType(resultSet.getString("type"));
                    product.setStatus(resultSet.getBoolean("product_status"));

                    Dish dish = new Dish();
                    dish.setId(resultSet.getInt("id"));
                    dish.setName(resultSet.getString("name"));
                    dish.setCategory(DishCategory.valueOf(resultSet.getString("category").toUpperCase()));
                    dish.setPortionSize(resultSet.getString("portion_size"));
                    dish.setPrice(resultSet.getFloat("price"));
                    dish.setStatus(DishStatus.valueOf(resultSet.getString("status").toUpperCase()));
                    dish.setDescription(resultSet.getString("description"));

                    dish.setProduct(product);

                    return dish;
                }

                return null;
            } catch (Exception e) {
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
    }

    public static boolean addDish(Dish dish) {
        String sql = "INSERT INTO dish (id, name, category, portion_size, price, status, description) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, dish.getId());
            preparedStatement.setString(2, dish.getName());
            preparedStatement.setString(3, dish.getCategory().name().toLowerCase());
            preparedStatement.setString(4, dish.getPortionSize());
            preparedStatement.setFloat(5, dish.getPrice());
            preparedStatement.setString(6, dish.getStatus().name().toLowerCase());
            preparedStatement.setString(7, dish.getDescription());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean updateDish(Dish updateDish) {
        String sql = "UPDATE dish SET name = ?, category = ?, portion_size = ?, price = ?, status = ?, description = ? WHERE id = ?";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, updateDish.getName());
            preparedStatement.setString(2, updateDish.getCategory().name().toLowerCase());
            preparedStatement.setString(3, updateDish.getPortionSize());
            preparedStatement.setFloat(4, updateDish.getPrice());
            preparedStatement.setString(5, updateDish.getStatus().name().toLowerCase());
            preparedStatement.setString(6, updateDish.getDescription());
            preparedStatement.setInt(7, updateDish.getId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static List<Dish> search(String query) {
        String sql;
        if (query.matches("\\d+")) {
            sql = "SELECT d.*, p.id, p.type, p.status AS product_status " +
                    "FROM dish AS d " +
                    "JOIN product AS p ON d.id = p.id " +
                    "WHERE p.type = 0 AND p.delete_at IS NULL AND " +
                    "(d.name COLLATE Latin1_General_CI_AI LIKE ? OR d.category LIKE ? OR d.portion_size COLLATE Latin1_General_CI_AI LIKE ? OR d.status LIKE ? OR d.description COLLATE Latin1_General_CI_AI LIKE ? OR p.status LIKE ? " +
                    "OR EXISTS(SELECT 1 FROM dish_ingredient AS di JOIN ingredient AS i ON di.ingredient_id = i.id WHERE di.dish_id = d.id AND i.name COLLATE Latin1_General_CI_AI LIKE ?) " +
                    "OR d.price = ?)";
        } else {
            sql = "SELECT d.*, p.id, p.type, p.status AS product_status " +
                    "FROM dish AS d " +
                    "JOIN product AS p ON d.id = p.id " +
                    "WHERE p.type = 0 AND p.delete_at IS NULL AND " +
                    "(d.name COLLATE Latin1_General_CI_AI LIKE ? OR d.category LIKE ? OR d.portion_size COLLATE Latin1_General_CI_AI LIKE ? OR d.status LIKE ? OR d.description COLLATE Latin1_General_CI_AI LIKE ? OR p.status LIKE ? " +
                    "OR EXISTS(SELECT 1 FROM dish_ingredient AS di JOIN ingredient AS i ON di.ingredient_id = i.id WHERE di.dish_id = d.id AND i.name COLLATE Latin1_General_CI_AI LIKE ?))";
        }
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 1; i <= 7; i++) {
                preparedStatement.setString(i, "%" + query + "%");
            }
            if (query.matches("\\d+")) {
                preparedStatement.setFloat(8, Float.parseFloat(query));
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Dish> dishes = new ArrayList<>();

                while (resultSet.next()) {
                    Product product = new Product();
                    product.setId(resultSet.getInt("id"));
                    product.setType(resultSet.getString("type"));
                    product.setStatus(resultSet.getBoolean("product_status"));

                    Dish dish = new Dish();
                    dish.setId(resultSet.getInt("id"));
                    dish.setName(resultSet.getString("name"));
                    dish.setCategory(DishCategory.valueOf(resultSet.getString("category").toUpperCase()));
                    dish.setPortionSize(resultSet.getString("portion_size"));
                    dish.setPrice(resultSet.getFloat("price"));
                    dish.setStatus(DishStatus.valueOf(resultSet.getString("status").toUpperCase()));
                    dish.setDescription(resultSet.getString("description"));

                    dish.setProduct(product);

                    dishes.add(dish);
                }

                return dishes;
            } catch (SQLException e) {
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
    }
}
