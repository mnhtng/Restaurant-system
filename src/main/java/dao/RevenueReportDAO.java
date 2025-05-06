package main.java.dao;

import main.java.controller.admin.RevenueReportController;
import main.java.util.JDBCConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class RevenueReportDAO {
    public static void RevenueReportByMonth(int month, int year) {
        int previousMonth = (month == 1) ? 12 : month - 1;
        int previousYear = (month == 1) ? year - 1 : year;

        String previousMonthSql = "SELECT \n" +
                "    COUNT(DISTINCT orders.id) AS total_orders,\n" +
                "    SUM(CASE WHEN orders.status = 'completed' THEN orders.total_amount ELSE 0 END) AS total_revenue\n" +
                "FROM [order] orders\n" +
                "WHERE \n" +
                "    YEAR(orders.paid_at) = ? AND \n" +
                "    MONTH(orders.paid_at) = ? AND\n" +
                "    orders.status IN ('completed')";

        String sql = "SELECT \n" +
                "    FORMAT(orders.paid_at, 'MM/yyyy') AS month_year,\n" +
                "    COUNT(DISTINCT orders.id) AS total_orders,\n" +
                "    SUM(CASE WHEN orders.status = 'completed' THEN orders.total_amount ELSE 0 END) AS total_revenue,\n" +
                "    AVG(orders.total_amount) AS average_order_value,\n" +
                "    \n" +
                "    SUM(CASE WHEN orders.payment_method = 'cash' AND orders.status = 'completed' THEN orders.total_amount ELSE 0 END) AS cash_revenue,\n" +
                "    SUM(CASE WHEN orders.payment_method = 'card' AND orders.status = 'completed' THEN orders.total_amount ELSE 0 END) AS card_revenue,\n" +
                "    SUM(CASE WHEN orders.payment_method = 'qr_code' AND orders.status = 'completed' THEN orders.total_amount ELSE 0 END) AS qr_code_revenue,\n" +
                "    \n" +
                "    \n" +
                "\tCOUNT(DISTINCT CASE WHEN orders.status = 'completed' THEN orders.id END) AS completed_orders,\n" +
                "    COUNT(DISTINCT CASE WHEN orders.status = 'refunded' THEN orders.id END) AS refunded_orders,\n" +
                "\n" +
                "\tSUM(CASE WHEN CAST(orders.expected_arrival_time AS TIME) BETWEEN '11:00:00' AND '14:00:00' AND orders.status = 'completed' THEN orders.total_amount ELSE 0 END) AS lunch_peak_revenue,\n" +
                "    SUM(CASE WHEN CAST(orders.expected_arrival_time AS TIME) BETWEEN '17:00:00' AND '21:00:00' AND orders.status = 'completed' THEN orders.total_amount ELSE 0 END) AS dinner_peak_revenue \n" +
                "FROM [order] orders\n" +
                "JOIN member ON member.id = orders.customer_id\n" +
                "WHERE \n" +
                "    YEAR(orders.paid_at) = ? AND \n" +
                "    MONTH(orders.paid_at) = ? AND\n" +
                "    orders.status IN ('completed', 'refunded')\n" +
                "GROUP BY FORMAT(orders.paid_at, 'MM/yyyy')";
        Connection connection = JDBCConnection.getInstance().getConnection();

        int prevTotalOrders = 0;
        float prevTotalRevenue = 0;

        try (PreparedStatement prevStatement = connection.prepareStatement(previousMonthSql)) {
            prevStatement.setString(1, String.valueOf(previousYear));
            prevStatement.setString(2, String.valueOf(previousMonth));
            try (ResultSet prevResultSet = prevStatement.executeQuery()) {
                if (prevResultSet.next()) {
                    prevTotalOrders = prevResultSet.getInt("total_orders");
                    prevTotalRevenue = prevResultSet.getFloat("total_revenue");
                }
                prevStatement.close();
            } catch (SQLException e) {
                System.out.println("Đã có lỗi xảy ra khi truy vấn kết quả!");
            }
        } catch (SQLException e) {
            System.out.println("Đã có lỗi xảy ra khi truy vấn kết quả!");
        }

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, String.valueOf(year));
            statement.setString(2, String.valueOf(month));
            try (ResultSet resultSet = statement.executeQuery()) {
                System.out.println("===========================================================");
                System.out.println();
                System.out.println("BÁO CÁO DOANH THU THÁNG " + month + "/" + year);
                System.out.println();
                System.out.println("===========================================================");
                System.out.println();

                while (resultSet.next()) {
                    int totalOrders = resultSet.getInt("total_orders");
                    float totalRevenue = resultSet.getFloat("total_revenue");
                    float averageOrderValue = resultSet.getFloat("average_order_value");
                    System.out.println("TỔNG QUAN: ");
                    System.out.println("-----------------------------------------------");
                    System.out.printf("| %-25s | %-15d |\n", "Tổng số đơn hàng:", totalOrders);
                    System.out.printf("| %-25s | %-15s |\n", "Tổng doanh thu:", String.format("%.2f", totalRevenue) + " VND");
                    System.out.printf("| %-25s | %-15s |\n", "Giá trị đơn trung bình:", String.format("%.2f", averageOrderValue) + " VND");
                    System.out.println("-----------------------------------------------");

                    float cashRevenue = resultSet.getFloat("cash_revenue");
                    float cardRevenue = resultSet.getFloat("card_revenue");
                    float qrcodeRevenue = resultSet.getFloat("qr_code_revenue");
                    System.out.println("PHÂN TÍCH THEO PHƯƠNG THỨC THANH TOÁN");
                    System.out.println("-----------------------------------------------");
                    System.out.printf("| %-15s | %-15s | %-10s | %n", "Phương thức", "Thanh toán", "Tỷ lệ (%)");
                    System.out.println("-----------------------------------------------");
                    System.out.printf("| %-15s | %-15s | %-10.2f | %n", "Tiền mặt", String.format("%.2f", cashRevenue) + " VND", cashRevenue / totalRevenue * 100);
                    System.out.printf("| %-15s | %-15s | %-10.2f | %n", "Thẻ", String.format("%.2f", cardRevenue) + " VND", cardRevenue / totalRevenue * 100);
                    System.out.printf("| %-15s | %-15s | %-10.2f | %n", "Quét mã QR", String.format("%.2f", qrcodeRevenue) + " VND", qrcodeRevenue / totalRevenue * 100);
                    System.out.println("-----------------------------------------------");

                    int completedOrder = resultSet.getInt("completed_orders");
                    int refundedOrder = resultSet.getInt("refunded_orders");
                    System.out.println("PHÂN TÍCH THEO TRẠNG THÁI ĐƠN HÀNG:");
                    System.out.println("-----------------------------------------------");
                    System.out.printf("| %-15s | %-15s | %-10s | %n", "Trạng thái", "Số đơn", "Tỷ lệ (%)");
                    System.out.println("-----------------------------------------------");
                    System.out.printf("| %-15s | %-15d | %-10.2f | %n", "Hoàn thành", completedOrder, (float) completedOrder / totalOrders * 100);
                    System.out.printf("| %-15s | %-15d | %-10.2f | %n", "Hoàn tiền", refundedOrder, (float) refundedOrder / totalOrders * 100);
                    System.out.println("-----------------------------------------------");

                    float peakLunchRevenue = resultSet.getFloat("lunch_peak_revenue");
                    float peakDinnerRevenue = resultSet.getFloat("dinner_peak_revenue");
                    float otherHourPeakRevenue = totalRevenue - peakDinnerRevenue - peakLunchRevenue;
                    System.out.println("PHÂN TÍCH THEO KHUNG GIỜ:");
                    System.out.println("-----------------------------------------------");
                    System.out.printf("| %-22s | %-15s | %-10s | %n", "Khung giờ", "Doanh thu (VND)", "Tỷ lệ (%)");
                    System.out.println("-----------------------------------------------");
                    System.out.printf("| %-22s | %-15.2f | %-10.2f | %n", "Cao điểm trưa (11-14h):", peakLunchRevenue, (float) peakLunchRevenue / totalRevenue * 100);
                    System.out.printf("| %-22s | %-15.2f | %-10.2f | %n", "Cao điểm tối (17-21h):", peakDinnerRevenue, (float) peakDinnerRevenue / totalRevenue * 100);
                    System.out.printf("| %-22s | %-15.2f | %-10.2f | %n", "Khung giờ khác:", otherHourPeakRevenue, (float) otherHourPeakRevenue / totalRevenue * 100);
                    System.out.println("-----------------------------------------------");

                    System.out.println("SO SÁNH VỚI THÁNG TRƯỚC:");
                    System.out.println("-----------------------------------------------");
                    if (prevTotalRevenue > totalRevenue) {
                        System.out.println("Doanh thu (VND): -" + (prevTotalRevenue - totalRevenue) + " VND");
                    } else {
                        System.out.println("Doanh thu (VND): +" + (totalRevenue - prevTotalRevenue) + " VND");
                    }
                    if (prevTotalOrders > totalOrders) {
                        System.out.println("Số đơn: -" + (prevTotalOrders - totalOrders) + " đơn");
                    } else {
                        System.out.println("Số đơn: +" + (totalOrders - prevTotalOrders) + " đơn");
                    }

                    System.out.println("-----------------------------------------------");

                }

            } catch (SQLException e) {
                System.out.println("Đã có lỗi xảy ra khi truy vấn kết quả!");
            }
        } catch (SQLException e) {
            System.out.println("Đã có lỗi xảy ra khi truy vấn kết quả!");
        }
    }

    public static void RevenueReportByQuarter(int quarter, int year) {
        int previousQuarter = (quarter == 1) ? 4 : quarter - 1;
        int previousYear = (quarter == 1) ? year - 1 : year;

        String previousQuarterSql = "SELECT \n" +
                "    COUNT(DISTINCT orders.id) AS total_orders,\n" +
                "    SUM(CASE WHEN orders.status = 'completed' THEN orders.total_amount ELSE 0 END) AS total_revenue\n" +
                "FROM [order] orders\n" +
                "WHERE \n" +
                "    YEAR(orders.paid_at) = ? AND \n" +
                "    DATEPART(QUARTER, orders.paid_at) = ? AND\n" +
                "    orders.status IN ('completed')";

        String sql = "SELECT \n" +
                "    CONCAT('Q', DATEPART(QUARTER, orders.paid_at), '/', YEAR(orders.paid_at)) AS quarter_year,\n" +
                "    COUNT(DISTINCT orders.id) AS total_orders,\n" +
                "    SUM(CASE WHEN orders.status = 'completed' THEN orders.total_amount ELSE 0 END) AS total_revenue,\n" +
                "    AVG(orders.total_amount) AS average_order_value,\n" +
                "    \n" +
                "    SUM(CASE WHEN orders.payment_method = 'cash' AND orders.status = 'completed' THEN orders.total_amount ELSE 0 END) AS cash_revenue,\n" +
                "    SUM(CASE WHEN orders.payment_method = 'card' AND orders.status = 'completed' THEN orders.total_amount ELSE 0 END) AS card_revenue,\n" +
                "    SUM(CASE WHEN orders.payment_method = 'qr_code' AND orders.status = 'completed' THEN orders.total_amount ELSE 0 END) AS qr_code_revenue,\n" +
                "    \n" +
                "    COUNT(DISTINCT CASE WHEN orders.status = 'completed' THEN orders.id END) AS completed_orders,\n" +
                "    COUNT(DISTINCT CASE WHEN orders.status = 'refunded' THEN orders.id END) AS refunded_orders,\n" +
                "\n" +
                "    SUM(CASE WHEN CAST(orders.expected_arrival_time AS TIME) BETWEEN '11:00:00' AND '14:00:00' AND orders.status = 'completed' THEN orders.total_amount ELSE 0 END) AS lunch_peak_revenue,\n" +
                "    SUM(CASE WHEN CAST(orders.expected_arrival_time AS TIME) BETWEEN '17:00:00' AND '21:00:00' AND orders.status = 'completed' THEN orders.total_amount ELSE 0 END) AS dinner_peak_revenue\n" +
                "                   \n" +
                "FROM [order] orders\n" +
                "WHERE \n" +
                "    YEAR(orders.paid_at) = ? AND \n" +
                "    DATEPART(QUARTER, orders.paid_at) = ? AND\n" +
                "    orders.status IN ('completed', 'refunded')\n" +
                "GROUP BY DATEPART(QUARTER, orders.paid_at), YEAR(orders.paid_at)\n" +
                "ORDER BY YEAR(orders.paid_at), DATEPART(QUARTER, orders.paid_at)";
        Connection connection = JDBCConnection.getInstance().getConnection();

        int prevTotalOrders = 0;
        float prevTotalRevenue = 0;

        try (PreparedStatement prevStatement = connection.prepareStatement(previousQuarterSql)) {
            prevStatement.setString(1, String.valueOf(previousYear));
            prevStatement.setString(2, String.valueOf(previousQuarter));
            try (ResultSet prevResultSet = prevStatement.executeQuery()) {
                if (prevResultSet.next()) {
                    prevTotalOrders = prevResultSet.getInt("total_orders");
                    prevTotalRevenue = prevResultSet.getFloat("total_revenue");
                }
            } catch (SQLException e) {
                System.out.println("Đã có lỗi xảy ra khi truy vấn kết quả!");
            }
        } catch (SQLException e) {
            System.out.println("Đã có lỗi xảy ra khi truy vấn kết quả!");
        }

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, String.valueOf(year));
            statement.setString(2, String.valueOf(quarter));
            try (ResultSet resultSet = statement.executeQuery()) {
                System.out.println("===========================================================");
                System.out.println();
                System.out.println("BÁO CÁO DOANH THU QUÝ " + quarter + "/" + year);
                System.out.println();
                System.out.println("===========================================================");
                System.out.println();

                while (resultSet.next()) {
                    int totalOrders = resultSet.getInt("total_orders");
                    float totalRevenue = resultSet.getFloat("total_revenue");
                    float averageOrderValue = resultSet.getFloat("average_order_value");

                    System.out.println("TỔNG QUAN: ");
                    System.out.println("-----------------------------------------------");
                    System.out.printf("| %-25s | %-15d | %n", "Tổng số đơn hàng:", totalOrders);
                    System.out.printf("| %-25s | %-15.2f | %n", "Tổng doanh thu:", totalRevenue);
                    System.out.printf("| %-25s | %-15.2f | %n", "Giá trị đơn trung bình:", averageOrderValue);
                    System.out.println("-----------------------------------------------");

                    float cashRevenue = resultSet.getFloat("cash_revenue");
                    float cardRevenue = resultSet.getFloat("card_revenue");
                    float qrcodeRevenue = resultSet.getFloat("qr_code_revenue");
                    System.out.println("PHÂN TÍCH THEO PHƯƠNG THỨC THANH TOÁN");
                    System.out.println("-----------------------------------------------");
                    System.out.printf("| %-15s | %-15s | %-10s | %n", "Phương thức", "Thanh toán", "Tỷ lệ (%)");
                    System.out.println("-----------------------------------------------");
                    System.out.printf("| %-15s | %-15.2f | %-10.2f | %n", "Tiền mặt", cashRevenue, cashRevenue / totalRevenue * 100);
                    System.out.printf("| %-15s | %-15.2f | %-10.2f | %n", "Thẻ", cardRevenue, cardRevenue / totalRevenue * 100);
                    System.out.printf("| %-15s | %-15.2f | %-10.2f | %n", "Quét mã QR", qrcodeRevenue, qrcodeRevenue / totalRevenue * 100);
                    System.out.println("-----------------------------------------------");

                    int completedOrder = resultSet.getInt("completed_orders");
                    int refundedOrder = resultSet.getInt("refunded_orders");
                    System.out.println("PHÂN TÍCH THEO TRẠNG THÁI ĐƠN HÀNG:");
                    System.out.println("-----------------------------------------------");
                    System.out.printf("| %-15s | %-15s | %-10s | %n", "Trạng thái", "Số đơn", "Tỷ lệ (%)");
                    System.out.println("-----------------------------------------------");
                    System.out.printf("| %-15s | %-15d | %-10.2f | %n", "Hoàn thành", completedOrder, (float) completedOrder / totalOrders * 100);
                    System.out.printf("| %-15s | %-15d | %-10.2f | %n", "Hoàn tiền", refundedOrder, (float) refundedOrder / totalOrders * 100);
                    System.out.println("-----------------------------------------------");

                    float peakLunchRevenue = resultSet.getFloat("lunch_peak_revenue");
                    float peakDinnerRevenue = resultSet.getFloat("dinner_peak_revenue");
                    float otherHourPeakRevenue = totalRevenue - peakDinnerRevenue - peakLunchRevenue;
                    System.out.println("PHÂN TÍCH THEO KHUNG GIỜ:");
                    System.out.println("-----------------------------------------------");
                    System.out.printf("| %-22s | %-15s | %-10s | %n", "Khung giờ", "Doanh thu (VND)", "Tỷ lệ (%)");
                    System.out.println("-----------------------------------------------");
                    System.out.printf("| %-22s | %-15.2f | %-10.2f | %n", "Cao điểm trưa (11-14h):", peakLunchRevenue, (float) peakLunchRevenue / totalRevenue * 100);
                    System.out.printf("| %-22s | %-15.2f | %-10.2f | %n", "Cao điểm tối (17-21h):", peakDinnerRevenue, (float) peakDinnerRevenue / totalRevenue * 100);
                    System.out.printf("| %-22s | %-15.2f | %-10.2f | %n", "Khung giờ khác:", otherHourPeakRevenue, (float) otherHourPeakRevenue / totalRevenue * 100);
                    System.out.println("-----------------------------------------------");

                    System.out.println("SO SÁNH VỚI QUÝ TRƯỚC:");
                    System.out.println("-----------------------------------------------");
                    if (prevTotalRevenue > totalRevenue) {
                        System.out.println("Doanh thu (VND): -" + (prevTotalRevenue - totalRevenue) + " VND");
                    } else {
                        System.out.println("Doanh thu (VND): +" + (totalRevenue - prevTotalRevenue) + "VND");
                    }
                    if (prevTotalOrders > totalOrders) {
                        System.out.println("Số đơn: -" + (prevTotalOrders - totalOrders) + " đơn");
                    } else {
                        System.out.println("Số đơn: +" + (totalOrders - prevTotalOrders) + " đơn");
                    }

                    System.out.println("-----------------------------------------------");

                    // Thêm phân tích theo tháng trong năm
                    RevenueReportController.RevenueReportByEachQuarter(year);
                }
            }
        } catch (SQLException e) {
            System.out.println("Đã có lỗi xảy ra khi truy vấn kết quả!");
        }
    }

    public static void RevenueReportByYear(int year) {
        int previousYear = year - 1;

        String previousYearSql = "SELECT \n" +
                "    COUNT(DISTINCT orders.id) AS total_orders,\n" +
                "    SUM(CASE WHEN orders.status = 'completed' THEN orders.total_amount ELSE 0 END) AS total_revenue\n" +
                "FROM [order] orders\n" +
                "WHERE \n" +
                "    YEAR(orders.paid_at) = ? AND \n" +
                "    orders.status IN ('completed')";

        String sql = "SELECT \n" +
                "    YEAR(orders.paid_at) AS year,\n" +
                "    COUNT(DISTINCT orders.id) AS total_orders,\n" +
                "    SUM(CASE WHEN orders.status = 'completed' THEN orders.total_amount ELSE 0 END) AS total_revenue,\n" +
                "    AVG(orders.total_amount) AS average_order_value,\n" +
                "    \n" +
                "    SUM(CASE WHEN orders.payment_method = 'cash' AND orders.status = 'completed' THEN orders.total_amount ELSE 0 END) AS cash_revenue,\n" +
                "    SUM(CASE WHEN orders.payment_method = 'card' AND orders.status = 'completed' THEN orders.total_amount ELSE 0 END) AS card_revenue,\n" +
                "    SUM(CASE WHEN orders.payment_method = 'qr_code' AND orders.status = 'completed' THEN orders.total_amount ELSE 0 END) AS qr_code_revenue,\n" +
                "    \n" +
                "    COUNT(DISTINCT CASE WHEN orders.status = 'completed' THEN orders.id END) AS completed_orders,\n" +
                "    COUNT(DISTINCT CASE WHEN orders.status = 'refunded' THEN orders.id END) AS refunded_orders,\n" +
                "\n" +
                "    SUM(CASE WHEN CAST(orders.expected_arrival_time AS TIME) BETWEEN '11:00:00' AND '14:00:00' AND orders.status = 'completed' THEN orders.total_amount ELSE 0 END) AS lunch_peak_revenue,\n" +
                "    SUM(CASE WHEN CAST(orders.expected_arrival_time AS TIME) BETWEEN '17:00:00' AND '21:00:00' AND orders.status = 'completed' THEN orders.total_amount ELSE 0 END) AS dinner_peak_revenue\n" +
                "                   \n" +
                "FROM [order] orders\n" +
                "WHERE \n" +
                "    YEAR(orders.paid_at) = ? AND \n" +
                "    orders.status IN ('completed', 'refunded')\n" +
                "GROUP BY YEAR(orders.paid_at)\n" +
                "ORDER BY YEAR(orders.paid_at)";

        Connection connection = JDBCConnection.getInstance().getConnection();

        int prevTotalOrders = 0;
        float prevTotalRevenue = 0;

        try (PreparedStatement prevStatement = connection.prepareStatement(previousYearSql)) {
            prevStatement.setString(1, String.valueOf(previousYear));
            try (ResultSet prevResultSet = prevStatement.executeQuery()) {
                if (prevResultSet.next()) {
                    prevTotalOrders = prevResultSet.getInt("total_orders");
                    prevTotalRevenue = prevResultSet.getFloat("total_revenue");
                }
            }
        } catch (SQLException e) {
            System.out.println("Đã có lỗi xảy ra khi truy vấn kết quả!");
        }

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, String.valueOf(year));
            try (ResultSet resultSet = statement.executeQuery()) {
                System.out.println("===========================================================");
                System.out.println();
                System.out.println("BÁO CÁO DOANH THU NĂM " + year);
                System.out.println();
                System.out.println("===========================================================");
                System.out.println();

                while (resultSet.next()) {
                    int totalOrders = resultSet.getInt("total_orders");
                    float totalRevenue = resultSet.getFloat("total_revenue");
                    float averageOrderValue = resultSet.getFloat("average_order_value");

                    System.out.println("TỔNG QUAN: ");
                    System.out.println("-----------------------------------------------");
                    System.out.printf("| %-25s | %-15d |\n", "Tổng số đơn hàng:", totalOrders);
                    System.out.printf("| %-25s | %-15.0f |\n", "Tổng doanh thu:", totalRevenue);
                    System.out.printf("| %-25s | %-15.0f |\n", "Giá trị đơn trung bình:", averageOrderValue);
                    System.out.println("-----------------------------------------------");

                    float cashRevenue = resultSet.getFloat("cash_revenue");
                    float cardRevenue = resultSet.getFloat("card_revenue");
                    float qrcodeRevenue = resultSet.getFloat("qr_code_revenue");
                    System.out.println("PHÂN TÍCH THEO PHƯƠNG THỨC THANH TOÁN");
                    System.out.println("-----------------------------------------------");
                    System.out.printf("| %-15s | %-15s | %-10s | %n", "Phương thức", "Thanh toán", "Tỷ lệ (%)");
                    System.out.println("-----------------------------------------------");
                    System.out.printf("| %-15s | %-15.0f | %-10.2f | %n", "Tiền mặt", cashRevenue, cashRevenue / totalRevenue * 100);
                    System.out.printf("| %-15s | %-15.0f | %-10.2f | %n", "Thẻ", cardRevenue, cardRevenue / totalRevenue * 100);
                    System.out.printf("| %-15s | %-15.0f | %-10.2f | %n", "Quét mã QR", qrcodeRevenue, qrcodeRevenue / totalRevenue * 100);
                    System.out.println("-----------------------------------------------");

                    int completedOrder = resultSet.getInt("completed_orders");
                    int refundedOrder = resultSet.getInt("refunded_orders");
                    System.out.println("PHÂN TÍCH THEO TRẠNG THÁI ĐƠN HÀNG:");
                    System.out.println("-----------------------------------------------");
                    System.out.printf("| %-15s | %-15s | %-10s | %n", "Trạng thái", "Số đơn", "Tỷ lệ (%)");
                    System.out.println("-----------------------------------------------");
                    System.out.printf("| %-15s | %-15d | %-10.2f | %n", "Hoàn thành", completedOrder, (float) completedOrder / totalOrders * 100);
                    System.out.printf("| %-15s | %-15d | %-10.2f | %n", "Hoàn tiền", refundedOrder, (float) refundedOrder / totalOrders * 100);
                    System.out.println("-----------------------------------------------");

                    float peakLunchRevenue = resultSet.getFloat("lunch_peak_revenue");
                    float peakDinnerRevenue = resultSet.getFloat("dinner_peak_revenue");
                    float otherHourPeakRevenue = totalRevenue - peakDinnerRevenue - peakLunchRevenue;
                    System.out.println("PHÂN TÍCH THEO KHUNG GIỜ:");
                    System.out.println("-----------------------------------------------");
                    System.out.printf("| %-22s | %-15s | %-10s | %n", "Khung giờ", "Doanh thu (VND)", "Tỷ lệ (%)");
                    System.out.println("-----------------------------------------------");
                    System.out.printf("| %-22s | %-15.0f | %-10.2f | %n", "Cao điểm trưa (11-14h):", peakLunchRevenue, (float) peakLunchRevenue / totalRevenue * 100);
                    System.out.printf("| %-22s | %-15.0f | %-10.2f | %n", "Cao điểm tối (17-21h):", peakDinnerRevenue, (float) peakDinnerRevenue / totalRevenue * 100);
                    System.out.printf("| %-22s | %-15.0f | %-10.2f | %n", "Khung giờ khác:", otherHourPeakRevenue, (float) otherHourPeakRevenue / totalRevenue * 100);
                    System.out.println("-----------------------------------------------");

                    System.out.println("SO SÁNH VỚI NĂM TRƯỚC:");
                    System.out.println("-----------------------------------------------");
                    if (prevTotalRevenue > totalRevenue) {
                        System.out.println("Doanh thu (VND): -" + (prevTotalRevenue - totalRevenue) + " VND");
                    } else {
                        System.out.println("Doanh thu (VND): +" + (totalRevenue - prevTotalRevenue) + " VND");
                    }
                    if (prevTotalOrders > totalOrders) {
                        System.out.println("Số đơn: -" + (prevTotalOrders - totalOrders) + " đơn");
                    } else {
                        System.out.println("Số đơn: +" + (totalOrders - prevTotalOrders) + " đơn");
                    }

                    System.out.println("-----------------------------------------------");

                    // Thêm phân tích theo tháng trong năm
                    RevenueReportController.RevenueReportByEachMonth(year);

                }
            }
        } catch (SQLException e) {
            System.out.println("Đã có lỗi xảy ra khi truy vấn kết quả!");
        }
    }

    public static void RevenueReportByEachMonth(int year) {
        String sql = "SELECT \n" +
                "    MONTH(orders.paid_at) AS month,\n" +
                "    COUNT(DISTINCT orders.id) AS total_orders,\n" +
                "    SUM(CASE WHEN orders.status = 'completed' THEN orders.total_amount ELSE 0 END) AS total_revenue\n" +
                "    \n" +
                "FROM [order] orders\n" +
                "WHERE \n" +
                "    YEAR(orders.paid_at) = ? AND \n" +
                "    orders.status IN ('completed', 'refunded')\n" +
                "GROUP BY MONTH(orders.paid_at)\n" +
                "ORDER BY MONTH(orders.paid_at)";

        Connection connection = JDBCConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, String.valueOf(year));
            try (ResultSet resultSet = statement.executeQuery()) {
                Map<Integer, Integer> totalOrdersByMonth = new HashMap<>();
                Map<Integer, Float> totalRevenueByMonth = new HashMap<>();

                for (int i = 1; i <= 12; i++) {
                    totalOrdersByMonth.put(i, 0);
                    totalRevenueByMonth.put(i, 0.0f);
                }

                while (resultSet.next()) {
                    int month = resultSet.getInt("month");
                    int totalOrders = resultSet.getInt("total_orders");
                    float totalRevenue = resultSet.getFloat("total_revenue");

                    totalOrdersByMonth.put(month, totalOrders);
                    totalRevenueByMonth.put(month, totalRevenue);
                }
                System.out.println("PHÂN TÍCH THEO THÁNG TRONG NĂM:");
                System.out.println("-----------------------------------------------");
                System.out.printf("| %-10s | %-15s | %-15s | %n", "Tháng", "Số đơn", "Doanh thu (VND)");
                System.out.println("-----------------------------------------------");
                for (int month = 1; month <= 12; month++) {
                    System.out.printf("| %-10d | %-15d | %-15.2f | %n", month, totalOrdersByMonth.getOrDefault(month, 0), totalRevenueByMonth.getOrDefault(month, 0.0f));
                }
            } catch (SQLException e) {
                System.out.println("Đã có lỗi xảy ra khi truy vấn kết quả!");
            }
        } catch (SQLException e) {
            System.out.println("Đã có lỗi xảy ra khi truy vấn kết quả!");
        }
    }

    public static void RevenueReportByEachQuarter(int year) {
        String sql = "SELECT \n" +
                "    DATEPART(QUARTER, orders.paid_at) AS quarter,\n" +
                "    COUNT(DISTINCT orders.id) AS total_orders,\n" +
                "    SUM(CASE WHEN orders.status = 'completed' THEN orders.total_amount ELSE 0 END) AS total_revenue\n" +
                "    \n" +
                "FROM [order] orders\n" +
                "WHERE \n" +
                "    YEAR(orders.paid_at) = ? AND \n" +
                "    orders.status IN ('completed', 'refunded')\n" +
                "GROUP BY DATEPART(QUARTER, orders.paid_at), YEAR(orders.paid_at)\n" +
                "ORDER BY YEAR(orders.paid_at), DATEPART(QUARTER, orders.paid_at)";

        Connection connection = JDBCConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, String.valueOf(year));
            try (ResultSet resultSet = statement.executeQuery()) {
                Map<Integer, Integer> totalOrdersByQuarter = new HashMap<>();
                Map<Integer, Float> totalRevenueByQuarter = new HashMap<>();

                for (int i = 1; i <= 4; i++) {
                    totalOrdersByQuarter.put(i, 0);
                    totalRevenueByQuarter.put(i, 0.0f);
                }

                while (resultSet.next()) {
                    int month = resultSet.getInt("quarter");
                    int totalOrders = resultSet.getInt("total_orders");
                    float totalRevenue = resultSet.getFloat("total_revenue");

                    totalOrdersByQuarter.put(month, totalOrders);
                    totalRevenueByQuarter.put(month, totalRevenue);
                }
                System.out.println("PHÂN TÍCH THEO QUÝ TRONG NĂM:");
                System.out.println("-----------------------------------------------");
                System.out.printf("| %-10s | %-15s | %-15s | %n", "Quý", "Số đơn", "Doanh thu (VND)");
                System.out.println("-----------------------------------------------");
                for (int quarter = 1; quarter <= 4; quarter++) {
                    System.out.printf("| %-10d | %-15d | %-15.2f | %n", quarter, totalOrdersByQuarter.getOrDefault(quarter, 0), totalRevenueByQuarter.getOrDefault(quarter, 0.0f));
                }
            } catch (SQLException e) {
                System.out.println("Đã có lỗi xảy ra khi truy vấn kết quả!");
            }
        } catch (SQLException e) {
            System.out.println("Đã có lỗi xảy ra khi truy vấn kết quả!");
        }
    }
}
