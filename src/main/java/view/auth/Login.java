package main.java.view.auth;

import com.formdev.flatlaf.FlatClientProperties;
import main.java.component.MainAdminView;
import main.java.controller.AuthController;
import main.java.util.AlertUtil;
import main.java.util.UiManager;
import main.java.view.admin.Dashboard;
import net.miginfocom.swing.MigLayout;
import raven.toast.Notifications;

import javax.swing.*;
import java.awt.*;

/**
 * @author MnhTng
 * @Package main.java.view.ui
 * @date 4/17/2025 6:06 PM
 * @Copyright tùng
 */

public class Login extends JPanel {
    private final JLabel lbEmail = new JLabel("Email");
    private final JTextField emailField;
    private final JLabel lbPassword = new JLabel("Password");
    private final JPasswordField passwordField;
    private final JCheckBox rememberMeCheckBox;
    private final JButton loginButton;

    public Login() {
        this.setLayout(new MigLayout("fill, insets 20", "[center]", "[center]"));

        emailField = new JTextField();
        passwordField = new JPasswordField();
        rememberMeCheckBox = new JCheckBox("Remember Me");
        loginButton = new JButton("Login");

        // Create login panel
        JPanel panel = new JPanel(new MigLayout("wrap,fillx,insets 35 45 30 45", "[fill,250:280]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:20;" +
                "[light]background:darken(@background,3%);" +
                "[dark]background:lighten(@background,3%)");
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        lbEmail.putClientProperty(FlatClientProperties.STYLE, "font:bold");
        lbPassword.putClientProperty(FlatClientProperties.STYLE, "font:bold");
        passwordField.putClientProperty(FlatClientProperties.STYLE, "showRevealButton:true"
        );
        loginButton.putClientProperty(FlatClientProperties.STYLE, "background:rgb(160,100,255);" +
                "foreground:rgb(250,250,250);" +
                "font:bold +3;" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Submit form
        loginButton.addActionListener(e -> onLogin());

        emailField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your email");
        passwordField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your password");

        JLabel title = new JLabel("Welcome back!");
        JLabel description = new JLabel("Please sign in to access your account");
        title.putClientProperty(FlatClientProperties.STYLE, "font:bold +10");
        description.putClientProperty(FlatClientProperties.STYLE, "[light]foreground:lighten(@foreground,30%);" +
                "[dark]foreground:darken(@foreground,30%)");

        panel.add(title);
        panel.add(description);
        panel.add(lbEmail, "gapy 16");
        panel.add(emailField);
        panel.add(lbPassword, "gapy 8");
        panel.add(passwordField);
        panel.add(rememberMeCheckBox, "grow 0, gapy 4");
        panel.add(loginButton, "gapy 10");
        panel.add(signupRedirect(), "gapy 10");
        this.add(scrollPane);
    }

    private Component signupRedirect() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panel.putClientProperty(FlatClientProperties.STYLE, "background:null");

        // Create link for registration.
        JButton registerLink = new JButton("<html><a href=\"#\">Sign up</a></html>");
        registerLink.putClientProperty(FlatClientProperties.STYLE, "border:3,3,3,3");
        registerLink.setContentAreaFilled(false);
        registerLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerLink.addActionListener(e -> UiManager.getInstance().showView(new Register()));

        JLabel label = new JLabel("Don't have an account ?");
        label.putClientProperty(FlatClientProperties.STYLE, "[light]foreground:lighten(@foreground,30%);" +
                "[dark]foreground:darken(@foreground,30%)");

        panel.add(label);
        panel.add(registerLink);
        return panel;
    }

    private void onLogin() {
        String email = emailField.getText();
        String password = String.valueOf(passwordField.getPassword());

        if (AuthController.login(email, password)) {
            AlertUtil.alertAndRedirect("success", "Login Successful!", new MainAdminView());
        } else {
            AlertUtil.alert("error", "Login Failed: Invalid credentials!");
        }
    }
}
