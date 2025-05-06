package main.java.controller.admin;

import main.java.dao.MemberCardDAO;
import main.java.model.Member;

import java.time.LocalDateTime;
import java.util.List;

public class MemberCardAdminController {
    public static List<Member> getMemberCardList() {
        return MemberCardDAO.getMemberCardList();
    }

    public static boolean updateMemberCard(int memberId, String membershipTier, float loyaltyPoint, LocalDateTime createCardAt) {
        return MemberCardDAO.updateMemberCard(memberId, membershipTier, loyaltyPoint, createCardAt);
    }

    public static boolean deleteMemberCard(int memberId) {
        return MemberCardDAO.deleteMemberCard(memberId);
    }

    public static List<Member> searchMemberCard(String query) {
        return MemberCardDAO.searchMemberCard(query);
    }
}
