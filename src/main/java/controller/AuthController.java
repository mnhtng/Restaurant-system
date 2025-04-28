package main.java.controller;

import main.java.dao.MemberDAO;
import main.java.model.Member;
import main.java.util.Session;
import main.java.view.auth.AuthView;

import java.util.List;

/**
 * @author MnhTng
 * @Package main.java.controller
 * @date 4/19/2025 11:06 PM
 * @Copyright tùng
 */

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
        List<Member> allMembers = MemberDAO.getAllMembers();
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
        new AuthView();
    }
}
