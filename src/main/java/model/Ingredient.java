package main.java.model;

import java.time.LocalDateTime;

public class Ingredient {
    private int id;
    private String name;
    private String category;
    private String unit;
    private boolean status;
    private LocalDateTime deleteAt;

    public Ingredient() {
    }

    public Ingredient(int id, String name, String category, String unit, boolean status, LocalDateTime deleteAt) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.unit = unit;
        this.status = status;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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
