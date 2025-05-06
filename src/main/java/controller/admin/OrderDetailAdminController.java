package main.java.controller.admin;

import main.java.dao.OrderDetailDAO;
import main.java.model.OrderDetail;

import java.util.List;

public class OrderDetailAdminController {
    public static List<OrderDetail> findOrderDetailById(String id){
        return OrderDetailDAO.findOrderDetailById(id);
    }

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
