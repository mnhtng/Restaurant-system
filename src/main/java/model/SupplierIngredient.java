package main.java.model;

/**
 * @author MnhTng
 * @Package Models
 * @date 4/16/2025 5:59 PM
 * @Copyright tùng
 */

public class SupplierIngredient {
    private int id;
    private int supplierId;
    private int ingredientId;
    private float defaultPrice;
    private boolean isPrimary;

    public SupplierIngredient() {}

    public SupplierIngredient(int id, int supplierId, int ingredientId, float defaultPrice, boolean isPrimary) {
        this.id = id;
        this.supplierId = supplierId;
        this.ingredientId = ingredientId;
        this.defaultPrice = defaultPrice;
        this.isPrimary = isPrimary;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public float getDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(float defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }
}
