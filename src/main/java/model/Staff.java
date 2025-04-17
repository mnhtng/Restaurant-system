package main.java.model;

import main.java.model.enums.Gender;
import main.java.model.enums.Role;

import java.time.LocalDate;
import java.util.List;

/**
 * @author MnhTng
 * @Package Models
 * @date 4/16/2025 5:54 PM
 * @Copyright tùng
 */

public class Staff {
    private int id;
    private float salary;

    public Staff() {}

    public Staff(Member member, float salary) {
        this.id = member.getId();
        this.salary = salary;
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
}
