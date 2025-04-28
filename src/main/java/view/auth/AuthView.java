package main.java.view.auth;

import main.java.Application;
import main.java.util.Session;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @author MnhTng
 * @Package main.java.view.auth
 * @date 4/23/2025 10:15 PM
 * @Copyright tùng
 */

public class AuthView {
    public AuthView() {
        while (!Session.getInstance().isAuthenticated()) {
            show();
        }

        new Application();
    }

    public void show() {
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Chào mừng tới nhà hàng của chúng tôi ==========");
        System.out.println("1. Đăng nhập");
        System.out.println("2. Đăng ký");
        System.out.print("Chọn một tùy chọn: ");

        try {
            switch (sc.nextInt()) {
                case 1:
                    new LoginView();
                    break;
                case 2:
                    new RegisterView();
                    break;
                default:
                    System.out.println("Tùy chọn không hợp lệ. Vui lòng thử lại.");
                    break;
            }
            sc.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Lỗi đầu vào. Vui lòng nhập một số.");
        }
    }
}
