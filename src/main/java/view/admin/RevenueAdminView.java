package main.java.view.admin;

import main.java.controller.admin.RevenueReportController;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @author MnhTng
 * @Package main.java.view.admin
 * @date 5/7/2025 3:43 AM
 * @Copyright tùng
 */

public class RevenueAdminView {
    private boolean closeView = false;

    public RevenueAdminView() {
        while (!closeView) {
            show();
        }
    }

    public void show(){
        Scanner sc = new Scanner(System.in);

        System.out.println("========== Xem báo cáo doanh thu ==========");
        System.out.println("1. Theo tháng ");
        System.out.println("2. Theo quý ");
        System.out.println("3. Theo năm ");
        System.out.println("4. Quay lại");
        System.out.print("Chọn chức năng: ");

        try {
            switch (sc.nextInt()) {
                case 1:
                    this.revenueReportByMonthView();
                    break;
                case 2:
                    this.revenueReportByQuarterView();
                    break;
                case 3:
                    this.revenueReportByYearView();
                    break;
                case 4:
                    this.closeView = true;
                    new MainAdminView();
                    break;
                default:
                    System.out.println("Chức năng không hợp lệ!");
                    break;
            }
            sc.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Lỗi nhập liệu! Vui lòng nhập lại.");
        }
    }


    public void revenueReportByMonthView(){
//        System.out.println(1);
        Scanner sc = new Scanner(System.in);

        System.out.print("Nhập năm (ví dụ: 2024): ");
        int year = sc.nextInt();
        if (String.valueOf(year).length() < 4 ){
            System.out.println("Năm nhập vào không hợp lệ!");
            return;
        }
        System.out.print("Nhập tháng (1-12): ");
        int month = sc.nextInt();
        if (month < 1 || month > 12){
            System.out.println("Tháng nhập vào không hợp lệ! ");
            return;
        }
//        System.out.println(year + " " + month);
        RevenueReportController.RevenueReportByMonth(month, year);
    }

    public void revenueReportByQuarterView(){
//        System.out.println(2);
        Scanner sc = new Scanner(System.in);

        System.out.print("Nhập năm (ví dụ: 2024): ");
        int year = sc.nextInt();
        if (String.valueOf(year).length() < 4 ){
            System.out.println("Năm nhập vào không hợp lệ!");
            return;
        }
        System.out.print("Nhập quý (1-4): ");
        int quarter = sc.nextInt();
        if (quarter < 1 || quarter > 4){
            System.out.println("Quý nhập vào không hợp lệ! ");
            return;
        }
//        System.out.println(year + " " + month);
        RevenueReportController.RevenueReportByQuarter(quarter, year);
    }

    public void revenueReportByYearView(){
//        System.out.println(3);
        Scanner sc = new Scanner(System.in);

        System.out.print("Nhập năm (ví dụ: 2024): ");
        int year = sc.nextInt();
        if (String.valueOf(year).length() < 4 ){
            System.out.println("Năm nhập vào không hợp lệ!");
            return;
        }

//        System.out.println(year + " " + month);
        RevenueReportController.RevenueReportByYear(year);
    }
}
