package main.java.controller;

import main.java.model.Member;
import main.java.service.MemberService;
import main.java.util.Session;
import main.java.util.UiManager;
import main.java.view.auth.Login;

/**
 * @author MnhTng
 * @Package main.java.controller
 * @date 4/19/2025 11:06 PM
 * @Copyright tùng
 */

public class AuthController {
    public static boolean login(String email, String password) {
        Member member = MemberService.findMember(email, password);

        if (member == null) {
            return false;
        }

        Session.getInstance().setCurrentUser(member);

        return true;
    }

    public static boolean signup(Member newUser) {
        for (Member member : MemberService.getAllUsers()) {
            if (member.getEmail().equals(newUser.getEmail())) {
                return false;
            }
        }

        if (!MemberService.addMember(newUser)) {
            return false;
        }

        return true;
    }

    public static void logout() {
        Session.getInstance().clear();
        UiManager.getInstance().showView(new Login());
    }
}
