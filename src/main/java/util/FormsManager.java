package main.java.util;

import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import main.java.Application;

import javax.swing.*;
import java.awt.*;

/**
 * @author MnhTng
 * @Package main.java.util
 * @date 4/18/2025 12:08 AM
 * @Copyright tùng
 */

public class FormsManager {
    private Application application;
    private static FormsManager instance;

    public static FormsManager getInstance() {
        if (instance == null) {
            instance = new FormsManager();
        }
        return instance;
    }

    private FormsManager() {
    }

    public void initApplication(Application application) {
        this.application = application;
    }

    public void showForm(JComponent form) {
        EventQueue.invokeLater(() -> {
            FlatAnimatedLafChange.showSnapshot();
            application.setContentPane(form);
            application.revalidate();
            application.repaint();
            FlatAnimatedLafChange.hideSnapshotWithAnimation();
        });
    }
}
