package main.java.model;

import java.time.LocalDate;

public class Warehouse {
    private int id;
    private int ingredientId;
    private float quantity;
    private float reservedQuantity;
    private LocalDate importDate;
    private LocalDate expire;

    public Warehouse() {}

    public Warehouse(int id, int ingredientId, float quantity, float reservedQuantity, LocalDate importDate, LocalDate expire) {
        this.id = id;
        this.ingredientId = ingredientId;
        this.quantity = quantity;
        this.reservedQuantity = reservedQuantity;
        this.importDate = importDate;
        this.expire = expire;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public float getReservedQuantity() {
        return reservedQuantity;
    }

    public void setReservedQuantity(float reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }

    public LocalDate getImportDate() {
        return importDate;
    }

    public void setImportDate(LocalDate importDate) {
        this.importDate = importDate;
    }

    public LocalDate getExpire() {
        return expire;
    }

    public void setExpire(LocalDate expire) {
        this.expire = expire;
    }
}
