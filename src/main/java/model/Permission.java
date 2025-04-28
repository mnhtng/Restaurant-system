package main.java.model;

/**
 * @author MnhTng
 * @Package main.java.model
 * @date 4/24/2025 4:05 PM
 * @Copyright tùng
 */

public class Permission {
    private int id;
    private String name;
    private String slug;

    public Permission() {}

    public Permission(int id, String name, String slug) {
        this.id = id;
        this.name = name;
        this.slug = slug;
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
