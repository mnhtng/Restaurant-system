package main.java.model;

public class ComboDetail {
    private int id;
    private int comboId;
    private int dishId;
    private int quantity;

    private Dish dish;
    private Combo combo;

    public ComboDetail() {}

    public ComboDetail(int id, int comboId, int dishId, int quantity) {
        this.id = id;
        this.comboId = comboId;
        this.dishId = dishId;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getComboId() {
        return comboId;
    }

    public void setComboId(int comboId) {
        this.comboId = comboId;
    }

    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public Combo getCombo() {
        return combo;
    }

    public void setCombo(Combo combo) {
        this.combo = combo;
    }
}
