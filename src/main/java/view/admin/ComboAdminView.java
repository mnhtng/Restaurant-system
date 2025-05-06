package main.java.view.admin;

import main.java.Application;
import main.java.component.DataTable;
import main.java.controller.admin.ComboAdminController;
import main.java.controller.admin.DishAdminController;
import main.java.controller.admin.PermissionAdminController;
import main.java.controller.admin.ProductAdminController;
import main.java.model.Combo;
import main.java.model.ComboDetail;
import main.java.model.Permission;
import main.java.model.Product;
import main.java.util.Session;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ComboAdminView {
    private boolean closeView = false;

    public ComboAdminView() {
        while (!closeView) {
            this.show();
        }
    }

    public void show() {
        List<Permission> permissions = PermissionAdminController.getPermissionsById(Session.getInstance().getCurrentUser().getId());

        Scanner sc = new Scanner(System.in);

        System.out.println("========== Quản lý combo ==========");
        System.out.println("1. Thêm combo");
        System.out.println("2. Sửa combo");
        System.out.println("3. Xóa combo");
        System.out.println("4. Hiển thị danh sách combo");
        System.out.println("5. Tìm kiếm combo");
        System.out.println("6. Quay lại");
        System.out.print("Chọn chức năng: ");

        try {
            switch (sc.nextInt()) {
                case 1:
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("combo.create"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.addComboView();
                    break;
                case 2:
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("combo.update"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.updateComboView();
                    break;
                case 3:
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("combo.delete"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.deleteComboView();
                    break;
                case 4:
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("combo.view"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.listComboView();
                    break;
                case 5:
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("combo.view"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.searchComboView();
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

    public void addComboView() {
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Thêm combo ==========");
        System.out.print("Tên combo: ");
        String name = sc.nextLine();
        if (name.length() < 2) {
            System.out.println("Tên combo phải có ít nhất 2 ký tự!");
            return;
        }

        System.out.println("Danh sách món ăn: ");
        System.out.println(DishAdminController.getAllDish()
                .stream()
                .map(dish -> "|-- " + dish.getId() + ". " + dish.getName() + " (" + NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(dish.getPrice()) + ")")
                .collect(Collectors.joining("\n")));
        System.out.print("Món ăn trong combo (VD: ID-Số lượng): ");
        String dishInput = sc.nextLine();
        if (dishInput.isEmpty() || !dishInput.trim().matches("^\\d+-\\d+(,\\d+-\\d+)*$")) {
            System.out.println("Nhập liệu không hợp lệ! Vui lòng nhập lại.");
            return;
        }

        List<Integer> dishes = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();
        Float price = 0f;
        String[] dishArray = dishInput.split(",");

        for (String dish : dishArray) {
            String[] parts = dish.split("-");

            try {
                int id = Integer.parseInt(parts[0]);
                int quantity = Integer.parseInt(parts[1]);

                if (DishAdminController.getAllDish().stream().noneMatch(d -> d.getId() == id)) {
                    System.out.println("Món ăn với ID " + id + " không tồn tại!");
                    return;
                }

                dishes.add(id);
                quantities.add(quantity);
                price += DishAdminController.getDishById(id).getPrice() * quantity;
            } catch (NumberFormatException e) {
                System.out.println("Nhập liệu không hợp lệ! Vui lòng nhập lại.");
                return;
            }
        }

        System.out.print("Giá combo (Đề xuất: " + NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(price) + "): ");
        float priceInput = sc.nextFloat();
        sc.nextLine();
        if (priceInput <= 0) {
            System.out.println("Giá combo không hợp lệ! Vui lòng nhập lại.");
            return;
        }

        System.out.print("Mô tả combo (Không bắt buộc): ");
        String description = sc.nextLine();
        if (!description.isEmpty() && description.length() < 2) {
            System.out.println("Mô tả combo phải có ít nhất 2 ký tự!");
            return;
        }

        System.out.println("Bạn có muốn hiển thị món ăn trên menu không?");
        System.out.println("|-- 1. Có");
        System.out.println("|-- 2. Không");
        System.out.print("Chọn 1 lựa chọn: ");
        int display = 0;
        boolean displayDish = false;
        try {
            display = sc.nextInt();
            sc.nextLine();

            if (display == 1) {
                displayDish = true;
            } else if (display == 2) {
                displayDish = false;
            } else {
                System.out.println("Lựa chọn không hợp lệ!");
                return;
            }
        } catch (InputMismatchException e) {
            System.out.println("Lựa chọn không hợp lệ!");
            return;
        }

        Product product = new Product();
        product.setType("1");
        product.setStatus(displayDish);
        product.setDeleteAt(null);

        Combo combo = new Combo();
        combo.setName(name);
        combo.setPrice(priceInput);
        combo.setDescription(description.isEmpty() ? null : description);

        combo.setProduct(product);

        this.onAddCombo(combo, dishes, quantities);
    }

    private void onAddCombo(Combo combo, List<Integer> dishes, List<Integer> quantities) {
        if (ComboAdminController.addCombo(combo, dishes, quantities)) {
            System.out.println("Thêm combo thành công!");
        } else {
            System.out.println("Thêm combo thất bại!");
        }
    }

    public void updateComboView() {
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Sửa combo ==========");
        System.out.print("Nhập ID combo cần sửa: ");
        int id = sc.nextInt();
        sc.nextLine();
        Combo updateCombo = this.checkExistCombo(id);
        List<ComboDetail> comboDetails = ComboAdminController.getComboDetailById(id);
        if (updateCombo == null) {
            System.out.println("Món ăn không tồn tại!");
            return;
        }

        System.out.println("Tên combo hiện tại: " + updateCombo.getName());
        System.out.print("Tên combo mới (Không muốn thay đổi, nhấn Enter): ");
        String name = sc.nextLine();
        if (!name.isEmpty() && name.length() < 2) {
            System.out.println("Tên phải có ít nhất 2 ký tự!");
            return;
        }

        System.out.println("Danh sách món ăn: ");
        System.out.println(DishAdminController.getAllDish()
                .stream()
                .map(dish -> "|-- " + dish.getId() + ". " + dish.getName() + " (" + NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(dish.getPrice()) + ")")
                .collect(Collectors.joining("\n")));
        System.out.println("Món ăn trong combo hiện tại: ");
        System.out.println(comboDetails
                .stream()
                .filter(combo -> combo.getComboId() == updateCombo.getId())
                .map(combo -> "|-- " + combo.getDishId() + ". " + combo.getDish().getName() + " - Số lượng: " + combo.getQuantity() + " (" + NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(combo.getDish().getPrice()) + ")").collect(Collectors.joining("\n")));
        System.out.print("Món ăn mới trong combo (VD: ID-Số lượng) (Không muốn thay đổi, nhấn Enter): ");
        String dishInput = sc.nextLine();
        if (!dishInput.isEmpty() && !dishInput.trim().matches("^\\d+-\\d+(,\\d+-\\d+)*$")) {
            System.out.println("Nhập liệu không hợp lệ! Vui lòng nhập lại.");
            return;
        }

        List<Integer> dishes = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();
        Float price = 0f;
        if (!dishInput.isEmpty()) {
            String[] dishArray = dishInput.split(",");

            for (String dish : dishArray) {
                String[] parts = dish.split("-");

                try {
                    int idDish = Integer.parseInt(parts[0]);
                    int quantity = Integer.parseInt(parts[1]);

                    if (DishAdminController.getAllDish().stream().noneMatch(d -> d.getId() == idDish)) {
                        System.out.println("Món ăn với ID " + idDish + " không tồn tại!");
                        return;
                    }

                    dishes.add(idDish);
                    quantities.add(quantity);
                    price += DishAdminController.getDishById(idDish).getPrice() * quantity;
                } catch (NumberFormatException e) {
                    System.out.println("Nhập liệu không hợp lệ! Vui lòng nhập lại.");
                    return;
                }
            }
        }

        System.out.println("Giá combo hiện tại: " + NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(updateCombo.getPrice()));
        System.out.print("Giá combo mới (Đề xuất: " + NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format((price == 0) ? updateCombo.getPrice() : price) + ") (Không muốn thay đổi, nhấn -1): ");
        float priceInput = sc.nextFloat();
        sc.nextLine();
        if (priceInput != -1 && priceInput <= 0) {
            System.out.println("Giá combo không hợp lệ! Vui lòng nhập lại.");
            return;
        }

        System.out.println("Mô tả combo hiện tại: " + updateCombo.getDescription());
        System.out.print("Mô tả combo mới (Không muốn thay đổi, nhấn Enter): ");
        String description = sc.nextLine();
        if (!description.isEmpty() && description.length() < 2) {
            System.out.println("Mô tả combo phải có ít nhất 2 ký tự!");
            return;
        }

        System.out.println("Trạng thái trên menu hiện tại: " + (updateCombo.getProduct().isStatus() ? "Hiển thị" : "Ẩn"));
        System.out.println("Bạn có muốn hiển thị món ăn trên menu không? (Không muốn thay đổi, nhấn số bất kỳ ngoài khoảng): ");
        System.out.println("|-- 1. Có");
        System.out.println("|-- 2. Không");
        System.out.print("Chọn 1 lựa chọn: ");
        int display = 0;
        boolean displayDish = false;
        try {
            display = sc.nextInt();
            sc.nextLine();

            if (display < 0) {
                System.out.println("Lựa chọn không hợp lệ!");
                return;
            }

            if (display == 1) {
                displayDish = true;
            } else if (display == 2) {
                displayDish = false;
            } else {
                displayDish = updateCombo.getProduct().isStatus();
            }
        } catch (InputMismatchException e) {
            System.out.println("Lựa chọn không hợp lệ!");
            return;
        }

        Product product = new Product();
        product.setId(id);
        product.setType("1");
        product.setStatus(displayDish);
        product.setDeleteAt(null);

        Combo updatedCombo = new Combo();
        updatedCombo.setId(id);
        updatedCombo.setName(name.isEmpty() ? updateCombo.getName() : name);
        updatedCombo.setPrice((priceInput == -1) ? updateCombo.getPrice() : priceInput);
        updatedCombo.setDescription(description.isEmpty() ? updateCombo.getDescription() : description);

        updatedCombo.setProduct(product);

        this.onUpdateCombo(updatedCombo, dishes, quantities);
    }

    private Combo checkExistCombo(int id) {
        return ComboAdminController.getComboById(id);
    }

    private void onUpdateCombo(Combo updatedCombo, List<Integer> dishes, List<Integer> quantities) {
        if (ComboAdminController.updateCombo(updatedCombo, dishes, quantities)) {
            System.out.println("Cập nhật combo thành công!");
        } else {
            System.out.println("Cập nhật combo thất bại!");
        }
    }

    public void deleteComboView() {
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Xóa combo ==========");
        System.out.print("Nhập ID combo cần xóa: ");
        int id = sc.nextInt();
        sc.nextLine();
        Combo deleteCombo = this.checkExistCombo(id);
        if (deleteCombo == null) {
            System.out.println("Combo không tồn tại!");
            return;
        }

        System.out.println("Bạn có chắc chắn muốn xóa combo này không?");
        System.out.println("|-- 1. Chắc chắn");
        System.out.println("|-- 2. Không");
        System.out.print("Chọn 1 lựa chọn: ");
        try {
            switch (sc.nextInt()) {
                case 1:
                    this.onDeleteCombo(id);
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

    private void onDeleteCombo(int id) {
        if (ProductAdminController.deleteProduct(id)) {
            System.out.println("Xóa combo thành công!");
        } else {
            System.out.println("Xóa combo thất bại!");
        }
    }

    public void listComboView() {
        System.out.println("========== Danh sách combo ==========");

        List<Combo> combos = ComboAdminController.getAllCombo();
        this.displayComboList(combos);
    }

    private void displayComboList(List<Combo> combos) {
        String[] headers = {"ID", "Tên combo", "Giá", "Món ăn trong combo", "Trạng thái hiển thị trên menu", "Mô tả"};

        DataTable.printComboTable(headers, combos);
    }

    public void searchComboView() {
        boolean isClose = false;

        while (!isClose) {
            Scanner sc = new Scanner(System.in);

            System.out.println("========== Tìm kiếm combo ==========");
            System.out.println("0. Quay lại (Nhập '0' để quay lại)");
            System.out.println("Phạm vi tìm kiếm: combo, giá, món ăn, trạng thái, mô tả");
            System.out.print("Nhập từ khóa tìm kiếm: ");
            String query = sc.nextLine();

            if (query.isEmpty()) {
                System.out.println("Từ khóa tìm kiếm không được để trống!");
                return;
            }

            if (query.equals("\'0\'")) {
                isClose = true;
            } else {
                List<Combo> combos = ComboAdminController.searchCombo(query);

                this.displayComboList(combos);
            }
        }
    }
}
