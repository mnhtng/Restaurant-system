package main.java.view.auth;

import main.java.controller.AuthController;
import main.java.model.Member;
import main.java.model.enums.Gender;
import main.java.model.enums.Role;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @author MnhTng
 * @Package main.java.view.ui
 * @date 4/17/2025 6:06 PM
 * @Copyright tùng
 */

public class RegisterView {
    public RegisterView() {
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Đăng ký ==========");
        System.out.print("Tên: ");
        String name = sc.nextLine();
        if (name.length() < 2) {
            System.out.println("Tên phải có ít nhất 2 ký tự!");
            return;
        }

        System.out.print("Số điện thoại: ");
        String phone = sc.nextLine();
        if (!phone.matches("0\\d{9,14}")) {
            System.out.println("Số điện thoại phải bắt đầu bằng số 0 và có độ dài từ 10 đến 15 ký tự!");
            return;
        }

        System.out.print("Ngày sinh: ");
        String birthday = sc.nextLine();
        String[] parts = birthday.split("-");
        if (!birthday.matches("^[0-9]{2}-[0-9]{2}-[0-9]{4}$")) {
            System.out.println("Vui lòng nhập theo định dạng dd-MM-yyyy!");
            return;
        } else if (parts.length != 3 || Integer.parseInt(parts[1]) > 12 || Integer.parseInt(parts[0]) > 31
                || Integer.parseInt(parts[2]) < 1900 || Integer.parseInt(parts[2]) > LocalDate.now().getYear()) {
            System.out.println("Ngày sinh không hợp lệ!");
            return;
        }

        System.out.println("|-- 1. Nam");
        System.out.println("|-- 2. Nữ");
        System.out.print("Giới tính: ");
        Gender gender = null;
        try {
            switch (sc.nextInt()) {
                case 1:
                    gender = Gender.MALE;
                    break;
                case 2:
                    gender = Gender.FEMALE;
                    break;
                default:
                    System.out.println("Giới tính không hợp lệ!");
                    return;
            }
            sc.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Lỗi đầu vào. Vui lòng nhập một số.");
            return;
        }

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

        System.out.print("Xác nhận mật khẩu: ");
        String confirmPassword = sc.nextLine();
        if (!password.equals(confirmPassword)) {
            System.out.println("Mật khẩu không khớp!");
            return;
        }

        if (name.isEmpty() || phone.isEmpty() || birthday.isEmpty() || email.isEmpty() || password.isEmpty()) {
            System.out.println("Tất cả các trường không được để trống!");
        } else {
            Member member = new Member();
            member.setName(name.trim());
            member.setEmail(email);
            member.setPassword(password);
            member.setPhone(phone.trim());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            member.setBirthday(LocalDate.parse(birthday.trim(), formatter));
            member.setGender(gender);
            member.setRole(Role.MEMBER);
            member.setMembershipTier(null);
            member.setLoyaltyPoint(0);
            member.setCreateCardAt(null);
            member.setDeleteAt(null);

            onRegister(member);
        }
    }

    private void onRegister(Member member) {
        if (AuthController.signup(member)) {
            System.out.println("Đăng ký thành công!");
            new LoginView();
        } else {
            System.out.println("Đăng ký thất bại: Email đã tồn tại hoặc lỗi khác!");
        }
    }
}
