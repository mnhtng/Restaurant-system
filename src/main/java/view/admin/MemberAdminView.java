package main.java.view.admin;

import main.java.Application;
import main.java.component.DataTable;
import main.java.controller.admin.MemberAdminController;
import main.java.controller.admin.PermissionAdminController;
import main.java.middleware.AuthMiddleware;
import main.java.model.Member;
import main.java.model.Permission;
import main.java.model.enums.Gender;
import main.java.model.enums.MembershipTier;
import main.java.model.enums.Role;
import main.java.util.Session;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MemberAdminView {
    private boolean closeView = false;

    public MemberAdminView() {
        while (!closeView) {
            this.show();
        }
    }

    public void show() {
        List<Permission> permissions = PermissionAdminController.getPermissionsById(Session.getInstance().getCurrentUser().getId());

        Scanner sc = new Scanner(System.in);

        System.out.println("========== Quản lý thành viên ==========");
        System.out.println("1. Thêm thành viên");
        System.out.println("2. Cập nhật thành viên");
        System.out.println("3. Xóa thành viên");
        System.out.println("4. Hiển thị danh sách thành viên");
        System.out.println("5. Tìm kiếm thành viên");
        System.out.println("6. Quay lại");
        System.out.print("Chọn chức năng: ");

        try {
            switch (sc.nextInt()) {
                case 1:
                    (new AuthMiddleware()).handle();
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("member.create"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.addMemberView();
                    break;
                case 2:
                    (new AuthMiddleware()).handle();
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("member.update"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.updateMemberView();
                    break;
                case 3:
                    (new AuthMiddleware()).handle();
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("member.delete"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.deleteMemberView();
                    break;
                case 4:
                    (new AuthMiddleware()).handle();
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("member.view"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.listMemberView();
                    break;
                case 5:
                    (new AuthMiddleware()).handle();
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("member.view"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.searchMemberView();
                    break;
                case 6:
                    this.closeView = true;
                    new Application();
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

    public void addMemberView() {
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Thêm thành viên ==========");
        System.out.print("Tên: ");
        String name = sc.nextLine();
        if (name.length() < 2) {
            System.out.println("Tên phải có ít nhất 2 ký tự!");
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
            System.out.println("Lỗi nhập liệu! Vui lòng nhập lại.");
            return;
        }

        System.out.println("Bạn có muốn tạo thẻ thành viên không?");
        System.out.println("|-- 1. Có");
        System.out.println("|-- 2. Không");
        System.out.print("Chọn 1 lựa chọn: ");
        MembershipTier membershipTier = null;
        float loyaltyPoint = 0;
        LocalDateTime createCardAt = null;
        try {
            switch (sc.nextInt()) {
                case 1:
                    membershipTier = MembershipTier.BRONZE;
                    createCardAt = LocalDateTime.now();
                    break;
                case 2:
                    break;
                default:
                    System.out.println("Chức năng không hợp lệ!");
                    return;
            }
            sc.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Lỗi nhập liệu! Vui lòng nhập lại.");
            return;
        }

        if (name.isEmpty() || phone.isEmpty() || birthday.isEmpty() || email.isEmpty() || password.isEmpty()) {
            System.out.println("Vui lòng nhập đầy đủ thông tin!");
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
            member.setMembershipTier(membershipTier);
            member.setLoyaltyPoint(loyaltyPoint);
            member.setCreateCardAt(createCardAt);
            member.setDeleteAt(null);

            this.onAddMember(member);
        }
    }

    private void onAddMember(Member member) {
        if (MemberAdminController.addMember(member)) {
            System.out.println("Thêm thành viên thành công!");
        } else {
            System.out.println("Xảy ra lỗi khi thêm thành viên! Có thể là do tài khoản đã tồn tại hoặc lỗi kết nối cơ sở dữ liệu.");
        }
    }

    public void updateMemberView() {
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Cập nhật thành viên ==========");
        System.out.print("Nhập ID thành viên cần cập nhật: ");
        int memberId = sc.nextInt();
        sc.nextLine();
        Member updateMember = this.checkExistMember(memberId);
        if (updateMember == null) {
            System.out.println("Thành viên không tồn tại!");
            return;
        }

        System.out.println("Tên hiện tại: " + updateMember.getName());
        System.out.print("Nhập tên mới (Không muốn thay đổi, nhấn Enter): ");
        String name = sc.nextLine();
        if (!name.isEmpty() && name.length() < 2) {
            System.out.println("Tên phải có ít nhất 2 ký tự!");
            return;
        }

        System.out.println("Email hiện tại: " + updateMember.getEmail());
        System.out.print("Nhập email mới (Không muốn thay đổi, nhấn Enter): ");
        String email = sc.nextLine();
        if (!email.isEmpty() && !email.matches("^[a-zA-Z0-9]{3,}@[a-z.]+\\.[a-z]{2,}$")) {
            System.out.println("Email không hợp lệ!");
        }

        System.out.println("Mật khẩu hiện tại: " + updateMember.getPassword());
        System.out.print("Nhập mật khẩu mới (Không muốn thay đổi, nhấn Enter): ");
        String password = sc.nextLine();
        if (!password.isEmpty() && password.contains(" ")) {
            System.out.println("Mật khẩu không được chứa khoảng trắng!");
            return;
        }

        System.out.println("Số điện thoại hiện tại: " + updateMember.getPhone());
        System.out.print("Nhập số điện thoại mới (Không muốn thay đổi, nhấn Enter): ");
        String phone = sc.nextLine();
        if (!phone.isEmpty() && !phone.matches("0\\d{9,14}")) {
            System.out.println("Số điện thoại phải bắt đầu bằng số 0 và có độ dài từ 10 đến 15 ký tự!");
            return;
        }

        System.out.println("Ngày sinh hiện tại: " + updateMember.getBirthday().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        System.out.print("Nhập ngày sinh mới (Không muốn thay đổi, nhấn Enter): ");
        String birthday = sc.nextLine();
        String[] parts = birthday.split("-");
        if (!birthday.isEmpty()) {
            if (!birthday.matches("^[0-9]{2}-[0-9]{2}-[0-9]{4}$")) {
                System.out.println("Vui lòng nhập theo định dạng dd-MM-yyyy!");
                return;
            } else if (parts.length != 3 || Integer.parseInt(parts[1]) > 12 || Integer.parseInt(parts[0]) > 31
                    || Integer.parseInt(parts[2]) < 1900 || Integer.parseInt(parts[2]) > LocalDate.now().getYear()) {
                System.out.println("Ngày sinh không hợp lệ!");
                return;
            }
        }

        System.out.println("Giới tính hiện tại: " + (updateMember.getGender() == Gender.MALE ? "Nam" : "Nữ"));
        System.out.println("|-- 1. Nam");
        System.out.println("|-- 2. Nữ");
        System.out.print("Giới tính mới (Không muốn thay đổi, nhấn số bất kỳ ngoài khoảng): ");
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
                    break;
            }
            sc.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Lỗi nhập liệu! Vui lòng nhập lại.");
            return;
        }

        System.out.println("Thông tin thẻ thành viên hiện tại: ");
        System.out.println("Hạng thành viên: " + (updateMember.getMembershipTier() == null ? "null" : updateMember.getMembershipTier().toString().toUpperCase()));
        System.out.println("Điểm tích lũy hiện tại: " + new BigDecimal(updateMember.getLoyaltyPoint()).toPlainString());
        System.out.println("Ngày tạo thẻ: " + (updateMember.getCreateCardAt() == null ? "null" : updateMember.getCreateCardAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))));
        System.out.println("Bạn có muốn cập nhật thẻ thành viên không?");
        System.out.println("|-- 1. Có");
        System.out.println("|-- 2. Không");
        System.out.print("Chọn 1 lựa chọn: ");
        MembershipTier membershipTier = null;
        float loyaltyPoint = -1;
        LocalDateTime createCardAt = null;
        try {
            switch (sc.nextInt()) {
                case 1:
                    System.out.println("|-- 1. Đồng");
                    System.out.println("|-- 2. Bạc");
                    System.out.println("|-- 3. Vàng");
                    System.out.println("|-- 4. Bạch kim");
                    System.out.println("|-- 5. Kim cương");
                    System.out.print("Hạng thành viên mới: ");
                    try {
                        switch (sc.nextInt()) {
                            case 1:
                                membershipTier = MembershipTier.BRONZE;
                                break;
                            case 2:
                                membershipTier = MembershipTier.SILVER;
                                break;
                            case 3:
                                membershipTier = MembershipTier.GOLD;
                                break;
                            case 4:
                                membershipTier = MembershipTier.PLATINUM;
                                break;
                            case 5:
                                membershipTier = MembershipTier.DIAMOND;
                                break;
                            default:
                                System.out.println("Chức năng không hợp lệ!");
                                return;
                        }
                        sc.nextLine();
                    } catch (InputMismatchException e) {
                        System.out.println("Lỗi nhập liệu! Vui lòng nhập lại.");
                        return;
                    }

                    System.out.println("Bạn có muốn cập nhật điểm tích lũy không?");
                    System.out.println("|-- 1. Có");
                    System.out.println("|-- 2. Không");
                    System.out.print("Chọn 1 lựa chọn: ");
                    try {
                        switch (sc.nextInt()) {
                            case 1:
                                try {
                                    System.out.print("Nhập điểm tích lũy mới: ");
                                    loyaltyPoint = sc.nextFloat();
                                    sc.nextLine();
                                    if (loyaltyPoint < 0) {
                                        System.out.println("Điểm tích lũy không hợp lệ!");
                                        return;
                                    }
                                } catch (InputMismatchException e) {
                                    System.out.println("Lỗi nhập liệu! Vui lòng nhập lại.");
                                    return;
                                }
                                break;
                            case 2:
                                sc.nextLine();
                                break;
                            default:
                                System.out.println("Chức năng không hợp lệ!");
                                return;
                        }
                    } catch (Exception e) {
                        System.out.println("Lỗi nhập liệu! Vui lòng nhập lại.");
                        return;
                    }

                    System.out.println("Bạn có muốn cập nhật ngày tạo thẻ không?");
                    System.out.println("|-- 1. Có");
                    System.out.println("|-- 2. Không");
                    System.out.print("Chọn 1 lựa chọn: ");
                    try {
                        switch (sc.nextInt()) {
                            case 1:
                                createCardAt = LocalDateTime.now();
                                break;
                            case 2:
                                break;
                            default:
                                System.out.println("Chức năng không hợp lệ!");
                                return;
                        }
                        sc.nextLine();
                    } catch (InputMismatchException e) {
                        System.out.println("Lỗi nhập liệu! Vui lòng nhập lại.");
                        return;
                    }
                    break;
                case 2:
                    break;
                default:
                    System.out.println("Chức năng không hợp lệ!");
                    return;
            }
            sc.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Lỗi nhập liệu! Vui lòng nhập lại.");
            return;
        }

        Member member = new Member();
        member.setId(memberId);
        member.setName(name.trim().isEmpty() ? updateMember.getName().trim() : name.trim());
        member.setEmail(email.trim().isEmpty() ? updateMember.getEmail() : email.trim());
        member.setPassword(password.trim().isEmpty() ? updateMember.getPassword() : password.trim());
        member.setPhone(phone.trim().isEmpty() ? updateMember.getPhone().trim() : phone.trim());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        member.setBirthday(birthday.trim().isEmpty() ? updateMember.getBirthday() : LocalDate.parse(birthday.trim(), formatter));
        member.setGender(gender == null ? updateMember.getGender() : gender);
        member.setRole(updateMember.getRole());
        member.setMembershipTier(membershipTier == null ? updateMember.getMembershipTier() : membershipTier);
        member.setLoyaltyPoint(loyaltyPoint == -1 ? updateMember.getLoyaltyPoint() : loyaltyPoint);
        member.setCreateCardAt(createCardAt == null ? updateMember.getCreateCardAt() : createCardAt);
        member.setDeleteAt(null);

        this.onUpdateMember(member);
    }

    private Member checkExistMember(int memberId) {
        return MemberAdminController.getAllMembers()
                .stream()
                .filter(member -> member.getId() == memberId)
                .findFirst()
                .orElse(null);
    }

    private void onUpdateMember(Member member) {
        if (MemberAdminController.updateMember(member)) {
            System.out.println("Cập nhật thành viên thành công!");
        } else {
            System.out.println("Xảy ra lỗi khi cập nhật thành viên!");
        }
    }

    public void deleteMemberView() {
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Xóa thành viên ==========");
        System.out.print("Nhập ID thành viên cần xóa: ");
        int memberId = sc.nextInt();
        sc.nextLine();
        Member deleteMember = this.checkExistMember(memberId);
        if (deleteMember == null) {
            System.out.println("Thành viên không tồn tại!");
            return;
        }

        System.out.println("Bạn chắc chắn muốn xóa thành viên này chứ?");
        System.out.println("|-- 1. Chắc chắn");
        System.out.println("|-- 2. Không");
        System.out.print("Chọn 1 lựa chọn: ");
        try {
            switch (sc.nextInt()) {
                case 1:
                    this.onDeleteMember(memberId);
                    break;
                case 2:
                    break;
                default:
                    System.out.println("Chức năng không hợp lệ!");
                    return;
            }
            sc.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Lỗi nhập liệu! Vui lòng nhập lại.");
        }
    }

    private void onDeleteMember(int memberId) {
        if (MemberAdminController.deleteMember(memberId)) {
            System.out.println("Xóa thành viên thành công!");
        } else {
            System.out.println("Xảy ra lỗi khi xóa thành viên!");
        }
    }

    public void listMemberView() {
        System.out.println("========== Danh sách thành viên ==========");

        List<Member> members = MemberAdminController.getAllMembers();
        this.displayMemberList(members);
    }

    private void displayMemberList(List<Member> members) {
        String[] headers = {"ID", "Tên", "Email", "Mật khẩu", "Số điện thoại", "Ngày sinh", "Giới tính", "Hạng thành viên", "Điểm tích lũy", "Ngày tạo thẻ", "Vai trò", "Quyền hạn"};

        DataTable.printMemberTable(headers, members);
    }

    public void searchMemberView() {
        boolean isClose = false;

        while (!isClose) {
            Scanner sc = new Scanner(System.in);

            System.out.println("========== Tìm kiếm thành viên ==========");
            System.out.println("0. Quay lại (Nhập '0' để quay lại)");
            System.out.println("Phạm vi tìm kiếm: tên, email, số điện thoại, ngày sinh, giới tính");
            System.out.print("Nhập từ khóa tìm kiếm: ");
            String query = sc.nextLine();

            if (query.isEmpty()) {
                System.out.println("Từ khóa tìm kiếm không được để trống!");
                return;
            }

            if (query.equals("\'0\'")) {
                isClose = true;
            } else {
                List<Member> matchMembers = MemberAdminController.searchMember(query);

                this.displayMemberList(matchMembers);
            }
        }
    }
}
