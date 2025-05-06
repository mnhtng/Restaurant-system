package main.java.controller.admin;

import main.java.dao.SupplierDAO;
import main.java.dao.SupplierIngredientDAO;
import main.java.model.Supplier;
import main.java.model.SupplierIngredient;

import java.util.List;

public class SupplierAdminController {
    public static List<Supplier> getAllSupplier() {
        return SupplierIngredientDAO.getAllSupplier();
    }

    public static Supplier getSupplierById(int suppilerId) {
        return SupplierDAO.getSupplierById(suppilerId);
    }

    public static List<SupplierIngredient> getSupplierIngredient(int supplierId) {
        return SupplierIngredientDAO.getIngredientBySupplier(supplierId);
    }

    public static boolean addSupplier(Supplier supplier) {
        return SupplierDAO.addSupplier(supplier);
    }

    public static boolean updateSupplier(Supplier supplier, List<Integer> ingredients, List<Float> prices) {
        if (!SupplierDAO.updateSupplier(supplier)) {
            return false;
        }

        if (!ingredients.isEmpty() && !prices.isEmpty()) {
            if (SupplierIngredientDAO.getSupplierIngredientById(supplier.getId()) != null && !SupplierIngredientDAO.getSupplierIngredientById(supplier.getId()).isEmpty()) {
                if (!SupplierIngredientDAO.deleteSupplierIngredient(supplier.getId())) {
                    return false;
                }
            }

            for (int i = 0; i < ingredients.size(); i++) {
                if (!SupplierIngredientDAO.addSupplierIngredient(supplier.getId(), ingredients.get(i), prices.get(i))) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean setMainSupplier(int supplierId, String[] primaryIngredientParts) {
        if (!SupplierIngredientDAO.resetMainSupplier(supplierId)) {
            return false;
        }

        for (String ingredientId : primaryIngredientParts) {
            if (!SupplierIngredientDAO.setMainSupplier(supplierId, Integer.parseInt(ingredientId))) {
                return false;
            }
        }

        return true;
    }

    public static boolean deleteSupplier(int supplierId) {
        if (!SupplierDAO.deleteSupplier(supplierId)) {
            return false;
        }

        if (SupplierIngredientDAO.getSupplierIngredientById(supplierId) != null && !SupplierIngredientDAO.getSupplierIngredientById(supplierId).isEmpty()) {
            if (!SupplierIngredientDAO.deleteSupplierIngredient(supplierId)) {
                return false;
            }
        }

        return true;
    }

    public static List<Supplier> searchSupplier(String query) {
        if (SupplierDAO.search(query) != null && !SupplierDAO.search(query).isEmpty()) {
            return SupplierDAO.search(query);
        } else {
            return SupplierIngredientDAO.search(query);
        }
    }
}
