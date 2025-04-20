package main.java.component.menu;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;
import main.java.component.menu.theme.LightDarkMode;
import main.java.util.Session;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author MnhTng
 * @Package main.java.component.menu
 * @date 4/19/2025 3:29 PM
 * @Copyright tùng
 */

public class Menu extends JPanel {
    private final JLabel header;
    private final JScrollPane scroll;
    private final JPanel panelMenu;
    private final LightDarkMode lightDarkMode;
    private final MenuUser menuUser;

    private final List<MenuEvent> events = new ArrayList<>();
    private boolean menuExpand = true;
    private final String headerName = "Hang Rong";

    // Menu config
    protected final boolean hideMenuTitleOnMinimum = true;
    protected final int menuTitleLeftInset = 5;
    protected final int menuTitleVgap = 5;
    protected final int menuMaxWidth = 250;
    protected final int menuMinWidth = 60;
    protected final int headerFullHgap = 5;

    public Menu() {
        this.setLayout(new MenuLayout());
        this.putClientProperty(FlatClientProperties.STYLE, "border:20,2,2,2;"
                + "background:$Menu.background;"
                + "arc:10");

        header = new JLabel(headerName);
        header.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/main/resources/images/logo.png"))));
        header.putClientProperty(FlatClientProperties.STYLE, "font:$Menu.header.font;"
                + "foreground:$Menu.foreground");

        //  Menu
        scroll = new JScrollPane();
        panelMenu = new JPanel(new MenuItemLayout(this));
        panelMenu.putClientProperty(FlatClientProperties.STYLE, "border:5,5,5,5;"
                + "background:$Menu.background");

        scroll.setViewportView(panelMenu);
        scroll.putClientProperty(FlatClientProperties.STYLE, "border:null");

        JScrollBar vscroll = scroll.getVerticalScrollBar();
        vscroll.setUnitIncrement(10);
        vscroll.putClientProperty(FlatClientProperties.STYLE, "width:$Menu.scroll.width;"
                + "trackInsets:$Menu.scroll.trackInsets;"
                + "thumbInsets:$Menu.scroll.thumbInsets;"
                + "background:$Menu.ScrollBar.background;"
                + "thumb:$Menu.ScrollBar.thumb");

        this.createMenu();

        lightDarkMode = new LightDarkMode();

        String authedUser = Session.getInstance().getCurrentUser().getName();
        String avatarURL = "";
        String role = Session.getInstance().getCurrentUser().getRole().toString();
        String gender = Session.getInstance().getCurrentUser().getGender().toString();

        if ("MANAGER".equals(role)) {
            avatarURL = "admin-avt.svg";
        } else if ("MEMBER".equals(role)) {
            if ("MALE".equals(gender)) {
                avatarURL = "user-male-avt.svg";
            } else {
                avatarURL = "user-female-avt.svg";
            }
        } else if ("SERVICE_CLERK".equals(role)) {
            if ("MALE".equals(gender)) {
                avatarURL = "service-male-avt.svg";
            } else {
                avatarURL = "service-female-avt.svg";
            }
        } else {
            if ("MALE".equals(gender)) {
                avatarURL = "inventory-male-avt.svg";
            } else {
                avatarURL = "inventory-female-avt.svg";
            }
        }
        menuUser = new MenuUser(authedUser, new FlatSVGIcon("main/resources/images/avatar/" + avatarURL, 0.1f));

        this.add(menuUser);
        this.add(header);
        this.add(scroll);
        this.add(lightDarkMode);
        this.add(menuUser);
    }

    private final String[][] menuItems = {
            {"~MAIN~"},
            {"Dashboard"},
            {"~INVENTORY~"},
            {"Email", "Inbox", "Read", "Compost"},
            {"Chat"},
            {"Calendar"},
            {"~SERVICE~"},
            {"Advanced UI", "Cropper", "Owl Carousel", "Sweet Alert"},
            {"Forms", "Basic Elements", "Advanced Elements", "Editors", "Wizard"},
            {"~MEMBER~"},
            {"Charts", "Apex", "Flot", "Peity", "Sparkline"},
            {"Icons", "Feather Icons", "Flag Icons", "Mdi Icons"},
            {"Special Pages", "Blank page", "Faq", "Invoice", "Profile", "Pricing", "Timeline"},
            {"Logout"}
    };

    public boolean isMenuExpand() {
        return menuExpand;
    }

