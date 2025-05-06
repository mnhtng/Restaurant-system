package main.java.model;

import main.java.model.enums.Gender;
import main.java.model.enums.MembershipTier;
import main.java.model.enums.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Member {
    private int id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private LocalDate birthday;
    private Gender gender;
    private Role role;
    private MembershipTier membershipTier;
    private float loyaltyPoint;
    private LocalDateTime createCardAt;
    private LocalDateTime deleteAt;

    public Member() {
    }

    public Member(int id, String name, String email, String password, String phone, LocalDate birthday, Gender gender, Role role, MembershipTier membershipTier, float loyaltyPoint, LocalDateTime createCardAt, LocalDateTime deleteAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.birthday = birthday;
        this.gender = gender;
        this.role = role;
        this.membershipTier = membershipTier;
        this.loyaltyPoint = loyaltyPoint;
        this.createCardAt = createCardAt;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public MembershipTier getMembershipTier() {
        return membershipTier;
    }

    public void setMembershipTier(MembershipTier membershipTier) {
        this.membershipTier = membershipTier;
    }

    public float getLoyaltyPoint() {
        return loyaltyPoint;
    }

    public void setLoyaltyPoint(float loyaltyPoint) {
        this.loyaltyPoint = loyaltyPoint;
    }

    public LocalDateTime getCreateCardAt() {
        return createCardAt;
    }

    public void setCreateCardAt(LocalDateTime createCardAt) {
        this.createCardAt = createCardAt;
    }

    public LocalDateTime getDeleteAt() {
        return deleteAt;
    }

    public void setDeleteAt(LocalDateTime deleteAt) {
        this.deleteAt = deleteAt;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", birthday='" + birthday + '\'' +
                ", gender='" + gender + '\'' +
                ", role='" + role + '\'' +
                ", membershipTier='" + membershipTier + '\'' +
                ", loyaltyPoint=" + loyaltyPoint +
                ", createCardAt='" + createCardAt + '\'' +
                ", deleteAt='" + deleteAt + '\'' +
                '}';
    }
}
