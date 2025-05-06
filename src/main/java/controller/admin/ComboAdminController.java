package main.java.controller.admin;

import main.java.dao.ComboDAO;
import main.java.dao.ComboDetailDAO;
import main.java.dao.ProductDAO;
import main.java.model.Combo;
import main.java.model.ComboDetail;

import java.util.List;

public class ComboAdminController {
    public static List<Combo> getAllCombo() {
        return ComboDAO.getAllCombo();
    }

    public static List<ComboDetail> getComboDetailById(int id) {
        return ComboDetailDAO.getComboDetailById(id);
    }

    public static Combo getComboById(int id) {
        return ComboDAO.getComboById(id);
    }

    public static boolean addCombo(Combo combo, List<Integer> dishes, List<Integer> quantities) {
        combo.setId(ProductDAO.addProduct(combo.getProduct()));

        if (combo.getId() == 0) {
            return false;
        }

        if (!ComboDAO.addCombo(combo)) {
            return false;
        }

        for (int i = 0; i < dishes.size(); i++) {
            if (!ComboDetailDAO.addComboDetail(combo.getId(), dishes.get(i), quantities.get(i))) {
                return false;
            }
        }

        return true;
    }

    public static boolean updateCombo(Combo updatedCombo, List<Integer> dishes, List<Integer> quantities) {
        if (!ProductDAO.updateProduct(updatedCombo.getProduct())) {
            return false;
        }

        if (!ComboDAO.updateCombo(updatedCombo)) {
            return false;
        }

        if (dishes.isEmpty() && quantities.isEmpty()) {
            return true;
        }

        if (!ComboDetailDAO.deleteComboDetail(updatedCombo.getId())) {
            return false;
        }

        for (int i = 0; i < dishes.size(); i++) {
            if (!ComboDetailDAO.addComboDetail(updatedCombo.getId(), dishes.get(i), quantities.get(i))) {
                return false;
            }
        }

        return true;
    }

    public static List<ComboDetail> getDishByCombo(int id) {
        return ComboDetailDAO.getDishByCombo(id);
    }

    public static List<Combo> searchCombo(String query) {
        return ComboDAO.search(query);
    }
}
