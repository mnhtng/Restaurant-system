package main.java.component;

import main.java.controller.admin.MemberAdminController;
import main.java.controller.admin.PermissionAdminController;
import main.java.model.Member;
import main.java.model.Permission;
import main.java.model.Staff;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author MnhTng
 * @Package main.java.component
 * @date 4/27/2025 1:01 PM
 * @Copyright tùng
 */

public class DataTable {
    public static void printMemberTable(String[] headers, List<Member> members) {
        int columnCount = headers.length;
        int[] maxWidths = new int[columnCount];

        // Max length for each cell
        int maxWidth = 0;
        for (int i = 0; i < columnCount; i++) {
            maxWidths[i] = headers[i].length();
            maxWidth += maxWidths[i] + 3;
        }

        List<List<Permission>> listMemberPermissions = new ArrayList<>();
        if (members != null) {
            for (Member member : members) {
                List<Permission> memberPermissions = PermissionAdminController.getPermissionsById(member.getId());
                listMemberPermissions.add(memberPermissions);

                for (int i = 0; i < columnCount; i++) {
                    maxWidths[0] = Math.max(maxWidths[0], String.valueOf(member.getId()).length());
                    maxWidths[1] = Math.max(maxWidths[1], member.getName().length());
                    maxWidths[2] = Math.max(maxWidths[2], member.getEmail().length());
                    maxWidths[3] = Math.max(maxWidths[3], member.getPassword().length());
                    maxWidths[4] = Math.max(maxWidths[4], member.getPhone().length());
                    maxWidths[5] = Math.max(maxWidths[5], member.getBirthday().toString().length());
                    maxWidths[6] = Math.max(maxWidths[6], member.getGender().toString().length());
                    if (member.getMembershipTier() != null) {
                        maxWidths[7] = Math.max(maxWidths[7], member.getMembershipTier().toString().length());
                    } else {
                        maxWidths[7] = Math.max(maxWidths[7], "null".length());
                    }
                    maxWidths[8] = Math.max(maxWidths[8], new BigDecimal(member.getLoyaltyPoint()).toPlainString().length());
                    if (member.getCreateCardAt() != null) {
                        maxWidths[9] = Math.max(maxWidths[9], member.getCreateCardAt().toString().length());
                    } else {
                        maxWidths[9] = Math.max(maxWidths[9], "null".length());
                    }
                    maxWidths[10] = Math.max(maxWidths[10], member.getRole().toString().length());
                    maxWidths[11] = Math.max(maxWidths[11], (memberPermissions == null || memberPermissions.isEmpty()) ?
                            "Không có quyền hạn".length() :
                            memberPermissions
                                    .stream()
                                    .map(Permission::getName)
                                    .collect(Collectors.joining(", "))
                                    .length());
                }
            }
        }

        // Build Table format
        // String format = "| %-3s | %-15s | %-14s | %-10s | %-10s | %-6s | %-9s | %-6s | %-16s | %-16s | %-20s | %-15s |%n";
        StringBuilder formatBuilder = new StringBuilder("|");
        for (int width : maxWidths) {
            formatBuilder.append(" %-").append(width).append("s |");
        }
        formatBuilder.append("%n");
        String format = formatBuilder.toString();

        // Build separate line
        // String line = "+-----+-----------------+----------------------+------------+------------+--------+-----------+--------+------------------+------------------+----------------+-----------------+";
        StringBuilder lineBuilder = new StringBuilder("+");
        for (int width : maxWidths) {
            lineBuilder.append("-".repeat(width + 2)).append("+");
        }
        String line = lineBuilder.toString();

        // Print
        System.out.println(line);
        System.out.printf(format, (Object[]) headers);
        System.out.println(line);

        if (members == null || members.isEmpty()) {
            format = "| %-" + (maxWidth - 3) + "s |%n";
            System.out.printf(format, "Không có dữ liệu nào trong bảng này.");
            System.out.println(line);
            return;
        }

        Integer index = 0;
        for (Member m : members) {
            List<Permission> memberPermissions = listMemberPermissions.get(index++);

            System.out.printf(
                    format,
                    m.getId(),
                    m.getName(),
                    m.getEmail(),
                    m.getPassword(),
                    m.getPhone(),
                    m.getBirthday().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    m.getGender(),
                    m.getMembershipTier(),
                    new BigDecimal(m.getLoyaltyPoint()).toPlainString(),
                    m.getCreateCardAt() == null ? "null" : m.getCreateCardAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                    m.getRole(),
                    (memberPermissions == null || memberPermissions.isEmpty()) ?
                            "Không có quyền hạn" :
                            memberPermissions
                                    .stream()
                                    .map(Permission::getName)
                                    .collect(Collectors.joining(", "))
            );

            System.out.println(line);
        }
    }

