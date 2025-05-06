package main.java.controller.admin;

import main.java.dao.IngredientDAO;
import main.java.model.Ingredient;

import java.util.List;

public class IngredientAdminController {
    public static List<Ingredient> getAllIngredient() {
        return IngredientDAO.getAllIngredient();
    }

    public static Ingredient getIngredientById(int id) {
        return IngredientDAO.findIngredient(id);
    }

    public static boolean addIngredient(Ingredient ingredient) {
        return IngredientDAO.addIngredient(ingredient);
    }

    public static boolean updateIngredient(Ingredient updateIngredient) {
        return IngredientDAO.updateIngredient(updateIngredient);
    }

    public static boolean deleteIngredient(int ingredientId) {
        return IngredientDAO.deleteIngredient(ingredientId);
    }

    public static List<Ingredient> searchIngredient(String query, String category) {
        return IngredientDAO.search(query, category);
    }
}
