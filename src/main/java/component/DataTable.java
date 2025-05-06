package main.java.component;

import main.java.controller.admin.ComboAdminController;
import main.java.controller.admin.DishAdminController;
import main.java.controller.admin.IngredientAdminController;
import main.java.controller.admin.MemberAdminController;
import main.java.controller.admin.PermissionAdminController;
import main.java.controller.admin.SupplierAdminController;
import main.java.model.*;
import main.java.util.SearchUtil;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

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
                        maxWidths[9] = Math.max(maxWidths[9], member.getCreateCardAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")).length());
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
                        maxWidths[10] = Math.max(maxWidths[10], staff.getMember().getCreateCardAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")).length());
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
                        maxWidths[5] = Math.max(maxWidths[5], m.getCreateCardAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")).length());
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

    public static void printSupplierTable(String[] headers, List<Supplier> suppliers) {
        int columnCount = headers.length;
        int[] maxWidths = new int[columnCount];

        // Max length for each cell
        int maxWidth = 0;
        for (int i = 0; i < columnCount; i++) {
            maxWidths[i] = headers[i].length();
            maxWidth += maxWidths[i] + 3;
        }

        List<List<SupplierIngredient>> supplierIngredientList = new ArrayList<>();

        if (suppliers != null) {
            for (Supplier supplier : suppliers) {
                List<SupplierIngredient> ingredientsPerSupplier = SupplierAdminController.getSupplierIngredient(supplier.getId());
                supplierIngredientList.add(ingredientsPerSupplier);

                for (int i = 0; i < columnCount; i++) {
                    maxWidths[0] = Math.max(maxWidths[0], String.valueOf(supplier.getId()).length());
                    maxWidths[1] = Math.max(maxWidths[1], supplier.getName().length());
                    maxWidths[2] = Math.max(maxWidths[2], supplier.getAddress().length());
                    maxWidths[3] = Math.max(maxWidths[3], supplier.getPhone().length());
                    maxWidths[4] = Math.max(maxWidths[4], (ingredientsPerSupplier == null || ingredientsPerSupplier.isEmpty()) ?
                            "Không có nguyên liệu".length() :
                            ingredientsPerSupplier
                                    .stream()
                                    .map(ingredient -> (ingredient.isPrimary() ? "☆" : "") + ingredient.getIngredient().getName() + " (" + NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(ingredient.getDefaultPrice()) + ")")
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

        if (suppliers == null || suppliers.isEmpty()) {
            format = "| %-" + (maxWidth - 3) + "s |%n";
            System.out.printf(format, "Không có dữ liệu nào trong bảng này.");
            System.out.println(line);
            return;
        }

        Integer index = 0;
        for (Supplier s : suppliers) {
            List<SupplierIngredient> ingredientsPerSupplier = supplierIngredientList.get(index++);

            System.out.printf(
                    format,
                    s.getId(),
                    s.getName(),
                    s.getAddress(),
                    s.getPhone(),
                    (ingredientsPerSupplier == null || ingredientsPerSupplier.isEmpty()) ?
                            "Không có nguyên liệu" :
                            ingredientsPerSupplier
                                    .stream()
                                    .map(ingredient -> (ingredient.isPrimary() ? "☆" : "") + ingredient.getIngredient().getName() + " (" + NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(ingredient.getDefaultPrice()) + ")")
                                    .collect(Collectors.joining(", "))
            );

            System.out.println(line);
        }
    }

    public static void printIngredientTable(String[] headers, List<Ingredient> ingredients, Map<String, String> categories) {
        int columnCount = headers.length;
        int[] maxWidths = new int[columnCount];

        // Max length for each cell
        int maxWidth = 0;
        for (int i = 0; i < columnCount; i++) {
            maxWidths[i] = headers[i].length();
            maxWidth += maxWidths[i] + 3;
        }

        if (ingredients != null) {
            for (Ingredient ingredient : ingredients) {
                for (int i = 0; i < columnCount; i++) {
                    maxWidths[0] = Math.max(maxWidths[0], String.valueOf(ingredient.getId()).length());
                    maxWidths[1] = Math.max(maxWidths[1], ingredient.getName().length());
                    maxWidths[2] = Math.max(maxWidths[2], SearchUtil.convert(categories, ingredient.getCategory()).length());
                    maxWidths[3] = Math.max(maxWidths[3], ingredient.isStatus() ? "Đang sử dụng".length() : "Ngừng sử dụng".length());
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

        if (ingredients == null || ingredients.isEmpty()) {
            format = "| %-" + (maxWidth - 3) + "s |%n";
            System.out.printf(format, "Không có dữ liệu nào trong bảng này.");
            System.out.println(line);
            return;
        }

        for (Ingredient i : ingredients) {
            System.out.printf(
                    format,
                    i.getId(),
                    i.getName(),
                    SearchUtil.convert(categories, i.getCategory()),
                    i.isStatus() ? "Đang sử dụng" : "Ngừng sử dụng"
            );

            System.out.println(line);
        }
    }

    public static void printPurchaseInvoiceTable(String[] headers, List<PurchaseInvoiceDetail> purchaseInvoices) {
        int columnCount = headers.length;
        int[] maxWidths = new int[columnCount];

        // Max length for each cell
        int maxWidth = 0;
        for (int i = 0; i < columnCount; i++) {
            maxWidths[i] = headers[i].length();
            maxWidth += maxWidths[i] + 3;
        }

        if (purchaseInvoices != null) {
            for (PurchaseInvoiceDetail invoice : purchaseInvoices) {
                for (int i = 0; i < columnCount; i++) {
                    maxWidths[0] = Math.max(maxWidths[0], String.valueOf(invoice.getId()).length());
                    maxWidths[1] = Math.max(maxWidths[1], IngredientAdminController.getIngredientById(invoice.getExpectedIngredient()).getName().length());
                    maxWidths[2] = Math.max(maxWidths[2], IngredientAdminController.getIngredientById(invoice.getActualIngredient()).getName().length());
                    maxWidths[3] = Math.max(maxWidths[3], String.valueOf(invoice.getExpectedQuantity()).length());
                    maxWidths[4] = Math.max(maxWidths[4], String.valueOf(invoice.getActualQuantity()).length());
                    maxWidths[5] = Math.max(maxWidths[5], NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(invoice.getUnitPrice()).length());
                    maxWidths[6] = Math.max(maxWidths[6], NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(invoice.getSubTotal()).length());
                    maxWidths[7] = Math.max(maxWidths[7], invoice.getUpdatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")).length());
                    maxWidths[8] = Math.max(maxWidths[8], MemberAdminController.getMemberById(invoice.getUpdatedBy()).getName().length());
                    maxWidths[9] = Math.max(maxWidths[9], invoice.getStatus().toString().length());
                    maxWidths[10] = Math.max(maxWidths[10], (invoice.getNote() == null) ? "null".length() : invoice.getNote().length());
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

        if (purchaseInvoices == null) {
            format = "| %-" + (maxWidth - 3) + "s |%n";
            System.out.printf(format, "Không có dữ liệu nào trong bảng này.");
            System.out.println(line);
            return;
        }

        for (PurchaseInvoiceDetail invoice : purchaseInvoices) {
            System.out.printf(
                    format,
                    invoice.getId(),
                    IngredientAdminController.getIngredientById(invoice.getExpectedIngredient()).getName(),
                    IngredientAdminController.getIngredientById(invoice.getActualIngredient()).getName(),
                    invoice.getExpectedQuantity(),
                    invoice.getActualQuantity(),
                    NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(invoice.getUnitPrice()),
                    NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(invoice.getSubTotal()),
                    invoice.getUpdatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                    MemberAdminController.getMemberById(invoice.getUpdatedBy()).getName(),
                    invoice.getStatus(),
                    (invoice.getNote() == null) ? "null" : invoice.getNote()
            );

            System.out.println(line);
        }
    }

    public static void printWarehouseTable(String[] headers, List<Warehouse> warehouses) {
        int columnCount = headers.length;
        int[] maxWidths = new int[columnCount];

        // Max length for each cell
        int maxWidth = 0;
        for (int i = 0; i < columnCount; i++) {
            maxWidths[i] = headers[i].length();
            maxWidth += maxWidths[i] + 3;
        }

        if (warehouses != null) {
            for (Warehouse warehouse : warehouses) {
                for (int i = 0; i < columnCount; i++) {
                    maxWidths[0] = Math.max(maxWidths[0], String.valueOf(warehouse.getId()).length());
                    maxWidths[1] = Math.max(maxWidths[1], IngredientAdminController.getIngredientById(warehouse.getIngredientId()).getName().length());
                    maxWidths[2] = Math.max(maxWidths[2], String.valueOf(warehouse.getQuantity()).length());
                    maxWidths[3] = Math.max(maxWidths[3], String.valueOf(warehouse.getReservedQuantity()).length());
                    maxWidths[4] = Math.max(maxWidths[4], warehouse.getImportDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).length());
                    maxWidths[5] = Math.max(maxWidths[5], warehouse.getExpire().isBefore(LocalDate.now()) ?
                            (warehouse.getExpire().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "[✘]").length() :
                            warehouse.getExpire().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).length());
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

        if (warehouses == null || warehouses.isEmpty()) {
            format = "| %-" + (maxWidth - 3) + "s |%n";
            System.out.printf(format, "Không có dữ liệu nào trong bảng này.");
            System.out.println(line);
            return;
        }

        for (Warehouse w : warehouses) {
            System.out.printf(
                    format,
                    w.getId(),
                    IngredientAdminController.getIngredientById(w.getIngredientId()).getName(),
                    w.getQuantity(),
                    w.getReservedQuantity(),
                    w.getImportDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    w.getExpire().isBefore(LocalDate.now()) ?
                            (w.getExpire().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "[✘]") :
                            w.getExpire().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            );

            System.out.println(line);
        }
    }

    public static void printDishTable(String[] headers, List<Dish> dishes) {
        int columnCount = headers.length;
        int[] maxWidths = new int[columnCount];

        // Max length for each cell
        int maxWidth = 0;
        for (int i = 0; i < columnCount; i++) {
            maxWidths[i] = headers[i].length();
            maxWidth += maxWidths[i] + 3;
        }

        List<List<DishIngredient>> listDishIngredients = new ArrayList<>();
        if (dishes != null) {
            for (Dish dish : dishes) {
                List<DishIngredient> dishIngredients = DishAdminController.getIngredientByDish(dish.getId());
                listDishIngredients.add(dishIngredients);

                for (int i = 0; i < columnCount; i++) {
                    maxWidths[0] = Math.max(maxWidths[0], String.valueOf(dish.getId()).length());
                    maxWidths[1] = Math.max(maxWidths[1], dish.getName().length());
                    maxWidths[2] = Math.max(maxWidths[2], dish.getCategory().name().length());
                    maxWidths[3] = Math.max(maxWidths[3], dish.getPortionSize().length());
                    maxWidths[4] = Math.max(maxWidths[4], dish.getStatus().name().length());
                    maxWidths[5] = Math.max(maxWidths[5], NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(dish.getPrice()).length());
                    maxWidths[6] = Math.max(maxWidths[6], (dishIngredients == null || dishIngredients.isEmpty()) ?
                            "Không có nguyên liệu".length() :
                            dishIngredients
                                    .stream()
                                    .map(di -> di.getIngredient().getName() + " (" + di.getQuantity() + di.getUnit() + ")")
                                    .collect(Collectors.joining(", "))
                                    .length());
                    maxWidths[7] = Math.max(maxWidths[7], (dish.getProduct().isStatus() ? "Hiển thị" : "Ẩn").length());
                    maxWidths[8] = Math.max(maxWidths[8], (dish.getDescription() == null) ? "null".length() : dish.getDescription().length());
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

        if (dishes == null || dishes.isEmpty()) {
            format = "| %-" + (maxWidth - 3) + "s |%n";
            System.out.printf(format, "Không có dữ liệu nào trong bảng này.");
            System.out.println(line);
            return;
        }

        Integer index = 0;
        for (Dish d : dishes) {
            List<DishIngredient> dishIngredients = listDishIngredients.get(index++);

            System.out.printf(
                    format,
                    d.getId(),
                    d.getName(),
                    d.getCategory().name(),
                    d.getPortionSize(),
                    d.getStatus().name(),
                    NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(d.getPrice()),
                    (dishIngredients == null || dishIngredients.isEmpty()) ?
                            "Không có nguyên liệu" :
                            dishIngredients
                                    .stream()
                                    .map(di -> di.getIngredient().getName() + " (" + di.getQuantity() + di.getUnit() + ")")
                                    .collect(Collectors.joining(", ")),
                    (d.getProduct().isStatus() ? "Hiển thị" : "Ẩn"),
                    (d.getDescription() == null) ? "null" : d.getDescription()
            );

            System.out.println(line);
        }
    }

    public static void printComboTable(String[] headers, List<Combo> combos) {
        int columnCount = headers.length;
        int[] maxWidths = new int[columnCount];

        // Max length for each cell
        int maxWidth = 0;
        for (int i = 0; i < columnCount; i++) {
            maxWidths[i] = headers[i].length();
            maxWidth += maxWidths[i] + 3;
        }

        List<List<ComboDetail>> listComboDishes = new ArrayList<>();
        if (combos != null) {
            for (Combo combo : combos) {
                List<ComboDetail> comboDishes = ComboAdminController.getDishByCombo(combo.getId());
                listComboDishes.add(comboDishes);

                for (int i = 0; i < columnCount; i++) {
                    maxWidths[0] = Math.max(maxWidths[0], String.valueOf(combo.getId()).length());
                    maxWidths[1] = Math.max(maxWidths[1], combo.getName().length());
                    maxWidths[2] = Math.max(maxWidths[2], NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(combo.getPrice()).length());
                    maxWidths[3] = Math.max(maxWidths[3], (comboDishes == null || comboDishes.isEmpty()) ?
                            "Không có món ăn".length() :
                            comboDishes
                                    .stream()
                                    .map(cd -> cd.getDish().getName() + " (" + cd.getQuantity() + " - " + NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(cd.getDish().getPrice()) + ")")
                                    .collect(Collectors.joining(", "))
                                    .length());
                    maxWidths[4] = Math.max(maxWidths[4], (combo.getProduct().isStatus() ? "Hiển thị" : "Ẩn").length());
                    maxWidths[5] = Math.max(maxWidths[5], (combo.getDescription() == null) ? "null".length() : combo.getDescription().length());
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

        if (combos == null || combos.isEmpty()) {
            format = "| %-" + (maxWidth - 3) + "s |%n";
            System.out.printf(format, "Không có dữ liệu nào trong bảng này.");
            System.out.println(line);
            return;
        }

        Integer index = 0;
        for (Combo c : combos) {
            List<ComboDetail> comboDishes = listComboDishes.get(index++);

            System.out.printf(
                    format,
                    c.getId(),
                    c.getName(),
                    NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(c.getPrice()),
                    (comboDishes == null || comboDishes.isEmpty()) ?
                            "Không có món ăn" :
                            comboDishes
                                    .stream()
                                    .map(cd -> cd.getDish().getName() + " (" + cd.getQuantity() + " - " + NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(cd.getDish().getPrice()) + ")")
                                    .collect(Collectors.joining(", ")),
                    (c.getProduct().isStatus() ? "Hiển thị" : "Ẩn"),
                    (c.getDescription() == null) ? "null" : c.getDescription()
            );

            System.out.println(line);
        }
    }

    public static void printTableTable(String[] headers, List<Table> tables) {
        int columnCount = headers.length;
        int[] maxWidths = new int[columnCount];

        // Max length for each cell
        int maxWidth = 0;
        for (int i = 0; i < columnCount; i++) {
            maxWidths[i] = headers[i].length();
            maxWidth += maxWidths[i] + 3;
        }

        if (tables != null) {
            for (Table table : tables) {
                for (int i = 0; i < columnCount; i++) {
                    maxWidths[0] = Math.max(maxWidths[0], String.valueOf(table.getId()).length());
                    maxWidths[1] = Math.max(maxWidths[1], String.valueOf(table.getSeatCount()).length());
                    maxWidths[2] = Math.max(maxWidths[2], table.getStatus().name().length());
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

        if (tables == null || tables.isEmpty()) {
            format = "| %-" + (maxWidth - 3) + "s |%n";
            System.out.printf(format, "Không có dữ liệu.");
            System.out.println(line);
            return;
        }

        for (Table table : tables) {
            System.out.printf(
                    format,
                    table.getId(),
                    table.getSeatCount(),
                    table.getStatus().name()
            );

            System.out.println(line);
        }
    }

    public static void printOrderTable(String[] headers, List<Order> orders) {
        int columnCount = headers.length;
        int[] maxWidths = new int[columnCount];

        // Max length for each cell
        int maxWidth = 0;
        for (int i = 0; i < columnCount; i++) {
            maxWidths[i] = headers[i].length();
            maxWidth += maxWidths[i] + 3;
        }

        for (Order order : orders) {
            for (int i = 0; i < columnCount; i++) {
                maxWidths[0] = Math.max(maxWidths[0], order.getId().length());
                int customerId = order.getCustomerId();
                Member customer = MemberAdminController.getAllMembers()
                        .stream()
                        .filter(member -> member.getId() == customerId)
                        .findFirst()
                        .orElse(null);
                maxWidths[1] = Math.max(maxWidths[1], customer.getName().length());
                maxWidths[2] = Math.max(maxWidths[2], String.valueOf(order.getServiceClerkId()).length());
                maxWidths[3] = Math.max(maxWidths[3], String.valueOf(order.getTableId()).length());
                maxWidths[4] = Math.max(maxWidths[4],order.getOrderTime().toString().length());
                maxWidths[5] = Math.max(maxWidths[5], order.getPaymentMethod().toString().length());
                maxWidths[6] = Math.max(maxWidths[6], String.valueOf(order.getTotalAmount()).length());
                maxWidths[7] = Math.max(maxWidths[7], order.getStatus().toString().length());
                maxWidths[8] = Math.max(maxWidths[8], order.getPaidAt().toString().length());
                if (order.getNote() != null) {
                    maxWidths[9] = Math.max(maxWidths[9], order.getNote().length());
                } else {
                    maxWidths[9] = Math.max(maxWidths[9], "null".length());
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

        if (orders == null || orders.isEmpty()) {
            format = "| %-" + (maxWidth - 3) + "s |%n";
            System.out.printf(format, "Không có dữ liệu nào trong bảng này.");
            System.out.println(line);
            return;
        }

        Integer index = 0;
        for (Order order : orders) {
            int customerId = order.getCustomerId();
            Member customer = MemberAdminController.getAllMembers()
                    .stream()
                    .filter(member -> member.getId() == customerId)
                    .findFirst()
                    .orElse(null);

            System.out.printf(
                    format,
                    order.getId(),
                    customer.getName(),
                    order.getServiceClerkId(),
                    order.getTableId(),
                    order.getOrderTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                    order.getPaymentMethod(),
                    order.getTotalAmount(),
                    order.getStatus(),
                    order.getPaidAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                    order.getNote()
            );

            System.out.println(line);
        }
    }
}
