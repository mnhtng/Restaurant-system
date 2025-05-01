package main.java.controller.admin;

import main.java.dao.PermissionDAO;
import main.java.model.Permission;

import java.util.List;

/**
 * @author MnhTng
 * @Package main.java.controller.admin
 * @date 4/28/2025 6:16 PM
 * @Copyright tùng
 */

public class PermissionAdminController {
    public static List<Permission> getAllPermissions() {
        return PermissionDAO.getAllPermissions();
    }

    public static List<Permission> getPermissionsById(int id) {
        return PermissionDAO.findPermissionById(id);
    }
}
