package main.java.dao;

import main.java.model.Member;
import main.java.model.Staff;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MnhTng
 * @Package main.java.dao
 * @date 4/17/2025 5:58 PM
 * @Copyright tùng
 */

public class StaffDAO {
    public static List<Staff> getAllStaffs() {
        String sql = "SELECT member.*, staff.salary FROM staff JOIN member ON staff.id = member.id WHERE member.delete_at IS NULL AND member.role != 'member'";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            List<Staff> staffs = new ArrayList<>();

            while (resultSet.next()) {
                Staff staff = new Staff();
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

                staff.setId(resultSet.getInt("id"));
                staff.setSalary(resultSet.getFloat("salary"));
                staff.setMember(member);

                staffs.add(staff);
            }

            return staffs;
        } catch (SQLException e) {
            return null;
        }
    }

    public static Staff findStaff(String email, String password) {
        String sql = "SELECT member.*, staff.salary FROM staff JOIN member ON staff.id = staff.id WHERE staff.email = ? AND staff.password = ? AND staff.delete_at IS NULL";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Staff staff = new Staff();
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

                    staff.setId(resultSet.getInt("id"));
                    staff.setSalary(resultSet.getFloat("salary"));
                    staff.setMember(member);

                    return staff;
                }
            } catch (SQLException e) {
                return null;
            }
        } catch (SQLException e) {
            return null;
        }

        return null;
    }

    public static boolean addStaff(Staff staff) {
        String sql = "INSERT staff (id, salary) VALUES (?, ?)";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, staff.getId());
            statement.setFloat(2, staff.getSalary());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean updateStaff(Staff staff) {
        String sql = "UPDATE staff SET salary = ? WHERE id = ?";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setFloat(1, staff.getSalary());
            statement.setInt(2, staff.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean deleteStaff(int id) {
        String sql = "UPDATE member SET delete_at = ? WHERE id = ? AND role != 'member'";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, LocalDateTime.now());
            statement.setInt(2, id);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static List<Staff> search(String query) {
        String sql;
        if (query.matches("\\d+")) {
            sql = "SELECT member.*, staff.salary FROM staff JOIN member ON staff.id = member.id WHERE member.delete_at IS NULL AND member.role != 'member' AND (member.name COLLATE Latin1_General_CI_AI LIKE ? OR member.email LIKE ? OR member.phone LIKE ? OR member.birthday LIKE ? OR member.gender LIKE ? OR member.role LIKE ? OR staff.salary = ?)";
        } else {
            sql = "SELECT member.*, staff.salary FROM staff JOIN member ON staff.id = member.id WHERE member.delete_at IS NULL AND member.role != 'member' AND (member.name COLLATE Latin1_General_CI_AI LIKE ? OR member.email LIKE ? OR member.phone LIKE ? OR member.birthday LIKE ? OR member.gender LIKE ? OR member.role LIKE ?)";
        }
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + query + "%");
            statement.setString(2, "%" + query + "%");
            statement.setString(3, "%" + query + "%");
            statement.setString(4, "%" + query + "%");
            statement.setString(5, "%" + query + "%");
            statement.setString(6, "%" + query + "%");
            if (query.matches("\\d+")) {
                statement.setString(7, query);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                List<Staff> staffs = new ArrayList<>();

                while (resultSet.next()) {
                    Staff staff = new Staff();
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

                    staff.setId(resultSet.getInt("id"));
                    staff.setSalary(resultSet.getFloat("salary"));
                    staff.setMember(member);

                    staffs.add(staff);
                }

                return staffs;
            } catch (SQLException e) {
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
    }
}
