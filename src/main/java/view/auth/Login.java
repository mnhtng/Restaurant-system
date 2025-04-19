package main.java.view.auth;

import com.formdev.flatlaf.FlatClientProperties;
import main.java.util.FormsManager;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * @author MnhTng
 * @Package main.java.view.ui
 * @date 4/17/2025 6:06 PM
 * @Copyright tùng
 */

public class Login extends JPanel {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JCheckBox rememberMeCheckBox;
    private final JButton loginButton;

    public Login() {
        this.setLayout(new MigLayout("fill, insets 20", "[center]", "[center]"));

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        rememberMeCheckBox = new JCheckBox("Remember Me");
        loginButton = new JButton("Login");

        // Create login panel and
        JPanel panel = new JPanel(new MigLayout("wrap,fillx,insets 35 45 30 45", "[fill,250:280]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:20;" +
                "[light]background:darken(@background,3%);" +
                "[dark]background:lighten(@background,3%)");
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        passwordField.putClientProperty(FlatClientProperties.STYLE, "showRevealButton:true"
        );
        loginButton.putClientProperty(FlatClientProperties.STYLE, "[light]background:darken(@background,10%);" +
                "[dark]background:lighten(@background,10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");

        usernameField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your email");
        passwordField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your password");

        JLabel title = new JLabel("Welcome back!");
        JLabel description = new JLabel("Please sign in to access your account");
        title.putClientProperty(FlatClientProperties.STYLE, "font:bold +10");
        description.putClientProperty(FlatClientProperties.STYLE, "[light]foreground:lighten(@foreground,30%);" +
                "[dark]foreground:darken(@foreground,30%)");

        panel.add(title);
        panel.add(description);
        panel.add(new JLabel("Username"), "gapy 8");
        panel.add(usernameField);
        panel.add(new JLabel("Password"), "gapy 8");
        panel.add(passwordField);
        panel.add(rememberMeCheckBox, "grow 0");
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
        registerLink.addActionListener((e) -> {
            FormsManager.getInstance().showForm(new Register());
        });

        JLabel label = new JLabel("Don't have an account ?");
        label.putClientProperty(FlatClientProperties.STYLE, "[light]foreground:lighten(@foreground,30%);" +
                "[dark]foreground:darken(@foreground,30%)");

        panel.add(label);
        panel.add(registerLink);
        return panel;
    }


    public static void main(String[] args) {
        new Login();
    }
}
