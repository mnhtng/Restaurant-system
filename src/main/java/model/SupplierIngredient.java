package main.java.model;

public class SupplierIngredient {
    private int id;
    private int supplierId;
    private int ingredientId;
    private float defaultPrice;
    private boolean isPrimary;

    private Supplier supplier;
    private Ingredient ingredient;

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

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }
}
