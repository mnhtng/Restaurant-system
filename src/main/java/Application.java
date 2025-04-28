package main.java;

import main.java.model.enums.Role;
import main.java.util.JDBCConnection;
import main.java.util.Session;
import main.java.view.admin.MainAdminView;
import main.java.view.auth.AuthView;
import main.java.view.member.MainMemberView;

/**
 * @author MnhTng
 * @Package main.java
 * @date 4/17/2025 3:36 PM
 * @Copyright tùng
 */

public class Application {
    public Application() {
        if (Session.getInstance().isAuthenticated()) {
            Role role = Session.getInstance().getCurrentUser().getRole();

            switch (role) {
                case MEMBER:
                    new MainMemberView();
                    break;
                default:
                    new MainAdminView();
                    break;
            }
        } else {
            new AuthView();
        }
    }

    public static void main(String[] args) {
        JDBCConnection dbConnection = JDBCConnection.getInstance();

        new Application();
    }
}
