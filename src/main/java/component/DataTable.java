package main.java.component;

import main.java.controller.admin.MemberAdminController;
import main.java.model.Member;
import main.java.model.Permission;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
        for (int i = 0; i < columnCount; i++) {
            maxWidths[i] = headers[i].length();
        }

        List<List<Permission>> listMemberPermissions = new ArrayList<>();
        for (Member member : members) {
            List<Permission> memberPermissions = MemberAdminController.getPermissionsById(member.getId());
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
                maxWidths[8] = Math.max(maxWidths[8], String.valueOf(member.getLoyaltyPoint()).length());
                if (member.getCreateCardAt() != null) {
                    maxWidths[9] = Math.max(maxWidths[9], member.getCreateCardAt().toString().length());
                } else {
                    maxWidths[9] = Math.max(maxWidths[9], "null".length());
                }
                if (member.getDeleteAt() != null) {
                    maxWidths[10] = Math.max(maxWidths[10], member.getDeleteAt().toString().length());
                } else {
                    maxWidths[10] = Math.max(maxWidths[10], "null".length());
                }
                maxWidths[11] = Math.max(maxWidths[11], member.getRole().toString().length());
                maxWidths[12] = Math.max(maxWidths[12], (memberPermissions == null || memberPermissions.isEmpty()) ?
                        "Không có quyền hạn".length() :
                        memberPermissions
                                .stream()
                                .map(Permission::getName)
                                .collect(Collectors.joining(", "))
                                .length());
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
                    m.getLoyaltyPoint(),
                    m.getCreateCardAt() == null ? "null" : m.getCreateCardAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                    m.getDeleteAt() == null ? "null" : m.getDeleteAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
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
}
