package main.java;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import main.java.util.UiManager;
import main.java.util.JDBCConnection;
import main.java.view.auth.Login;
import raven.toast.Notifications;

import javax.swing.*;

/**
 * @author MnhTng
 * @Package main.java
 * @date 4/17/2025 3:36 PM
 * @Copyright tùng
 */

public class Application extends JFrame {
    public Application() {
        this.setTitle("Hang Rong");
        this.setSize(1200, 700);
        this.setLocationRelativeTo(null);

        Notifications.getInstance().setJFrame(this);

        this.add(new Login());

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);

        UiManager.getInstance().initApplication(this);
    }

    public static void main(String[] args) {
        // Connect to the database
        JDBCConnection dbConnection = JDBCConnection.getInstance();

        // Set the look and feel to FlatLaf
        FlatLaf.registerCustomDefaultsSource("main.resources.styles");
        FlatMacLightLaf.setup();

        // Show the application window (main frame)
        new Application();
    }
}
