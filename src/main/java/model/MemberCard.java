package main.java.model;

import main.java.model.enums.Gender;
import main.java.model.enums.MembershipTier;
import main.java.model.enums.Role;

import java.time.LocalDate;

/**
 * @author MnhTng
 * @Package Models
 * @date 4/16/2025 5:58 PM
 * @Copyright tùng
 */

public class MemberCard {
    private int id;
    private MembershipTier membershipTier;
    private float loyaltyPoint;
    private LocalDate createdDate;

    public MemberCard() {}

    public MemberCard(Member member, MembershipTier membershipTier, float loyaltyPoint, LocalDate createdDate) {
        this.id = member.getId();
        this.membershipTier = membershipTier;
        this.loyaltyPoint = loyaltyPoint;
        this.createdDate = createdDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }
}
