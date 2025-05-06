package main.java.controller.admin;

import main.java.dao.OrderDAO;
import main.java.model.Order;

import java.util.List;

public class OrderAdminController {
    public static List<Order> getAllOrder(){
        return OrderDAO.getAllOrder();
    }

    public static List<Order> getUncompletedOrder(){
        return OrderDAO.getUncompletedOrder();
    }

    public static boolean updateOrder(String id, String newStatus){
        if (!OrderDAO.updateOrder(id, newStatus)){
            return false;
        }
        return true;
    }

    public static List<Order> searchOrder(String query){
        return OrderDAO.searchOrder(query);
    }

    public static List<Order> getOrderInvoice(){
        return OrderDAO.getOrderInvoice();
    }

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
}
