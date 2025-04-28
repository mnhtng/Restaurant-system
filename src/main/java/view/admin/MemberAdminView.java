package main.java.view.admin;

import main.java.component.DataTable;
import main.java.controller.admin.MemberAdminController;
import main.java.model.Member;
import main.java.model.Permission;
import main.java.model.enums.Gender;
import main.java.model.enums.MembershipTier;
import main.java.model.enums.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * @author MnhTng
 * @Package main.java.view.admin
 * @date 4/24/2025 2:53 PM
 * @Copyright tùng
 */

public class MemberAdminView {
    private boolean closeView = false;

    public MemberAdminView() {
        while (!closeView) {
            show();
        }
    }

    public void show() {
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Quản lý thành viên ==========");
        System.out.println("1. Thêm thành viên");
        System.out.println("2. Cập nhật thành viên");
        System.out.println("3. Xóa thành viên");
        System.out.println("4. Xem danh sách thành viên");
        System.out.println("5. Tìm kiếm thành viên");
        System.out.println("6. Quay lại");
        System.out.print("Chọn chức năng: ");

        try {
            switch (sc.nextInt()) {
                case 1:
                    this.addMemberView();
                    break;
                case 2:
                    this.updateMemberView();
                    break;
                case 3:
                    this.deleteMemberView();
                    break;
                case 4:
                    this.listMemberView();
                    break;
                case 5:
                    this.searchMemberView();
                    break;
                case 6:
                    this.closeView = true;
                    new MainAdminView();
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

        System.out.println("|-- 1. Thành viên");
        System.out.println("|-- 2. Quản lý");
        System.out.println("|-- 3. Nhân viên kho");
        System.out.println("|-- 4. Nhân viên phục vụ");
        System.out.print("Vai trò: ");
        Role role = null;
        try {
            switch (sc.nextInt()) {
                case 1:
                    role = Role.MEMBER;
                    break;
                case 2:
                    role = Role.MANAGER;
                    break;
                case 3:
                    role = Role.INVENTORY_CLERK;
                    break;
                case 4:
                    role = Role.SERVICE_CLERK;
                    break;
                default:
                    System.out.println("Vai trò không hợp lệ!");
                    return;
            }
            sc.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Lỗi nhập liệu! Vui lòng nhập lại.");
            return;
        }

        List<Permission> permissions = MemberAdminController.getAllPermissions();
        this.loadPermissions(permissions);
        String matchPermission = this.getMatchPermission(role, permissions);
        System.out.println("Quyền hạn mặc định cho vai trò " + role.toString().toUpperCase() + " là: [" + matchPermission + "]");
        System.out.println("Bạn có muốn chỉnh sửa không?");
        System.out.println("|-- 1. Có");
        System.out.println("|-- 2. Không");
        System.out.print("Chọn 1 lựa chọn: ");
        String permission = matchPermission;
        try {
            switch (sc.nextInt()) {
                case 1:
                    System.out.print("Quyền hạn mới: ");
                    permission = sc.nextLine();
                    if (permission.trim().contains(" ") || (role != Role.MEMBER && permission.isEmpty()) || (!permission.isEmpty() && !permission.matches("\\d+(,\\d+)*"))) {
                        System.out.println("Quyền hạn không hợp lệ!");
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

            String[] permissionParts = permission.split(",");

            this.onAddMember(member, permissionParts);
        }
    }

    private void loadPermissions(List<Permission> permissions) {
        System.out.println("Quyền hạn: ");
        for (int i = 0; i < permissions.size(); i++) {
            System.out.println("|-- " + (i + 1) + ". " + permissions.get(i).getName());
        }
    }

    private String getMatchPermission(Role role, List<Permission> permissions) {
        StringBuffer permissionList = new StringBuffer();

        switch (role) {
            case MANAGER:
                for (int i = 0; i < permissions.size(); i++) {
                    permissionList.append(i + 1);

                    if (i < permissions.size() - 1) {
                        permissionList.append(",");
                    }
                }
                break;
            case INVENTORY_CLERK:
                for (int i = 1; i <= 15; i++) {
                    permissionList.append(i);

                    if (i < 15) {
                        permissionList.append(",");
                    }
                }
                break;
            case SERVICE_CLERK:
                for (int i = 16; i <= 32; i++) {
                    permissionList.append(i);

                    if (i < 32) {
                        permissionList.append(",");
                    }
                }
                break;
            default:
                break;
        }

        return permissionList.toString();
    }

    private void onAddMember(Member member, String[] permissionParts) {
        if (MemberAdminController.addMember(member, permissionParts)) {
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
        List<Permission> updatePermissions = this.checkExistPermission(memberId);
        if (updateMember == null) {
            System.out.println("Thành viên không tồn tại!");
            return;
        }

        System.out.println("Tên hiện tại: " + updateMember.getName());
        System.out.print("Nhập tên mới (Không muốn thay đổi, nhấn 'Enter'): ");
        String name = sc.nextLine();
        if (!name.isEmpty() && name.length() < 2) {
            System.out.println("Tên phải có ít nhất 2 ký tự!");
            return;
        }

        System.out.println("Email hiện tại: " + updateMember.getEmail());
        System.out.print("Nhập email mới (Không muốn thay đổi, nhấn 'Enter'): ");
        String email = sc.nextLine();
        if (!email.isEmpty() && !email.matches("^[a-zA-Z0-9]{3,}@[a-z.]+\\.[a-z]{2,}$")) {
            System.out.println("Email không hợp lệ!");
        }

        System.out.println("Mật khẩu hiện tại: " + updateMember.getPassword());
        System.out.print("Nhập mật khẩu mới (Không muốn thay đổi, nhấn 'Enter'): ");
        String password = sc.nextLine();
        if (!password.isEmpty() && password.contains(" ")) {
            System.out.println("Mật khẩu không được chứa khoảng trắng!");
            return;
        }

        System.out.println("Số điện thoại hiện tại: " + updateMember.getPhone());
        System.out.print("Nhập số điện thoại mới (Không muốn thay đổi, nhấn 'Enter'): ");
        String phone = sc.nextLine();
        if (!phone.isEmpty() && !phone.matches("0\\d{9,14}")) {
            System.out.println("Số điện thoại phải bắt đầu bằng số 0 và có độ dài từ 10 đến 15 ký tự!");
            return;
        }

        System.out.println("Ngày sinh hiện tại: " + updateMember.getBirthday().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        System.out.print("Nhập ngày sinh mới (Không muốn thay đổi, nhấn 'Enter'): ");
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

        System.out.println("Vai trò hiện tại: " + (updateMember.getRole() == Role.MANAGER ?
                "Quản lý" :
                updateMember.getRole() == Role.INVENTORY_CLERK ?
                        "Nhân viên kho" :
                        updateMember.getRole() == Role.SERVICE_CLERK ?
                                "Nhân viên phục vụ" :
                                "Thành viên"));
        System.out.println("|-- 1. Thành viên");
        System.out.println("|-- 2. Quản lý");
        System.out.println("|-- 3. Nhân viên kho");
        System.out.println("|-- 4. Nhân viên phục vụ");
        System.out.print("Chọn 1 lựa chọn (Không muốn thay đổi, nhấn số bất kỳ ngoài khoảng): ");
        Role role = null;
        try {
            switch (sc.nextInt()) {
                case 1:
                    role = Role.MEMBER;
                    break;
                case 2:
                    role = Role.MANAGER;
                    break;
                case 3:
                    role = Role.INVENTORY_CLERK;
                    break;
                case 4:
                    role = Role.SERVICE_CLERK;
                    break;
                default:
                    break;
            }
            sc.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Lỗi nhập liệu! Vui lòng nhập lại.");
            return;
        }

        List<Permission> permissions = MemberAdminController.getAllPermissions();
        this.loadPermissions(permissions);
        System.out.println("Quyền hạn hiện tại: [" + updatePermissions
                .stream()
                .map(permission -> String.valueOf(permission.getId()))
                .collect(Collectors.joining(",")) + "]");
        System.out.print("Quyền hạn mới (Không muốn thay đổi, nhấn 'Enter' - Thu hồi mọi quyền hạn, nhấn '0'): ");
        String permission = sc.nextLine();
        if (!permission.isEmpty() && (permission.trim().contains(" ") || (role != Role.MEMBER && permission.isEmpty()) || (!permission.isEmpty() && !permission.matches("\\d+(,\\d+)*")))) {
            System.out.println("Quyền hạn không hợp lệ!");
            return;
        }

        System.out.println("Thông tin thẻ thành viên hiện tại: ");
        System.out.println("Hạng thành viên: " + (updateMember.getMembershipTier() == null ? "null" : updateMember.getMembershipTier().toString().toUpperCase()));
        System.out.println("Điểm tích lũy hiện tại: " + updateMember.getLoyaltyPoint());
        System.out.println("Ngày tạo thẻ: " + (updateMember.getCreateCardAt() == null ? "null" : updateMember.getCreateCardAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))));
        System.out.println("Bạn có muốn cập nhật thẻ thành viên không?");
        System.out.println("|-- 1. Có");
        System.out.println("|-- 2. Không");
        System.out.print("Chọn 1 lựa chọn: ");
        MembershipTier membershipTier = null;
        float loyaltyPoint = 0;
        LocalDateTime createCardAt = null;
        try {
            switch (sc.nextInt()) {
                case 1:
                    System.out.println("|-- 1. Đồng");
                    System.out.println("|-- 2. Bạc");
                    System.out.println("|-- 3. Vàng");
                    System.out.println("|-- 4. Bạch kim");
                    System.out.println("|-- 5. Kim cương");
                    System.out.println("Hạng thành viên mới: ");
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
        member.setRole(role == null ? updateMember.getRole() : role);
        member.setMembershipTier(membershipTier == null ? updateMember.getMembershipTier() : membershipTier);
        member.setLoyaltyPoint(loyaltyPoint == 0 ? updateMember.getLoyaltyPoint() : loyaltyPoint);
        member.setCreateCardAt(createCardAt == null ? updateMember.getCreateCardAt() : createCardAt);
        member.setDeleteAt(null);

        String[] permissionParts = permission.trim().isEmpty() ?
                updatePermissions.stream()
                        .map(p -> String.valueOf(p.getId()))
                        .toArray(String[]::new) :
                permission.split(",");

        this.onUpdateMember(member, permissionParts);
    }

    private Member checkExistMember(int memberId) {
        return MemberAdminController.getAllMembers()
                .stream()
                .filter(member -> member.getId() == memberId)
                .findFirst()
                .orElse(null);
    }

    private List<Permission> checkExistPermission(int memberId) {
        return MemberAdminController.getPermissionsById(memberId);
    }

    private void onUpdateMember(Member member, String[] permissionParts) {
        if (MemberAdminController.updateMember(member, permissionParts)) {
            System.out.println("Cập nhật thành viên thành công!");
        } else {
            System.out.println("Xảy ra lỗi khi cập nhật thành viên!");
        }
    }

    public void deleteMemberView() {
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Delete Member ==========");
        System.out.print("Enter member ID to delete: ");
        int memberId = sc.nextInt();
        sc.nextLine();
        Member updateMember = this.checkExistMember(memberId);
        if (updateMember == null) {
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
        } catch (InputMismatchException e) {
            System.out.println("Lỗi nhập liệu! Vui lòng nhập lại.");
            return;
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
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Danh sách thành viên ==========");

        List<Member> members = MemberAdminController.getAllMembers();
        this.displayListMembers(members);
    }

    private void displayListMembers(List<Member> members) {
        String[] headers = {"ID", "Tên", "Email", "Mật khẩu", "Số điện thoại", "Ngày sinh", "Giới tính", "Hạng thành viên", "Điểm tích lũy", "Ngày tạo thẻ", "Ngày xóa thành viên", "Vai trò", "Quyền hạn"};

        DataTable.printMemberTable(headers, members);
    }

    public void searchMemberView() {
        boolean isClose = false;

        while (!isClose) {
            Scanner sc = new Scanner(System.in);
            System.out.println("========== Search Member ==========");
            System.out.println("0. Quay lại (Nhập '0' để quay lại)");
            System.out.println("Phạm vi tìm kiếm: tên, email, số điện thoại, ngày sinh, giới tính, vai trò");
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

                String[] headers = {"ID", "Tên", "Email", "Mật khẩu", "Số điện thoại", "Ngày sinh", "Giới tính", "Hạng thành viên", "Điểm tích lũy", "Ngày tạo thẻ", "Ngày xóa thành viên", "Vai trò", "Quyền hạn"};
                DataTable.printMemberTable(headers, matchMembers);
            }
        }
    }
}
