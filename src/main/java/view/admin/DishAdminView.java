package main.java.view.admin;

import main.java.Application;
import main.java.component.DataTable;
import main.java.controller.admin.DishAdminController;
import main.java.controller.admin.IngredientAdminController;
import main.java.controller.admin.PermissionAdminController;
import main.java.controller.admin.ProductAdminController;
import main.java.model.Dish;
import main.java.model.Ingredient;
import main.java.model.Permission;
import main.java.model.Product;
import main.java.model.enums.DishCategory;
import main.java.model.enums.DishStatus;
import main.java.util.Session;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Collectors;

public class DishAdminView {
    private boolean closeView = false;

    public DishAdminView() {
        while (!closeView) {
            this.show();
        }
    }

    public void show() {
        List<Permission> permissions = PermissionAdminController.getPermissionsById(Session.getInstance().getCurrentUser().getId());

        Scanner sc = new Scanner(System.in);

        System.out.println("========== Quản lý món ăn ==========");
        System.out.println("1. Thêm món ăn");
        System.out.println("2. Sửa món ăn");
        System.out.println("3. Xóa món ăn");
        System.out.println("4. Hiển thị danh sách món ăn");
        System.out.println("5. Tìm kiếm món ăn");
        System.out.println("6. Quay lại");
        System.out.print("Chọn chức năng: ");

        try {
            switch (sc.nextInt()) {
                case 1:
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("dish.create"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.addDishView();
                    break;
                case 2:
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("dish.update"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.updateDishView();
                    break;
                case 3:
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("dish.delete"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.deleteDishView();
                    break;
                case 4:
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("dish.view"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.listDishView();
                    break;
                case 5:
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("dish.view"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.searchDishView();
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

    public void addDishView() {
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Thêm món ăn ==========");
        System.out.print("Tên món ăn: ");
        String name = sc.nextLine();
        if (name.length() < 2) {
            System.out.println("Tên phải có ít nhất 2 ký tự!");
            return;
        }

        for (DishCategory category : DishCategory.values()) {
            System.out.println("|-- " + category.name());
        }
        System.out.print("Danh mục món ăn: ");
        String categoryInput = sc.nextLine();
        DishCategory category = null;
        try {
            category = DishCategory.valueOf(categoryInput.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Danh mục không hợp lệ!");
            return;
        }

        System.out.print("Khẩu phần: ");
        String portionSize = sc.nextLine();
        if (portionSize.length() < 2) {
            System.out.println("Khẩu phần phải có ít nhất 2 ký tự!");
            return;
        }

        System.out.print("Giá: ");
        float price = 0;
        try {
            price = sc.nextFloat();
            sc.nextLine();
            if (price <= 0) {
                System.out.println("Giá phải lớn hơn 0!");
                return;
            }
        } catch (InputMismatchException e) {
            System.out.println("Giá không hợp lệ!");
            return;
        }

        System.out.println("Danh sách nguyên liệu hiện có: ");
        System.out.println(IngredientAdminController.getAllIngredient()
                .stream()
                .map(ingredient -> "|-- " + ingredient.getId() + ". " + ingredient.getName() + " (" + ingredient.getUnit() + ")")
                .collect(Collectors.joining("\n")));
        System.out.print("Nguyên liệu (VD: ID-Số lượng): ");
        String ingredientInput = sc.nextLine();
        if (ingredientInput.isEmpty() || !ingredientInput.trim().matches("^\\d+-\\d+(\\.\\d+)?(,\\d+-\\d+(\\.\\d+)?)*$")) {
            System.out.println("Nhập liệu không hợp lệ! Vui lòng nhập lại.");
            return;
        }

        List<Integer> ingredients = new ArrayList<>();
        List<Float> quantities = new ArrayList<>();
        List<String> units = new ArrayList<>();
        String[] ingredientArray = ingredientInput.split(",");

        for (String ingredient : ingredientArray) {
            String[] parts = ingredient.split("-");

            try {
                int id = Integer.parseInt(parts[0]);
                float quantity = Float.parseFloat(parts[1]);

                if (IngredientAdminController.getAllIngredient().stream().noneMatch(i -> i.getId() == id)) {
                    System.out.println("Nguyên liệu không tồn tại!");
                    return;
                }

                Ingredient ingredientObj = IngredientAdminController.getIngredientById(id);
                String ingredientUnit = ingredientObj.getUnit();

                ingredients.add(id);
                quantities.add(quantity);
                units.add(ingredientUnit);
            } catch (NumberFormatException e) {
                System.out.println("Nhập liệu không hợp lệ! Vui lòng nhập lại.");
                return;
            }
        }

        System.out.print("Mô tả (Không bắt buộc): ");
        String description = sc.nextLine();
        if (!description.isEmpty() && description.length() < 2) {
            System.out.println("Mô tả phải có ít nhất 2 ký tự!");
            return;
        }

        System.out.println("Trạng thái: ");
        System.out.println("|-- 1. Món hot");
        System.out.println("|-- 2. Được bán");
        System.out.println("|-- 3. Ngừng bán");
        System.out.print("Chọn trạng thái: ");
        int status = 0;
        DishStatus dishStatus = null;
        try {
            status = sc.nextInt();
            sc.nextLine();

            switch (status) {
                case 1:
                    dishStatus = DishStatus.HOT;
                    break;
                case 2:
                    dishStatus = DishStatus.ACTIVE;
                    break;
                case 3:
                    dishStatus = DishStatus.INACTIVE;
                    break;
                default:
                    System.out.println("Trạng thái không hợp lệ!");
                    return;
            }
        } catch (InputMismatchException e) {
            System.out.println("Trạng thái không hợp lệ!");
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
        product.setType("0");
        product.setStatus(displayDish);
        product.setDeleteAt(null);

        Dish dish = new Dish();
        dish.setName(name);
        dish.setCategory(category);
        dish.setPortionSize(portionSize);
        dish.setPrice(price);
        dish.setStatus(dishStatus);
        dish.setDescription(description.isEmpty() ? null : description);

        dish.setProduct(product);

        this.onAddDish(dish, ingredients, quantities, units);
    }

    private void onAddDish(Dish dish, List<Integer> ingredients, List<Float> quantities, List<String> units) {
        if (DishAdminController.addDish(dish, ingredients, quantities, units)) {
            System.out.println("Thêm món ăn thành công!");
        } else {
            System.out.println("Thêm món ăn thất bại!");
        }
    }

    public void updateDishView() {
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Sửa món ăn ==========");
        System.out.print("Nhập ID món ăn cần sửa: ");
        int id = sc.nextInt();
        sc.nextLine();
        Dish updateDish = this.checkExistDish(id);
        if (updateDish == null) {
            System.out.println("Món ăn không tồn tại!");
            return;
        }

        System.out.println("Tên món ăn hiện tại: " + updateDish.getName());
        System.out.print("Tên món ăn mới (Không muốn thay đổi, nhấn Enter): ");
        String name = sc.nextLine();
        if (!name.isEmpty() && name.length() < 2) {
            System.out.println("Tên phải có ít nhất 2 ký tự!");
            return;
        }

        for (DishCategory category : DishCategory.values()) {
            System.out.println("|-- " + category.name());
        }
        System.out.println("Danh mục món ăn hiện tại: " + updateDish.getCategory().name());
        System.out.print("Danh mục món ăn mới (Không muốn thay đổi, nhấn Enter): ");
        String categoryInput = sc.nextLine();
        DishCategory category = null;
        try {
            if (!categoryInput.isEmpty()) {
                category = DishCategory.valueOf(categoryInput.toUpperCase());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Danh mục không hợp lệ!");
            return;
        }

        System.out.println("Khẩu phần hiện tại: " + updateDish.getPortionSize());
        System.out.print("Khẩu phần mới (Không muốn thay đổi, nhấn Enter): ");
        String portionSize = sc.nextLine();
        if (!portionSize.isEmpty() && portionSize.length() < 2) {
            System.out.println("Khẩu phần phải có ít nhất 2 ký tự!");
            return;
        }

        System.out.println("Giá hiện tại: " + NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(updateDish.getPrice()));
        System.out.print("Giá mới (Không muốn thay đổi, nhấn -1): ");
        float price = 0;
        try {
            price = sc.nextFloat();
            sc.nextLine();
            if (price != -1 && price <= 0) {
                System.out.println("Giá phải lớn hơn 0!");
                return;
            }
        } catch (InputMismatchException e) {
            System.out.println("Giá không hợp lệ!");
            return;
        }

        System.out.println("Danh sách nguyên liệu hiện có: ");
        System.out.println(IngredientAdminController.getAllIngredient()
                .stream()
                .map(ingredient -> "|-- " + ingredient.getId() + ". " + ingredient.getName())
                .collect(Collectors.joining("\n")));
        System.out.println("Danh sách nguyên liệu hiện tại của món ăn: ");
        System.out.println(DishAdminController.getAllDishIngredient()
                .stream()
                .filter(di -> di.getDishId() == updateDish.getId())
                .map(di -> "|-- " + di.getId() + ". " + di.getIngredient().getName() + " (" + di.getQuantity() + " " + di.getUnit() + ")")
                .collect(Collectors.joining("\n")));
        System.out.print("Nguyên liệu mới (VD: ID-Số lượng-Đơn vị) (Không muốn thay đổi, nhấn Enter): ");
        String ingredientInput = sc.nextLine();
        if (!ingredientInput.isEmpty() && !ingredientInput.trim().matches("^\\d+-\\d+(\\.\\d+)?(,\\d+-\\d+(\\.\\d+)?)*$")) {
            System.out.println("Nhập liệu không hợp lệ! Vui lòng nhập lại.");
            return;
        }

        List<Integer> ingredients = new ArrayList<>();
        List<Float> quantities = new ArrayList<>();
        List<String> units = new ArrayList<>();
        if (!ingredientInput.isEmpty()) {
            String[] ingredientArray = ingredientInput.split(",");

            for (String ingredient : ingredientArray) {
                String[] parts = ingredient.split("-");

                try {
                    int ingredientId = Integer.parseInt(parts[0]);
                    float quantity = Float.parseFloat(parts[1]);

                    if (IngredientAdminController.getAllIngredient().stream().noneMatch(i -> i.getId() == ingredientId)) {
                        System.out.println("Nguyên liệu không tồn tại!");
                        return;
                    }

                    Ingredient ingredientObj = IngredientAdminController.getIngredientById(ingredientId);
                    String ingredientUnit = ingredientObj.getUnit();

                    ingredients.add(ingredientId);
                    quantities.add(quantity);
                    units.add(ingredientUnit);
                } catch (NumberFormatException e) {
                    System.out.println("Nhập liệu không hợp lệ! Vui lòng nhập lại.");
                    return;
                }
            }
        }

        System.out.println("Mô tả hiện tại: " + (updateDish.getDescription() == null ? "Không có" : updateDish.getDescription()));
        System.out.print("Mô tả mới (Không muốn thay đổi, nhấn Enter): ");
        String description = sc.nextLine();
        if (!description.isEmpty() && description.length() < 2) {
            System.out.println("Mô tả phải có ít nhất 2 ký tự!");
            return;
        }

        System.out.println("Trạng thái hiện tại: " + updateDish.getStatus().name());
        System.out.println("|-- 1. Món hot");
        System.out.println("|-- 2. Được bán");
        System.out.println("|-- 3. Ngừng bán");
        System.out.print("Chọn trạng thái mới (Không muốn thay đổi, nhấn số bất kỳ ngoài khoảng): ");
        int status = 0;
        DishStatus dishStatus = null;
        try {
            status = sc.nextInt();
            sc.nextLine();

            if (status != 0) {
                switch (status) {
                    case 1:
                        dishStatus = DishStatus.HOT;
                        break;
                    case 2:
                        dishStatus = DishStatus.ACTIVE;
                        break;
                    case 3:
                        dishStatus = DishStatus.INACTIVE;
                        break;
                    default:
                        break;
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Trạng thái không hợp lệ!");
            return;
        }

        System.out.println("Trạng thái trên menu hiện tại: " + (updateDish.getProduct().isStatus() ? "Hiển thị" : "Ẩn"));
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
                displayDish = updateDish.getProduct().isStatus();
            }
        } catch (InputMismatchException e) {
            System.out.println("Lựa chọn không hợp lệ!");
            return;
        }

        Product product = new Product();
        product.setId(id);
        product.setType("0");
        product.setStatus(displayDish);
        product.setDeleteAt(null);

        updateDish.setId(id);
        updateDish.setName(name.isEmpty() ? updateDish.getName() : name);
        updateDish.setCategory(category == null ? updateDish.getCategory() : category);
        updateDish.setPortionSize(portionSize.isEmpty() ? updateDish.getPortionSize() : portionSize);
        updateDish.setPrice(price == -1 ? updateDish.getPrice() : price);
        updateDish.setStatus(dishStatus == null ? updateDish.getStatus() : dishStatus);
        updateDish.setDescription(description.isEmpty() ? updateDish.getDescription() : description);

        updateDish.setProduct(product);

        this.onUpdateDish(updateDish, ingredients, quantities, units);
    }

    private Dish checkExistDish(int id) {
        return DishAdminController.getDishById(id);
    }

    private void onUpdateDish(Dish updateDish, List<Integer> ingredients, List<Float> quantities, List<String> units) {
        if (DishAdminController.updateDish(updateDish, ingredients, quantities, units)) {
            System.out.println("Sửa món ăn thành công!");
        } else {
            System.out.println("Sửa món ăn thất bại!");
        }
    }

    public void deleteDishView() {
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Xóa món ăn ==========");
        System.out.print("Nhập ID món ăn cần xóa: ");
        int id = sc.nextInt();
        sc.nextLine();
        Dish dish = this.checkExistDish(id);
        if (dish == null) {
            System.out.println("Món ăn không tồn tại!");
            return;
        }

        System.out.println("Bạn có chắc chắn muốn xóa món ăn này không?");
        System.out.println("|-- 1. Chắc chắn");
        System.out.println("|-- 2. Không");
        System.out.print("Chọn 1 lựa chọn: ");
        try {
            switch (sc.nextInt()) {
                case 1:
                    this.onDeleteDish(id);
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

    private void onDeleteDish(int id) {
        if (ProductAdminController.deleteProduct(id)) {
            System.out.println("Xóa món ăn thành công!");
        } else {
            System.out.println("Xóa món ăn thất bại!");
        }
    }

    public void listDishView() {
        System.out.println("========== Danh sách món ăn ==========");

        List<Dish> dishes = DishAdminController.getAllDish();
        this.displayDishList(dishes);
    }

    private void displayDishList(List<Dish> dishIngredients) {
        String[] headers = {"ID", "Tên món ăn", "Danh mục", "Khẩu phần", "Trạng thái", "Giá", "Nguyên liệu", "Trạng thái hiển thị trên menu", "Mô tả"};

        DataTable.printDishTable(headers, dishIngredients);
    }

    public void searchDishView() {
        boolean isClose = false;

        while (!isClose) {
            Scanner sc = new Scanner(System.in);

            System.out.println("========== Tìm kiếm món ăn ==========");
            System.out.println("0. Quay lại (Nhập '0' để quay lại)");
            System.out.println("Phạm vi tìm kiếm: món ăn, nguyên liệu, danh mục, trạng thái, giá, mô tả");
            System.out.print("Nhập từ khóa tìm kiếm: ");
            String query = sc.nextLine();

            if (query.isEmpty()) {
                System.out.println("Từ khóa tìm kiếm không được để trống!");
                return;
            }

            if (query.equals("\'0\'")) {
                isClose = true;
            } else {
                List<Dish> dishes = DishAdminController.searchDish(query);

                this.displayDishList(dishes);
            }
        }
    }
}
