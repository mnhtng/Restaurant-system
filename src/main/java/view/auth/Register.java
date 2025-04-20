package main.java.view.auth;

import com.formdev.flatlaf.FlatClientProperties;
import main.java.component.PasswordStrengthStatus;
import main.java.controller.AuthController;
import main.java.model.Member;
import main.java.model.enums.Gender;
import main.java.model.enums.Role;
import main.java.util.AlertUtil;
import main.java.util.UiManager;
import net.miginfocom.swing.MigLayout;
import raven.datetime.DatePicker;
import raven.datetime.event.DateSelectionEvent;
import raven.datetime.event.DateSelectionListener;
import raven.toast.Notifications;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * @author MnhTng
 * @Package main.java.view.ui
 * @date 4/17/2025 6:06 PM
 * @Copyright tùng
 */

public class Register extends JPanel {
    private final JLabel lbEmail = new JLabel("Email");
    private JTextField emailField;
    private final JLabel lbName = new JLabel("Full Name");
    private JTextField nameField;
    private final JLabel lbPassword = new JLabel("Password");
    private JPasswordField passwordField;
    private final JLabel lbConfirmPassword = new JLabel("Confirm Password");
    private JPasswordField confirmPasswordField;
    private final JLabel lbPhone = new JLabel("Phone Number");
    private JTextField phoneField;
    private final JLabel lbBirthday = new JLabel("Date of Birth");
    private JFormattedTextField birthdayField;
    private final JLabel lbGender = new JLabel("Gender");
    private JRadioButton maleRadio;
    private JRadioButton femaleRadio;
    private ButtonGroup groupGender;
    private JButton registerButton;
    private PasswordStrengthStatus passwordStrengthStatus;

    public Register() {
        this.setLayout(new MigLayout("fill,insets 20", "[center]", "[center]"));
        nameField = new JTextField();
        emailField = new JTextField();
        passwordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();
        phoneField = new JTextField();
        birthdayField = new JFormattedTextField();
        registerButton = new JButton("Sign Up");
        // Submit form
        registerButton.addActionListener(e -> onRegister());

        passwordStrengthStatus = new PasswordStrengthStatus();

        // Create register panel and add components
        JPanel panel = new JPanel(new MigLayout("wrap,fillx,insets 35 45 30 45", "[fill,360]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:20;" +
                "[light]background:darken(@background,3%);" +
                "[dark]background:lighten(@background,3%)");
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        lbName.putClientProperty(FlatClientProperties.STYLE, "font:bold");
        lbEmail.putClientProperty(FlatClientProperties.STYLE, "font:bold");
        lbPassword.putClientProperty(FlatClientProperties.STYLE, "font:bold");
        lbConfirmPassword.putClientProperty(FlatClientProperties.STYLE, "font:bold");
        lbPhone.putClientProperty(FlatClientProperties.STYLE, "font:bold");
        lbBirthday.putClientProperty(FlatClientProperties.STYLE, "font:bold");
        lbGender.putClientProperty(FlatClientProperties.STYLE, "font:bold");
        nameField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Your name");
        emailField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your username or email");
        passwordField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your password");
        confirmPasswordField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Re-enter your password");
        passwordField.putClientProperty(FlatClientProperties.STYLE, "showRevealButton:true");
        confirmPasswordField.putClientProperty(FlatClientProperties.STYLE, "showRevealButton:true");
        phoneField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Your phone number");
        DatePicker datePicker = new DatePicker();
        datePicker.setEditor(birthdayField);

//        String curDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
//        birthdayField.setText(curDate);
//        String curDate = "10/10/2000";
//        LocalDate date = LocalDate.parse(curDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
//        birthdayField.setText(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        registerButton.putClientProperty(FlatClientProperties.STYLE, "background:rgb(160,100,255);" +
                "foreground:rgb(250,250,250);" +
                "font:bold +3;" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel title = new JLabel("Welcome to our Restaurant Application");
        JLabel description = new JLabel("Create your account to enjoy our services!");
        title.putClientProperty(FlatClientProperties.STYLE, "font:bold +10");
        description.putClientProperty(FlatClientProperties.STYLE, "[light]foreground:lighten(@foreground,30%);" +
                "[dark]foreground:darken(@foreground,30%)");

        passwordStrengthStatus.initPasswordField(passwordField);

        panel.add(title);
        panel.add(description);
        panel.add(lbName, "gapy 16");
        panel.add(nameField);
        panel.add(lbPhone, "gapy 8");
        panel.add(phoneField);
        panel.add(lbBirthday, "gapy 8");
        panel.add(birthdayField);
        panel.add(lbGender, "gapy 8");
        panel.add(createGenderPanel());
        panel.add(new JSeparator(), "gapy 5 5");
        panel.add(lbEmail, "gapy 8");
        panel.add(emailField);
        panel.add(lbPassword, "gapy 8");
        panel.add(passwordField);
        panel.add(passwordStrengthStatus, "gapy 0");
        panel.add(lbConfirmPassword, "gapy 0");
        panel.add(confirmPasswordField);
        panel.add(registerButton, "gapy 20");
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

        // Create link for login.
        JButton loginLink = new JButton("<html><a href=\"#\">Sign in here</a></html>");
        loginLink.putClientProperty(FlatClientProperties.STYLE, "border:3,3,3,3");
        loginLink.setContentAreaFilled(false);
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLink.addActionListener(e -> UiManager.getInstance().showView(new Login()));

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

    private void onRegister() {
        Enumeration<AbstractButton> genderOptions = groupGender.getElements();
        String gender = "";

        while (genderOptions.hasMoreElements()) {
            AbstractButton button = genderOptions.nextElement();

            if (button.isSelected()) {
                gender = button.getText();
                break;
            }
        }

        // Validate
        if (emailField.getText().isEmpty() || nameField.getText().isEmpty() || phoneField.getText().isEmpty() ||
                birthdayField.getText().isEmpty() || passwordField.getPassword().length == 0 ||
                confirmPasswordField.getPassword().length == 0 || groupGender.getSelection() == null) {
            AlertUtil.alert("error", "Please fill in all fields!");
            return;
        }

        String email = emailField.getText().trim();
        HashMap<String, String> registerError = new HashMap<>();
        if (!email.matches("^[a-zA-Z0-9._-]{3,}@[a-z.]+\\.[a-z]{2,}$")) {
            registerError.put("email", "Invalid email format!");
        }

        String phone = phoneField.getText().trim();
        if (!phone.matches("^0\\d{9,14}$")) {
            registerError.put("phone", "Phone number must start with 0 and above 9 digits!");
        }

        if (!isMatchPassword()) {
            registerError.put("password", "Passwords do not match!");
        }

        if (!registerError.isEmpty()) {
            registerError.forEach((key, value) -> {
                AlertUtil.alert("error", value);
            });

            return;
        }

        Member newMember = new Member();
        newMember.setEmail(emailField.getText());
        newMember.setName(nameField.getText());
        newMember.setPhone(phoneField.getText());
        newMember.setBirthday(LocalDate.parse(birthdayField.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        newMember.setPassword(String.valueOf(passwordField.getPassword()));
        newMember.setGender(Gender.valueOf(gender.toUpperCase()));
        newMember.setRole(Role.MEMBER);

        if (AuthController.signup(newMember)) {
            AlertUtil.alertAndRedirect("success", "Registration successful! Please log in.", new Login());
        } else {
            AlertUtil.alert("error", "Registration failed! Please try again.");
        }
    }
}
