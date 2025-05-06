package main.java.dao;

import main.java.model.Combo;
import main.java.model.ComboDetail;
import main.java.model.Dish;
import main.java.model.enums.DishCategory;
import main.java.model.enums.DishStatus;
import main.java.util.JDBCConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ComboDetailDAO {
    public static List<ComboDetail> getComboDetailById(int id) {
        String sql = "SELECT c.*, cd.id AS combo_detail_id, cd.quantity, " +
                "d.id AS dish_id, d.name AS dish_name, d.category, d.portion_size, d.price AS dish_price, d.status AS dish_status, d.description AS dish_description " +
                "FROM combo AS c " +
                "JOIN product AS p ON c.id = p.id " +
                "JOIN combo_detail AS cd ON cd.combo_id = c.id " +
                "JOIN dish AS d ON cd.dish_id = d.id " +
                "WHERE p.delete_at IS NULL AND p.type = 1 AND c.id = ?";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<ComboDetail> comboDetails = new ArrayList<>();

                while (resultSet.next()) {
                    Combo combo = new Combo();
                    combo.setId(resultSet.getInt("id"));
                    combo.setName(resultSet.getString("name"));
                    combo.setPrice(resultSet.getFloat("price"));
                    combo.setDescription(resultSet.getString("description"));

                    Dish dish = new Dish();
                    dish.setId(resultSet.getInt("dish_id"));
                    dish.setName(resultSet.getString("dish_name"));
                    dish.setCategory(DishCategory.valueOf(resultSet.getString("category").toUpperCase()));
                    dish.setPortionSize(resultSet.getString("portion_size"));
                    dish.setPrice(resultSet.getFloat("dish_price"));
                    dish.setStatus(DishStatus.valueOf(resultSet.getString("dish_status").toUpperCase()));
                    dish.setDescription(resultSet.getString("dish_description"));

                    ComboDetail comboDetail = new ComboDetail();
                    comboDetail.setId(resultSet.getInt("combo_detail_id"));
                    comboDetail.setComboId(resultSet.getInt("id"));
                    comboDetail.setDishId(resultSet.getInt("dish_id"));
                    comboDetail.setQuantity(resultSet.getInt("quantity"));

                    comboDetail.setCombo(combo);
                    comboDetail.setDish(dish);

                    comboDetails.add(comboDetail);
                }

                return comboDetails;
            } catch (Exception e) {
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
    }

    public static List<ComboDetail> getDishByCombo(int id) {
        String sql = "SELECT cd.*, c.id AS combo_id, c.name AS combo_name, c.price AS combo_price, c.description AS combo_description, " +
                "d.id AS dish_id, d.name AS dish_name, d.category, d.portion_size, d.price AS dish_price, d.status AS dish_status, d.description AS dish_description " +
                "FROM combo AS c " +
                "JOIN product AS p ON c.id = p.id " +
                "JOIN combo_detail AS cd ON cd.combo_id = c.id " +
                "JOIN dish AS d ON cd.dish_id = d.id " +
                "WHERE p.delete_at IS NULL AND p.type = 1 AND cd.combo_id = ?";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<ComboDetail> comboDetails = new ArrayList<>();

                while (resultSet.next()) {
                    Dish dish = new Dish();
                    dish.setId(resultSet.getInt("dish_id"));
                    dish.setName(resultSet.getString("dish_name"));
                    dish.setCategory(DishCategory.valueOf(resultSet.getString("category").toUpperCase()));
                    dish.setPortionSize(resultSet.getString("portion_size"));
                    dish.setPrice(resultSet.getFloat("dish_price"));
                    dish.setStatus(DishStatus.valueOf(resultSet.getString("dish_status").toUpperCase()));
                    dish.setDescription(resultSet.getString("dish_description"));

                    Combo combo = new Combo();
                    combo.setId(resultSet.getInt("combo_id"));
                    combo.setName(resultSet.getString("combo_name"));
                    combo.setPrice(resultSet.getFloat("combo_price"));
                    combo.setDescription(resultSet.getString("combo_description"));

                    ComboDetail comboDetail = new ComboDetail();
                    comboDetail.setId(resultSet.getInt("id"));
                    comboDetail.setComboId(resultSet.getInt("combo_id"));
                    comboDetail.setDishId(resultSet.getInt("dish_id"));
                    comboDetail.setQuantity(resultSet.getInt("quantity"));

                    comboDetail.setDish(dish);
                    comboDetail.setCombo(combo);

                    comboDetails.add(comboDetail);
                }

                return comboDetails;
            } catch (Exception e) {
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
    }

    public static boolean addComboDetail(int comboId, Integer dishId, Integer quantity) {
        String sql = "INSERT INTO combo_detail (combo_id, dish_id, quantity) VALUES (?, ?, ?)";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, comboId);
            statement.setInt(2, dishId);
            statement.setInt(3, quantity);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean deleteComboDetail(int id) {
        String sql = "DELETE FROM combo_detail WHERE combo_id = ?";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }
}