    public static void printStaffTable(String[] headers, List<Staff> staffs) {
        int columnCount = headers.length;
        int[] maxWidths = new int[columnCount];

        // Max length for each cell
        int maxWidth = 0;
        for (int i = 0; i < columnCount; i++) {
            maxWidths[i] = headers[i].length();
            maxWidth += maxWidths[i] + 3;
        }

        List<List<Permission>> listMemberPermissions = new ArrayList<>();

        if (staffs != null) {
            for (Staff staff : staffs) {
                List<Permission> memberPermissions = PermissionAdminController.getPermissionsById(staff.getId());
                listMemberPermissions.add(memberPermissions);

                for (int i = 0; i < columnCount; i++) {
                    maxWidths[0] = Math.max(maxWidths[0], String.valueOf(staff.getId()).length());
                    maxWidths[1] = Math.max(maxWidths[1], staff.getMember().getName().length());
                    maxWidths[2] = Math.max(maxWidths[2], staff.getMember().getEmail().length());
                    maxWidths[3] = Math.max(maxWidths[3], staff.getMember().getPassword().length());
                    maxWidths[4] = Math.max(maxWidths[4], staff.getMember().getPhone().length());
                    maxWidths[5] = Math.max(maxWidths[5], staff.getMember().getBirthday().toString().length());
                    maxWidths[6] = Math.max(maxWidths[6], staff.getMember().getGender().toString().length());
                    maxWidths[7] = Math.max(maxWidths[7], String.valueOf(NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(staff.getSalary())).length());
                    if (staff.getMember().getMembershipTier() != null) {
                        maxWidths[8] = Math.max(maxWidths[8], staff.getMember().getMembershipTier().toString().length());
                    } else {
                        maxWidths[8] = Math.max(maxWidths[8], "null".length());
                    }
                    maxWidths[9] = Math.max(maxWidths[9], new BigDecimal(staff.getMember().getLoyaltyPoint()).toPlainString().length());
                    if (staff.getMember().getCreateCardAt() != null) {
                        maxWidths[10] = Math.max(maxWidths[10], staff.getMember().getCreateCardAt().toString().length());
                    } else {
                        maxWidths[10] = Math.max(maxWidths[10], "null".length());
                    }
                    maxWidths[11] = Math.max(maxWidths[11], staff.getMember().getRole().toString().length());
                    maxWidths[12] = Math.max(maxWidths[12], (memberPermissions == null || memberPermissions.isEmpty()) ?
                            "Không có quyền hạn".length() :
                            memberPermissions
                                    .stream()
                                    .map(Permission::getName)
                                    .collect(Collectors.joining(", "))
                                    .length());
                }
            }
        }

        // Build Table format
        StringBuilder formatBuilder = new StringBuilder("|");
        for (int width : maxWidths) {
            formatBuilder.append(" %-").append(width).append("s |");
        }
        formatBuilder.append("%n");
        String format = formatBuilder.toString();

        // Build separate line
        StringBuilder lineBuilder = new StringBuilder("+");
        for (int width : maxWidths) {
            lineBuilder.append("-".repeat(width + 2)).append("+");
        }
        String line = lineBuilder.toString();

        // Print
        System.out.println(line);
        System.out.printf(format, (Object[]) headers);
        System.out.println(line);

        if (staffs == null || staffs.isEmpty()) {
            format = "| %-" + (maxWidth - 3) + "s |%n";
            System.out.printf(format, "Không có dữ liệu nào trong bảng này.");
            System.out.println(line);
            return;
        }

        Integer index = 0;
        for (Staff s : staffs) {
            List<Permission> memberPermissions = listMemberPermissions.get(index++);

            System.out.printf(
                    format,
                    s.getId(),
                    s.getMember().getName(),
                    s.getMember().getEmail(),
                    s.getMember().getPassword(),
                    s.getMember().getPhone(),
                    s.getMember().getBirthday().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    s.getMember().getGender(),
                    NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(s.getSalary()),
                    s.getMember().getMembershipTier(),
                    new BigDecimal(s.getMember().getLoyaltyPoint()).toPlainString(),
                    s.getMember().getCreateCardAt() == null ? "null" : s.getMember().getCreateCardAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                    s.getMember().getRole(),
                    (memberPermissions == null || memberPermissions.isEmpty()) ?
                            "Không có quyền hạn" :
                            memberPermissions
                                    .stream()
                                    .map(Permission::getName)
                                    .collect(Collectors.joining(", "))
            );

            System.out.println(line);
        }
    }

