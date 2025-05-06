package main.java.controller.admin;

import main.java.dao.OrderDAO;

public class OrderInvoiceAdminController {
    public static boolean updateOrderInvoice(String id, String paymentMethod, float totalAmount){
        if (!OrderDAO.updateOrderInvoice(id, paymentMethod, totalAmount)){
            return false;
        }
        return true;
    }
}
