package main.java.model;

/**
 * @author MnhTng
 * @Package Models
 * @date 4/16/2025 6:01 PM
 * @Copyright tùng
 */

public class Combo {
    private int id;
    private String name;
    private float price;
    private String description;

    public Combo() {}

    public Combo(int id, String name, float price, String description) {
        this.id = id;
        this.name = name;
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
