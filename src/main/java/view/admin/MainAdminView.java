package main.java.view.admin;

import main.java.controller.AuthController;
import main.java.util.Session;
import main.java.view.auth.AuthView;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @author MnhTng
 * @Package main.java.view.admin
 * @date 4/23/2025 10:35 PM
 * @Copyright tùng
 */

public class MainAdminView {
    public MainAdminView() {
        while (Session.getInstance().isAuthenticated()) {
            show();
        }

        new AuthView();
    }

    public void show() {
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
                    new MemberAdminView();
                    break;
                case 2:
                    new StaffAdminView();
                    break;
                case 3:
                    new MemberCardAdminView();
                    break;
                case 4:
                    new SupplierAdminView();
                    break;
                case 5:
                    // new IngredientView();
                    break;
                case 6:
                    // new WarehouseView();
                    break;
                case 7:
                    // new InventoryInvoiceView();
                    break;
                case 8:
                    // new DishView();
                    break;
                case 9:
                    // new ComboView();
                    break;
                case 10:
                    // new TableView();
                    break;
                case 11:
                    // new OrderInvoiceView();
                    break;
                case 12:
                    // new RevenueView();
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
