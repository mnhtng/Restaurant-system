package main.java.view.admin;

import main.java.component.DataTable;
import main.java.controller.admin.PermissionAdminController;
import main.java.controller.admin.TableAdminController;
import main.java.model.Permission;
import main.java.model.Table;
import main.java.model.enums.TableStatus;
import main.java.util.Session;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class TableAdminView {
    private boolean closeView = false;

    public TableAdminView() {
        while (!closeView) {
            show();
        }
    }

    public void show() {
        List<Permission> permissions = PermissionAdminController.getPermissionsById(Session.getInstance().getCurrentUser().getId());

        Scanner sc = new Scanner(System.in);

        System.out.println("========== Quản lý bàn ăn ==========");
        System.out.println("1. Thêm bàn ăn");
        System.out.println("2. Cập nhật bàn ăn");
        System.out.println("3. Xóa bàn ăn");
        System.out.println("4. Hiển thị danh sách bàn ăn");
        System.out.println("5. Tìm kiếm bàn ăn");
        System.out.println("6. Quay lại");
        System.out.print("Chọn chức năng: ");

        try {
            switch (sc.nextInt()) {
                case 1:
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("table.create"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.addTableView();
                    break;
                case 2:
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("table.update"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.updateTableView();
                    break;
                case 3:
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("table.delete"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.deleteTableView();
                    break;
                case 4:
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("table.view"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.listTableView();
                    break;
                case 5:
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("table.view"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.searchTableView();
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

    public void addTableView() {
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Thêm bàn ăn ==========");
        System.out.print("Số ghế: ");
        int seatCount = sc.nextInt();
        sc.nextLine();
        if (seatCount <= 0) {
            System.out.println("Số ghế phải lớn hơn 0!");
            return;
        }

        Table table = new Table();
        table.setSeatCount(seatCount);
        table.setStatus(TableStatus.AVAILABLE);
        table.setDeleteAt(null);

        this.onAddTable(table);
    }

    private void onAddTable(Table table) {
        if (TableAdminController.addTable(table)) {
            System.out.println("Thêm bàn ăn thành công!");
        } else {
            System.out.println("Xảy ra lỗi khi thêm bàn ăn!");
        }
    }

    public void updateTableView() {
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Cập nhật bàn ăn ==========");
        System.out.print("Nhập ID bàn ăn cần cập nhật: ");
        int tableId = sc.nextInt();
        sc.nextLine();
        Table existingTable = TableAdminController.getTableById(tableId);
        if (existingTable == null) {
            System.out.println("Bàn ăn không tồn tại!");
            return;
        }

        System.out.println("Số ghế hiện tại: " + existingTable.getSeatCount());
        System.out.print("Số ghế mới (nhấn Enter để không thay đổi): ");
        String seatInput = sc.nextLine();
        int seatCount = seatInput.isEmpty() ? existingTable.getSeatCount() : Integer.parseInt(seatInput);

        System.out.println("Trạng thái hiện tại: " + existingTable.getStatus().toString());
        System.out.println("|-- 1. Available");
        System.out.println("|-- 2. Occupied");
        System.out.println("|-- 3. Reserved");
        System.out.println("|-- 4. Unavailable");
        System.out.print("Chọn trạng thái mới (nhấn số bất kỳ ngoài khoảng để không thay đổi): ");
        TableStatus status = existingTable.getStatus();
        int statusChoice = sc.nextInt();
        sc.nextLine();

        switch (statusChoice) {
            case 1:
                status = TableStatus.AVAILABLE;
                break;
            case 2:
                status = TableStatus.OCCUPIED;
                break;
            case 3:
                status = TableStatus.RESERVED;
                break;
            case 4:
                status = TableStatus.UNAVAILABLE;
                break;
            default:
                break;
        }

        existingTable.setSeatCount(seatCount);
        existingTable.setStatus(status);

        this.onUpdateTable(existingTable);
    }

    private void onUpdateTable(Table existingTable) {
        if (TableAdminController.updateTable(existingTable)) {
            System.out.println("Cập nhật bàn ăn thành công!");
        } else {
            System.out.println("Xảy ra lỗi khi cập nhật bàn ăn!");
        }
    }

    public void deleteTableView() {
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Xóa bàn ăn ==========");
        System.out.print("Nhập ID bàn ăn cần xóa: ");
        int tableId = sc.nextInt();
        sc.nextLine();
        Table existingTable = TableAdminController.getTableById(tableId);
        if (existingTable == null) {
            System.out.println("Bàn ăn không tồn tại!");
            return;
        }

        System.out.println("Bạn chắc chắn muốn xóa bàn ăn này?");
        System.out.println("|-- 1. Chắc chắn");
        System.out.println("|-- 2. Không");
        System.out.print("Chọn 1 lựa chọn: ");
        try {
            switch (sc.nextInt()) {
                case 1:
                    this.onDeleteTable(tableId);
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

    private void onDeleteTable(int tableId) {
        if (TableAdminController.deleteTable(tableId)) {
            System.out.println("Xóa bàn ăn thành công!");
        } else {
            System.out.println("Xảy ra lỗi khi xóa bàn ăn!");
        }
    }

    public void listTableView() {
        System.out.println("========== Danh sách bàn ăn ==========");

        List<Table> tables = TableAdminController.getAllTables();

        this.displayTableList(tables);
    }

    private void displayTableList(List<Table> tables) {
        String[] headers = {"ID", "Số ghế", "Trạng thái"};

        DataTable.printTableTable(headers, tables);
    }

    public void searchTableView() {
        boolean isClose = false;

        while (!isClose) {
            Scanner sc = new Scanner(System.in);

            System.out.println("========== Tìm kiếm combo ==========");
            System.out.println("0. Quay lại (Nhập '0' để quay lại)");
            System.out.println("Phạm vi tìm kiếm: số ghế, trạng thái bàn ăn");
            System.out.print("Nhập từ khóa tìm kiếm: ");
            String query = sc.nextLine();

            if (query.isEmpty()) {
                System.out.println("Từ khóa tìm kiếm không được để trống!");
                return;
            }

            if (query.equals("\'0\'")) {
                isClose = true;
            } else {
                List<Table> tables = TableAdminController.searchTables(query);

                this.displayTableList(tables);
            }
        }
    }
}
