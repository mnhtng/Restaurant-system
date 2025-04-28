package main.java.dao;

import main.java.model.Member;
import main.java.model.enums.Gender;
import main.java.model.enums.MembershipTier;
import main.java.model.enums.Role;
import main.java.util.JDBCConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MnhTng
 * @Package main.java.dao
 * @date 4/17/2025 5:57 PM
 * @Copyright tùng
 */

public class MemberDAO {
    public static List<Member> getAllMembers() {
        String sql = "SELECT * FROM member WHERE delete_at IS NULL";
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

    public static Member findMember(int id) {
        String sql = "SELECT * FROM member WHERE id = ? AND delete_at IS NULL";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

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

                    return member;
                }
            } catch (SQLException e) {
                return null;
            }
        } catch (SQLException e) {
            return null;
        }

        return null;
    }

    public static Member findMember(String email, String password) {
        String sql = "SELECT * FROM member WHERE email = ? AND password = ? AND delete_at IS NULL";
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

                    return member;
                }
            } catch (SQLException e) {
                return null;
            }
        } catch (SQLException e) {
            return null;
        }

        return null;
    }

    public static List<Member> search(String search) {
        String sql = "SELECT * FROM member WHERE delete_at IS NULL AND (email LIKE ? OR name LIKE ? OR phone LIKE ? OR birthday LIKE ? OR gender LIKE ? OR role LIKE ?)";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + search + "%");
            statement.setString(2, "%" + search + "%");
            statement.setString(3, "%" + search + "%");
            statement.setString(4, "%" + search + "%");
            statement.setString(5, "%" + search + "%");
            statement.setString(6, "%" + search + "%");

            try (ResultSet resultSet = statement.executeQuery()) {
                List<Member> members = new ArrayList<>();

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

    public static int addMember(Member member) {
        String sql = "INSERT member (name, email, password, phone, birthday, gender, role, membership_tier, loyalty_point, create_card_at, delete_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, member.getName());
            statement.setString(2, member.getEmail());
            statement.setString(3, member.getPassword());
            statement.setString(4, member.getPhone());
            statement.setDate(5, Date.valueOf(member.getBirthday()));
            statement.setString(6, member.getGender().name().toLowerCase());
            statement.setString(7, member.getRole().name().toLowerCase());
            if (member.getMembershipTier() == null) {
                statement.setString(8, null);
            } else {
                statement.setString(8, member.getMembershipTier().name().toLowerCase());
            }
            statement.setFloat(9, member.getLoyaltyPoint());
            if (member.getCreateCardAt() == null) {
                statement.setObject(10, null);
            } else {
                statement.setObject(10, member.getCreateCardAt());
            }
            if (member.getDeleteAt() == null) {
                statement.setObject(11, null);
            } else {
                statement.setObject(11, member.getDeleteAt());
            }

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

    public static boolean updateMember(Member member) {
        String sql = "UPDATE member SET name = ?, email = ?, password = ?, phone = ?, birthday = ?, gender = ?, role = ?, membership_tier = ?, loyalty_point = ?, create_card_at = ?, delete_at = ? WHERE id = ?";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, member.getName());
            statement.setString(2, member.getEmail());
            statement.setString(3, member.getPassword());
            statement.setString(4, member.getPhone());
            statement.setDate(5, Date.valueOf(member.getBirthday()));
            statement.setString(6, member.getGender().name().toLowerCase());
            statement.setString(7, member.getRole().name().toLowerCase());
            if (member.getMembershipTier() == null) {
                statement.setString(8, null);
            } else {
                statement.setString(8, member.getMembershipTier().name().toLowerCase());
            }
            statement.setFloat(9, member.getLoyaltyPoint());
            if (member.getCreateCardAt() == null) {
                statement.setObject(10, null);
            } else {
                statement.setObject(10, member.getCreateCardAt());
            }
            if (member.getDeleteAt() == null) {
                statement.setObject(11, null);
            } else {
                statement.setObject(11, member.getDeleteAt());
            }
            statement.setInt(12, member.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean deleteMember(int id) {
        String sql = "UPDATE member SET delete_at = ? WHERE id = ?";
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
