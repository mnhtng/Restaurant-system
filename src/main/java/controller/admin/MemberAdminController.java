package main.java.controller.admin;

import main.java.dao.MemberDAO;
import main.java.dao.PermissionDAO;
import main.java.model.Member;
import main.java.model.Permission;

import java.util.List;

/**
 * @author MnhTng
 * @Package main.java.controller.admin
 * @date 4/21/2025 6:04 PM
 * @Copyright tùng
 */

public class MemberAdminController {
    public static List<Member> getAllUsers() {
        return MemberDAO.getAllUsers();
    }

    public static List<Member> getAllMembers() {
        return MemberDAO.getAllMembers();
    }

    public static boolean addMember(Member member) {
        Member checkExistAccount = MemberDAO.findMember(member.getEmail(), member.getPassword());
        if (checkExistAccount != null) {
            return false;
        }

        if (MemberDAO.addMember(member) == 0) {
            return false;
        }

        return true;
    }

    public static boolean updateMember(Member member, String[] permissionParts) {
        if (!MemberDAO.updateMember(member)) {
            return false;
        }

        List<Permission> allPermissions = PermissionDAO.getAllPermissions();
        if (!PermissionDAO.updateSelectedPermission(member, permissionParts, allPermissions)) {
            return false;
        }

        return true;
    }

    public static boolean deleteMember(int memberId) {
        return MemberDAO.deleteMember(memberId);
    }

    public static List<Member> searchMember(String query) {
        return MemberDAO.search(query);
    }

    public static List<Member> getMemberCardList() {
        return MemberDAO.getMemberCardList();
    }

    public static List<Member> searchMemberCard(String query) {
        return MemberDAO.searchMemberCard(query);
    }
}
