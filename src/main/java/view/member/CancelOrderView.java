package main.java.view.member;

import main.java.component.DataTable;
import main.java.controller.admin.TableAdminController;
import main.java.controller.member.OrderMemberController;
import main.java.middleware.AuthMiddleware;
import main.java.model.Order;
import main.java.util.Session;

import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class CancelOrderView {
    private boolean closeView = false;

    public CancelOrderView() {
        while (!closeView) {
            show();
        }
    }

    public void show() {
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Hủy đơn đặt bàn ==========");
        System.out.println("1. Hủy đơn");
        System.out.println("2. Quay lại");
        System.out.print("Chọn chức năng: ");

        try {
            switch (sc.nextInt()) {
                case 1:
                    (new AuthMiddleware()).handle();
                    this.cancelOrderView();
                    break;
                case 2:
                    (new AuthMiddleware()).handle();
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

    public void cancelOrderView(){
        Scanner sc = new Scanner(System.in);

        int customerId = Session.getInstance().getCurrentUser().getId();

        List<Order> customerOrders = OrderMemberController.getOrdersByCustomerId(customerId);

        if (customerOrders == null || customerOrders.isEmpty()) {
            System.out.println("Bạn không có đơn hàng nào để hủy!");
            return;
        }

        System.out.println("\n===== Danh sách đơn hàng có thể hủy =====");
        this.displayListOrder(customerOrders);


        System.out.print("\nNhập mã đơn hàng cần hủy (hoặc nhập '0' để quay lại): ");
        String orderId = sc.nextLine().trim();

        if (orderId.equalsIgnoreCase("0")) {
            return;
        }

        Order orderToCancel = null;
        for (Order order : customerOrders) {
            if (order.getId().equals(orderId)) {
                orderToCancel = order;
                break;
            }
        }

        if (orderToCancel == null) {
            System.out.println("Mã đơn hàng không hợp lệ!");
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        System.out.println("\nThông tin đơn hàng:");
        System.out.println("Mã đơn: " + orderToCancel.getId());
        System.out.println("Thời gian đặt bàn: " + orderToCancel.getOrderTime().format(formatter));
        System.out.println("Thời gian đến dự kiến: " + orderToCancel.getExpectedArrivalTime().format(formatter));
        System.out.println("Tổng tiền: " + orderToCancel.getTotalAmount());
        System.out.println("Trạng thái: " + orderToCancel.getStatus());

        System.out.print("\nBạn có chắc chắn muốn hủy đơn hàng này? (Y/N): ");
        String confirm = sc.nextLine().trim();

        if (confirm.equalsIgnoreCase("Y")) {

            if (OrderMemberController.cancelOrder(orderId)) {
                TableAdminController.updateTableStatus(orderToCancel.getTableId(), "available");

                System.out.println("Hủy đơn hàng thành công!");
            } else {
                System.out.println("Hủy đơn hàng thất bại! Vui lòng thử lại sau.");
            }
        } else {
            System.out.println("Đã hủy thao tác!");
        }
        closeView = true;

    }

    public void displayListOrder(List<Order> orders){
        String[] headers = {"ID", "Tên khách hàng", "Mã nhân viên", "Mã bàn", "Thời gian đặt bàn", "Phương thức thanh toán", "Tổng tiền", "Trạng thái", "Thanh toán lúc", "Ghi chứ"};

        DataTable.printOrderTable(headers, orders);
    }
}
