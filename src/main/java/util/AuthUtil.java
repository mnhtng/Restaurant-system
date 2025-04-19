package main.java.util;

/**
 * @author MnhTng
 * @Package main.java.util
 * @date 4/18/2025 11:04 PM
 * @Copyright tùng
 */

public class AuthUtil {
    public static int checkPasswordStrength(String password) {
        int securityLevel = 0;

        if (password.length() >= 8) {
            securityLevel++;
        }

        boolean hasUppercase = password.matches(".*[A-Z].*");
        if (hasUppercase) {
            securityLevel++;
        }

        boolean hasLowercase = password.matches(".*[a-z].*");
        if (hasLowercase) {
            securityLevel++;
        }

        boolean hasDigit = password.matches(".*\\d.*");
        if (hasDigit) {
            securityLevel++;
        }

        boolean hasSpecialChar = password.matches(".*[^a-zA-Z0-9].*");
        if (hasSpecialChar) {
            securityLevel++;
        }

        if (securityLevel < 3) {
            return 1;
        } else if (securityLevel < 5) {
            return 2;
        } else {
            return 3;
        }
    }
}
