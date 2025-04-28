package main.java.model;

import java.time.LocalDateTime;

/**
 * @author MnhTng
 * @Package Models
 * @date 4/16/2025 5:54 PM
 * @Copyright tùng
 */

public class Staff {
    private int id;
    private float salary;
    private LocalDateTime deleteAt;

    public Staff() {
    }

    public Staff(int id, float salary, LocalDateTime deleteAt) {
        this.id = id;
        this.salary = salary;
        this.deleteAt = deleteAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    public LocalDateTime getDeleteAt() {
        return deleteAt;
    }

    public void setDeleteAt(LocalDateTime deleteAt) {
        this.deleteAt = deleteAt;
    }
}
