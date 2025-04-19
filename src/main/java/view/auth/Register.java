package main.java.view.auth;

import com.formdev.flatlaf.FlatClientProperties;
import main.java.component.PasswordStrengthStatus;
import main.java.util.FormsManager;
import net.miginfocom.swing.MigLayout;
import raven.datetime.DatePicker;
import raven.toast.Notifications;

import javax.swing.*;
import java.awt.*;

/**
 * @author MnhTng
 * @Package main.java.view.ui
 * @date 4/17/2025 6:06 PM
 * @Copyright tùng
 */

public class Register extends JPanel {
    private JTextField emailField;
    private JTextField nameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField phoneField;
    private JFormattedTextField birthdayField;
    private JRadioButton maleRadio;
    private JRadioButton femaleRadio;
    private ButtonGroup groupGender;
    private JButton RegisterButton;
    private PasswordStrengthStatus passwordStrengthStatus;

    public Register() {
        this.setLayout(new MigLayout("fill,insets 20", "[center]", "[center]"));
        nameField = new JTextField();
        emailField = new JTextField();
        passwordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();
        phoneField = new JTextField();
        birthdayField = new JFormattedTextField();
        RegisterButton = new JButton("Sign Up");

        // Validate
        RegisterButton.addActionListener(e -> {
            if (isMatchPassword()) {
                //  Do something here
            } else {
                Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_RIGHT, "Passwords don't match. Try again!");
            }
        });
        passwordStrengthStatus = new PasswordStrengthStatus();

        // Create register panel and add components
        JPanel panel = new JPanel(new MigLayout("wrap,fillx,insets 35 45 30 45", "[fill,360]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:20;" +
                "[light]background:darken(@background,3%);" +
                "[dark]background:lighten(@background,3%)");
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        nameField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Your name");
        emailField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your username or email");
        passwordField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your password");
        confirmPasswordField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Re-enter your password");
        passwordField.putClientProperty(FlatClientProperties.STYLE, "showRevealButton:true");
        confirmPasswordField.putClientProperty(FlatClientProperties.STYLE, "showRevealButton:true");
        phoneField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Your phone number");
        DatePicker datePicker = new DatePicker();
        datePicker.setEditor(birthdayField);

        RegisterButton.putClientProperty(FlatClientProperties.STYLE, "[light]background:darken(@background,10%);" +
                "[dark]background:lighten(@background,10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");

        JLabel title = new JLabel("Welcome to our Restaurant Application");
        JLabel description = new JLabel("Create your account to enjoy our services!");
        title.putClientProperty(FlatClientProperties.STYLE, "font:bold +10");
        description.putClientProperty(FlatClientProperties.STYLE, "[light]foreground:lighten(@foreground,30%);" +
                "[dark]foreground:darken(@foreground,30%)");

        passwordStrengthStatus.initPasswordField(passwordField);

        panel.add(title);
        panel.add(description);
        panel.add(new JLabel("Full Name"), "gapy 10");
        panel.add(nameField);
        panel.add(new JLabel("Phone Number"), "gapy 8");
        panel.add(phoneField);
        panel.add(new JLabel("Date of Birth"), "gapy 8");
        panel.add(birthdayField);
        panel.add(new JLabel("Gender"), "gapy 8");
        panel.add(createGenderPanel());
        panel.add(new JSeparator(), "gapy 5 5");
        panel.add(new JLabel("Email"));
        panel.add(emailField);
        panel.add(new JLabel("Password"), "gapy 8");
        panel.add(passwordField);
        panel.add(passwordStrengthStatus, "gapy 0");
        panel.add(new JLabel("Confirm Password"), "gapy 0");
        panel.add(confirmPasswordField);
        panel.add(RegisterButton, "gapy 20");
        panel.add(loginRedirect(), "gapy 10");
        this.add(scrollPane);
    }

    private Component createGenderPanel() {
        JPanel panel = new JPanel(new MigLayout("insets 0"));
        panel.putClientProperty(FlatClientProperties.STYLE, "background:null");
        maleRadio = new JRadioButton("Male");
        femaleRadio = new JRadioButton("Female");
        groupGender = new ButtonGroup();
        groupGender.add(maleRadio);
        groupGender.add(femaleRadio);
        maleRadio.setSelected(true);
        panel.add(maleRadio);
        panel.add(femaleRadio);
        return panel;
    }

    private Component loginRedirect() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panel.putClientProperty(FlatClientProperties.STYLE, "background:null");

        // Create link for login
        JButton loginLink = new JButton("<html><a href=\"#\">Sign in here</a></html>");
        loginLink.putClientProperty(FlatClientProperties.STYLE, "border:3,3,3,3");
        loginLink.setContentAreaFilled(false);
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLink.addActionListener(e -> {
            FormsManager.getInstance().showForm(new Login());
        });

        JLabel label = new JLabel("Already have an account ?");
        label.putClientProperty(FlatClientProperties.STYLE, "[light]foreground:lighten(@foreground,30%);" +
                "[dark]foreground:darken(@foreground,30%)");

        panel.add(label);
        panel.add(loginLink);
        return panel;
    }

    public boolean isMatchPassword() {
        String password = String.valueOf(passwordField.getPassword());
        String confirmPassword = String.valueOf(confirmPasswordField.getPassword());
        return password.equals(confirmPassword);
    }

    public static void main(String[] args) {
        new Register();
    }
}
