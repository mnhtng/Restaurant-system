package main.java.view.admin;

import main.java.Application;
import main.java.component.DataTable;
import main.java.controller.admin.PermissionAdminController;
import main.java.controller.admin.WarehouseAdminController;
import main.java.middleware.AuthMiddleware;
import main.java.model.Permission;
import main.java.model.Warehouse;
import main.java.util.Session;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class WarehouseAdminView {
    private boolean closeView = false;

    public WarehouseAdminView() {
        while (!closeView) {
            this.show();
        }
    }

    public void show() {
        List<Permission> permissions = PermissionAdminController.getPermissionsById(Session.getInstance().getCurrentUser().getId());

        Scanner sc = new Scanner(System.in);

        System.out.println("========== Quản lý kho ==========");
        System.out.println("1. Cập nhật nguyên liệu trong kho");
        System.out.println("2. Xóa nguyên liệu trong kho");
        System.out.println("3. Hiển thị danh sách tồn kho");
        System.out.println("4. Tìm kiếm trong kho");
        System.out.println("5. Quay lại");
        System.out.print("Chọn chức năng: ");

        try {
            switch (sc.nextInt()) {
                case 1:
                    (new AuthMiddleware()).handle();
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("warehouse.update"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.updateWarehouseView();
                    break;
                case 2:
                    (new AuthMiddleware()).handle();
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("warehouse.delete"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.deleteWarehouseView();
                    break;
                case 3:
                    (new AuthMiddleware()).handle();
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("warehouse.view"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.listWarehouseView();
                    break;
                case 4:
                    (new AuthMiddleware()).handle();
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("warehouse.view"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.searchWarehouseView();
                    break;
                case 5:
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

    public void updateWarehouseView() {
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Cập nhật nguyên liệu trong kho ==========");
        System.out.print("Nhập ID tồn kho cần cập nhật: ");
        int inventoryId = sc.nextInt();
        sc.nextLine();
        Warehouse updateWarehouse = this.checkExistInventory(inventoryId);
        if (updateWarehouse == null) {
            System.out.println("Nguyên liệu không tồn tại trong kho!");
            return;
        }

        System.out.println("Số lượng nguyên liệu hiện tại: " + updateWarehouse.getQuantity());
        System.out.print("Nhập số lượng nguyên liệu mới (Không muốn thay đổi, nhấn -1): ");
        float newQuantity = sc.nextFloat();
        sc.nextLine();
        if (newQuantity < 0 && newQuantity != -1) {
            System.out.println("Số lượng không hợp lệ!");
            return;
        }

        System.out.println("Hạn sử dụng hiện tại: " + DateTimeFormatter.ofPattern("dd-MM-yyyy").format(updateWarehouse.getExpire()));
        System.out.print("Nhập hạn sử dụng mới (Không muốn thay đổi, nhấn Enter): ");
        String newExpire = sc.nextLine();
        String[] parts = newExpire.split("-");
        if (!newExpire.isEmpty()) {
            if (!newExpire.matches("^[0-9]{2}-[0-9]{2}-[0-9]{4}$")) {
                System.out.println("Vui lòng nhập theo định dạng dd-MM-yyyy!");
                return;
            } else if (parts.length != 3 || Integer.parseInt(parts[1]) > 12 || Integer.parseInt(parts[0]) > 31
                    || Integer.parseInt(parts[2]) < 1900 || Integer.parseInt(parts[2]) > LocalDate.now().getYear()) {
                System.out.println("Ngày tháng năm không hợp lệ!");
                return;
            }
        }

        updateWarehouse.setQuantity((newQuantity == -1) ? updateWarehouse.getQuantity() : newQuantity);
        updateWarehouse.setExpire(newExpire.isEmpty() ? updateWarehouse.getExpire() : LocalDate.parse(newExpire, DateTimeFormatter.ofPattern("dd-MM-yyyy")));

        this.onUpdateInventory(updateWarehouse);
    }

    private Warehouse checkExistInventory(int inventoryId) {
        return WarehouseAdminController.getInventoryById(inventoryId);
    }

    private void onUpdateInventory(Warehouse updateWarehouse) {
        if (WarehouseAdminController.updateInventory(updateWarehouse)) {
            System.out.println("Cập nhật nguyên liệu thành công!");
        } else {
            System.out.println("Cập nhật nguyên liệu thất bại!");
        }
    }

    public void deleteWarehouseView() {
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Xóa nguyên liệu trong kho ==========");
        System.out.print("Nhập ID tồn kho cần xóa: ");
        int inventoryId = sc.nextInt();
        sc.nextLine();
        Warehouse deleteWarehouse = this.checkExistInventory(inventoryId);
        if (deleteWarehouse == null) {
            System.out.println("Nguyên liệu không tồn tại trong kho!");
            return;
        }

        System.out.println("Bạn chắc chắn muốn xóa tồn kho này không?");
        System.out.println("|-- 1. Chắc chắn");
        System.out.println("|-- 2. Không");
        System.out.print("Chọn 1 lựa chọn: ");
        try {
            switch (sc.nextInt()) {
                case 1:
                    this.onDeleteInventory(inventoryId);
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

    private void onDeleteInventory(int inventoryId) {
        if (WarehouseAdminController.deleteInventory(inventoryId)) {
            System.out.println("Xóa nguyên liệu thành công!");
        } else {
            System.out.println("Xóa nguyên liệu thất bại!");
        }
    }

    public void listWarehouseView() {
        System.out.println("========== Danh sách tồn kho ==========");

        List<Warehouse> warehouses = WarehouseAdminController.getAllInventory();

        this.displayWarehouseList(warehouses);
    }

    private void displayWarehouseList(List<Warehouse> warehouses) {
        String[] headers = {"ID", "Nguyên liệu", "Số lượng", "Số lượng đã đặt", "Ngày nhập", "Hạn sử dụng"};

        DataTable.printWarehouseTable(headers, warehouses);
    }

    public void searchWarehouseView() {
        boolean isClose = false;

        while (!isClose) {
            Scanner sc = new Scanner(System.in);

            System.out.println("========== Tìm kiếm tồn kho ==========");
            System.out.println("0. Quay lại (Nhập '0' để quay lại)");
            System.out.println("Phạm vi tìm kiếm: nguyên liệu, số lượng, ngày nhập, hạn sử dụng");
            System.out.print("Nhập từ khóa tìm kiếm: ");
            String query = sc.nextLine();

            if (query.isEmpty()) {
                System.out.println("Từ khóa tìm kiếm không được để trống!");
                return;
            }

            if (query.equals("\'0\'")) {
                isClose = true;
            } else {
                List<Warehouse> matchInventories = WarehouseAdminController.searchInventory(query);

                this.displayWarehouseList(matchInventories);
            }
        }
    }
}
