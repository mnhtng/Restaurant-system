package main.java.middleware;

import main.java.util.Session;
import main.java.util.UiManager;
import main.java.view.auth.Login;

/**
 * @author MnhTng
 * @Package main.java.middleware
 * @date 4/20/2025 11:11 PM
 * @Copyright tùng
 */

public class AuthMiddleware implements Middleware {
    @Override
    public void handle() {
        if (!Session.getInstance().isAuthenticated()) {
            UiManager.getInstance().showView(new Login());
        }
    }
}
