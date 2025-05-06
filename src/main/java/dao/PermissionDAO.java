package main.java.dao;

import main.java.model.Member;
import main.java.model.Permission;
import main.java.util.JDBCConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PermissionDAO {
    public static List<Permission> getAllPermissions() {
        String sql = "SELECT * FROM permission";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            List<Permission> permissions = new ArrayList<>();

            while (resultSet.next()) {
                Permission permission = new Permission();
                permission.setId(resultSet.getInt("id"));
                permission.setName(resultSet.getString("name"));
                permission.setSlug(resultSet.getString("slug"));

                permissions.add(permission);
            }

            return permissions;
        } catch (SQLException e) {
            return null;
        }
    }

    public static List<Permission> findPermissionById(int id) {
        String sql = "SELECT * FROM permission AS p JOIN member_permission AS mp ON mp.permission_id = p.id JOIN member AS m ON m.id = mp.member_id WHERE mp.member_id = ? AND m.delete_at IS NULL";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                List<Permission> permissions = new ArrayList<>();

                while (resultSet.next()) {
                    Permission permission = new Permission();

                    permission.setId(resultSet.getInt("id"));
                    permission.setName(resultSet.getString("name"));
                    permission.setSlug(resultSet.getString("slug"));

                    permissions.add(permission);
                }

                return permissions;
            }
        } catch (SQLException e) {
            return null;
        }
    }

    public static boolean addMemberPermission(int memberId, Permission permission) {
        String sql = "INSERT INTO member_permission (member_id, permission_id) VALUES (?, ?)";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, memberId);
            statement.setInt(2, permission.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean addSelectedPermission(Member member, String[] selectedPermission, List<Permission> allPermissions) {
        if (selectedPermission == null || selectedPermission.length == 0) {
            return true;
        }

        for (Permission p : allPermissions) {
            if (Arrays.asList(selectedPermission).contains(String.valueOf(p.getId()))) {
                if (!PermissionDAO.addMemberPermission(member.getId(), p)) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean updateSelectedPermission(Member member, String[] selectedPermission, List<Permission> allPermissions) {
        if (selectedPermission == null || selectedPermission.length == 0) {
            return true;
        }

        if (!PermissionDAO.deleteMemberPermission(member)) {
            return false;
        }

        if (selectedPermission[0].equals(("0"))) {
            return true;
        }

        for (Permission p : allPermissions) {
            if (Arrays.asList(selectedPermission).contains(String.valueOf(p.getId()))) {
                if (!PermissionDAO.addMemberPermission(member.getId(), p)) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean deleteMemberPermission(Member member) {
        String sql = "DELETE FROM member_permission WHERE member_id = ?";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, member.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            return false;
        }

        return true;
    }
}
