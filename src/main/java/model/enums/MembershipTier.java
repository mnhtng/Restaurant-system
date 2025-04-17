package main.java.model.enums;

/**
 * @author MnhTng
 * @Package main.java.model.enums
 * @date 4/16/2025 9:43 PM
 * @Copyright tùng
 */

public enum MembershipTier {
    BRONZE(0, 0.01f),
    SILVER(1, 0.03f),
    GOLD(2, 0.05f),
    PLATINUM(3, 0.07f),
    DIAMOND(4, 0.1f);

    private final int id;
    private final float discountRate;

    MembershipTier(int id, float discountRate) {
        this.id = id;
        this.discountRate = discountRate;
    }

    public int getId() {
        return id;
    }

    public float getDiscountRate() {
        return discountRate;
    }

    public static MembershipTier getMembershipTier(int id) {
        for (MembershipTier r : MembershipTier.values()) {
            if (r.id == id) {
                return r;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Present Membership Tier: " + this.name();
    }
}
