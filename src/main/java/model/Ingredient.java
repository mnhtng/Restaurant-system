package main.java.model;

import java.time.LocalDate;

/**
 * @author MnhTng
 * @Package Models
 * @date 4/16/2025 5:58 PM
 * @Copyright tùng
 */

public class Ingredient {
    private int id;
    private String name;
    private String category;
    private String unit;
    private LocalDate expire;

    public Ingredient() {}

    public Ingredient(int id, String name, String category, String unit, LocalDate expire) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.unit = unit;
        this.expire = expire;
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

    public LocalDate getExpire() {
        return expire;
    }

    public void setExpire(LocalDate expire) {
        this.expire = expire;
    }
}
