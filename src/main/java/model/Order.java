package main.java.model;

import java.time.LocalDate;

/**
 * @author MnhTng
 * @Package Models
 * @date 4/16/2025 6:00 PM
 * @Copyright tùng
 */

public class Order {
    private int id;
    private int customerId;
    private int serviceClerkId;
    private int tableId;
    private LocalDate orderTime;
    private String note;

    public Order() {}

    public Order(int id, int customerId, int serviceClerkId, int tableId, LocalDate orderTime, String note) {
        this.id = id;
        this.customerId = customerId;
        this.serviceClerkId = serviceClerkId;
        this.tableId = tableId;
        this.orderTime = orderTime;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public LocalDate getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDate orderTime) {
        this.orderTime = orderTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
