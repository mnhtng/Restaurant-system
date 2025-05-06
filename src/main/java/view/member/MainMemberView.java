package main.java.view.member;

import main.java.controller.AuthController;
import main.java.util.Session;
import main.java.view.auth.AuthView;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MainMemberView {
    public MainMemberView() {
        while (Session.getInstance().isAuthenticated()) {
            show();
        }

        new AuthView();
    }

    public void show(){
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Giao diện thành viên ==========");
        System.out.println("1. Đặt bàn");
        System.out.println("2. Hủy đơn đặt bàn");
        System.out.println("3. Xem đơn đặt bàn đã đặt");
        System.out.println("4. Đăng xuất");
        System.out.print("Chọn chức năng: ");

        try {
            switch (sc.nextInt()) {
                case 1:
                    new OrderMemberView();
                    break;
                case 2:
                    new CancelOrderView();
                    break;
                case 3:
                    new TotalOrderView();
                    break;
                case 4:
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
