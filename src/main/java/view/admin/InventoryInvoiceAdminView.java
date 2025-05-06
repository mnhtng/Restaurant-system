package main.java.view.admin;

import main.java.Application;
import main.java.component.DataTable;
import main.java.controller.admin.IngredientAdminController;
import main.java.controller.admin.MemberAdminController;
import main.java.controller.admin.PermissionAdminController;
import main.java.controller.admin.PurchaseInvoiceAdminController;
import main.java.controller.admin.SupplierAdminController;
import main.java.middleware.AuthMiddleware;
import main.java.model.Ingredient;
import main.java.model.Permission;
import main.java.model.PurchaseInvoice;
import main.java.model.PurchaseInvoiceDetail;
import main.java.model.Supplier;
import main.java.model.SupplierIngredient;
import main.java.model.enums.PurchaseInvoiceStatus;
import main.java.util.Session;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class InventoryInvoiceAdminView {
    private boolean closeView = false;

    public InventoryInvoiceAdminView() {
        while (!closeView) {
            this.show();
        }
    }

    public void show() {
        List<Permission> permissions = PermissionAdminController.getPermissionsById(Session.getInstance().getCurrentUser().getId());

        Scanner sc = new Scanner(System.in);

        System.out.println("========== Quản lý hóa đơn nhập kho ==========");
        System.out.println("1. Thêm hóa đơn nhập kho");
        System.out.println("2. Cập nhật hóa đơn nhập kho");
        System.out.println("3. Hiển thị danh sách hóa đơn nhập kho");
        System.out.println("4. Tìm kiếm hóa đơn nhập kho");
        System.out.println("5. Quay lại");
        System.out.print("Chọn chức năng: ");

        try {
            switch (sc.nextInt()) {
                case 1:
                    (new AuthMiddleware()).handle();
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("purchase_invoice.create"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.addInventoryInvoiceView();
                    break;
                case 2:
                    (new AuthMiddleware()).handle();
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("purchase_invoice.update"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.updateInventoryInvoiceView();
                    break;
                case 3:
                    (new AuthMiddleware()).handle();
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("purchase_invoice.view"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.listInventoryInvoiceView();
                    break;
                case 4:
                    (new AuthMiddleware()).handle();
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("purchase_invoice.view"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.searchInventoryInvoiceView();
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

    public void addInventoryInvoiceView() {
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Thêm hóa đơn nhập kho ==========");
        System.out.print("Nhập ID nhà cung cấp: ");
        int supplierId = sc.nextInt();
        sc.nextLine();
        Supplier updateSupplier = this.checkExistSupplier(supplierId);
        if (updateSupplier == null) {
            System.out.println("Nhà cung cấp không tồn tại!");
            return;
        }

        List<SupplierIngredient> ingredientBySupplier = this.getListSupplierIngredient(supplierId);
        System.out.print("Nhà cung cấp '" + updateSupplier.getName() + "' có các nguyên liệu sau: ");
        if (ingredientBySupplier == null || ingredientBySupplier.isEmpty()) {
            System.out.println("[]");
        } else {
            System.out.println();
            System.out.println(ingredientBySupplier
                    .stream()
                    .map(i -> "|-- " + i.getIngredientId() + ". " + i.getIngredient().getName() + " (" + NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(i.getDefaultPrice()) + ")")
                    .collect(Collectors.joining("\n")));
        }

        System.out.print("Nhập những nguyên liệu cần nhập vào kho (VD: ID-Số lượng-Giá): ");
        String importIngredient = sc.nextLine();
        if (importIngredient.isEmpty() || !importIngredient.trim().matches("^\\d+-\\d+(\\.\\d+)?-\\d+(\\.\\d+)?(,\\d+-\\d+(\\.\\d+)?-\\d+(\\.\\d+)?)*$")) {
            System.out.println("Nhập liệu không hợp lệ! Vui lòng nhập lại.");
            return;
        }

        List<Integer> ingredients = new ArrayList<>();
        List<Float> quantities = new ArrayList<>();
        List<Float> prices = new ArrayList<>();
        String[] ingredientPricePairs = importIngredient.split(",");
        for (String pair : ingredientPricePairs) {
            String[] parts = pair.split("-");

            try {
                int ingredientId = Integer.parseInt(parts[0]);
                float quantity = Float.parseFloat(parts[1]);
                float price = Float.parseFloat(parts[2]);

                if (ingredientBySupplier.stream().noneMatch(i -> i.getIngredientId() == ingredientId)) {
                    System.out.println("Nguyên liệu không tồn tại!");
                    return;
                }

                ingredients.add(ingredientId);
                quantities.add(quantity);
                prices.add(price);
            } catch (NumberFormatException e) {
                System.out.println("Nguyên liệu không hợp lệ!");
                return;
            }
        }

        System.out.print("Nhập ghi chú (Nếu có): ");
        String note = sc.nextLine();
        if (note.isEmpty()) {
            note = null;
        }

        PurchaseInvoice purchaseInvoice = new PurchaseInvoice();

        String generateInvoiceId = "PI" + System.currentTimeMillis();
        purchaseInvoice.setId(generateInvoiceId);
        purchaseInvoice.setSupplierId(supplierId);
        purchaseInvoice.setInventoryClerkId(Session.getInstance().getCurrentUser().getId());
        Float totalAmount = 0f;
        for (int i = 0; i < ingredients.size(); i++) {
            float quantity = quantities.get(i);
            float price = prices.get(i);

            totalAmount += quantity * price;
        }
        purchaseInvoice.setTotalAmount(totalAmount);
        purchaseInvoice.setCreatedAt(LocalDateTime.now());

        List<PurchaseInvoiceDetail> purchaseInvoiceDetailList = new ArrayList<>();

        for (int i = 0; i < ingredients.size(); i++) {
            PurchaseInvoiceDetail purchaseInvoiceDetail1 = new PurchaseInvoiceDetail();

            purchaseInvoiceDetail1.setId("PID" + System.currentTimeMillis() + i);
            purchaseInvoiceDetail1.setPurchaseInvoiceId(generateInvoiceId);
            purchaseInvoiceDetail1.setExpectedIngredient(ingredients.get(i));
            purchaseInvoiceDetail1.setActualIngredient(ingredients.get(i));
            purchaseInvoiceDetail1.setExpectedQuantity(quantities.get(i));
            purchaseInvoiceDetail1.setActualQuantity(quantities.get(i));
            purchaseInvoiceDetail1.setUnitPrice(prices.get(i));
            purchaseInvoiceDetail1.setSubTotal(quantities.get(i) * prices.get(i));
            purchaseInvoiceDetail1.setUpdatedBy(Session.getInstance().getCurrentUser().getId());
            purchaseInvoiceDetail1.setUpdatedAt(LocalDateTime.now());
            purchaseInvoiceDetail1.setStatus(PurchaseInvoiceStatus.PENDING);
            purchaseInvoiceDetail1.setNote(note);
            purchaseInvoiceDetail1.setPurchaseInvoice(purchaseInvoice);

            purchaseInvoiceDetailList.add(purchaseInvoiceDetail1);
        }

        this.onAddInventoryInvoice(purchaseInvoice, purchaseInvoiceDetailList);
    }

    private Supplier checkExistSupplier(int supplierId) {
        return SupplierAdminController.getSupplierById(supplierId);
    }

    private List<SupplierIngredient> getListSupplierIngredient(int supplierId) {
        return SupplierAdminController.getSupplierIngredient(supplierId);
    }

    private void onAddInventoryInvoice(PurchaseInvoice purchaseInvoice, List<PurchaseInvoiceDetail> purchaseInvoiceDetailList) {
        if (PurchaseInvoiceAdminController.addInventoryInvoice(purchaseInvoice, purchaseInvoiceDetailList)) {
            System.out.println("Thêm hóa đơn nhập kho thành công!");
        } else {
            System.out.println("Thêm hóa đơn nhập kho thất bại!");
        }
    }

    public void updateInventoryInvoiceView() {
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Cập nhật hóa đơn nhập kho ==========");
        System.out.print("Nhập ID hóa đơn nhập kho: ");
        String invoiceId = sc.nextLine();
        if (invoiceId.isEmpty()) {
            System.out.println("ID hóa đơn nhập kho không được để trống!");
            return;
        }

        PurchaseInvoice purchaseInvoice = this.checkExistPurchaseInvoice(invoiceId);
        if (purchaseInvoice == null) {
            System.out.println("Hóa đơn nhập kho không tồn tại!");
            return;
        }

        // Show details of the invoice
        boolean isClose = false;

        while (!isClose) {
            List<PurchaseInvoiceDetail> purchaseInvoiceDetailList = this.getDetailsByInvoiceId(invoiceId);

            System.out.print("Hóa đơn nhập kho " + invoiceId + ": ");
            if (purchaseInvoiceDetailList == null || purchaseInvoiceDetailList.isEmpty()) {
                System.out.println("[Không có nguyên liệu nào]");
            } else {
                System.out.println();
                System.out.println(purchaseInvoiceDetailList
                        .stream()
                        .map(i -> "|-- ID " + i.getId() + ": " + i.getIngredient().getName() + ", " + i.getActualQuantity() + " " + i.getIngredient().getUnit() + " (Tổng: " + NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(i.getSubTotal()) + ")")
                        .collect(Collectors.joining("\n")));
            }

            System.out.print("Nhập ID hóa đơn chi tiết cần cập nhật (Thoát cập nhật, nhấn 0): ");
            String updateInvoiceId = sc.nextLine();

            if (updateInvoiceId.equals("0")) {
                isClose = true;
            } else if (purchaseInvoiceDetailList.stream().anyMatch(i -> i.getId().equals(updateInvoiceId))) {
                PurchaseInvoiceDetail purchaseInvoiceDetail = purchaseInvoiceDetailList.stream().filter(i -> i.getId().equals(updateInvoiceId)).findFirst().orElse(null);

                if (purchaseInvoiceDetail != null) {
                    List<Ingredient> ingredients = IngredientAdminController.getAllIngredient();

                    System.out.println("Danh sách nguyên liệu hiện có: ");
                    System.out.println(ingredients
                            .stream()
                            .map(i -> "|-- " + i.getId() + ": " + i.getName())
                            .collect(Collectors.joining("\n")));

                    System.out.println("Nguyên liệu dự kiến: " + purchaseInvoiceDetail.getIngredient().getName());
                    System.out.print("Nguyên liệu thực nhận (Không muốn thay đổi, nhấn 0): ");
                    int actualIngredientId = sc.nextInt();
                    sc.nextLine();
                    if (actualIngredientId == 0) {
                        actualIngredientId = purchaseInvoiceDetail.getActualIngredient();
                    } else {
                        int id = actualIngredientId;

                        if (ingredients.stream().noneMatch(i -> i.getId() == id)) {
                            System.out.println("Nguyên liệu không tồn tại!");
                            return;
                        }
                    }

                    System.out.println("Số lượng dự kiến: " + purchaseInvoiceDetail.getExpectedQuantity() + " " + purchaseInvoiceDetail.getIngredient().getUnit());
                    System.out.print("Nhập số lượng thực tế (Không muốn thay đổi, nhấn -1): ");
                    float actualQuantity = sc.nextFloat();
                    sc.nextLine();
                    if (actualQuantity == -1) {
                        actualQuantity = purchaseInvoiceDetail.getActualQuantity();
                    } else if (actualQuantity < 0) {
                        System.out.println("Số lượng không hợp lệ!");
                        return;
                    }

                    System.out.println("Giá theo đơn vị: " + NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(purchaseInvoiceDetail.getUnitPrice()));
                    System.out.print("Nhập giá thực tế (Không muốn thay đổi, nhấn -1): ");
                    float unitPrice = sc.nextFloat();
                    sc.nextLine();
                    if (unitPrice == -1) {
                        unitPrice = purchaseInvoiceDetail.getUnitPrice();
                    } else if (unitPrice < 0) {
                        System.out.println("Giá không hợp lệ!");
                        return;
                    }

                    System.out.println("Danh sách trạng thái hóa đơn nhập kho: ");
                    for (PurchaseInvoiceStatus status : PurchaseInvoiceStatus.values()) {
                        System.out.println(status.name());
                    }
                    System.out.println("Trạng thái hiện tại: " + purchaseInvoiceDetail.getStatus());
                    System.out.print("Nhập trạng thái mới (Không muốn thay đổi, nhấn 0): ");
                    String status = sc.nextLine();
                    if (status.equals("0")) {
                        status = purchaseInvoiceDetail.getStatus().name();
                    } else if (PurchaseInvoiceStatus.valueOf(status.toUpperCase()) == null) {
                        System.out.println("Trạng thái không hợp lệ!");
                        return;
                    }

                    System.out.print("Nhập ghi chú (Nếu có): ");
                    String note = sc.nextLine();
                    if (note.isEmpty()) {
                        note = purchaseInvoiceDetail.getNote();
                    }

                    purchaseInvoiceDetail.setActualIngredient(actualIngredientId);
                    purchaseInvoiceDetail.setActualQuantity(actualQuantity);
                    purchaseInvoiceDetail.setUnitPrice(unitPrice);
                    purchaseInvoiceDetail.setSubTotal(actualQuantity * unitPrice);
                    purchaseInvoiceDetail.setStatus(PurchaseInvoiceStatus.valueOf(status.toUpperCase()));
                    purchaseInvoiceDetail.setNote(note);

                    this.onUpdateInventoryInvoice(purchaseInvoiceDetail);
                }
            } else {
                System.out.println("Hóa đơn không hợp lệ!");
                isClose = true;
            }
        }
    }

    private PurchaseInvoice checkExistPurchaseInvoice(String invoiceId) {
        return PurchaseInvoiceAdminController.getPurchaseInvoiceById(invoiceId);
    }

    private List<PurchaseInvoiceDetail> getDetailsByInvoiceId(String invoiceId) {
        return PurchaseInvoiceAdminController.getPurchaseInvoiceDetailById(invoiceId);
    }

    private void onUpdateInventoryInvoice(PurchaseInvoiceDetail purchaseInvoiceDetail) {
        if (PurchaseInvoiceAdminController.updateInventoryInvoice(purchaseInvoiceDetail)) {
            System.out.println("Cập nhật hóa đơn nhập kho thành công!");
        } else {
            System.out.println("Cập nhật hóa đơn nhập kho thất bại!");
        }
    }

    public void listInventoryInvoiceView() {
        System.out.println("========== Danh sách hóa đơn nhập kho ==========");

        List<PurchaseInvoiceDetail> purchaseInvoices = PurchaseInvoiceAdminController.getAllPurchaseInvoice();
        this.displayPurchaseInvoiceList(purchaseInvoices);
    }

    private void displayPurchaseInvoiceList(List<PurchaseInvoiceDetail> purchaseInvoices) {
        if (purchaseInvoices == null || purchaseInvoices.isEmpty()) {
            System.out.println("Không có hóa đơn nhập kho nào!");
            return;
        }

        Map<String, List<PurchaseInvoiceDetail>> purchaseInvoiceMap = purchaseInvoices
                .stream()
                .collect(Collectors.groupingBy(p -> p.getPurchaseInvoice().getId()));

        for (Map.Entry<String, List<PurchaseInvoiceDetail>> entry : purchaseInvoiceMap.entrySet()) {
            String invoiceId = entry.getKey();
            List<PurchaseInvoiceDetail> details = entry.getValue();

            System.out.println("Hóa đơn nhập kho " + invoiceId + ": ");
            System.out.println("|-- Nhà cung cấp: " + SupplierAdminController.getSupplierById(details.get(0).getPurchaseInvoice().getSupplierId()).getName());
            System.out.println("|-- Nhân viên tạo hóa đơn: " + MemberAdminController.getMemberById(details.get(0).getPurchaseInvoice().getInventoryClerkId()).getName());
            System.out.println("|-- Tổng tiền: " + NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(details.get(0).getPurchaseInvoice().getTotalAmount()));
            System.out.println("|-- Ngày tạo: " + details.get(0).getUpdatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

            String[] headers = {"ID", "Nguyên liệu dự kiến", "Nguyên liệu thực tế", "Số lượng dự kiến", "Số lượng thực tế", "Giá theo đơn vị", "Thành tiền", "Ngày cập nhật", "Người cập nhật", "Trạng thái", "Ghi chú"};
            DataTable.printPurchaseInvoiceTable(headers, details);
        }
    }

    public void searchInventoryInvoiceView() {
        boolean isClose = false;

        while (!isClose) {
            Scanner sc = new Scanner(System.in);

            System.out.println("========== Tìm kiếm hóa đơn nhập kho ==========");
            System.out.println("0. Quay lại (Nhập '0' để quay lại)");
            System.out.println("Phạm vi tìm kiếm: nguyên liệu, nhà cung cấp, nhân viên tạo hóa đơn, ngày tạo/cập nhật, trạng thái, ghi chú");
            System.out.print("Nhập từ khóa tìm kiếm: ");
            String query = sc.nextLine();

            if (query.isEmpty()) {
                System.out.println("Từ khóa tìm kiếm không được để trống!");
                return;
            }

            if (query.equals("\'0\'")) {
                isClose = true;
            } else {
                List<PurchaseInvoiceDetail> purchaseInvoices = PurchaseInvoiceAdminController.searchPurchaseInvoice(query);

                this.displayPurchaseInvoiceList(purchaseInvoices);
            }
        }
    }
}
