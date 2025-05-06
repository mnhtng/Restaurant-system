package main.java.view.member;

import main.java.component.DataTable;
import main.java.controller.admin.ComboAdminController;
import main.java.controller.admin.DishAdminController;
import main.java.controller.admin.StaffAdminController;
import main.java.controller.admin.TableAdminController;
import main.java.controller.member.OrderDetailMemberController;
import main.java.controller.member.OrderMemberController;
import main.java.model.Combo;
import main.java.model.Dish;
import main.java.model.Order;
import main.java.model.OrderDetail;
import main.java.model.Staff;
import main.java.model.Table;
import main.java.model.enums.PaymentMethod;
import main.java.model.enums.PaymentStatus;
import main.java.model.enums.Role;
import main.java.util.Session;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class OrderMemberView {
    private boolean closeView = false;

    public OrderMemberView() {
        while (!closeView) {
            show();
        }
    }

    public void show() {
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Hệ thống đặt bàn ==========");
        System.out.println("1. Bắt đầu đặt bàn");
        System.out.println("2. Quay lại");
        System.out.print("Chọn chức năng: ");

        try {
            switch (sc.nextInt()) {
                case 1:
                    this.addOrderView();
                    break;
                case 2:
                    closeView = true;
                    new MainMemberView();
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
            System.out.print("Lựa chọn của bạn: ");

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
                            System.out.print("\nNhập ID bàn bạn muốn đặt: ");
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
                            // Chọn giờ đặt trước
                            System.out.print("\nNhập giờ bạn muốn đặt (Định dạng dd/MM/yyyy HH:mm:ss): ");
                            String expectedDateStr = sc.nextLine();
                            LocalDateTime expectedDate = LocalDateTime.now();
                            LocalDateTime orderAt = LocalDateTime.now();
                            try {
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                                expectedDate = LocalDateTime.parse(expectedDateStr, formatter);

                                if (expectedDate.isBefore(orderAt)){
                                    System.out.println("Thời gian đặt trước không hợp lệ!");
                                    break;
                                }

                                System.out.println("Thời gian đặt trước của bạn là: " + expectedDate);
                            } catch (DateTimeParseException e) {
                                System.out.println("Định dạng ngày giờ không hợp lệ. Vui lòng nhập theo định dạng dd/MM/yyyy HH:mm:ss");
                                return;
                            }

                            // Tính tổng tiền
                            float totalAmount = 0;
                            for (int i = 0; i < selectedDishes.size(); i++) {
                                totalAmount += selectedDishes.get(i).getPrice() * dishQuantities.get(i);
                            }

                            for (int i = 0; i < selectedCombos.size(); i++) {
                                totalAmount += selectedCombos.get(i).getPrice() * comboQuantities.get(i);
                            }

                            // Hiển thị thông tin đơn hàng
                            System.out.println("\n========== Thông tin đơn hàng ==========");
                            System.out.println("Khách hàng: " + Session.getInstance().getCurrentUser().getName());
                            System.out.println("Số điện thoại: " + Session.getInstance().getCurrentUser().getPhone());
                            System.out.println("Bàn số: " + selectedTable.getId());
                            System.out.println("Tổng tiền: " + String.format("%.2f", totalAmount) + " VND");


                            // Xác nhận đặt bàn
                            System.out.print("\nXác nhận đặt bàn? (Y/N): ");
                            String confirm = sc.nextLine();

                            float reserveMoney;
                            Duration duration = Duration.between(orderAt, expectedDate);
                            long days = duration.toDays();
                            if (days <=1 ){
                                reserveMoney = Math.round(totalAmount*0.1f * 100)/100.0f;
                            }
                            else{
                                reserveMoney = Math.round(totalAmount*0.1f * 100)/100.0f;
                            }

                            if (confirm.equalsIgnoreCase("Y")) {
                                // Thanh toán tiền cọc
                                System.out.println("Vui lòng thanh toán tiền cọc trước (" + reserveMoney + " VND): ");
//                                System.out.println(reserveMoney);
                                String inputMoney = sc.nextLine();
                                if (!inputMoney.equals(String.valueOf(reserveMoney))){
                                    System.out.println("Số tiền thanh toán không đúng với yêu cầu!");
                                    break;
                                }
                                totalAmount -= reserveMoney;

                                // Cập nhật trạng thái bàn thành RESERVED
                                if (TableAdminController.updateTableStatus(selectedTable.getId(), "reserved")){
                                    System.out.println("Đặt bàn thành công!");
                                }
                                else{
                                    System.out.println("Đã có lỗi xảy ra khi đặt bàn!");
                                }
                                int totalOrder = OrderMemberController.getTotalOrderNumber();
                                int totalOrderDetail = OrderDetailMemberController.getTotalOrderDetailNumber();
                                // Tạo class Order
                                Order order = new Order();

                                String orderId = "ORD" + (totalOrder + 1);
                                int customerId = Session.getInstance().getCurrentUser().getId();
                                Staff serviceClerk = getServiceClerk();
                                int serviceClerkID = serviceClerk.getMember().getId();
                                PaymentMethod paymentMethod = PaymentMethod.CASH;
                                PaymentStatus paymentStatus = PaymentStatus.RESERVED;

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
                                OrderMemberController.addOrder(order);

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
                                    OrderDetailMemberController.addOrderDetail(orderDetail);
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
                                selectedTable = null; // Reset bàn đã chọn
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
}
