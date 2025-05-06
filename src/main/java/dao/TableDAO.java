package main.java.dao;

import main.java.model.Table;
import main.java.model.enums.TableStatus;
import main.java.util.JDBCConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TableDAO {
    public static List<Table> getAllAvailableTable(){
        String sql = "SELECT *\n" +
                "FROM [table]\n" +
                "WHERE [status] = 'available'";
        Connection connection = JDBCConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()){
            List<Table> tables = new ArrayList<>();
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                int seatCount = resultSet.getInt("seat_count");
                String tableStatus = resultSet.getString("status").toUpperCase();

                Table table = new Table();
                table.setId(id);
                table.setSeatCount(seatCount);
                table.setStatus(TableStatus.valueOf(tableStatus));

                tables.add(table);
            }
            return tables;
        }
        catch (SQLException e){
            return null;
        }
    }

    public static boolean updateTableStatus(int id, String newStatus){
        String sql = "UPDATE [table]\n" +
                "SET [status] = ?\n" +
                "WHERE [id] = ?";

        Connection connection = JDBCConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, newStatus);
            statement.setString(2, String.valueOf(id));

            return statement.executeUpdate() > 0;

        }
        catch (SQLException e){
            return false;
        }
    }

    public static List<Table> getAllTables() {
        String sql = "SELECT * FROM [table] WHERE delete_at IS NULL";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            List<Table> tables = new ArrayList<>();
            while (resultSet.next()) {
                Table table = new Table();
                table.setId(resultSet.getInt("id"));
                table.setSeatCount(resultSet.getInt("seat_count"));
                table.setStatus(TableStatus.valueOf(resultSet.getString("status").toUpperCase()));
                table.setDeleteAt(resultSet.getObject("delete_at", LocalDateTime.class));

                tables.add(table);
            }

            return tables;
        } catch (SQLException e) {
            return null;
        }
    }

    public static Table findTable(int id) {
        String sql = "SELECT * FROM [table] WHERE id = ? AND delete_at IS NULL";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Table table = new Table();
                    table.setId(resultSet.getInt("id"));
                    table.setSeatCount(resultSet.getInt("seat_count"));
                    table.setStatus(TableStatus.valueOf(resultSet.getString("status").toUpperCase()));
                    table.setDeleteAt(resultSet.getObject("delete_at", LocalDateTime.class));

                    return table;
                }
            }
        } catch (SQLException e) {
            return null;
        }

        return null;
    }

    public static List<Table> searchTables(String query) {
        String sql = "SELECT * FROM [table] WHERE delete_at IS NULL AND (status LIKE ? OR seat_count LIKE ?)";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + query + "%");
            statement.setString(2, "%" + query + "%");

            try (ResultSet resultSet = statement.executeQuery()) {
                List<Table> tables = new ArrayList<>();

                while (resultSet.next()) {
                    Table table = new Table();
                    table.setId(resultSet.getInt("id"));
                    table.setSeatCount(resultSet.getInt("seat_count"));
                    table.setStatus(TableStatus.valueOf(resultSet.getString("status").toUpperCase()));
                    table.setDeleteAt(resultSet.getObject("delete_at", LocalDateTime.class));

                    tables.add(table);
                }

                return tables;
            }
        } catch (SQLException e) {
            return null;
        }
    }

    public static int addTable(Table table) {
        String sql = "INSERT INTO [table] (seat_count, status, delete_at) VALUES (?, ?, ?)";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, table.getSeatCount());
            statement.setString(2, table.getStatus().name().toLowerCase());
            statement.setObject(3, table.getDeleteAt());

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }

            return 0;
        } catch (SQLException e) {
            return 0;
        }
    }

    public static boolean updateTable(Table table) {
        String sql = "UPDATE [table] SET seat_count = ?, status = ?, delete_at = ? WHERE id = ?";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, table.getSeatCount());
            statement.setString(2, table.getStatus().name().toLowerCase());
            statement.setObject(3, table.getDeleteAt());
            statement.setInt(4, table.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean deleteTable(int id) {
        String sql = "UPDATE [table] SET delete_at = ? WHERE id = ?";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, LocalDateTime.now());
            statement.setInt(2, id);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }
}
