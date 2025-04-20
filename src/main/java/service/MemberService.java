package main.java.service;

import main.java.model.Member;
import main.java.model.enums.Gender;
import main.java.model.enums.Role;
import main.java.util.JDBCConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MnhTng
 * @Package main.java.service
 * @date 4/20/2025 2:53 PM
 * @Copyright tùng
 */

public class MemberService {
    public static Member[] getAllUsers() {
        String sql = "SELECT * FROM member";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            List<Member> members = new ArrayList<>();

            while (resultSet.next()) {
                Member member = new Member();

                member.setId(resultSet.getInt("id"));
                member.setName(resultSet.getString("name"));
                member.setEmail(resultSet.getString("email"));
                member.setPhone(resultSet.getString("phone"));
                member.setBirthday(resultSet.getDate("birthday").toLocalDate());
                member.setGender(Gender.valueOf(resultSet.getString("gender").toUpperCase()));
                member.setRole(Role.valueOf(resultSet.getString("role").toUpperCase()));

                members.add(member);
            }

            return members.toArray(new Member[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Member[0];
    }

    public static Member findMember(String email, String password) {
        String sql = "SELECT * FROM member WHERE email = ? AND password = ?";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Member member = new Member();

                    member.setId(resultSet.getInt("id"));
                    member.setName(resultSet.getString("name"));
                    member.setEmail(resultSet.getString("email"));
                    member.setPassword(resultSet.getString("password"));
                    member.setPhone(resultSet.getString("phone"));
                    member.setBirthday(resultSet.getDate("birthday").toLocalDate());
                    member.setGender(Gender.valueOf(resultSet.getString("gender").toUpperCase()));
                    member.setRole(Role.valueOf(resultSet.getString("role").toUpperCase()));

                    return member;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean addMember(Member member) {
        String sql = "INSERT member (name, email, password, phone, birthday, gender, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, member.getName());
            statement.setString(2, member.getEmail());
            statement.setString(3, member.getPassword());
            statement.setString(4, member.getPhone());
            statement.setDate(5, java.sql.Date.valueOf(member.getBirthday()));
            statement.setString(6, member.getGender().name().toLowerCase());
            statement.setString(7, member.getRole().name().toLowerCase());

            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
