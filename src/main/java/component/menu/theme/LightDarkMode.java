package main.java.component.menu.theme;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author MnhTng
 * @Package main.java.component.menu.theme
 * @date 4/19/2025 5:04 PM
 * @Copyright tùng
 */

public class LightDarkMode extends JPanel {
    private boolean menuExpand = true;

    private final JButton buttonLight;
    private final JButton buttonDark;
    private final JButton toggleLightDark;

    public LightDarkMode() {
        this.setBorder(new EmptyBorder(2, 2, 2, 2));
        this.setLayout(new LightDarkModeLayout());
        this.putClientProperty(FlatClientProperties.STYLE, "arc:999;"
                + "background:$Menu.lightdark.background");

        buttonLight = new JButton("Light", new FlatSVGIcon("main/resources/images/theme/light.svg"));
        buttonDark = new JButton("Dark", new FlatSVGIcon("main/resources/images/theme/dark.svg"));
        toggleLightDark = new JButton();
        toggleLightDark.putClientProperty(FlatClientProperties.STYLE, "arc:999;"
                + "background:$Menu.lightdark.button.background;"
                + "foreground:$Menu.foreground;"
                + "focusWidth:0;"
                + "borderWidth:0;"
                + "innerFocusWidth:0");
        toggleLightDark.addActionListener((ActionEvent e) -> changeMode(!FlatLaf.isLafDark()));
        this.checkStyle();
        buttonDark.addActionListener((ActionEvent e) -> changeMode(true));
        buttonLight.addActionListener((ActionEvent e) -> changeMode(false));

        this.add(buttonLight);
        this.add(buttonDark);
        this.add(toggleLightDark);
    }

    public void setMenuExpand(boolean menuExpand) {
        this.menuExpand = menuExpand;

        if (menuExpand) {
            buttonLight.setVisible(true);
            buttonDark.setVisible(true);
            toggleLightDark.setVisible(false);
        } else {
            buttonLight.setVisible(false);
            buttonDark.setVisible(false);
            toggleLightDark.setVisible(true);
        }
    }

    private void changeMode(boolean dark) {
        if (FlatLaf.isLafDark() != dark) {
            if (dark) {
                EventQueue.invokeLater(() -> {
                    FlatAnimatedLafChange.showSnapshot();
                    FlatMacDarkLaf.setup();
                    FlatLaf.updateUI();
                    checkStyle();
                    FlatAnimatedLafChange.hideSnapshotWithAnimation();
                });
            } else {
                EventQueue.invokeLater(() -> {
                    FlatAnimatedLafChange.showSnapshot();
                    FlatMacLightLaf.setup();
                    FlatLaf.updateUI();
                    checkStyle();
                    FlatAnimatedLafChange.hideSnapshotWithAnimation();
                });
            }
        }
    }

    private void checkStyle() {
        boolean isDark = FlatLaf.isLafDark();
        this.addStyle(buttonLight, !isDark);
        this.addStyle(buttonDark, isDark);

        if (isDark) {
            toggleLightDark.setIcon(new FlatSVGIcon("main/resources/images/theme/dark.svg"));
        } else {
            toggleLightDark.setIcon(new FlatSVGIcon("main/resources/images/theme/light.svg"));
        }
    }

    private void addStyle(JButton button, boolean style) {
        if (style) {
            button.putClientProperty(FlatClientProperties.STYLE, "arc:999;"
                    + "background:$Menu.lightdark.button.background;"
                    + "foreground:$Menu.foreground;"
                    + "focusWidth:0;"
                    + "borderWidth:0;"
                    + "innerFocusWidth:0");
        } else {
            button.putClientProperty(FlatClientProperties.STYLE, "arc:999;"
                    + "background:$Menu.lightdark.button.background;"
                    + "foreground:$Menu.foreground;"
                    + "focusWidth:0;"
                    + "borderWidth:0;"
                    + "innerFocusWidth:0;"
                    + "background:null");
        }
    }

    private class LightDarkModeLayout implements LayoutManager {
        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                return new Dimension(5, buttonDark.getPreferredSize().height + (menuExpand ? 0 : 5));
            }
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                return new Dimension(0, 0);
            }
        }

        @Override
        public void layoutContainer(Container parent) {
            synchronized (parent.getTreeLock()) {
                Insets insets = parent.getInsets();
                int x = insets.left;
                int y = insets.top;
                int gap = 5;
                int width = parent.getWidth() - (insets.left + insets.right);
                int height = parent.getHeight() - (insets.top + insets.bottom);
                int buttonWidth = (width - gap) / 2;
                if (menuExpand) {
                    buttonLight.setBounds(x, y, buttonWidth, height);
                    buttonDark.setBounds(x + buttonWidth + gap, y, buttonWidth, height);
                } else {
                    toggleLightDark.setBounds(x, y, width, height);
                }
            }
        }
    }
}
