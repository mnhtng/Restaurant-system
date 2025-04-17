package main.java.view.auth;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * @author MnhTng
 * @Package main.java.view.ui
 * @date 4/17/2025 6:06 PM
 * @Copyright tùng
 */

public class Register extends JPanel {
    public Register() {
        JPanel registerPanel = new JPanel(new MigLayout("fill, insets 20", "[center]", "[center]"));
        String panelStyle = "arc: 20; [light]background:darken(@background,3%); [dark]background:lighten(@background,3%)";

        registerPanel.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:20;" +
                "[light]background:darken(@background,3%);" +
                "[dark]background:lighten(@background,3%)");

        this.add(registerPanel);
    }

    public static void main(String[] args) {
        new Register();
    }
}
