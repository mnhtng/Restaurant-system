package main.java.controller.admin;

import main.java.dao.MemberDAO;
import main.java.dao.PermissionDAO;
import main.java.model.Ingredient;
import main.java.model.Member;
import main.java.model.Permission;

import java.util.List;

public class MemberAdminController {
    public static List<Member> getAllUsers() {
        return MemberDAO.getAllUsers();
    }

    public static List<Member> getAllMembers() {
        return MemberDAO.getAllMembers();
    }

    public static Member getMemberById(int id) {
        return MemberDAO.findMember(id);
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

    public static boolean updateMember(Member member) {
        if (!MemberDAO.updateMember(member)) {
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
}
