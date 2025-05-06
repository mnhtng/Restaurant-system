package main.java.view.admin;

import main.java.Application;
import main.java.component.DataTable;
import main.java.controller.admin.StaffAdminController;
import main.java.controller.admin.PermissionAdminController;
import main.java.middleware.AuthMiddleware;
import main.java.model.Member;
import main.java.model.Permission;
import main.java.model.Staff;
import main.java.model.enums.Gender;
import main.java.model.enums.MembershipTier;
import main.java.model.enums.Role;
import main.java.util.Session;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Collectors;

public class StaffAdminView {
    private boolean closeView = false;

    public StaffAdminView() {
        while (!closeView) {
            this.show();
        }
    }

    public void show() {
        List<Permission> permissions = PermissionAdminController.getPermissionsById(Session.getInstance().getCurrentUser().getId());

        Scanner sc = new Scanner(System.in);

        System.out.println("========== Quản lý nhân viên ==========");
        System.out.println("1. Thêm nhân viên");
        System.out.println("2. Cập nhật thông tin nhân viên");
        System.out.println("3. Xóa nhân viên");
        System.out.println("4. Hiển thị danh sách nhân viên");
        System.out.println("5. Tìm kiếm nhân viên");
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
                    this.addStaffView();
                    break;
                case 2:
                    (new AuthMiddleware()).handle();
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("member.update"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.updateStaffView();
                    break;
                case 3:
                    (new AuthMiddleware()).handle();
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("member.delete"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.deleteStaffView();
                    break;
                case 4:
                    (new AuthMiddleware()).handle();
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("member.view"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.listStaffView();
                    break;
                case 5:
                    (new AuthMiddleware()).handle();
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("member.view"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.searchStaffView();
                    break;
                case 6:
                    closeView = true;
                    new Application();
                    break;
                default:
                    System.out.println("Chức năng không hợp lệ!");
                    break;
            }
        } catch (InputMismatchException e) {
            System.out.println("Lỗi nhập liệu! Vui lòng nhập lại.");
        }
    }

    public void addStaffView() {
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Thêm nhân viên ==========");
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

        System.out.println("|-- 1. Quản lý");
        System.out.println("|-- 2. Nhân viên kho");
        System.out.println("|-- 3. Nhân viên phục vụ");
        System.out.print("Vai trò: ");
        Role role = null;
        try {
            switch (sc.nextInt()) {
                case 1:
                    role = Role.MANAGER;
                    break;
                case 2:
                    role = Role.INVENTORY_CLERK;
                    break;
                case 3:
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

        List<Permission> permissions = PermissionAdminController.getAllPermissions();
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
                    sc.nextLine();
                    System.out.print("Quyền hạn mới: ");
                    permission = sc.nextLine();
                    if (permission.trim().contains(" ") || permission.isEmpty() || (!permission.isEmpty() && !permission.matches("\\d+(,\\d+)*"))) {
                        System.out.println("Quyền hạn không hợp lệ!");
                        return;
                    } else {
                        String[] permissionParts = permission.split(",");
                        for (String part : permissionParts) {
                            if (!permissions.stream().anyMatch(p -> p.getId() == Integer.parseInt(part))) {
                                System.out.println("Quyền hạn không hợp lệ!");
                                return;
                            }
                        }
                    }
                    break;
                case 2:
                    sc.nextLine();
                    break;
                default:
                    System.out.println("Chức năng không hợp lệ!");
                    return;
            }
        } catch (InputMismatchException e) {
            System.out.println("Lỗi nhập liệu! Vui lòng nhập lại.");
            return;
        }

        System.out.print("Lương: ");
        float salary = sc.nextFloat();
        sc.nextLine();
        if (salary < 0) {
            System.out.println("Lương không hợp lệ!");
            return;
        }

        System.out.println("Bạn có muốn tạo thẻ thành viên không?");
        System.out.println("|-- 1. Có");
        System.out.println("|-- 2. Không");
        System.out.print("Chọn 1 lựa chọn: ");
        MembershipTier staffshipTier = null;
        float loyaltyPoint = 0;
        LocalDateTime createCardAt = null;
        try {
            switch (sc.nextInt()) {
                case 1:
                    staffshipTier = MembershipTier.BRONZE;
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
            Staff staff = new Staff();
            Member member = new Member();

            member.setName(name.trim());
            member.setEmail(email);
            member.setPassword(password);
            member.setPhone(phone.trim());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            member.setBirthday(LocalDate.parse(birthday.trim(), formatter));
            member.setGender(gender);
            member.setRole(role);
            member.setMembershipTier(staffshipTier);
            member.setLoyaltyPoint(loyaltyPoint);
            member.setCreateCardAt(createCardAt);
            member.setDeleteAt(null);

            staff.setSalary(salary);
            staff.setMember(member);

            String[] permissionParts = permission.split(",");

            this.onAddStaff(staff, permissionParts);
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

    private void onAddStaff(Staff staff, String[] permissionParts) {
        if (StaffAdminController.addStaff(staff, permissionParts)) {
            System.out.println("Thêm nhân viên thành công!");
        } else {
            System.out.println("Thêm nhân viên thất bại!");
        }
    }

    public void updateStaffView() {
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Cập nhật nhân viên ==========");
        System.out.print("Nhập ID nhân viên cần cập nhật: ");
        int staffId = sc.nextInt();
        sc.nextLine();
        Staff updateStaff = this.checkExistStaff(staffId);
        List<Permission> updatePermissions = this.checkExistPermission(staffId);
        if (updateStaff == null) {
            System.out.println("Nhân viên không tồn tại!");
            return;
        }

        System.out.println("Tên hiện tại: " + updateStaff.getMember().getName());
        System.out.print("Nhập tên mới (Không muốn thay đổi, nhấn Enter): ");
        String name = sc.nextLine();
        if (!name.isEmpty() && name.length() < 2) {
            System.out.println("Tên phải có ít nhất 2 ký tự!");
            return;
        }

        System.out.println("Email hiện tại: " + updateStaff.getMember().getEmail());
        System.out.print("Nhập email mới (Không muốn thay đổi, nhấn Enter): ");
        String email = sc.nextLine();
        if (!email.isEmpty() && !email.matches("^[a-zA-Z0-9]{3,}@[a-z.]+\\.[a-z]{2,}$")) {
            System.out.println("Email không hợp lệ!");
        }

        System.out.println("Mật khẩu hiện tại: " + updateStaff.getMember().getPassword());
        System.out.print("Nhập mật khẩu mới (Không muốn thay đổi, nhấn Enter): ");
        String password = sc.nextLine();
        if (!password.isEmpty() && password.contains(" ")) {
            System.out.println("Mật khẩu không được chứa khoảng trắng!");
            return;
        }

        System.out.println("Số điện thoại hiện tại: " + updateStaff.getMember().getPhone());
        System.out.print("Nhập số điện thoại mới (Không muốn thay đổi, nhấn Enter): ");
        String phone = sc.nextLine();
        if (!phone.isEmpty() && !phone.matches("0\\d{9,14}")) {
            System.out.println("Số điện thoại phải bắt đầu bằng số 0 và có độ dài từ 10 đến 15 ký tự!");
            return;
        }

        System.out.println("Ngày sinh hiện tại: " + updateStaff.getMember().getBirthday().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
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

        System.out.println("Giới tính hiện tại: " + (updateStaff.getMember().getGender() == Gender.MALE ? "Nam" : "Nữ"));
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

        System.out.println("Vai trò hiện tại: " + (updateStaff.getMember().getRole() == Role.MANAGER ?
                "Quản lý" :
                updateStaff.getMember().getRole() == Role.INVENTORY_CLERK ?
                        "Nhân viên kho" :
                        updateStaff.getMember().getRole() == Role.SERVICE_CLERK ?
                                "Nhân viên phục vụ" :
                                "Thành viên"));
        System.out.println("|-- 1. Quản lý");
        System.out.println("|-- 2. Nhân viên kho");
        System.out.println("|-- 3. Nhân viên phục vụ");
        System.out.print("Chọn 1 lựa chọn (Không muốn thay đổi, nhấn số bất kỳ ngoài khoảng): ");
        Role role = null;
        try {
            switch (sc.nextInt()) {
                case 1:
                    role = Role.MANAGER;
                    break;
                case 2:
                    role = Role.INVENTORY_CLERK;
                    break;
                case 3:
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

        List<Permission> permissions = PermissionAdminController.getAllPermissions();
        this.loadPermissions(permissions);
        System.out.println("Quyền hạn hiện tại: [" + updatePermissions
                .stream()
                .map(p -> String.valueOf(p.getId()))
                .collect(Collectors.joining(",")) + "]");
        System.out.print("Quyền hạn mới (Không muốn thay đổi, nhấn Enter - Thu hồi mọi quyền hạn, nhấn 0): ");
        String permission = sc.nextLine();
        if (!permission.isEmpty() && (permission.trim().contains(" ") || !permission.matches("\\d+(,\\d+)*"))) {
            System.out.println("Quyền hạn không hợp lệ!");
            return;
        }

        System.out.println("Lương hiện tại: " + NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(updateStaff.getSalary()));
        System.out.print("Nhập lương mới (Không muốn thay đổi, nhấn -1): ");
        float salary = sc.nextFloat();
        sc.nextLine();
        if (salary < 0 && salary != -1) {
            System.out.println("Lương không hợp lệ!");
            return;
        }

        System.out.println("Thông tin thẻ thành viên hiện tại: ");
        System.out.println("Hạng thành viên: " + (updateStaff.getMember().getMembershipTier() == null ? "null" : updateStaff.getMember().getMembershipTier().toString().toUpperCase()));
        System.out.println("Điểm tích lũy hiện tại: " + new BigDecimal(updateStaff.getMember().getLoyaltyPoint()).toPlainString());
        System.out.println("Ngày tạo thẻ: " + (updateStaff.getMember().getCreateCardAt() == null ? "null" : updateStaff.getMember().getCreateCardAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))));
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
                    System.out.print("Hạng thành viên mới (Không muốn thay đổi, nhấn số bất kỳ ngoài khoảng): ");
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
                                break;
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
                                    sc.nextLine();
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
        member.setId(staffId);
        member.setName(name.trim().isEmpty() ? updateStaff.getMember().getName().trim() : name.trim());
        member.setEmail(email.trim().isEmpty() ? updateStaff.getMember().getEmail() : email.trim());
        member.setPassword(password.trim().isEmpty() ? updateStaff.getMember().getPassword() : password.trim());
        member.setPhone(phone.trim().isEmpty() ? updateStaff.getMember().getPhone().trim() : phone.trim());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        member.setBirthday(birthday.trim().isEmpty() ? updateStaff.getMember().getBirthday() : LocalDate.parse(birthday.trim(), formatter));
        member.setGender(gender == null ? updateStaff.getMember().getGender() : gender);
        member.setRole(role == null ? updateStaff.getMember().getRole() : role);
        member.setMembershipTier(membershipTier == null ? updateStaff.getMember().getMembershipTier() : membershipTier);
        member.setLoyaltyPoint(loyaltyPoint == -1 ? updateStaff.getMember().getLoyaltyPoint() : loyaltyPoint);
        member.setCreateCardAt(createCardAt == null ? updateStaff.getMember().getCreateCardAt() : createCardAt);
        member.setDeleteAt(null);

        Staff staff = new Staff();
        staff.setId(staffId);
        staff.setSalary((salary == -1) ? updateStaff.getSalary() : salary);
        staff.setMember(member);

        String[] permissionParts = permission.trim().isEmpty() ?
                updatePermissions.stream()
                        .map(p -> String.valueOf(p.getId()))
                        .toArray(String[]::new) :
                permission.split(",");

        this.onUpdateStaff(staff, permissionParts);
    }

    private Staff checkExistStaff(int staffId) {
        return StaffAdminController.getAllStaffs()
                .stream()
                .filter(staff -> staff.getId() == staffId)
                .findFirst()
                .orElse(null);
    }

    private List<Permission> checkExistPermission(int staffId) {
        return PermissionAdminController.getPermissionsById(staffId);
    }

    private void onUpdateStaff(Staff staff, String[] permissionParts) {
        if (StaffAdminController.updateStaff(staff, permissionParts)) {
            System.out.println("Cập nhật nhân viên thành công!");
        } else {
            System.out.println("Cập nhật nhân viên thất bại!");
        }
    }

    public void deleteStaffView() {
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Xóa nhân viên ==========");
        System.out.print("Nhập ID nhân viên cần xóa: ");
        int staffId = sc.nextInt();
        sc.nextLine();
        Staff deleteStaff = this.checkExistStaff(staffId);
        if (deleteStaff == null) {
            System.out.println("Nhân viên không tồn tại!");
            return;
        }

        System.out.println("Bạn có chắc chắn muốn xóa nhân viên này không?");
        System.out.println("|-- 1. Chắc chắn");
        System.out.println("|-- 2. Không");
        System.out.print("Chọn 1 lựa chọn: ");
        try {
            switch (sc.nextInt()) {
                case 1:
                    this.onDeleteStaff(staffId);
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

    private void onDeleteStaff(int staffId) {
        if (StaffAdminController.deleteStaff(staffId)) {
            System.out.println("Xóa nhân viên thành công!");
        } else {
            System.out.println("Xóa nhân viên thất bại!");
        }
    }

    public void listStaffView() {
        System.out.println("========== Danh sách thành viên ==========");

        List<Staff> staffs = StaffAdminController.getAllStaffs();
        this.displayStaffList(staffs);
    }

    private void displayStaffList(List<Staff> staffs) {
        String[] headers = {"ID", "Tên", "Email", "Mật khẩu", "Số điện thoại", "Ngày sinh", "Giới tính", "Lương", "Hạng thành viên", "Điểm tích lũy", "Ngày tạo thẻ", "Vai trò", "Quyền hạn"};

        DataTable.printStaffTable(headers, staffs);
    }

    public void searchStaffView() {
        boolean isClose = false;

        while (!isClose) {
            Scanner sc = new Scanner(System.in);

            System.out.println("========== Tìm kiếm nhân viên ==========");
            System.out.println("0. Quay lại (Nhập '0' để quay lại)");
            System.out.println("Phạm vi tìm kiếm: tên, email, số điện thoại, ngày sinh, giới tính, lương, vai trò");
            System.out.print("Nhập từ khóa tìm kiếm: ");
            String query = sc.nextLine();

            if (query.isEmpty()) {
                System.out.println("Từ khóa tìm kiếm không được để trống!");
                return;
            }

            if (query.equals("\'0\'")) {
                isClose = true;
            } else {
                List<Staff> matchStaffs = StaffAdminController.searchStaff(query);

                this.displayStaffList(matchStaffs);
            }
        }
    }
}
