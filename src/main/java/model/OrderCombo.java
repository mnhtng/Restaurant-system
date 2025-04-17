package main.java.model;

/**
 * @author MnhTng
 * @Package Models
 * @date 4/16/2025 6:01 PM
 * @Copyright tùng
 */

public class OrderCombo {
    private int id;
    private int orderId;
    private int comboId;
    private int quantity;
    private float unitPrice;
    private float subTotal;
    private String note;

    public OrderCombo() {}

    public OrderCombo(int id, int orderId, int comboId, int quantity, float unitPrice, float subTotal, String note) {
        this.id = id;
        this.orderId = orderId;
        this.comboId = comboId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subTotal = subTotal;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getComboId() {
        return comboId;
    }

    public void setComboId(int comboId) {
        this.comboId = comboId;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
