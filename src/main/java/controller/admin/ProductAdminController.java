package main.java.controller.admin;

import main.java.dao.ProductDAO;

public class ProductAdminController {
    public static boolean deleteProduct(int id) {
        return ProductDAO.deleteProduct(id);
    }
}
