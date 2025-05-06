package main.java.view.admin;

import main.java.controller.AuthController;
import main.java.controller.admin.PermissionAdminController;
import main.java.middleware.AuthMiddleware;
import main.java.model.Permission;
import main.java.util.Session;
import main.java.view.auth.AuthView;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class MainAdminView {
    public MainAdminView() {
        while (Session.getInstance().isAuthenticated()) {
            show();
        }

        new AuthView();
    }

    public void show() {
        List<Permission> permissions = PermissionAdminController.getPermissionsById(Session.getInstance().getCurrentUser().getId());

        Scanner sc = new Scanner(System.in);

        System.out.println("========== Giao diện quản lý ==========");
        System.out.println("1. Quản lý thành viên");
        System.out.println("2. Quản lý nhân viên");
        System.out.println("3. Quản lý thẻ thành viên");
        System.out.println("4. Quản lý nhà cung cấp");
        System.out.println("5. Quản lý nguyên liệu");
        System.out.println("6. Quản lý kho");
        System.out.println("7. Quản lý hóa đơn nhập kho");
        System.out.println("8. Quản lý món ăn");
        System.out.println("9. Quản lý combo");
        System.out.println("10. Quản lý bàn");
        System.out.println("11. Quản lý hóa đơn đặt hàng");
        System.out.println("12. Quản lý doanh thu");
        System.out.println("13. Đăng xuất");
        System.out.print("Chọn chức năng: ");

        try {
            switch (sc.nextInt()) {
                case 1:
                    (new AuthMiddleware()).handle();
                    if (permissions.stream().noneMatch(p -> List.of("member.view", "member.create", "member.update", "member.delete").contains(p.getSlug()))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    new MemberAdminView();
                    break;
                case 2:
                    (new AuthMiddleware()).handle();
                    if (permissions.stream().noneMatch(p -> List.of("member.view", "member.create", "member.update", "member.delete").contains(p.getSlug()))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    new StaffAdminView();
                    break;
                case 3:
                    (new AuthMiddleware()).handle();
                    if (permissions.stream().noneMatch(p -> List.of("member.view", "member.create", "member.update", "member.delete").contains(p.getSlug()))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    new MemberCardAdminView();
                    break;
                case 4:
                    (new AuthMiddleware()).handle();
                    if (permissions.stream().noneMatch(p -> List.of("supplier.view", "supplier.create", "supplier.update", "supplier.delete").contains(p.getSlug()))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    new SupplierAdminView();
                    break;
                case 5:
                    (new AuthMiddleware()).handle();
                    if (permissions.stream().noneMatch(p -> List.of("ingredient.view", "ingredient.create", "ingredient.update", "ingredient.delete").contains(p.getSlug()))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    new IngredientAdminView();
                    break;
                case 6:
                    (new AuthMiddleware()).handle();
                    if (permissions.stream().noneMatch(p -> List.of("warehouse.view", "warehouse.create", "warehouse.update", "warehouse.delete").contains(p.getSlug()))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    new WarehouseAdminView();
                    break;
                case 7:
                    (new AuthMiddleware()).handle();
                    if (permissions.stream().noneMatch(p -> List.of("purchase_invoice.view", "purchase_invoice.create", "purchase_invoice.update").contains(p.getSlug()))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    new InventoryInvoiceAdminView();
                    break;
                case 8:
                    (new AuthMiddleware()).handle();
                    if (permissions.stream().noneMatch(p -> List.of("dish.view", "dish.create", "dish.update", "dish.delete").contains(p.getSlug()))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    new DishAdminView();
                    break;
                case 9:
                    (new AuthMiddleware()).handle();
                    if (permissions.stream().noneMatch(p -> List.of("combo.view", "combo.create", "combo.update", "combo.delete").contains(p.getSlug()))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    new ComboAdminView();
                    break;
                case 10:
                    (new AuthMiddleware()).handle();
                    if (permissions.stream().noneMatch(p -> List.of("table.view", "table.create", "table.update", "table.delete").contains(p.getSlug()))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    new TableAdminView();
                    break;
                case 11:
                    (new AuthMiddleware()).handle();
                    if (permissions.stream().noneMatch(p -> List.of("order.view", "order.create", "order.update").contains(p.getSlug()))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    new OrderAdminView();
                    break;
                case 12:
                    (new AuthMiddleware()).handle();
                    if (permissions.stream().noneMatch(p -> Objects.equals("revenue_report.view", p.getSlug()))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    new RevenueAdminView();
                    break;
                case 13:
                    AuthController.logout();
                    break;
                default:
                    System.out.println("Chức năng không hợp lệ!");
                    break;
            }
            sc.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Lỗi nhập liệu! Vui lòng nhập lại.");
        }
    }
}
