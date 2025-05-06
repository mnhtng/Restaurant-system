package main.java.controller.admin;

import main.java.dao.TableDAO;
import main.java.model.Table;

import java.util.List;

public class TableAdminController {
    public static List<Table> getAllAvailabeTable(){
        return TableDAO.getAllAvailableTable();
    }

    public static boolean updateTableStatus(int id, String newStatus){
        if (!TableDAO.updateTableStatus(id, newStatus)){
            return false;
        }
        return true;
    }

    public static List<Table> getAllTables() {
        return TableDAO.getAllTables();
    }

    public static boolean addTable(Table table) {
        if (TableDAO.addTable(table) == 0){
            return false;
        }

        return true;
    }

    public static boolean updateTable(Table table) {
        return TableDAO.updateTable(table);
    }

    public static boolean deleteTable(int tableId) {
        return TableDAO.deleteTable(tableId);
    }

    public static List<Table> searchTables(String query) {
        return TableDAO.searchTables(query);
    }

    public static Table getTableById(int id) {
        return TableDAO.findTable(id);
    }
}
