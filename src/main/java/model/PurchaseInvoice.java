package main.java.model;

import java.time.LocalDateTime;

public class PurchaseInvoice {
    private String id;
    private int supplierId;
    private int inventoryClerkId;
    private float totalAmount;
    private LocalDateTime createdAt;

    private Supplier supplier;

    public PurchaseInvoice() {}

    public PurchaseInvoice(String id, int supplierId, int inventoryClerkId, float totalAmount, LocalDateTime createdAt) {
        this.id = id;
        this.supplierId = supplierId;
        this.inventoryClerkId = inventoryClerkId;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
}
