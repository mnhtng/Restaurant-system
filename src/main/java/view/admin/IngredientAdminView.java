package main.java.view.admin;

import main.java.Application;
import main.java.component.DataTable;
import main.java.controller.admin.IngredientAdminController;
import main.java.controller.admin.PermissionAdminController;
import main.java.model.Ingredient;
import main.java.model.Permission;
import main.java.util.SearchUtil;
import main.java.util.Session;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class IngredientAdminView {
    private boolean closeView = false;

    public IngredientAdminView() {
        while (!closeView) {
            this.show();
        }
    }

    public void show() {
        List<Permission> permissions = PermissionAdminController.getPermissionsById(Session.getInstance().getCurrentUser().getId());

        Scanner sc = new Scanner(System.in);

        System.out.println("========== Quản lý nguyên liệu ==========");
        System.out.println("1. Thêm nguyên liệu");
        System.out.println("2. Cập nhật nguyên liệu");
        System.out.println("3. Xóa nguyên liệu");
        System.out.println("4. Hiển thị danh sách nguyên liệu");
        System.out.println("5. Tìm kiếm nguyên liệu");
        System.out.println("6. Quay lại");
        System.out.print("Chọn chức năng: ");

        try {
            switch (sc.nextInt()) {
                case 1:
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("ingredient.create"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.addIngredientView();
                    break;
                case 2:
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("ingredient.update"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.updateIngredientView();
                    break;
                case 3:
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("ingredient.delete"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.deleteIngredientView();
                    break;
                case 4:
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("ingredient.view"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.listIngredientView();
                    break;
                case 5:
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("ingredient.view"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.searchIngredientView();
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

    public void addIngredientView() {
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Thêm nguyên liệu ==========");
        System.out.print("Nhập tên nguyên liệu: ");
        String name = sc.nextLine();
        if (name.length() < 2) {
            System.out.println("Tên phải có ít nhất 2 ký tự!");
            return;
        }

        System.out.print("Nhập loại nguyên liệu: ");
        String category = sc.nextLine();
        if (category.length() < 2) {
            System.out.println("Loại phải có ít nhất 2 ký tự!");
            return;
        }

        System.out.print("Nhập đơn vị đo lường: ");
        String unit = sc.nextLine();
        if (unit.isEmpty()) {
            System.out.println("Đơn vị không được để trống!");
            return;
        }

        System.out.println("Nhập trạng thái nguyên liệu: ");
        System.out.println("0. Ngừng sử dụng");
        System.out.println("1. Đang sử dụng");
        System.out.print("Nhập trạng thái nguyên liệu: ");
        int statusInput = sc.nextInt();
        boolean status;
        try {
            if (statusInput == 0) {
                status = false;
            } else if (statusInput == 1) {
                status = true;
            } else {
                System.out.println("Trạng thái không hợp lệ!");
                return;
            }
        } catch (InputMismatchException e) {
            System.out.println("Lỗi nhập liệu! Vui lòng nhập lại.");
            return;
        }

        Ingredient ingredient = new Ingredient();
        ingredient.setName(name);
        ingredient.setCategory(category);
        ingredient.setUnit(unit);
        ingredient.setStatus(status);
        ingredient.setDeleteAt(null);

        this.onAddIngredient(ingredient);
    }

    private void onAddIngredient(Ingredient ingredient) {
        if (IngredientAdminController.addIngredient(ingredient)) {
            System.out.println("Thêm nguyên liệu thành công!");
        } else {
            System.out.println("Thêm nguyên liệu thất bại!");
        }
    }

    public void updateIngredientView() {
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Cập nhật nguyên liệu ==========");
        System.out.print("Nhập ID nguyên liệu cần cập nhật: ");
        int ingredientId = sc.nextInt();
        sc.nextLine();
        Ingredient updateIngredient = this.checkExistIngredient(ingredientId);
        if (updateIngredient == null) {
            System.out.println("Nguyên liệu không tồn tại!");
            return;
        }

        System.out.println("Nguyên liệu hiện tại: " + updateIngredient.getName());
        System.out.print("Nhập tên nguyên liệu mới (Không muốn thay đổi, nhấn Enter): ");
        String name = sc.nextLine();
        if (!name.isEmpty() && name.length() < 2) {
            System.out.println("Tên phải có ít nhất 2 ký tự!");
            return;
        }

        System.out.println("Loại nguyên liệu hiện tại: " + updateIngredient.getCategory());
        System.out.print("Nhập loại nguyên liệu mới (Không muốn thay đổi, nhấn Enter): ");
        String category = sc.nextLine();
        if (!category.isEmpty() && category.length() < 2) {
            System.out.println("Loại phải có ít nhất 2 ký tự!");
            return;
        }

        System.out.println("Đơn vị đo lường hiện tại: " + updateIngredient.getUnit());
        System.out.print("Nhập đơn vị đo lường mới (Không muốn thay đổi, nhấn Enter): ");
        String unit = sc.nextLine();

        System.out.println("Trạng thái nguyên liệu hiện tại: " + (updateIngredient.isStatus() == true ? "Đang sử dụng" : "Ngừng sử dụng"));
        System.out.println("Nhập trạng thái nguyên liệu mới (Không muốn thay đổi, nhấn -1): ");
        System.out.println("0. Ngừng sử dụng");
        System.out.println("1. Đang sử dụng");
        System.out.print("Nhập trạng thái nguyên liệu: ");
        int statusInput = sc.nextInt();
        boolean status;
        try {
            if (statusInput == 0) {
                status = false;
            } else if (statusInput == 1) {
                status = true;
            } else if (statusInput == -1) {
                status = updateIngredient.isStatus();
            } else {
                System.out.println("Trạng thái không hợp lệ!");
                return;
            }
        } catch (InputMismatchException e) {
            System.out.println("Lỗi nhập liệu! Vui lòng nhập lại.");
            return;
        }

        updateIngredient.setId(ingredientId);
        updateIngredient.setName(!name.isEmpty() ? name : updateIngredient.getName());
        updateIngredient.setCategory(!category.isEmpty() ? category : updateIngredient.getCategory());
        updateIngredient.setUnit(!unit.isEmpty() ? unit : updateIngredient.getUnit());
        updateIngredient.setStatus(status);

        this.onUpdateIngredient(updateIngredient);
    }

    private Ingredient checkExistIngredient(int ingredientId) {
        return IngredientAdminController.getAllIngredient().stream()
                .filter(i -> i.getId() == ingredientId)
                .findFirst()
                .orElse(null);
    }

    private void onUpdateIngredient(Ingredient updateIngredient) {
        if (IngredientAdminController.updateIngredient(updateIngredient)) {
            System.out.println("Cập nhật nguyên liệu thành công!");
        } else {
            System.out.println("Cập nhật nguyên liệu thất bại!");
        }
    }

    public void deleteIngredientView() {
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Xóa nguyên liệu ==========");
        System.out.print("Nhập ID nguyên liệu cần xóa: ");
        int ingredientId = sc.nextInt();
        sc.nextLine();
        Ingredient deleteIngredient = this.checkExistIngredient(ingredientId);
        if (deleteIngredient == null) {
            System.out.println("Nguyên liệu không tồn tại!");
            return;
        }

        System.out.println("Bạn có chắc chắn muốn xóa nguyên liệu này không?");
        System.out.println("|-- 1. Chắc chắn");
        System.out.println("|-- 2. Không");
        System.out.print("Chọn 1 lựa chọn: ");
        try {
            switch (sc.nextInt()) {
                case 1:
                    this.onDeleteIngredient(ingredientId);
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

    private void onDeleteIngredient(int ingredientId) {
        if (IngredientAdminController.deleteIngredient(ingredientId)) {
            System.out.println("Xóa nguyên liệu thành công!");
        } else {
            System.out.println("Xóa nguyên liệu thất bại!");
        }
    }

    public void listIngredientView() {
        System.out.println("========== Danh sách nguyên liệu ==========");

        List<Ingredient> ingredients = IngredientAdminController.getAllIngredient();
        this.displayIngredientList(ingredients);
    }

    private void displayIngredientList(List<Ingredient> ingredients) {
        String[] headers = {"ID", "Tên nguyên liệu", "Loại nguyên liệu", "Trạng thái"};

        Map<String, String> convert = Map.of(
                "meat", "Thịt",
                "vegetable", "Rau củ",
                "fruit", "Trái cây",
                "seafood", "Hải sản",
                "starch", "Tinh bột",
                "egg", "Trứng",
                "liquid", "Chất lỏng",
                "seasoning", "Gia vị",
                "plant_based_protein", "Đạm thực vật",
                "beverage", "Đồ uống"
        );

        DataTable.printIngredientTable(headers, ingredients, convert);
    }

    public void searchIngredientView() {
        boolean isClose = false;

        while (!isClose) {
            Scanner sc = new Scanner(System.in);

            System.out.println("========== Tìm kiếm nguyên liệu ==========");
            System.out.println("0. Quay lại (Nhập '0' để quay lại)");
            System.out.println("Phạm vi tìm kiếm: tên, loại nguyên liệu, trạng thái");
            System.out.print("Nhập từ khóa tìm kiếm: ");
            String query = sc.nextLine();

            if (query.isEmpty()) {
                System.out.println("Từ khóa tìm kiếm không được để trống!");
                return;
            }

            if (query.equals("\'0\'")) {
                isClose = true;
            } else {
                Map<String, String> convertSearch = Map.of(
                        "Thịt", "meat",
                        "Rau củ", "vegetable",
                        "Trái cây", "fruit",
                        "Hải sản", "seafood",
                        "Tinh bột", "starch",
                        "Trứng", "egg",
                        "Chất lỏng", "liquid",
                        "Gia vị", "seasoning",
                        "Đạm thực vật", "plant_based_protein",
                        "Đồ uống", "beverage"
                );
                String queryCategorySearch = SearchUtil.convert(convertSearch, query);

                List<Ingredient> ingredients = IngredientAdminController.searchIngredient(query, queryCategorySearch);

                this.displayIngredientList(ingredients);
            }
        }
    }
}
