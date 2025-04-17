package main.java.model;

/**
 * @author MnhTng
 * @Package Models
 * @date 4/16/2025 5:59 PM
 * @Copyright tùng
 */

public class PurchaseInvoiceDetail {
    private int id;
    private int purchaseInvoiceId;
    private int supplierIngredientId;
    private int quantity;
    private float unitPrice;
    private float subTotal;

    public PurchaseInvoiceDetail() {}

    public PurchaseInvoiceDetail(int id, int purchaseInvoiceId, int supplierIngredientId, int quantity, float unitPrice, float subTotal) {
        this.id = id;
        this.purchaseInvoiceId = purchaseInvoiceId;
        this.supplierIngredientId = supplierIngredientId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subTotal = subTotal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPurchaseInvoiceId() {
        return purchaseInvoiceId;
    }

    public void setPurchaseInvoiceId(int purchaseInvoiceId) {
        this.purchaseInvoiceId = purchaseInvoiceId;
    }

    public int getSupplierIngredientId() {
        return supplierIngredientId;
    }

    public void setSupplierIngredientId(int supplierIngredientId) {
        this.supplierIngredientId = supplierIngredientId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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
}
