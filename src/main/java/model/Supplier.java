package main.java.model;

import java.time.LocalDateTime;

public class Supplier {
    private int id;
    private String name;
    private String address;
    private String phone;
    private LocalDateTime deleteAt;

    public Supplier() {
    }

    public Supplier(int id, String name, String address, String phone, LocalDateTime deleteAt) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.deleteAt = deleteAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDateTime getDeleteAt() {
        return deleteAt;
    }

    public void setDeleteAt(LocalDateTime deleteAt) {
        this.deleteAt = deleteAt;
    }
}
