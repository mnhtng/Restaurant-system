package main.java.model;

import main.java.model.enums.Gender;
import main.java.model.enums.Role;

import java.time.LocalDate;
import java.util.List;

/**
 * @author MnhTng
 * @Package Models
 * @date 4/16/2025 5:53 PM
 * @Copyright tùng
 */

public class Member {
    private int id;
    private String name;
    private String password;
    private String email;
    private String phone;
    private LocalDate birthday;
    private Gender gender;
    private Role role;

    public Member() {}

    public Member(int id, String name, String password, String email, String phone, LocalDate birthday, Gender gender, Role role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
        this.gender = gender;
        this.role = role;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
