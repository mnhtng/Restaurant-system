package main.java.controller.member;

import main.java.dao.OrderDAO;
import main.java.model.Order;

import java.util.List;

public class OrderMemberController {
    public static boolean addOrder(Order order){
        if (OrderDAO.addOrder(order) == false){
            return false;
        }
        return true;
    }

    public static int getTotalOrderNumber(){
        return OrderDAO.getTotalOrderNumber();
    }

    public static List<Order> getOrdersByCustomerId(int customerId){
        return OrderDAO.getOrdersByCustomerId(customerId);
    }

    public static boolean cancelOrder(String orderId){
        if (OrderDAO.cancelOrder(orderId) == false){
            return false;
        }
        return true;
    }

    public static List<Order> getAllOrder(){
        return OrderDAO.getAllOrder();
    }
}
