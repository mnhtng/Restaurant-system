package main.java.model;

import main.java.model.enums.DishCategory;

/**
 * @author MnhTng
 * @Package Models
 * @date 4/16/2025 6:01 PM
 * @Copyright tùng
 */

public class Dish {
    private int id;
    private String name;
    private DishCategory category;
    private String portionSize;
    private float price;
    private String description;

    public Dish() {}

    public Dish(int id, String name, DishCategory category, String portionSize, float price, String description) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.portionSize = portionSize;
        this.price = price;
        this.description = description;
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

    public DishCategory getCategory() {
        return category;
    }

    public void setCategory(DishCategory category) {
        this.category = category;
    }

    public String getPortionSize() {
        return portionSize;
    }

    public void setPortionSize(String portionSize) {
        this.portionSize = portionSize;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
