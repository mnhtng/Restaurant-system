package main.java.model;

import main.java.model.enums.PaymentMethod;
import main.java.model.enums.PaymentStatus;

import java.time.LocalDate;

/**
 * @author MnhTng
 * @Package Models
 * @date 4/16/2025 6:00 PM
 * @Copyright tùng
 */

public class OrderInvoice {
    private int id;
    private int orderId;
    private PaymentMethod paymentMethod;
    private float totalAmount;
    private PaymentStatus status;
    private LocalDate paidAt;

    public OrderInvoice() {}

    public OrderInvoice(int id, int orderId, PaymentMethod paymentMethod, float totalAmount, PaymentStatus status, LocalDate paidAt) {
        this.id = id;
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.totalAmount = totalAmount;
        this.status = status;
        this.paidAt = paidAt;
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

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public LocalDate getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDate paidAt) {
        this.paidAt = paidAt;
    }
}
