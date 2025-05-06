package main.java.controller.admin;

import main.java.dao.DishDAO;
import main.java.dao.DishIngredientDAO;
import main.java.dao.ProductDAO;
import main.java.model.Dish;
import main.java.model.DishIngredient;

import java.util.List;

public class DishAdminController {
    public static List<Dish> getAllDish() {
        return DishDAO.getAllDish();
    }

    public static List<DishIngredient> getAllDishIngredient() {
        return DishIngredientDAO.getAllDishIngredient();
    }

    public static List<DishIngredient> getIngredientByDish(int id) {
        return DishIngredientDAO.getIngredientByDish(id);
    }

    public static Dish getDishById(int id) {
        return DishDAO.getDishById(id);
    }

    public static boolean addDish(Dish dish, List<Integer> ingredients, List<Float> quantities, List<String> units) {
        dish.setId(ProductDAO.addProduct(dish.getProduct()));

        if (dish.getId() == 0) {
            return false;
        }

        if (!DishDAO.addDish(dish)) {
            return false;
        }

        if (!ingredients.isEmpty() && !quantities.isEmpty() && !units.isEmpty()) {
            for (int i = 0; i < ingredients.size(); i++) {
                if (!DishIngredientDAO.addIngredient(dish.getId(), ingredients.get(i), quantities.get(i), units.get(i))) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean updateDish(Dish updateDish, List<Integer> ingredients, List<Float> quantities, List<String> units) {
        if (!ProductDAO.updateProduct(updateDish.getProduct())) {
            return false;
        }

        if (!DishDAO.updateDish(updateDish)) {
            return false;
        }

        if (ingredients.isEmpty() && quantities.isEmpty() && units.isEmpty()) {
            return true;
        }

        if (!DishIngredientDAO.deleteDishIngredient(updateDish.getId())) {
            return false;
        }

        for (int i = 0; i < ingredients.size(); i++) {
            if (!DishIngredientDAO.addIngredient(updateDish.getId(), ingredients.get(i), quantities.get(i), units.get(i))) {
                return false;
            }
        }

        return true;
    }

    public static List<Dish> searchDish(String query) {
        return DishDAO.search(query);
    }
}
