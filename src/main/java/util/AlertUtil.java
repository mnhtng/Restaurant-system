package main.java.util;

import raven.toast.Notifications;

import java.awt.*;

/**
 * @author MnhTng
 * @Package main.java.util
 * @date 4/20/2025 1:41 PM
 * @Copyright tùng
 */

public class AlertUtil {
    public static void alert(String type, String message) {
        switch (type) {
            case "success" -> Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_RIGHT, message);
            case "error" -> Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_RIGHT, message);
            case "warning" -> Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_RIGHT, message);
            case "info" -> Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_RIGHT, message);
            default -> throw new IllegalArgumentException("Invalid alert type: " + type);
        }
    }

    public static void alertAndRedirect(String type, String message, Container nextView) {
        UiManager.getInstance().showView(nextView);

        switch (type) {
            case "success" -> Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_RIGHT, message);
            case "error" -> Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_RIGHT, message);
            case "warning" -> Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_RIGHT, message);
            case "info" -> Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_RIGHT, message);
            default -> throw new IllegalArgumentException("Invalid alert type: " + type);
        }
    }
}
