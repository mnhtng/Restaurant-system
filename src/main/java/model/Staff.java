package main.java.model;

import main.java.model.enums.Gender;
import main.java.model.enums.MembershipTier;
import main.java.model.enums.Role;

import java.time.LocalDate;
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

    private Member member;

    public Staff() {
    }

    public Staff(int id, String name, String email, String password, String phone, LocalDate birthday, Gender gender, Role role, float salary, MembershipTier membershipTier, float loyaltyPoint, LocalDateTime createCardAt, LocalDateTime deleteAt) {
        this.id = id;
        this.salary = salary;
    }

    public Staff(int id, float salary, Member member) {
        this.id = id;
        this.salary = salary;
        this.member = member;
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

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
