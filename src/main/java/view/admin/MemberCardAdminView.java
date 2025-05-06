package main.java.view.admin;

import main.java.Application;
import main.java.component.DataTable;
import main.java.controller.admin.MemberAdminController;
import main.java.controller.admin.MemberCardAdminController;
import main.java.controller.admin.PermissionAdminController;
import main.java.model.Member;
import main.java.model.Permission;
import main.java.model.enums.MembershipTier;
import main.java.util.Session;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MemberCardAdminView {
    private boolean closeView = false;

    public MemberCardAdminView() {
        while (!closeView) {
            this.show();
        }
    }

    public void show() {
        List<Permission> permissions = PermissionAdminController.getPermissionsById(Session.getInstance().getCurrentUser().getId());

        Scanner sc = new Scanner(System.in);

        System.out.println("========== Quản lý thẻ thành viên ==========");
        System.out.println("1. Thêm thẻ thành viên");
        System.out.println("2. Cập nhật thẻ thành viên");
        System.out.println("3. Xóa thẻ thành viên");
        System.out.println("4. Hiển thị danh sách thẻ thành viên");
        System.out.println("5. Tìm kiếm thẻ thành viên");
        System.out.println("6. Quay lại");
        System.out.print("Chọn chức năng: ");

        try {
            switch (sc.nextInt()) {
                case 1:
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("member.create"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.addMemberCardView();
                    break;
                case 2:
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("member.update"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.updateMemberCardView();
                    break;
                case 3:
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("member.delete"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.deleteMemberCardView();
                    break;
                case 4:
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("member.view"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.listMemberCardView();
                    break;
                case 5:
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("member.view"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.searchMemberCardView();
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

    public void addMemberCardView() {
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Thêm thẻ thành viên ==========");
        System.out.print("Nhập ID của thành viên: ");
        int memberId = sc.nextInt();
        sc.nextLine();
        Member memberNoCard = this.checkNoCard(memberId);
        if (memberNoCard == null) {
            System.out.println("Thành viên không tồn tại hoặc đã có thẻ thành viên!");
            return;
        }

        MembershipTier membershipTier = MembershipTier.BRONZE;
        float loyaltyPoint = 0;
        LocalDateTime createCardAt = LocalDateTime.now();

        this.onAddMemberCard(memberNoCard, membershipTier, loyaltyPoint, createCardAt);
    }

    private Member checkNoCard(int memberId) {
        return MemberAdminController.getAllUsers()
                .stream()
                .filter(member -> member.getId() == memberId && member.getCreateCardAt() == null)
                .findFirst()
                .orElse(null);
    }

    private void onAddMemberCard(Member memberNoCard, MembershipTier membershipTier, float loyaltyPoint, LocalDateTime createCardAt) {
        if (MemberCardAdminController.updateMemberCard(memberNoCard.getId(), membershipTier.toString().toLowerCase(), loyaltyPoint, createCardAt)) {
            System.out.println("Thêm thẻ thành viên thành công!");
        } else {
            System.out.println("Thêm thẻ thành viên thất bại!");
        }
    }

    public void updateMemberCardView() {
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Cập nhật thẻ thành viên ==========");
        System.out.print("Nhập ID của thành viên cần cập nhật thẻ: ");
        int memberId = sc.nextInt();
        sc.nextLine();
        Member memberCard = this.checkCard(memberId);
        if (memberCard == null) {
            System.out.println("Thành viên không tồn tại hoặc không có thẻ thành viên!");
            return;
        }

        System.out.println("Hạng thành viên hiện tại: " + (memberCard.getMembershipTier() == null ? "null" : memberCard.getMembershipTier().toString().toUpperCase()));
        System.out.println("|-- 1. Đồng");
        System.out.println("|-- 2. Bạc");
        System.out.println("|-- 3. Vàng");
        System.out.println("|-- 4. Bạch kim");
        System.out.println("|-- 5. Kim cương");
        System.out.print("Hạng thành viên mới (Không muốn thay đổi, nhấn số bất kỳ ngoài khoảng): ");
        MembershipTier membershipTier = null;
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

        System.out.println("Điểm tích lũy hiện tại: " + new BigDecimal(memberCard.getLoyaltyPoint()).toPlainString());
        System.out.print("Điểm tích lũy mới (Không muốn thay đổi, nhấn -1): ");
        float loyaltyPoint = sc.nextFloat();
        sc.nextLine();
        if (loyaltyPoint < 0 && loyaltyPoint != -1) {
            System.out.println("Điểm tích lũy không hợp lệ!");
            return;
        }

        System.out.println("Ngày tạo thẻ: " + (memberCard.getCreateCardAt() == null ? "null" : memberCard.getCreateCardAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))));
        System.out.println("Bạn có muốn thay đổi ngày tạo thẻ không?");
        System.out.println("|-- 1. Có");
        System.out.println("|-- 2. Không");
        System.out.print("Chọn 1 lựa chọn: ");
        LocalDateTime createCardAt = null;
        try {
            switch (sc.nextInt()) {
                case 1:
                    createCardAt = LocalDateTime.now();
                    break;
                case 2:
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
                    return;
            }
        } catch (InputMismatchException e) {
            System.out.println("Lỗi nhập liệu! Vui lòng nhập lại.");
            return;
        }

        membershipTier = (membershipTier == null) ? memberCard.getMembershipTier() : membershipTier;
        loyaltyPoint = (loyaltyPoint == -1) ? memberCard.getLoyaltyPoint() : loyaltyPoint;
        createCardAt = (createCardAt == null) ? memberCard.getCreateCardAt() : createCardAt;

        this.onUpdateMemberCard(memberCard, membershipTier, loyaltyPoint, createCardAt);
    }

    private Member checkCard(int memberId) {
        return MemberAdminController.getAllUsers()
                .stream()
                .filter(member -> member.getId() == memberId && member.getCreateCardAt() != null)
                .findFirst()
                .orElse(null);
    }

    private void onUpdateMemberCard(Member memberCard, MembershipTier membershipTier, float loyaltyPoint, LocalDateTime createCardAt) {
        if (MemberCardAdminController.updateMemberCard(memberCard.getId(), membershipTier.toString().toLowerCase(), loyaltyPoint, createCardAt)) {
            System.out.println("Cập nhật thẻ thành viên thành công!");
        } else {
            System.out.println("Cập nhật thẻ thành viên thất bại!");
        }
    }

    public void deleteMemberCardView() {
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Xóa thẻ thành viên ==========");
        System.out.print("Nhập ID của thành viên cần xóa thẻ: ");
        int memberId = sc.nextInt();
        sc.nextLine();
        Member memberCard = this.checkCard(memberId);
        if (memberCard == null) {
            System.out.println("Thành viên không tồn tại hoặc không có thẻ thành viên!");
            return;
        }

        System.out.println("Bạn có chắc chắn muốn xóa thẻ thành viên này không?");
        System.out.println("|-- 1. Chắc chắn");
        System.out.println("|-- 2. Không");
        System.out.print("Chọn 1 lựa chọn: ");
        try {
            switch (sc.nextInt()) {
                case 1:
                    this.onDeleteMemberCard(memberId);
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

    private void onDeleteMemberCard(int memberId) {
        if (MemberCardAdminController.deleteMemberCard(memberId)) {
            System.out.println("Xóa thẻ thành viên thành công!");
        } else {
            System.out.println("Xóa thẻ thành viên thất bại!");
        }
    }

    public void listMemberCardView() {
        System.out.println("========== Danh sách thẻ thành viên ==========");

        List<Member> members = MemberCardAdminController.getMemberCardList();
        this.displayMemberCardList(members);
    }

    private void displayMemberCardList(List<Member> members) {
        String[] headers = {"ID", "Tên", "Số điện thoại", "Hạng thành viên", "Điểm tích lũy", "Ngày tạo thẻ"};

        DataTable.printMemberCardTable(headers, members);
    }

    public void searchMemberCardView() {
        boolean isClose = false;

        while (!isClose) {
            Scanner sc = new Scanner(System.in);

            System.out.println("========== Tìm kiếm thẻ thành viên ==========");
            System.out.println("0. Quay lại (Nhập '0' để quay lại)");
            System.out.println("Phạm vi tìm kiếm: tên, số điện thoại, hạng thành viên, điểm tích lũy, ngày tạo thẻ");
            System.out.print("Nhập từ khóa tìm kiếm: ");
            String query = sc.nextLine();

            if (query.isEmpty()) {
                System.out.println("Từ khóa tìm kiếm không được để trống!");
                return;
            }

            if (query.equals("\'0\'")) {
                isClose = true;
            } else {
                List<Member> matchMemberCard = MemberCardAdminController.searchMemberCard(query);

                this.displayMemberCardList(matchMemberCard);
            }
        }
    }
}
