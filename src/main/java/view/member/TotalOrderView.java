package main.java.view.member;

import main.java.component.DataTable;
import main.java.controller.member.OrderMemberController;
import main.java.model.Order;
import main.java.util.Session;

import java.util.List;

public class TotalOrderView {
    private boolean closeView = false;

    public TotalOrderView() {
        while (!closeView) {
            show();
        }
    }

    public void show(){
        System.out.println("========== Danh sách đơn đã đặt ==========");
        List<Order> orders = OrderMemberController.getOrdersByCustomerId(Session.getInstance().getCurrentUser().getId());
        if (!orders.isEmpty()) {
            this.displayListOrder(orders);
        }
        else {
            System.out.println("Không có đơn hàng đã đặt!");
        }

        closeView = true;
    }

    public void displayListOrder(List<Order> orders){
        String[] headers = {"ID", "Tên khách hàng", "Mã nhân viên", "Mã bàn", "Thời gian đặt bàn", "Phương thức thanh toán", "Tổng tiền", "Trạng thái", "Thanh toán lúc", "Ghi chứ"};

        DataTable.printOrderTable(headers, orders);
    }
}
