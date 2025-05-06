package main.java.dao;

import main.java.model.Member;
import main.java.model.enums.Gender;
import main.java.model.enums.MembershipTier;
import main.java.model.enums.Role;
import main.java.util.JDBCConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MemberCardDAO {
    public static boolean updateMemberCard(int memberId, String membershipTier, float loyaltyPoint, LocalDateTime createCardAt) {
        String sql = "UPDATE member SET membership_tier = ?, loyalty_point = ?, create_card_at = ? WHERE id = ?";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, membershipTier);
            statement.setFloat(2, loyaltyPoint);
            statement.setObject(3, createCardAt);
            statement.setInt(4, memberId);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean deleteMemberCard(int memberId) {
        String sql = "UPDATE member SET membership_tier = NULL, loyalty_point = 0, create_card_at = NULL WHERE id = ?";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, memberId);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static List<Member> getMemberCardList() {
        String sql = "SELECT * FROM member WHERE create_card_at IS NOT NULL AND delete_at IS NULL";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            List<Member> members = new ArrayList<>();

            while (resultSet.next()) {
                Member member = new Member();

                member.setId(resultSet.getInt("id"));
                member.setName(resultSet.getString("name"));
                member.setEmail(resultSet.getString("email"));
                member.setPassword(resultSet.getString("password"));
                member.setPhone(resultSet.getString("phone"));
                member.setBirthday(resultSet.getDate("birthday").toLocalDate());
                member.setGender(Gender.valueOf(resultSet.getString("gender").toUpperCase()));
                member.setRole(Role.valueOf(resultSet.getString("role").toUpperCase()));
                if (resultSet.getString("membership_tier") == null) {
                    member.setMembershipTier(null);
                } else {
                    member.setMembershipTier(MembershipTier.valueOf(resultSet.getString("membership_tier").toUpperCase()));
                }
                member.setLoyaltyPoint(resultSet.getFloat("loyalty_point"));
                if (resultSet.getObject("create_card_at", LocalDateTime.class) == null) {
                    member.setCreateCardAt(null);
                } else {
                    member.setCreateCardAt(resultSet.getObject("create_card_at", LocalDateTime.class));
                }
                if (resultSet.getObject("delete_at", LocalDateTime.class) == null) {
                    member.setDeleteAt(null);
                } else {
                    member.setDeleteAt(resultSet.getObject("delete_at", LocalDateTime.class));
                }

                members.add(member);
            }

            return members;
        } catch (SQLException e) {
            return null;
        }
    }

    public static List<Member> searchMemberCard(String query) {
        String sql;
        if (query.matches("\\d+")) {
            sql = "SELECT * FROM member WHERE create_card_at IS NOT NULL AND delete_at IS NULL AND (name COLLATE Latin1_General_CI_AI LIKE ? OR phone LIKE ? OR membership_tier LIKE ? OR CONVERT(VARCHAR(30), create_card_at, 120) LIKE ? OR loyalty_point = ?)";
        } else {
            sql = "SELECT * FROM member WHERE create_card_at IS NOT NULL AND delete_at IS NULL AND (name COLLATE Latin1_General_CI_AI LIKE ? OR phone LIKE ? OR membership_tier LIKE ? OR CONVERT(VARCHAR(30), create_card_at, 120) LIKE ?)";
        }
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + query + "%");
            statement.setString(2, "%" + query + "%");
            statement.setString(3, "%" + query + "%");
            statement.setString(4, "%" + query + "%");
            if (query.matches("\\d+")) {
                statement.setString(5, query);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                List<Member> members = new ArrayList<>();

                while (resultSet.next()) {
                    Member member = new Member();

                    member.setId(resultSet.getInt("id"));
                    member.setName(resultSet.getString("name"));
                    member.setEmail(resultSet.getString("email"));
                    member.setPassword(resultSet.getString("password"));
                    member.setPhone(resultSet.getString("phone"));
                    member.setBirthday(resultSet.getDate("birthday").toLocalDate());
                    member.setGender(Gender.valueOf(resultSet.getString("gender").toUpperCase()));
                    member.setRole(Role.valueOf(resultSet.getString("role").toUpperCase()));
                    if (resultSet.getString("membership_tier") == null) {
                        member.setMembershipTier(null);
                    } else {
                        member.setMembershipTier(MembershipTier.valueOf(resultSet.getString("membership_tier").toUpperCase()));
                    }
                    member.setLoyaltyPoint(resultSet.getFloat("loyalty_point"));
                    if (resultSet.getObject("create_card_at", LocalDateTime.class) == null) {
                        member.setCreateCardAt(null);
                    } else {
                        member.setCreateCardAt(resultSet.getObject("create_card_at", LocalDateTime.class));
                    }
                    if (resultSet.getObject("delete_at", LocalDateTime.class) == null) {
                        member.setDeleteAt(null);
                    } else {
                        member.setDeleteAt(resultSet.getObject("delete_at", LocalDateTime.class));
                    }

                    members.add(member);
                }

                return members;
            } catch (SQLException e) {
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
    }
}
