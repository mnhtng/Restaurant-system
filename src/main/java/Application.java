package main.java;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import main.java.util.JDBCConnection;
import net.miginfocom.swing.MigLayout;

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

        JPanel registerPanel = new JPanel(new MigLayout("fill, insets 20", "[center]", "[center]"));
        String panelStyle = "arc: 20; [light]background:darken(@background,2%); [dark]background:lighten(@background,3%)";

        registerPanel.putClientProperty(FlatClientProperties.STYLE, panelStyle);

        this.add(registerPanel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        // Connect to the database
        JDBCConnection dbConnection = JDBCConnection.getInstance();

        // Set the look and feel to FlatLaf
        FlatMacLightLaf.setup();
        // Show the application window (main frame)
        new Application();
    }
}
