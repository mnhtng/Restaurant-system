package main.java.model;

import main.java.model.enums.DishCategory;
import main.java.model.enums.DishStatus;

public class Dish {
    private int id;
    private String name;
    private DishCategory category;
    private String portionSize;
    private float price;
    private DishStatus status;
    private String description;

    private Product product;

    public Dish() {}

    public Dish(int id, String name, DishCategory category, String portionSize, float price, DishStatus status, String description) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.portionSize = portionSize;
        this.price = price;
        this.status = status;
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

    public DishStatus getStatus() {
        return status;
    }

    public void setStatus(DishStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
