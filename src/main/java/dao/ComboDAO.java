package main.java.dao;

import main.java.model.Combo;
import main.java.model.Product;
import main.java.util.JDBCConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ComboDAO {
    public static List<Combo> getAllCombo() {
        String sql = "SELECT combo.*, product.id, product.type, product.status AS product_status FROM combo JOIN product ON combo.id = product.id WHERE product.type = 1 AND product.delete_at IS NULL";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            List<Combo> combos = new ArrayList<>();

            while (resultSet.next()) {
                Product product = new Product();
                product.setId(resultSet.getInt("id"));
                product.setType(resultSet.getString("type"));
                product.setStatus(resultSet.getBoolean("product_status"));

                Combo combo = new Combo();
                combo.setId(resultSet.getInt("id"));
                combo.setName(resultSet.getString("name"));
                combo.setPrice(resultSet.getFloat("price"));
                combo.setDescription(resultSet.getString("description"));

                combo.setProduct(product);

                combos.add(combo);
            }

            return combos;
        } catch (SQLException e) {
            return null;
        }
    }

    public static Combo getComboById(int id) {
        String sql = "SELECT combo.*, product.id, product.type, product.status AS product_status FROM combo JOIN product ON combo.id = product.id WHERE combo.id = ? AND product.delete_at IS NULL AND product.type = 1";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Product product = new Product();
                    product.setId(resultSet.getInt("id"));
                    product.setType(resultSet.getString("type"));
                    product.setStatus(resultSet.getBoolean("product_status"));

                    Combo combo = new Combo();
                    combo.setId(resultSet.getInt("id"));
                    combo.setName(resultSet.getString("name"));
                    combo.setPrice(resultSet.getFloat("price"));
                    combo.setDescription(resultSet.getString("description"));

                    combo.setProduct(product);

                    return combo;
                }

                return null;
            } catch (Exception e) {
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
    }

    public static boolean addCombo(Combo combo) {
        String sql = "INSERT INTO combo (id, name, price, description) VALUES (?, ?, ?, ?)";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, combo.getId());
            statement.setString(2, combo.getName());
            statement.setFloat(3, combo.getPrice());
            statement.setString(4, combo.getDescription());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean updateCombo(Combo updatedCombo) {
        String sql = "UPDATE combo SET name = ?, price = ?, description = ? WHERE id = ?";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, updatedCombo.getName());
            statement.setFloat(2, updatedCombo.getPrice());
            statement.setString(3, updatedCombo.getDescription());
            statement.setInt(4, updatedCombo.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static List<Combo> search(String query) {
        String sql;
        if (query.matches("\\d+")) {
            sql = "SELECT c.*, p.id, p.type, p.status AS product_status " +
                    "FROM combo AS c " +
                    "JOIN product AS p ON c.id = p.id " +
                    "WHERE p.type = 1 AND p.delete_at IS NULL AND " +
                    "(c.name COLLATE Latin1_General_CI_AI LIKE ? OR c.description COLLATE Latin1_General_CI_AI LIKE ? OR p.status LIKE ? " +
                    "OR EXISTS(SELECT 1 FROM combo_detail AS cd JOIN dish AS d ON cd.dish_id = d.id WHERE cd.combo_id = c.id AND d.name COLLATE Latin1_General_CI_AI LIKE ?) " +
                    "OR c.price = ?)";
        } else {
            sql = "SELECT c.*, p.id, p.type, p.status AS product_status " +
                    "FROM combo AS c " +
                    "JOIN product AS p ON c.id = p.id " +
                    "WHERE p.type = 1 AND p.delete_at IS NULL AND " +
                    "(c.name COLLATE Latin1_General_CI_AI LIKE ? OR c.description COLLATE Latin1_General_CI_AI LIKE ? OR p.status LIKE ? " +
                    "OR EXISTS(SELECT 1 FROM combo_detail AS cd JOIN dish AS d ON cd.dish_id = d.id WHERE cd.combo_id = c.id AND d.name COLLATE Latin1_General_CI_AI LIKE ?))";
        }
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 1; i <= 4; i++) {
                preparedStatement.setString(i, "%" + query + "%");
            }
            if (query.matches("\\d+")) {
                preparedStatement.setFloat(5, Float.parseFloat(query));
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Combo> combos = new ArrayList<>();

                while (resultSet.next()) {
                    Product product = new Product();
                    product.setId(resultSet.getInt("id"));
                    product.setType(resultSet.getString("type"));
                    product.setStatus(resultSet.getBoolean("product_status"));

                    Combo combo = new Combo();
                    combo.setId(resultSet.getInt("id"));
                    combo.setName(resultSet.getString("name"));
                    combo.setPrice(resultSet.getFloat("price"));
                    combo.setDescription(resultSet.getString("description"));

                    combo.setProduct(product);

                    combos.add(combo);
                }

                return combos;
            } catch (Exception e) {
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
    }
}