    public static void printMemberCardTable(String[] headers, List<Member> members) {
        int columnCount = headers.length;
        int[] maxWidths = new int[columnCount];

        // Max length for each cell
        int maxWidth = 0;
        for (int i = 0; i < columnCount; i++) {
            maxWidths[i] = headers[i].length();
            maxWidth += maxWidths[i] + 3;
        }

        List<List<Permission>> listMemberPermissions = new ArrayList<>();

        if (members != null) {
            for (Member m : members) {
                List<Permission> memberPermissions = PermissionAdminController.getPermissionsById(m.getId());
                listMemberPermissions.add(memberPermissions);

                for (int i = 0; i < columnCount; i++) {
                    maxWidths[0] = Math.max(maxWidths[0], String.valueOf(m.getId()).length());
                    maxWidths[1] = Math.max(maxWidths[1], m.getName().length());
                    maxWidths[2] = Math.max(maxWidths[2], m.getPhone().length());
                    if (m.getMembershipTier() != null) {
                        maxWidths[3] = Math.max(maxWidths[3], m.getMembershipTier().toString().length());
                    } else {
                        maxWidths[3] = Math.max(maxWidths[3], "null".length());
                    }
                    maxWidths[4] = Math.max(maxWidths[4], new BigDecimal(m.getLoyaltyPoint()).toPlainString().length());
                    if (m.getCreateCardAt() != null) {
                        maxWidths[5] = Math.max(maxWidths[5], m.getCreateCardAt().toString().length());
                    } else {
                        maxWidths[5] = Math.max(maxWidths[5], "null".length());
                    }
                }
            }
        }

        // Build Table format
        StringBuilder formatBuilder = new StringBuilder("|");
        for (int width : maxWidths) {
            formatBuilder.append(" %-").append(width).append("s |");
        }
        formatBuilder.append("%n");
        String format = formatBuilder.toString();

        // Build separate line
        StringBuilder lineBuilder = new StringBuilder("+");
        for (int width : maxWidths) {
            lineBuilder.append("-".repeat(width + 2)).append("+");
        }
        String line = lineBuilder.toString();

        // Print
        System.out.println(line);
        System.out.printf(format, (Object[]) headers);
        System.out.println(line);

        if (members == null || members.isEmpty()) {
            format = "| %-" + (maxWidth - 3) + "s |%n";
            System.out.printf(format, "Không có dữ liệu nào trong bảng này.");
            System.out.println(line);
            return;
        }

        Integer index = 0;
        for (Member m : members) {
            List<Permission> memberPermissions = listMemberPermissions.get(index++);

            System.out.printf(
                    format,
                    m.getId(),
                    m.getName(),
                    m.getPhone(),
                    m.getMembershipTier(),
                    new BigDecimal(m.getLoyaltyPoint()).toPlainString(),
                    m.getCreateCardAt() == null ? "null" : m.getCreateCardAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
            );

            System.out.println(line);
        }
    }
}
