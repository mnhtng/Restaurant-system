package main.java.view.admin;

import main.java.Application;
import main.java.component.DataTable;
import main.java.controller.admin.IngredientAdminController;
import main.java.controller.admin.PermissionAdminController;
import main.java.controller.admin.SupplierAdminController;
import main.java.middleware.AuthMiddleware;
import main.java.model.Permission;
import main.java.model.Supplier;
import main.java.model.SupplierIngredient;
import main.java.util.Session;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Collectors;

public class SupplierAdminView {
    private boolean closeView = false;

    public SupplierAdminView() {
        while (!closeView) {
            this.show();
        }
    }

    public void show() {
        List<Permission> permissions = PermissionAdminController.getPermissionsById(Session.getInstance().getCurrentUser().getId());

        Scanner sc = new Scanner(System.in);

        System.out.println("========== Quản lý nhà cung cấp ==========");
        System.out.println("1. Thêm nhà cung cấp");
        System.out.println("2. Cập nhật nhà cung cấp");
        System.out.println("3. Thiết lập nhà cung cấp chính");
        System.out.println("4. Xóa nhà cung cấp");
        System.out.println("5. Hiển thị danh sách nhà cung cấp");
        System.out.println("6. Tìm kiếm nhà cung cấp");
        System.out.println("7. Quay lại");
        System.out.print("Chọn chức năng: ");

        try {
            switch (sc.nextInt()) {
                case 1:
                    (new AuthMiddleware()).handle();
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("supplier.create"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.addSupplierView();
                    break;
                case 2:
                    (new AuthMiddleware()).handle();
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("supplier.update"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.updateSupplierView();
                    break;
                case 3:
                    (new AuthMiddleware()).handle();
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("supplier.update"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.setMainSupplierView();
                    break;
                case 4:
                    (new AuthMiddleware()).handle();
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("supplier.delete"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.deleteSupplierView();
                    break;
                case 5:
                    (new AuthMiddleware()).handle();
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("supplier.view"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.listSupplierView();
                    break;
                case 6:
                    (new AuthMiddleware()).handle();
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("supplier.view"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.searchSupplierView();
                    break;
                case 7:
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

    public void addSupplierView() {
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Thêm nhà cung cấp ==========");
        System.out.print("Tên: ");
        String name = sc.nextLine();
        if (name.length() < 2) {
            System.out.println("Tên phải có ít nhất 2 ký tự!");
            return;
        }

        System.out.print("Địa chỉ: ");
        String address = sc.nextLine();
        if (address.length() < 5) {
            System.out.println("Địa chỉ phải có ít nhất 5 ký tự!");
            return;
        }

        System.out.print("Số điện thoại: ");
        String phone = sc.nextLine();
        if (!phone.matches("0\\d{9,14}")) {
            System.out.println("Số điện thoại phải bắt đầu bằng số 0 và có độ dài từ 10 đến 15 ký tự!");
            return;
        }

        Supplier supplier = new Supplier();
        supplier.setName(name);
        supplier.setAddress(address);
        supplier.setPhone(phone);

        this.onAddSupplier(supplier);
    }

    private void onAddSupplier(Supplier supplier) {
        if (SupplierAdminController.addSupplier(supplier)) {
            System.out.println("Thêm nhà cung cấp thành công!");
        } else {
            System.out.println("Thêm nhà cung cấp thất bại!");
        }
    }

    public void updateSupplierView() {
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Cập nhật nhà cung cấp ==========");
        System.out.print("Nhập ID nhà cung cấp cần cập nhật: ");
        int supplierId = sc.nextInt();
        sc.nextLine();
        Supplier updateSupplier = this.checkExistSupplier(supplierId);
        if (updateSupplier == null) {
            System.out.println("Nhà cung cấp không tồn tại!");
            return;
        }

        System.out.println("Tên hiện tại: " + updateSupplier.getName());
        System.out.print("Tên mới (Không muốn thay đổi, nhấn Enter): ");
        String name = sc.nextLine();
        if (!name.isEmpty() && name.length() < 2) {
            System.out.println("Tên phải có ít nhất 2 ký tự!");
            return;
        }

        System.out.println("Địa chỉ hiện tại: " + updateSupplier.getAddress());
        System.out.print("Địa chỉ mới (Không muốn thay đổi, nhấn Enter): ");
        String address = sc.nextLine();
        if (!address.isEmpty() && address.length() < 5) {
            System.out.println("Địa chỉ phải có ít nhất 5 ký tự!");
            return;
        }

        System.out.println("Số điện thoại hiện tại: " + updateSupplier.getPhone());
        System.out.print("Số điện thoại mới (Không muốn thay đổi, nhấn Enter): ");
        String phone = sc.nextLine();
        if (!phone.isEmpty() && !phone.matches("0\\d{9,14}")) {
            System.out.println("Số điện thoại phải bắt đầu bằng số 0 và có độ dài từ 10 đến 15 ký tự!");
            return;
        }

        List<SupplierIngredient> ingredientBySupplier = this.getListSupplierIngredient(supplierId);
        System.out.println("Danh sách nguyên liệu hiện có: ");
        System.out.println(this.getAllIngredient());
        System.out.print("Nguyên liệu hiện tại mà nhà cung cấp đang cung cấp: ");
        if (ingredientBySupplier == null || ingredientBySupplier.isEmpty()) {
            System.out.println("[]");
        } else {
            System.out.println();
            System.out.println(ingredientBySupplier
                    .stream()
                    .map(i -> "|-- ID " + i.getIngredientId() + ": " + i.getIngredient().getName() + " (" + NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(i.getDefaultPrice()) + ")")
                    .collect(Collectors.joining("\n")));
        }
        System.out.print("Nhập nguyên liệu mới (VD: ID-Giá, không muốn thay đổi, nhấn 0): ");
        String newIngredients = sc.nextLine();
        if (newIngredients.equals("0")) {
            newIngredients = "";
        } else if (!newIngredients.isEmpty() && !newIngredients.trim().matches("^\\d+-\\d+(\\.\\d+)?(,\\d+-\\d+(\\.\\d+)?)*$")) {
            System.out.println("Nhập liệu không hợp lệ! Vui lòng nhập lại.");
            return;
        }

        List<Integer> ingredients = new ArrayList<>();
        List<Float> prices = new ArrayList<>();
        if (!newIngredients.isEmpty()) {
            String[] ingredientPricePairs = newIngredients.split(",");

            for (String pair : ingredientPricePairs) {
                String[] parts = pair.split("-");

                try {
                    int ingredientId = Integer.parseInt(parts[0]);
                    float price = Float.parseFloat(parts[1]);

                    if (IngredientAdminController.getAllIngredient().stream().noneMatch(i -> i.getId() == ingredientId)) {
                        System.out.println("Nguyên liệu không tồn tại!");
                        return;
                    }

                    ingredients.add(ingredientId);
                    prices.add(price);
                } catch (NumberFormatException e) {
                    System.out.println("Nguyên liệu không hợp lệ!");
                    return;
                }
            }
        }

        Supplier supplier = new Supplier();
        supplier.setId(supplierId);
        supplier.setName(name.isEmpty() ? updateSupplier.getName() : name);
        supplier.setAddress(address.isEmpty() ? updateSupplier.getAddress() : address);
        supplier.setPhone(phone.isEmpty() ? updateSupplier.getPhone() : phone);

        this.onUpdateSupplier(supplier, ingredients, prices);
    }

    private Supplier checkExistSupplier(int supplierId) {
        return SupplierAdminController.getSupplierById(supplierId);
    }

    private List<SupplierIngredient> getListSupplierIngredient(int supplierId) {
        return SupplierAdminController.getSupplierIngredient(supplierId);
    }

    private String getAllIngredient() {
        return IngredientAdminController.getAllIngredient()
                .stream()
                .map(ingredient -> "|-- " + ingredient.getId() + ". " + ingredient.getName())
                .collect(Collectors.joining("\n"));
    }

    private void onUpdateSupplier(Supplier supplier, List<Integer> ingredients, List<Float> prices) {
        if (SupplierAdminController.updateSupplier(supplier, ingredients, prices)) {
            System.out.println("Cập nhật nhà cung cấp thành công!");
        } else {
            System.out.println("Cập nhật nhà cung cấp thất bại!");
        }
    }

    public void setMainSupplierView() {
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Thiết lập nhà cung cấp chính ==========");
        System.out.print("Nhập ID nhà cung cấp cần thiết lập: ");
        int supplierId = sc.nextInt();
        sc.nextLine();
        Supplier updateSupplier = this.checkExistSupplier(supplierId);
        if (updateSupplier == null) {
            System.out.println("Nhà cung cấp không tồn tại!");
            return;
        }

        System.out.print("Nhà cung cấp chính cho các nguyên liệu: ");
        List<SupplierIngredient> ingredientBySupplier = this.getListSupplierIngredient(supplierId);
        if (ingredientBySupplier == null) {
            System.out.println("[]");
        } else {
            System.out.println();
            System.out.println(ingredientBySupplier
                    .stream()
                    .map(i -> "|-- ID " + i.getIngredientId() + ": " + i.getIngredient().getName() + " (" + NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(i.getDefaultPrice()) + ")")
                    .collect(Collectors.joining("\n")));
        }
        System.out.print("Nhập nguyên liệu chính mới: ");
        String primaryIngredient = sc.nextLine();
        if (primaryIngredient.isEmpty() || primaryIngredient.trim().contains(" ") || !primaryIngredient.matches("\\d+(,\\d+)*")) {
            System.out.println("Nguyên liệu không hợp lệ!");
            return;
        } else {
            String[] part = primaryIngredient.split(",");

            for (String id : part) {
                try {
                    int ingredientId = Integer.parseInt(id);

                    if (ingredientBySupplier.stream().noneMatch(i -> i.getIngredientId() == ingredientId)) {
                        System.out.println("Nguyên liệu không thuộc nhà cung cấp này!");
                        return;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Nguyên liệu không hợp lệ!");
                    return;
                }
            }
        }

        String[] primaryIngredientParts = primaryIngredient.split(",");

        this.onSetMainSupplier(supplierId, primaryIngredientParts);
    }

    private void onSetMainSupplier(int supplierId, String[] primaryIngredientParts) {
        if (SupplierAdminController.setMainSupplier(supplierId, primaryIngredientParts)) {
            System.out.println("Thiết lập nhà cung cấp chính thành công!");
        } else {
            System.out.println("Thiết lập nhà cung cấp chính thất bại!");
        }
    }

    public void deleteSupplierView() {
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Xóa nhà cung cấp ==========");
        System.out.print("Nhập ID nhà cung cấp cần xóa: ");
        int supplierId = sc.nextInt();
        sc.nextLine();
        Supplier deleteSupplier = this.checkExistSupplier(supplierId);
        if (deleteSupplier == null) {
            System.out.println("Nhà cung cấp không tồn tại!");
            return;
        }

        System.out.println("Bạn có chắc chắn muốn xóa nhà cung cấp này không?");
        System.out.println("|-- 1. Chắc chắn");
        System.out.println("|-- 2. Không");
        System.out.print("Chọn 1 lựa chọn: ");
        try {
            switch (sc.nextInt()) {
                case 1:
                    this.onDeleteSupplier(supplierId);
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

    private void onDeleteSupplier(int supplierId) {
        if (SupplierAdminController.deleteSupplier(supplierId)) {
            System.out.println("Xóa nhà cung cấp thành công!");
        } else {
            System.out.println("Xóa nhà cung cấp thất bại!");
        }
    }

    public void listSupplierView() {
        System.out.println("========== Danh sách nhà cung cấp ==========");

        List<Supplier> suppliers = SupplierAdminController.getAllSupplier();
        this.displaySupplierList(suppliers);
    }

    private void displaySupplierList(List<Supplier> suppliers) {
        String[] headers = {"ID", "Tên", "Địa chỉ", "Số điện thoại", "Nguyên liệu cung cấp"};

        DataTable.printSupplierTable(headers, suppliers);
    }

    public void searchSupplierView() {
        boolean isClose = false;

        while (!isClose) {
            Scanner sc = new Scanner(System.in);

            System.out.println("========== Tìm kiếm nhà cung cấp ==========");
            System.out.println("0. Quay lại (Nhập '0' để quay lại)");
            System.out.println("Phạm vi tìm kiếm: tên, địa chỉ, số điện thoại, nguyên liệu cung cấp");
            System.out.print("Nhập từ khóa tìm kiếm: ");
            String query = sc.nextLine();

            if (query.isEmpty()) {
                System.out.println("Từ khóa tìm kiếm không được để trống!");
                return;
            }

            if (query.equals("\'0\'")) {
                isClose = true;
            } else {
                List<Supplier> suppliers = SupplierAdminController.searchSupplier(query);

                this.displaySupplierList(suppliers);
            }
        }
    }
}
