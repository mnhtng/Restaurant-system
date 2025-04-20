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

public class UiManager {
    private Application application;
    private static UiManager instance;

    public static UiManager getInstance() {
        if (instance == null) {
            instance = new UiManager();
        }
        return instance;
    }

    private UiManager() {
    }

    public void initApplication(Application application) {
        this.application = application;
    }

    public void showView(Container form) {
        EventQueue.invokeLater(() -> {
            FlatAnimatedLafChange.showSnapshot();
            application.setContentPane(form);
            application.revalidate();
            application.repaint();
            FlatAnimatedLafChange.hideSnapshotWithAnimation();
        });
    }
}
