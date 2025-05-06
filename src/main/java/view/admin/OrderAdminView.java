package main.java.view.admin;

import main.java.component.DataTable;
import main.java.controller.admin.ComboAdminController;
import main.java.controller.admin.DishAdminController;
import main.java.controller.admin.MemberAdminController;
import main.java.controller.admin.OrderAdminController;
import main.java.controller.admin.OrderDetailAdminController;
import main.java.controller.admin.OrderInvoiceAdminController;
import main.java.controller.admin.PermissionAdminController;
import main.java.controller.admin.StaffAdminController;
import main.java.controller.admin.TableAdminController;
import main.java.controller.member.OrderDetailMemberController;
import main.java.model.Combo;
import main.java.model.Dish;
import main.java.model.Member;
import main.java.model.Order;
import main.java.model.OrderDetail;
import main.java.model.Permission;
import main.java.model.Staff;
import main.java.model.Table;
import main.java.model.enums.PaymentMethod;
import main.java.model.enums.PaymentStatus;
import main.java.model.enums.Role;
import main.java.util.Session;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class OrderAdminView {
    private boolean closeView = false;

    public OrderAdminView() {
        while (!closeView) {
            show();
        }
    }

    public void show(){
        List<Permission> permissions = PermissionAdminController.getPermissionsById(Session.getInstance().getCurrentUser().getId());

        Scanner sc = new Scanner(System.in);

        System.out.println("========== Quản lý hóa đơn ==========");
        System.out.println("1. Quản lý hóa đơn đặt bàn");
        System.out.println("2. Quản lý hóa đơn thanh toán");
        System.out.println("3. Quay lại");
        System.out.print("Chọn chức năng: ");

        try {
            switch (sc.nextInt()) {
                case 1:
                    if (permissions.stream().noneMatch(p -> List.of("order.view", "order.create", "order.update").contains(p.getSlug()))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.orderView();
                    break;
                case 2:
                    if (permissions.stream().noneMatch(p -> List.of("order.view", "order.create", "order.update").contains(p.getSlug()))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.orderInvoiceView();
                    break;
                case 3:
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

    public void orderView(){
        List<Permission> permissions = PermissionAdminController.getPermissionsById(Session.getInstance().getCurrentUser().getId());

        System.out.println("========== Quản lý đơn đặt bàn==========");
        System.out.println("1. Tạo đơn đặt bàn");
        System.out.println("2. Cập nhật đơn đặt bàn");
        System.out.println("3. Xem danh sách đơn đặt bàn ");
        System.out.println("4. Tìm kiếm đơn đặt bàn");
        System.out.println("5. Quay lại");
        System.out.print("Chọn chức năng: ");

        Scanner sc = new Scanner(System.in);
        try {
            switch (sc.nextInt()) {
                case 1:
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("order.create"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.addOrderView();
                    break;
                case 2:
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("order.update"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.updateOrderView();
                    break;
                case 3:
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("order.view"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.listOrderView();
                    break;
                case 4:
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("order.view"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.searchOrderView();
                    break;
                case 5:
                    this.closeView = true;
                    new OrderAdminView();
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

    public void orderInvoiceView(){
        List<Permission> permissions = PermissionAdminController.getPermissionsById(Session.getInstance().getCurrentUser().getId());

        System.out.println("========== Quản lý hóa đơn thanh toán ==========");
        System.out.println("1. Thanh toán hóa đơn đặt bàn");
        System.out.println("2. Xem danh sách hóa đơn ");
        System.out.println("3. Tìm kiếm hóa đơn theo mã");
        System.out.println("4. Quay lại");
        System.out.print("Chọn chức năng: ");

        Scanner sc = new Scanner(System.in);
        try {
            switch (sc.nextInt()) {
                case 1:
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("order.create"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.addOrderInvoiceView();
                    break;
                case 2:
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("order.view"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.listOrderInvoiceView();
                    break;
                case 3:
                    if (permissions.stream().noneMatch(p -> p.getSlug().equals("order.view"))) {
                        System.out.println("Bạn không có quyền truy cập vào chức năng này.");
                        return;
                    }
                    this.searchOrderInvoiceByIDView();
                    break;
                case 4:
                    this.closeView = true;
                    new OrderAdminView();
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

    private Order checkExistOrderInvoice(String id){
        return OrderAdminController.getUncompletedOrder()
                .stream()
                .filter(orderDetail -> orderDetail.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private Order checkExistOrder(String id){
        return OrderAdminController.getOrderInvoice()
                .stream()
                .filter(orderDetail -> orderDetail.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    public void updateOrderView(){
//        System.out.println(2);
        System.out.println("========== Danh sách hóa đơn chưa hoàn thành ==========");
        List<Order> uncompletedOrder = OrderAdminController.getUncompletedOrder();
        if (!uncompletedOrder.isEmpty()) {
            this.displayListInvoice(uncompletedOrder);

            System.out.print("Nhập mã hóa đơn cần cập nhật: ");
            Scanner sc = new Scanner(System.in);
            String id = sc.nextLine();
            Order orderUpdate = checkExistOrderInvoice(id);
            if (orderUpdate == null) {
                System.out.println("Không tìm thấy hóa đơn cần cập nhật!");
                return;
            }

            System.out.println("Trạng thái hiện tại: " + orderUpdate.getStatus());
            System.out.print("Nhập trạng thái mới (Không muốn thay đổi, nhấn 'Enter'): ");
            String newStatus = sc.nextLine();

            String[] statusArray = {"pending", "cancelled", "reserved", "refunded"};
            if (newStatus.isEmpty() || !Arrays.asList(statusArray).contains(newStatus.toLowerCase())) {
                System.out.println("Trạng thái không có trong danh sách");
                return;
            }

            this.onUpdateOrder(id, newStatus);
        }
        else{
            System.out.println("Không có hóa đơn cần cập nhật!");
        }
    }
    private void onUpdateOrder(String id, String newStatus){
        if (OrderAdminController.updateOrder(id, newStatus)){
            System.out.println("Cập nhật trạng thái hóa đơn thành công ");
        }
        else {
            System.out.println("Đã xảy ra lỗi khi cập nhật trạng thái hóa đơn!");
        }
    }

    private void onUpdateOrderInvoice(String id, String paymentMethod, float totalAmount){
        if (OrderInvoiceAdminController.updateOrderInvoice(id, paymentMethod, totalAmount)){
            System.out.println("Thanh toán hóa đơn " + id + " thành công ");
        }
        else {
            System.out.println("Đã xảy ra lỗi khi thanh toán hóa đơn!");
        }
    }
    public void listOrderView(){
//        System.out.println(3);
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Danh sách hóa đơn đặt bàn ==========");
        List<Order> orderDetails = OrderAdminController.getUncompletedOrder();
        if (!orderDetails.isEmpty()) {
            this.displayListInvoice(orderDetails);
        }
        else{
            System.out.println("Danh sách hóa đơn đặt bàn trống!");
        }
    }
    public void displayListInvoice(List<Order> orders){
        String[] headers = {"ID", "Tên khách hàng", "Mã nhân viên", "Mã bàn", "Thời gian đặt bàn", "Phương thức thanh toán", "Tổng tiền", "Trạng thái", "Thanh toán lúc", "Ghi chứ"};

        DataTable.printOrderTable(headers, orders);
    }
    public void searchOrderView(){
//        System.out.println(4);
        boolean isClose = false;

        while (!isClose) {
            Scanner sc = new Scanner(System.in);
            System.out.println("========== Search Member ==========");
            System.out.println("0. Quay lại (Nhập '0' để quay lại)");
            System.out.println("Phạm vi tìm kiếm: tên khách bàn, mã nhân viên, mã bàn, ngày đặt đơn, phương thức thanh toán, tổng tiền, trạng thái, thời gian thanh toán, ghi chú");
            System.out.print("Nhập từ khóa tìm kiếm: ");
            String query = sc.nextLine();

            if (query.isEmpty()) {
                System.out.println("Từ khóa tìm kiếm không được để trống!");
                return;
            }

            if (query.equals("\'0\'")) {
                isClose = true;
            } else {
                List<Order> matchOrders = OrderAdminController.searchOrder(query);

                String[] headers = {"ID", "Tên khách hàng", "Mã nhân viên", "Mã bàn", "Thời gian đặt bàn", "Phương thức thanh toán", "Tổng tiền", "Trạng thái", "Thanh toán lúc", "Ghi chứ"};
                DataTable.printOrderTable(headers, matchOrders);
            }
        }
    }

    public void addOrderInvoiceView(){
//        System.out.println(1);
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Danh sách đơn đặt bàn==========");
        List<Order> orderDetails = OrderAdminController.getUncompletedOrder();
        if (!orderDetails.isEmpty()) {
            this.displayListInvoice(orderDetails);
        }
        else{
            System.out.println("Danh sách đơn đặt bàn trống!");
        }
        System.out.print("Nhập mã đơn cần thanh toán: ");
        String id = sc.nextLine();
        Order orderUpdate = checkExistOrderInvoice(id);
        if (orderUpdate == null || orderUpdate.getStatus() == PaymentStatus.CANCELLED) {
            System.out.println("Không tìm thấy hóa đơn cần cập nhật!");
            return;
        }
        System.out.println("Chọn phương thức thanh toán: ");
        System.out.println("1. Tiền mặt");
        System.out.println("2. Mã QR");
        System.out.println("3. Thẻ tín dụng");
        String paymentMethod = null;
        try {
            switch (sc.nextInt()) {
                case 1:
                    paymentMethod = "cash";
                    break;
                case 2:
                    paymentMethod = "qr_code";
                    break;
                case 3:
                    paymentMethod = "card";
                    break;
                default:
                    System.out.println("Phương thức thanh toán không hợp lệ! ");
                    break;
            }
            sc.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Lỗi nhập liệu! Vui lòng nhập lại.");
        }

        // Giảm giá
        float totalAmount = orderUpdate.getTotalAmount();
        Member customer = MemberAdminController.getMemberById(orderUpdate.getCustomerId());
        if (customer.getMembershipTier() != null) {
            float discounted = customer.getMembershipTier().getDiscountRate();
            totalAmount -= Math.round(totalAmount*discounted * 100)/100.0f;
        }
        System.out.println("Vui lòng nhập số tiền cần thanh toán (" + totalAmount + " VND ):");
        String inputMoney = sc.nextLine();
        if (!inputMoney.equals(String.valueOf(totalAmount))){
            System.out.println("Số tiền thanh toán không đúng với yêu cầu!");
            return;
        }
        this.onUpdateOrderInvoice(id, paymentMethod, totalAmount);
    }

    public void listOrderInvoiceView(){
//        System.out.println(3);
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Danh sách hóa đơn thanh toán ==========");
        List<Order> orderDetails = OrderAdminController.getOrderInvoice();
        if (!orderDetails.isEmpty()) {
            this.displayListInvoice(orderDetails);
        }
        else{
            System.out.println("Danh sách hóa đơn thanh toán trống!");
        }
    }

    public void searchOrderInvoiceByIDView(){
//        System.out.println(4);
        System.out.print("Nhập mã hóa đơn cần tìm: ");
        Scanner sc = new Scanner(System.in);
        String id = sc.nextLine();
        Order orderUpdate = checkExistOrder(id);
        if (orderUpdate == null) {
            System.out.println("Không tìm thấy hóa đơn !");
            return;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        Member customer = MemberAdminController.getMemberById(orderUpdate.getCustomerId());
        System.out.println("Chi tiết hóa đơn thanh toán");
        System.out.println("-----------------------------------------------");
        System.out.println("Hóa đơn số: " + orderUpdate.getId());
        System.out.println("Ngày hiện tại: " + LocalDateTime.now().format(formatter));
        System.out.println("Tên khách hàng: " + customer.getName());
        System.out.println("Số điện thoại: " + customer.getPhone());
        System.out.println("Bàn số: " + orderUpdate.getTableId());
        System.out.println("-----------------------------------------------");
        System.out.printf(" | %-20s | %-10s | %-15s | %-15s | %n", "Món ăn", "Số lượng", "Đơn giá", "Thành tiền");
        List<OrderDetail> orderDetails = OrderDetailAdminController.findOrderDetailById(orderUpdate.getId());
        for (OrderDetail orderDetail : orderDetails){
            String productName = null;

            Combo combo = ComboAdminController.getComboById(orderDetail.getProductId());
            if (combo != null) {
                productName = combo.getName();
            } else {
                Dish dish = DishAdminController.getDishById(orderDetail.getProductId());
                if (dish != null) {
                    productName = dish.getName();
                }
            }

            if (productName != null) {
                System.out.printf(" | %-20s | %-10d | %-15.2f | %-15.2f | %n", productName, orderDetail.getQuantity(), orderDetail.getUnitPrice(), orderDetail.getSubTotal());
            }
        }
        System.out.println("-----------------------------------------------");
        System.out.println("Tổng tiền: " + orderUpdate.getTotalAmount());
        System.out.println("Phương thức thanh toán: " + orderUpdate.getPaymentMethod().name());
        System.out.println("Trạng thái: Đã thanh toán");
        System.out.println("Ngày thanh toán: " + orderUpdate.getPaidAt().format(formatter));

    }

    public void addOrderView() {
        boolean isClose = false;
        List<Dish> selectedDishes = new ArrayList<>();
        List<Integer> dishQuantities = new ArrayList<>();
        List<Combo> selectedCombos = new ArrayList<>();
        List<Integer> comboQuantities = new ArrayList<>();
        Table selectedTable = null;

        while (!isClose) {
            Scanner sc = new Scanner(System.in);

            System.out.println("========== Danh sách món ăn hiện có ==========");
            List<Dish> dishes = DishAdminController.getAllDish();
            this.displayListDishInMenu(dishes);

            System.out.println("\n========== Danh sách combo hiện có ==========");
            List<Combo> combos = ComboAdminController.getAllCombo();
            this.displayListComboInMenu(combos);

            System.out.println("\n--- Tùy chọn ---");
            System.out.println("1. Chọn món theo ID");
            System.out.println("2. Chọn combo theo ID");
            System.out.println("3. Xem các món đã chọn");
            System.out.println("4. Hoàn tất đặt món");
            System.out.println("5. Hủy và quay lại");
            System.out.print("Chọn chức năng: ");

            try {
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1:
                        System.out.print("Nhập ID món ăn: ");
                        int dishId = sc.nextInt();
                        sc.nextLine();

                        // Tìm món ăn theo ID
                        Dish selectedDish = null;
                        for (Dish dish : dishes) {
                            if (dish.getId() == dishId) {
                                selectedDish = dish;
                                break;
                            }
                        }

                        if (selectedDish != null) {
                            System.out.print("Nhập số lượng: ");
                            int quantity = sc.nextInt();
                            sc.nextLine();

                            if (quantity > 0) {
                                // Kiểm tra xem món đã được chọn trước đó chưa
                                int existingIndex = -1;
                                for (int i = 0; i < selectedDishes.size(); i++) {
                                    if (selectedDishes.get(i).getId() == selectedDish.getId()) {
                                        existingIndex = i;
                                        break;
                                    }
                                }

                                if (existingIndex != -1) {
                                    // Nếu món đã được chọn, cập nhật số lượng
                                    int newQuantity = dishQuantities.get(existingIndex) + quantity;
                                    dishQuantities.set(existingIndex, newQuantity);
                                    System.out.println("Đã cập nhật số lượng " + selectedDish.getName() + " thành " + newQuantity);
                                } else {
                                    // Nếu món chưa được chọn, thêm mới
                                    selectedDishes.add(selectedDish);
                                    dishQuantities.add(quantity);
                                    System.out.println("Đã thêm " + quantity + " " + selectedDish.getName() + " vào đơn hàng.");
                                }
                            } else {
                                System.out.println("Số lượng phải lớn hơn 0!");
                            }
                        } else {
                            System.out.println("Không tìm thấy món ăn với ID: " + dishId);
                        }
                        break;

                    case 2:
                        System.out.print("Nhập ID combo: ");
                        int comboId = sc.nextInt();
                        sc.nextLine();

                        // Tìm combo theo ID
                        Combo selectedCombo = null;
                        for (Combo combo : combos) {
                            if (combo.getId() == comboId) {
                                selectedCombo = combo;
                                break;
                            }
                        }

                        if (selectedCombo != null) {
                            System.out.print("Nhập số lượng: ");
                            int quantity = sc.nextInt();
                            sc.nextLine(); // Đọc bỏ dòng mới

                            if (quantity > 0) {
                                // Kiểm tra xem combo đã được chọn trước đó chưa
                                int existingIndex = -1;
                                for (int i = 0; i < selectedCombos.size(); i++) {
                                    if (selectedCombos.get(i).getId() == selectedCombo.getId()) {
                                        existingIndex = i;
                                        break;
                                    }
                                }

                                if (existingIndex != -1) {
                                    // Nếu combo đã được chọn, cập nhật số lượng
                                    int newQuantity = comboQuantities.get(existingIndex) + quantity;
                                    comboQuantities.set(existingIndex, newQuantity);
                                    System.out.println("Đã cập nhật số lượng " + selectedCombo.getName() + " thành " + newQuantity);
                                } else {
                                    // Nếu combo chưa được chọn, thêm mới
                                    selectedCombos.add(selectedCombo);
                                    comboQuantities.add(quantity);
                                    System.out.println("Đã thêm " + quantity + " " + selectedCombo.getName() + " vào đơn hàng.");
                                }
                            } else {
                                System.out.println("Số lượng phải lớn hơn 0!");
                            }
                        } else {
                            System.out.println("Không tìm thấy combo với ID: " + comboId);
                        }
                        break;

                    case 3:
                        if (selectedDishes.isEmpty() && selectedCombos.isEmpty()) {
                            System.out.println("Bạn chưa chọn món hoặc combo nào.");
                        } else {
                            System.out.println("\n========== Món đã chọn ==========");
                            if (!selectedDishes.isEmpty()) {
                                System.out.printf("| %-5s | %-20s | %-7s | %-15s | %-15s | %n", "ID" , "Tên món", "Số lượng",  "Đơn giá (VND)" , "Thành tiền (VND)");
                                System.out.println("--------------------------------------------------------");

                                for (int i = 0; i < selectedDishes.size(); i++) {
                                    Dish dish = selectedDishes.get(i);
                                    int quantity = dishQuantities.get(i);
                                    float subtotal = dish.getPrice() * quantity;

                                    System.out.printf("| %-5d | %-20s | %-7d | %-15.2f | %-15.2f |\n",
                                            dish.getId(), dish.getName(), quantity, dish.getPrice(), subtotal);
                                }
                                System.out.println("--------------------------------------------------------");
                            }

                            if (!selectedCombos.isEmpty()) {
                                System.out.println("\n========== Combo đã chọn ==========");
                                System.out.printf("| %-5s | %-20s | %-7s | %-15s | %-15s | %n", "ID" , "Tên món", "Số lượng",  "Đơn giá (VND)" , "Thành tiền (VND)");
                                System.out.println("----------------------------------------------------------");

                                for (int i = 0; i < selectedCombos.size(); i++) {
                                    Combo combo = selectedCombos.get(i);
                                    int quantity = comboQuantities.get(i);
                                    float subtotal = combo.getPrice() * quantity;

                                    System.out.printf("| %-5d | %-20s | %-7d | %-15.2f | %-15.2f | %n",
                                            combo.getId(), combo.getName(), quantity, combo.getPrice(), subtotal);
                                }
                                System.out.println("----------------------------------------------------------");
                            }

                            // Tính tổng tiền
                            float totalAmount = 0;
                            for (int i = 0; i < selectedDishes.size(); i++) {
                                totalAmount += selectedDishes.get(i).getPrice() * dishQuantities.get(i);
                            }

                            for (int i = 0; i < selectedCombos.size(); i++) {
                                totalAmount += selectedCombos.get(i).getPrice() * comboQuantities.get(i);
                            }

                            System.out.println("\n Tổng cộng: " + String.format("%15.2f", totalAmount) + " ");
                            System.out.println("------------------------------------------------------------");

                            // Hiển thị bàn đã chọn (nếu có)
                            if (selectedTable != null) {
                                System.out.println("\n========== Bàn đã chọn ==========");
                                System.out.println("Bàn số: " + selectedTable.getId() + " - Số chỗ ngồi: " + selectedTable.getSeatCount());
                            }
                        }
                        break;

                    case 4:
                        if (selectedDishes.isEmpty() && selectedCombos.isEmpty()) {
                            System.out.println("Bạn chưa chọn món hoặc combo nào. Vui lòng chọn ít nhất một món hoặc combo.");
                        } else {
                            // Hiển thị danh sách bàn khả dụng
                            System.out.println("\n========== Danh sách bàn khả dụng ==========");
                            List<Table> availableTables = TableAdminController.getAllAvailabeTable();

                            if (availableTables.isEmpty()) {
                                System.out.println("Không có bàn nào khả dụng. Không thể hoàn tất đặt món!");
                                break;
                            }

                            this.displayListTableAvailable(availableTables);

                            // Cho phép người dùng chọn bàn
                            System.out.print("\nNhập ID bàn : ");
                            int tableId = sc.nextInt();
                            sc.nextLine();

                            // Tìm bàn theo ID
                            selectedTable = null;
                            for (Table table : availableTables) {
                                if (table.getId() == tableId) {
                                    selectedTable = table;
                                    break;
                                }
                            }

                            if (selectedTable == null) {
                                System.out.println("Không tìm thấy bàn với ID: " + tableId);
                                break;
                            }
                            LocalDateTime expectedDate = LocalDateTime.now();
                            LocalDateTime orderAt = LocalDateTime.now();


                            // Tính tổng tiền
                            float totalAmount = 0;
                            for (int i = 0; i < selectedDishes.size(); i++) {
                                totalAmount += selectedDishes.get(i).getPrice() * dishQuantities.get(i);
                            }

                            for (int i = 0; i < selectedCombos.size(); i++) {
                                totalAmount += selectedCombos.get(i).getPrice() * comboQuantities.get(i);
                            }

                            System.out.println("Nhập tên khách hàng: ");
                            String name = sc.nextLine();

                            Member customer = findExistMember(name);
                            if (customer == null){
                                System.out.println("Không tìm thấy khách hàng trong cơ sở dữ liệu!");
                                return;
                            }
                            String phone = customer.getPhone();
                            // Hiển thị thông tin đơn hàng
                            System.out.println("\n========== Thông tin đơn hàng ==========");
                            System.out.println("Khách hàng: " + name);
                            System.out.println("Số điện thoại: " + phone);
                            System.out.println("Bàn số: " + selectedTable.getId());
                            System.out.println("Tổng tiền: " + String.format("%.2f", totalAmount) + " VND");


                            // Xác nhận đặt bàn
                            System.out.print("\nXác nhận đặt bàn? (Y/N): ");
                            String confirm = sc.nextLine();


                            if (confirm.equalsIgnoreCase("Y")) {
                                // Cập nhật trạng thái bàn thành
                                if (TableAdminController.updateTableStatus(selectedTable.getId(), "occupied")){
                                    System.out.println("Đặt bàn thành công!");
                                }
                                else{
                                    System.out.println("Đã có lỗi xảy ra khi đặt bàn!");
                                }
                                int totalOrder = OrderAdminController.getTotalOrderNumber();
                                int totalOrderDetail = OrderDetailAdminController.getTotalOrderDetailNumber();
                                // Tạo class Order
                                Order order = new Order();

                                String orderId = "ORD" + (totalOrder + 1);
                                int customerId = customer.getId();
                                int serviceClerkID = Session.getInstance().getCurrentUser().getId();
                                PaymentMethod paymentMethod = PaymentMethod.CASH;
                                PaymentStatus paymentStatus = PaymentStatus.PENDING;

                                order.setId(orderId);
                                order.setCustomerId(customerId);
                                order.setServiceClerkId(serviceClerkID);
                                order.setTableId(tableId);
                                order.setOrderTime(orderAt);
                                order.setExpectedArrivalTime(expectedDate);
                                order.setPaymentMethod(paymentMethod);
                                order.setTotalAmount(totalAmount);
                                order.setStatus(paymentStatus);
                                order.setPaidAt(LocalDateTime.now());
                                order.setNote(null);

                                //Insert Order
                                OrderAdminController.addOrder(order);

                                // Tạo class OrderDetail
                                for (int i = 0; i < selectedDishes.size(); i++) {
                                    OrderDetail orderDetail = new OrderDetail();
                                    String orderDetailId = "OD" + (totalOrderDetail + 1)  + "-" + (i + 1);

                                    int productId = selectedDishes.get(i).getId();
                                    int quantities = dishQuantities.get(i);
                                    Float unitPrice = selectedDishes.get(i).getPrice();
                                    Float subTotal = unitPrice * (float) quantities;
                                    orderDetail.setId(orderDetailId);
                                    orderDetail.setOrderId(orderId);
                                    orderDetail.setProductId(productId);
                                    orderDetail.setQuantity(quantities);
                                    orderDetail.setUnitPrice(unitPrice);
                                    orderDetail.setSubTotal(subTotal);
                                    orderDetail.setNote(null);

                                    // Thêm OrderDetail vào cơ sở dữ liệu
                                    OrderDetailAdminController.addOrderDetail(orderDetail);
                                }

                                // Thêm các combo đã chọn vào OrderDetail
                                for (int i = 0; i < selectedCombos.size(); i++) {
                                    OrderDetail orderDetail = new OrderDetail();
                                    String orderDetailId = "OD" + (totalOrderDetail + selectedDishes.size() + 1) + "-" + (i + 1);

                                    int productId = selectedCombos.get(i).getId();
                                    int quantities = comboQuantities.get(i);
                                    Float unitPrice = selectedCombos.get(i).getPrice();
                                    Float subTotal = unitPrice * (float) quantities;
                                    orderDetail.setId(orderDetailId);
                                    orderDetail.setOrderId(orderId);
                                    orderDetail.setProductId(productId);
                                    orderDetail.setQuantity(quantities);
                                    orderDetail.setUnitPrice(unitPrice);
                                    orderDetail.setSubTotal(subTotal);
                                    orderDetail.setNote(null);

                                    // Thêm OrderDetail vào cơ sở dữ liệu
                                    OrderDetailMemberController.addOrderDetail(orderDetail);
                                }

                                System.out.println("Đã thêm thông tin order thành công!");

                                isClose = true;
                            } else {
                                System.out.println("Đã hủy xác nhận đặt bàn.");
                                selectedTable = null;
                            }
                        }
                        break;

                    case 5:
                        System.out.println("Đã hủy đặt món.");
                        isClose = true;
                        break;

                    default:
                        System.out.println("Lựa chọn không hợp lệ!");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Lỗi nhập liệu! Vui lòng nhập lại.");
                sc.nextLine();
            }

            if (!isClose) {
                System.out.println("\n-----------------------------------------\n");
            }
        }
    }


    public void displayListDishInMenu(List<Dish> dishes){
        String[] headers = {"ID", "Tên món", "Loại", "Định lượng", "Giá (VND)", "Mô tả"};

        DataTable.printDishTable(headers, dishes);
    }

    public void displayListComboInMenu(List<Combo> combos){
        String[] headers = {"ID", "Tên combo", "Giá (VND)", "Mô tả"};

        DataTable.printComboTable(headers, combos);
    }

    public void displayListTableAvailable(List<Table> tables){
        String[] headers = {"ID", "Số ghế"};

        DataTable.printTableTable(headers, tables);
    }

    public static Staff getServiceClerk() {
        List<Staff> allStaffs = StaffAdminController.getAllStaffs();

        if (allStaffs == null) {
            return null;
        }

        return allStaffs.stream()
                .filter(staff -> staff.getMember() != null)
                .filter(staff -> staff.getMember().getRole() == Role.SERVICE_CLERK)
                .findFirst()
                .orElse(null);
    }

    public static Member findExistMember(String name){
        List<Member> allMem = MemberAdminController.getAllMembers();

        if (allMem == null){
            return null;
        }

        return allMem.stream()
                .filter(member -> member.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
