package main.java.component;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.util.UIScale;
import main.java.component.menu.Menu;
import main.java.component.menu.MenuAction;
import main.java.controller.AuthController;
import main.java.middleware.AuthMiddleware;
import main.java.util.UiManager;
import main.java.view.admin.Dashboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author MnhTng
 * @Package main.java.component
 * @date 4/19/2025 10:53 PM
 * @Copyright tùng
 */

public class MainAdminView extends JLayeredPane {
    private final Menu menu;
    private final JPanel panelBody;
    private JButton menuButton;

    public MainAdminView() {
        this.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setLayout(new MainViewLayout());

        menu = new Menu();
        panelBody = new JPanel(new BorderLayout());
        this.initMenuArrowIcon();

        menuButton.putClientProperty(FlatClientProperties.STYLE, "background:$Menu.button.background;"
                + "arc:999;"
                + "focusWidth:0;"
                + "borderWidth:0");
        menuButton.addActionListener((ActionEvent e) -> {
            this.setMenuExpand(!menu.isMenuExpand());
        });

        this.initMenuEvent();
        this.setLayer(menuButton, JLayeredPane.POPUP_LAYER);

        this.add(menuButton);
        this.add(menu);
        this.add(panelBody);
    }

    @Override
    public void applyComponentOrientation(ComponentOrientation o) {
        super.applyComponentOrientation(o);

        initMenuArrowIcon();
    }

    // Create menu toggle icon
    private void initMenuArrowIcon() {
        if (menuButton == null) {
            menuButton = new JButton();
        }
        String icon = (getComponentOrientation().isLeftToRight()) ? "menu_left.svg" : "menu_right.svg";
        menuButton.setIcon(new FlatSVGIcon("main/resources/images/menu/" + icon, 0.8f));
    }

    // Add event for each menu item
    private void initMenuEvent() {
        menu.addMenuEvent((int index, int subIndex, MenuAction action) -> {
            // Application.mainForm.showView(new DefaultForm("Form : " + index + " " + subIndex));
            if (index == 0) {
                UiManager.getInstance().showView(new Dashboard());
            } else if (index == 1) {
                if (subIndex == 1) {
                    UiManager.getInstance().showView(new Dashboard());
                } else if (subIndex == 2) {
                    UiManager.getInstance().showView(new Dashboard());
                } else {
                    action.cancel();
                }
            } else if (index == 9) {
                AuthController.logout();
            } else {
                action.cancel();
            }
        });
    }

    private void setMenuExpand(boolean full) {
        String icon;
        if (getComponentOrientation().isLeftToRight()) {
            icon = (full) ? "menu_left.svg" : "menu_right.svg";
        } else {
            icon = (full) ? "menu_right.svg" : "menu_left.svg";
        }

        menuButton.setIcon(new FlatSVGIcon("main/resources/images/menu/" + icon, 0.8f));
        menu.setMenuExpand(full);
        revalidate();
    }

    public void hideMenu() {
        menu.hideMenuItem();
    }

    public void showView(Component component) {
        panelBody.removeAll();
        panelBody.add(component);
        panelBody.repaint();
        panelBody.revalidate();
    }

    public void setSelectedMenu(int index, int subIndex) {
        menu.setSelectedMenu(index, subIndex);
    }

    private class MainViewLayout implements LayoutManager {
        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                return new Dimension(5, 5);
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
                boolean ltr = parent.getComponentOrientation().isLeftToRight();
                Insets insets = UIScale.scale(parent.getInsets());
                int x = insets.left;
                int y = insets.top;
                int width = parent.getWidth() - (insets.left + insets.right);
                int height = parent.getHeight() - (insets.top + insets.bottom);
                int menuWidth = UIScale.scale(menu.isMenuExpand() ? menu.getMenuMaxWidth() : menu.getMenuMinWidth());
                int menuX = ltr ? x : x + width - menuWidth;
                menu.setBounds(menuX, y, menuWidth, height);
                int menuButtonWidth = menuButton.getPreferredSize().width;
                int menuButtonHeight = menuButton.getPreferredSize().height;
                int menubX;
                if (ltr) {
                    menubX = (int) (x + menuWidth - (menuButtonWidth * (menu.isMenuExpand() ? 0.5f : 0.3f)));
                } else {
                    menubX = (int) (menuX - (menuButtonWidth * (menu.isMenuExpand() ? 0.5f : 0.7f)));
                }
                menuButton.setBounds(menubX, UIScale.scale(30), menuButtonWidth, menuButtonHeight);
                int gap = UIScale.scale(5);
                int bodyWidth = width - menuWidth - gap;
                int bodyHeight = height;
                int bodyX = ltr ? (x + menuWidth + gap) : x;
                int bodyY = y;
                panelBody.setBounds(bodyX, bodyY, bodyWidth, bodyHeight);
            }
        }
    }
}
