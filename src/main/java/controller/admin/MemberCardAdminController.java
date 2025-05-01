package main.java.controller.admin;

import main.java.dao.MemberDAO;

import java.time.LocalDateTime;

/**
 * @author MnhTng
 * @Package main.java.controller.admin
 * @date 4/30/2025 12:51 PM
 * @Copyright tùng
 */

public class MemberCardAdminController {
    public static boolean updateMemberCard(int memberId, String membershipTier, float loyaltyPoint, LocalDateTime createCardAt) {
        return MemberDAO.updateMemberCard(memberId, membershipTier, loyaltyPoint, createCardAt);
    }

    public static boolean deleteMemberCard(int memberId) {
        return MemberDAO.deleteMemberCard(memberId);
    }
}
