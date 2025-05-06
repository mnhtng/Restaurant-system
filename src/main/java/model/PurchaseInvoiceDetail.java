package main.java.model;

import main.java.model.enums.PurchaseInvoiceStatus;

import java.time.LocalDateTime;

public class PurchaseInvoiceDetail {
    private String id;
    private String purchaseInvoiceId;
    private int expectedIngredient;
    private int actualIngredient;
    private float expectedQuantity;
    private float actualQuantity;
    private float unitPrice;
    private float subTotal;
    private LocalDateTime updatedAt;
    private int updatedBy;
    private PurchaseInvoiceStatus status;
    private String note;

    private PurchaseInvoice purchaseInvoice;
    private Ingredient ingredient;
    private Staff staff;

    public PurchaseInvoiceDetail() {}

    public PurchaseInvoiceDetail(String id, String purchaseInvoiceId, int expectedIngredient, int actualIngredient, float expectedQuantity, float actualQuantity, float unitPrice, float subTotal, LocalDateTime updatedAt, int updatedBy, PurchaseInvoiceStatus status, String note) {
        this.id = id;
        this.purchaseInvoiceId = purchaseInvoiceId;
        this.expectedIngredient = expectedIngredient;
        this.actualIngredient = actualIngredient;
        this.expectedQuantity = expectedQuantity;
        this.actualQuantity = actualQuantity;
        this.unitPrice = unitPrice;
        this.subTotal = subTotal;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
        this.status = status;
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPurchaseInvoiceId() {
        return purchaseInvoiceId;
    }

    public void setPurchaseInvoiceId(String purchaseInvoiceId) {
        this.purchaseInvoiceId = purchaseInvoiceId;
    }

    public int getExpectedIngredient() {
        return expectedIngredient;
    }

    public void setExpectedIngredient(int expectedIngredient) {
        this.expectedIngredient = expectedIngredient;
    }

    public int getActualIngredient() {
        return actualIngredient;
    }

    public void setActualIngredient(int actualIngredient) {
        this.actualIngredient = actualIngredient;
    }

    public float getExpectedQuantity() {
        return expectedQuantity;
    }

    public void setExpectedQuantity(float expectedQuantity) {
        this.expectedQuantity = expectedQuantity;
    }

    public float getActualQuantity() {
        return actualQuantity;
    }

    public void setActualQuantity(float actualQuantity) {
        this.actualQuantity = actualQuantity;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public float getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(float subTotal) {
        this.subTotal = subTotal;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(int updatedBy) {
        this.updatedBy = updatedBy;
    }

    public PurchaseInvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(PurchaseInvoiceStatus status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public PurchaseInvoice getPurchaseInvoice() {
        return purchaseInvoice;
    }

    public void setPurchaseInvoice(PurchaseInvoice purchaseInvoice) {
        this.purchaseInvoice = purchaseInvoice;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }
}
