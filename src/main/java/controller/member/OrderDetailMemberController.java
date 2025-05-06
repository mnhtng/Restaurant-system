package main.java.controller.member;

import main.java.dao.OrderDetailDAO;
import main.java.model.OrderDetail;

public class OrderDetailMemberController {
    public static boolean addOrderDetail(OrderDetail orderDetail){
        if (!OrderDetailDAO.addOrderDetail(orderDetail)){
            return false;
        }

        return true;
    }
    public static int getTotalOrderDetailNumber(){
        return OrderDetailDAO.getTotalOrderDetailNumber();
    }
}
