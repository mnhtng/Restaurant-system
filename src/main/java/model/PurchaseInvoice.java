package main.java.model;

import java.time.LocalDate;

/**
 * @author MnhTng
 * @Package Models
 * @date 4/16/2025 5:59 PM
 * @Copyright tùng
 */

public class PurchaseInvoice {
    private int id;
    private int supplierId;
    private int inventoryClerkId;
    private float totalAmount;
    private LocalDate createdAt;

    public PurchaseInvoice() {}

    public PurchaseInvoice(int id, int supplierId, int inventoryClerkId, float totalAmount, LocalDate createdAt) {
        this.id = id;
        this.supplierId = supplierId;
        this.inventoryClerkId = inventoryClerkId;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
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

    public int getInventoryClerkId() {
        return inventoryClerkId;
    }

    public void setInventoryClerkId(int inventoryClerkId) {
        this.inventoryClerkId = inventoryClerkId;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
}
