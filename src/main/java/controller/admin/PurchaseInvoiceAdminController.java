package main.java.controller.admin;

import main.java.dao.PurchaseInvoiceDAO;
import main.java.dao.PurchaseInvoiceDetailDAO;
import main.java.dao.WarehouseDAO;
import main.java.model.PurchaseInvoice;
import main.java.model.PurchaseInvoiceDetail;

import java.util.List;

public class PurchaseInvoiceAdminController {
    public static List<PurchaseInvoiceDetail> getAllPurchaseInvoice() {
        return PurchaseInvoiceDetailDAO.getDetail("all");
    }

    public static PurchaseInvoice getPurchaseInvoiceById(String invoiceId) {
        return PurchaseInvoiceDAO.getPurchaseInvoiceById(invoiceId);
    }

    public static List<PurchaseInvoiceDetail> getPurchaseInvoiceDetailById(String invoiceId) {
        return PurchaseInvoiceDetailDAO.getDetail(invoiceId);
    }

    public static boolean addInventoryInvoice(PurchaseInvoice purchaseInvoice, List<PurchaseInvoiceDetail> purchaseInvoiceDetailList) {
        if (!PurchaseInvoiceDAO.addPurchaseInvoice(purchaseInvoice)) {
            return false;
        }

        for (PurchaseInvoiceDetail purchaseInvoiceDetail : purchaseInvoiceDetailList) {
            if (!PurchaseInvoiceDetailDAO.addPurchaseInvoiceDetail(purchaseInvoiceDetail)) {
                return false;
            }
        }

        return true;
    }

    public static boolean updateInventoryInvoice(PurchaseInvoiceDetail purchaseInvoiceDetail) {
        if (!PurchaseInvoiceDetailDAO.updatePurchaseInvoiceDetail(purchaseInvoiceDetail)) {
            return false;
        }

        // Update total amount
        float totalAmount = 0f;
        List<PurchaseInvoiceDetail> purchaseInvoiceDetails = PurchaseInvoiceDetailDAO.getDetail(purchaseInvoiceDetail.getPurchaseInvoiceId());
        for (PurchaseInvoiceDetail detail : purchaseInvoiceDetails) {
            totalAmount += detail.getSubTotal();
        }

        PurchaseInvoice purchaseInvoice = PurchaseInvoiceDAO.getPurchaseInvoiceById(purchaseInvoiceDetail.getPurchaseInvoiceId());
        purchaseInvoice.setTotalAmount(totalAmount);
        if (!PurchaseInvoiceDAO.updatePurchaseInvoice(purchaseInvoice)) {
            return false;
        }

        // Update warehouse
        String[] updateStatus = {"CONFIRMED", "EXCESS", "MISSING"};
        for (String status : updateStatus) {
            if (purchaseInvoiceDetail.getStatus().name().equals(status)) {
                if (!WarehouseDAO.importInventory(purchaseInvoiceDetail)) {
                    return false;
                }
            }
        }

        return true;
    }

    public static List<PurchaseInvoiceDetail> searchPurchaseInvoice(String query) {
        return PurchaseInvoiceDetailDAO.search(query);
    }
}
