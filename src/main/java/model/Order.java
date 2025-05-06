package main.java.model;

import main.java.model.enums.PaymentMethod;
import main.java.model.enums.PaymentStatus;

import java.time.LocalDateTime;

public class Order {
    private String id;
    private int customerId;
    private int serviceClerkId;
    private int tableId;
    private LocalDateTime orderTime;
    private LocalDateTime expectedArrivalTime;
    private PaymentMethod paymentMethod;
    private float totalAmount;
    private PaymentStatus status;
    private LocalDateTime paidAt;
    private String note;

    public Order() {}

    public Order(String id, int customerId, int serviceClerkId, int tableId, LocalDateTime orderTime, LocalDateTime expectedArrivalTime, PaymentMethod paymentMethod, float totalAmount, PaymentStatus status,LocalDateTime paidAt, String note) {
        this.id = id;
        this.customerId = customerId;
        this.serviceClerkId = serviceClerkId;
        this.tableId = tableId;
        this.orderTime = orderTime;
        this.expectedArrivalTime = expectedArrivalTime;
        this.paymentMethod = paymentMethod;
        this.totalAmount = totalAmount;
        this.status = status;
        this.paidAt = paidAt;
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getServiceClerkId() {
        return serviceClerkId;
    }

    public void setServiceClerkId(int serviceClerkId) {
        this.serviceClerkId = serviceClerkId;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public LocalDateTime getExpectedArrivalTime() {
        return expectedArrivalTime;
    }

    public void setExpectedArrivalTime(LocalDateTime expectedArrivalTime) {
        this.expectedArrivalTime = expectedArrivalTime;
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

    public void setStatus(PaymentStatus status){
        this.status = status;
    }

    public PaymentStatus getStatus(){
        return status;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
