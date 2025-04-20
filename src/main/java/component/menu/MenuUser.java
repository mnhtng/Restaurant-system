package main.java.component.menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author MnhTng
 * @Package main.java.component.menu
 * @date 4/20/2025 9:19 PM
 * @Copyright tùng
 */

public class MenuUser extends JPanel {
    private final JLabel avatar;
    private final JLabel usernameLabel;

    public MenuUser(String username, ImageIcon avatarIcon) {
        this.setLayout(new BorderLayout(10, 0));
        this.putClientProperty("FlatLaf.style", "border:5,5,5,5; background:$Menu.background; arc:10");

        // Avatar
        avatar = new JLabel();
        avatar.setIcon(avatarIcon);

        // Username
        usernameLabel = new JLabel(username);
        usernameLabel.putClientProperty("FlatLaf.style", "font: bold +1; foreground:$Menu.foreground");

        // Add components
        this.add(avatar, BorderLayout.WEST);
        this.add(usernameLabel, BorderLayout.CENTER);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onUserMenuClick();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getDefaultCursor());
            }
        });
    }

    public void setMenuExpand(boolean expand) {
        if (expand) {
            usernameLabel.setVisible(true);
        } else {
            usernameLabel.setVisible(false);
        }

        revalidate();
        repaint();
    }

    private void onUserMenuClick() {
        JOptionPane.showMessageDialog(this, "User menu clicked!");
    }
}