    public void setMenuExpand(boolean menuExpand) {
        this.menuExpand = menuExpand;

        if (menuExpand) {
            header.setText(headerName);
            header.setHorizontalAlignment(getComponentOrientation().isLeftToRight() ? SwingConstants.LEFT : SwingConstants.RIGHT);
        } else {
            header.setText("");
            header.setHorizontalAlignment(SwingConstants.CENTER);
        }

        for (Component com : panelMenu.getComponents()) {
            if (com instanceof MenuItem) {
                ((MenuItem) com).setExpand(menuExpand);
            }
        }

        lightDarkMode.setMenuExpand(menuExpand);
        menuUser.setMenuExpand(menuExpand);
    }

    private void createMenu() {
        int index = 0;

        for (String[] item : menuItems) {
            String menuName = item[0];
            if (menuName.startsWith("~") && menuName.endsWith("~")) {
                panelMenu.add(createTitle(menuName));
            } else {
                MenuItem menuItem = new MenuItem(this, item, index++, events);
                panelMenu.add(menuItem);
            }
        }
    }

    private JLabel createTitle(String title) {
        String menuName = title.substring(1, title.length() - 1);
        JLabel lbTitle = new JLabel(menuName);
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:$Menu.label.font;"
                + "foreground:$Menu.title.foreground");
        return lbTitle;
    }

    public void setSelectedMenu(int index, int subIndex) {
        runEvent(index, subIndex);
    }

    protected void setSelected(int index, int subIndex) {
        int size = panelMenu.getComponentCount();
        for (int i = 0; i < size; i++) {
            Component com = panelMenu.getComponent(i);
            if (com instanceof MenuItem item) {
                if (item.getMenuIndex() == index) {
                    item.setSelectedIndex(subIndex);
                } else {
                    item.setSelectedIndex(-1);
                }
            }
        }
    }

    protected void runEvent(int index, int subIndex) {
        MenuAction menuAction = new MenuAction();
        for (MenuEvent event : events) {
            event.menuSelected(index, subIndex, menuAction);
        }
        if (!menuAction.isCancel()) {
            setSelected(index, subIndex);
        }
    }

    public void addMenuEvent(MenuEvent event) {
        events.add(event);
    }

    public void hideMenuItem() {
        for (Component com : panelMenu.getComponents()) {
            if (com instanceof MenuItem) {
                ((MenuItem) com).hideMenuItem();
            }
        }
        revalidate();
    }

    public boolean isHideMenuTitleOnMinimum() {
        return hideMenuTitleOnMinimum;
    }

    public int getMenuTitleLeftInset() {
        return menuTitleLeftInset;
    }

    public int getMenuTitleVgap() {
        return menuTitleVgap;
    }

    public int getMenuMaxWidth() {
        return menuMaxWidth;
    }

    public int getMenuMinWidth() {
        return menuMinWidth;
    }

    private class MenuLayout implements LayoutManager {
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
                Insets insets = parent.getInsets();
                int x = insets.left;
                int y = insets.top;
                int gap = UIScale.scale(5);
                int sheaderFullHgap = UIScale.scale(headerFullHgap);
                int width = parent.getWidth() - (insets.left + insets.right);
                int height = parent.getHeight() - (insets.top + insets.bottom);
                int iconHeight = header.getPreferredSize().height;
                int hgap = menuExpand ? sheaderFullHgap : 0;
                int accentColorHeight = 0;

                if (menuUser.isVisible()) {
                    accentColorHeight = menuUser.getPreferredSize().height + gap;
                }

                header.setBounds(x + hgap, y, width - (hgap * 2), iconHeight);
                int lightDarkGap = UIScale.scale(10);
                int lightDarkWidth = width - lightDarkGap * 2;
                int lightDarkHeight = lightDarkMode.getPreferredSize().height;
                int ldx = x + lightDarkGap;
                int ldy = y + height - lightDarkHeight - lightDarkGap - accentColorHeight;

                int menuX = x;
                int menuY = y + iconHeight + gap;
                int menuWidth = width;
                int menuHeight = height - (iconHeight + gap) - (lightDarkHeight + lightDarkGap * 2) - (accentColorHeight);
                scroll.setBounds(menuX, menuY, menuWidth, menuHeight);

                lightDarkMode.setBounds(ldx, ldy, lightDarkWidth, lightDarkHeight);

                if (menuUser.isVisible()) {
                    int toolbarHeight = menuUser.getPreferredSize().height;
                    int toolbarWidth = Math.min(menuUser.getPreferredSize().width, lightDarkWidth);
                    int tby = y + height - toolbarHeight - lightDarkGap;
                    int tbx = ldx + ((lightDarkWidth - toolbarWidth) / 2);
                    menuUser.setBounds(tbx, tby, toolbarWidth, toolbarHeight);
                }
            }
        }
    }
}
