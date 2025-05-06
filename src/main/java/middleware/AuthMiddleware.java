package main.java.middleware;

import main.java.Application;
import main.java.dao.MemberDAO;
import main.java.model.Member;
import main.java.util.Session;

public class AuthMiddleware implements Middleware{
    @Override
    public void handle() {
        if (!isValidSession()) {
            Session.getInstance().clear();
            new Application();
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
