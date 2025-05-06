package main.java.controller.admin;

import main.java.dao.RevenueReportDAO;

public class RevenueReportController {
    public static void RevenueReportByMonth(int month, int year){
        RevenueReportDAO.RevenueReportByMonth(month, year);
    }

    public static void RevenueReportByQuarter(int quarter, int year){
        RevenueReportDAO.RevenueReportByQuarter(quarter, year);
    }

    public static void RevenueReportByYear(int year){
        RevenueReportDAO.RevenueReportByYear(year);
    }

    public static void RevenueReportByEachMonth(int year){
        RevenueReportDAO.RevenueReportByEachMonth(year);
    }

    public static void RevenueReportByEachQuarter(int year){
        RevenueReportDAO.RevenueReportByEachQuarter(year);
    }
}
