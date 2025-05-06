package main.java.controller.admin;

import main.java.dao.WarehouseDAO;
import main.java.model.Warehouse;

import java.util.List;

public class WarehouseAdminController {
    public static List<Warehouse> getAllInventory() {
        return WarehouseDAO.getAllInventory();
    }

    public static Warehouse getInventoryById(int inventoryId) {
        return WarehouseDAO.getInventoryById(inventoryId);
    }

    public static boolean updateInventory(Warehouse updateWarehouse) {
        return WarehouseDAO.updateInventory(updateWarehouse);
    }

    public static boolean deleteInventory(int inventoryId) {
        return WarehouseDAO.deleteInventory(inventoryId);
    }

    public static List<Warehouse> searchInventory(String query) {
        return WarehouseDAO.search(query);
    }
}
