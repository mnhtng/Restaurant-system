package main.java.controller;

import main.java.Application;
import main.java.dao.MemberDAO;
import main.java.model.Member;
import main.java.util.Session;

import java.util.List;

public class AuthController {
    public static boolean login(String email, String password) {
        Member member = MemberDAO.findMember(email, password);

        if (member == null) {
            return false;
        }

        Session.getInstance().setCurrentUser(member);

        return true;
    }

    public static boolean signup(Member newUser) {
        List<Member> allMembers = MemberDAO.getAllUsers();
        if (allMembers != null) {
            for (Member member : allMembers) {
                if (member.getEmail().equals(newUser.getEmail())) {
                    return false;
                }
            }
        }

        if (MemberDAO.addMember(newUser) == 0) {
            return false;
        }

        return true;
    }

    public static void logout() {
        Session.getInstance().clear();
        System.out.println("Đăng xuất thành công!");
        new Application();
    }
}
