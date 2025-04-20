package main.java.model.enums;

/**
 * @author MnhTng
 * @Package main.java.model.enums
 * @date 4/16/2025 6:56 PM
 * @Copyright tùng
 */

public enum Role {
    MEMBER(0), MANAGER(1), INVENTORY_CLERK(2), SERVICE_CLERK(3);

    private int value;

    Role(int value) {
        this.value = value;
    }

    public static Role getRole(int value) {
        for (Role r : Role.values()) {
            if (r.value == value) {
                return r;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Present Role: " + this.name();
    }
}
