package main.java.view.auth;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

/**
 * @author MnhTng
 * @Package main.java.view.ui
 * @date 4/17/2025 6:06 PM
 * @Copyright tùng
 */

public class Login extends JPanel {
    public Login() {
        this.setLayout(new MigLayout("fill, insets 20", "[center]", "[center]"));

    }

    public static void main(String[] args) {
        new Login();
    }
}
