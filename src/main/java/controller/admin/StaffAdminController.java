package main.java.controller.admin;

import main.java.dao.MemberDAO;
import main.java.dao.PermissionDAO;
import main.java.dao.StaffDAO;
import main.java.model.Permission;
import main.java.model.Staff;

import java.util.List;

public class StaffAdminController {
    public static List<Staff> getAllStaffs() {
        return StaffDAO.getAllStaffs();
    }

    public static boolean addStaff(Staff staff, String[] selectedPermission) {
        Staff checkExistAccount = StaffDAO.findStaff(staff.getMember().getEmail(), staff.getMember().getPassword());
        if (checkExistAccount != null) {
            return false;
        }

        staff.setId(MemberDAO.addMember(staff.getMember()));
        if (staff.getId() == 0 || !StaffDAO.addStaff(staff)) {
            return false;
        }
        staff.getMember().setId(staff.getId());

        List<Permission> allPermissions = PermissionDAO.getAllPermissions();
        if (!PermissionDAO.addSelectedPermission(staff.getMember(), selectedPermission, allPermissions)) {
            return false;
        }

        return true;
    }

    public static boolean updateStaff(Staff staff, String[] permissionParts) {
        if (!StaffDAO.updateStaff(staff) || !MemberDAO.updateMember(staff.getMember())) {
            return false;
        }

        List<Permission> allPermissions = PermissionDAO.getAllPermissions();
        if (!PermissionDAO.updateSelectedPermission(staff.getMember(), permissionParts, allPermissions)) {
            return false;
        }

        return true;
    }

    public static boolean deleteStaff(int staffId) {
        return StaffDAO.deleteStaff(staffId);
    }

    public static List<Staff> searchStaff(String query) {
        return StaffDAO.search(query);
    }
}
