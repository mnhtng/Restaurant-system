package main.java.view.auth;

import main.java.Application;
import main.java.controller.AuthController;

import java.util.Scanner;

public class LoginView {
    public LoginView() {
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Đăng nhập ==========");
        System.out.print("Email: ");
        String email = sc.nextLine();
        if (!email.matches("^[a-zA-Z0-9]{3,}@[a-z.]+\\.[a-z]{2,}$")) {
            System.out.println("Email không hợp lệ!");
            return;
        }

        System.out.print("Mật khẩu: ");
        String password = sc.nextLine();
        if (password.contains(" ")) {
            System.out.println("Mật khẩu không được chứa khoảng trắng!");
            return;
        }

        // Validate
        if (email.isEmpty() || password.isEmpty()) {
            System.out.println("Vui lòng nhập đầy đủ thông tin!");
        } else {
            onLogin(email.trim(), password.trim());
        }
    }

    private void onLogin(String email, String password) {
        if (AuthController.login(email, password)) {
            System.out.println("Đăng nhập thành công!");
            new Application();
        } else {
            System.out.println("Đăng nhập thất bại! Vui lòng kiểm tra lại thông tin đăng nhập.");
        }
    }
}
