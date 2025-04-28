package main.java.middleware;

import main.java.dao.MemberDAO;
import main.java.model.Member;
import main.java.util.Session;
import main.java.view.auth.AuthView;
import main.java.view.auth.LoginView;

/**
 * @author MnhTng
 * @Package main.java.middleware
 * @date 4/21/2025 5:45 PM
 * @Copyright tùng
 */

public class AuthMiddleware implements Middleware{
    @Override
    public void handle() {
        if (!isValidSession()) {
            Session.getInstance().clear();
            new AuthView();
        }
    }

    public boolean isValidSession() {
        if (Session.getInstance().getCurrentUser() == null) {
            return false;
        }

        Member checkValidUser = MemberDAO.findMember(Session.getInstance().getCurrentUser().getEmail(),
                Session.getInstance().getCurrentUser().getPassword());
        if (checkValidUser == null) {
            return false;
        }

        return true;
    }
}
