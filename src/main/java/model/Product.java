package main.java.model;

import java.time.LocalDateTime;

public class Product {
    private int id;
    private String type;
    private boolean status;
    private LocalDateTime deleteAt;

    public Product() {}

    public Product(int id, String type, boolean status, LocalDateTime deleteAt) {
        this.id = id;
        this.type = type;
        this.status = status;
        this.deleteAt = deleteAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public LocalDateTime getDeleteAt() {
        return deleteAt;
    }

    public void setDeleteAt(LocalDateTime deleteAt) {
        this.deleteAt = deleteAt;
    }
}
