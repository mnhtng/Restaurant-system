package main.java.dao;

import main.java.model.Order;
import main.java.model.enums.PaymentMethod;
import main.java.model.enums.PaymentStatus;
import main.java.util.JDBCConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    public static List<Order> getAllOrder(){
        String sql = "SELECT * FROM [order]";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()){
            List<Order> orders = new ArrayList<>();

            while(resultSet.next()){
                String id = resultSet.getString("id");
                int customerId = resultSet.getInt("customer_id");
                int serviceClerkId = resultSet.getInt("service_clerk_id");
                int tableId = resultSet.getInt("table_id");
                LocalDateTime orderTime = resultSet.getObject("order_time", LocalDateTime.class);
                String paymentMethod = resultSet.getString("payment_method").toUpperCase();
                Float totalAmount = resultSet.getFloat("total_amount");
                String status = resultSet.getString("status").toUpperCase();
                LocalDateTime paidAt = resultSet.getObject("paid_at", LocalDateTime.class);
                String note = resultSet.getString("note");

                Order orderDetail = new Order();
                orderDetail.setId(id);
                orderDetail.setCustomerId(customerId);
                orderDetail.setServiceClerkId(serviceClerkId);
                orderDetail.setTableId(tableId);
                orderDetail.setOrderTime(orderTime);
                orderDetail.setPaymentMethod(PaymentMethod.valueOf(paymentMethod));
                orderDetail.setTotalAmount(totalAmount);
                orderDetail.setStatus(PaymentStatus.valueOf(status));
                orderDetail.setPaidAt(paidAt);
                orderDetail.setNote(note);

                orders.add(orderDetail);
            }
            return orders;
        }
        catch (SQLException e){
            return null;
        }
    }

    public static List<Order> getUncompletedOrder(){
        String sql = "SELECT * FROM [order] WHERE [status] NOT IN ('completed')";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()){
            List<Order> orders = new ArrayList<>();

            while(resultSet.next()){
                String id = resultSet.getString("id");
                int customerId = resultSet.getInt("customer_id");
                int serviceClerkId = resultSet.getInt("service_clerk_id");
                int tableId = resultSet.getInt("table_id");
                LocalDateTime orderTime = resultSet.getObject("order_time", LocalDateTime.class);
                String paymentMethod = resultSet.getString("payment_method").toUpperCase();
                Float totalAmount = resultSet.getFloat("total_amount");
                String status = resultSet.getString("status").toUpperCase();
                LocalDateTime paidAt = resultSet.getObject("paid_at", LocalDateTime.class);
                String note = resultSet.getString("note");

                Order orderDetail = new Order();
                orderDetail.setId(id);
                orderDetail.setCustomerId(customerId);
                orderDetail.setServiceClerkId(serviceClerkId);
                orderDetail.setTableId(tableId);
                orderDetail.setOrderTime(orderTime);
                orderDetail.setPaymentMethod(PaymentMethod.valueOf(paymentMethod));
                orderDetail.setTotalAmount(totalAmount);
                orderDetail.setStatus(PaymentStatus.valueOf(status));
                orderDetail.setPaidAt(paidAt);
                orderDetail.setNote(note);

                orders.add(orderDetail);
            }
            return orders;
        }
        catch (SQLException e){
            return null;
        }
    }

    public static boolean updateOrder(String id, String newStatus){
        String sql = "UPDATE [order] SET status = ? WHERE id = ?";

        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, newStatus);
            statement.setString(2, id);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean updateOrderInvoice(String id, String paymentMethod, float totalAmount){
        String sql = "UPDATE [order] \n" +
                "SET \n" +
                "status = 'completed',\n" +
                "paid_at = ?,\n" +
                "total_amount = ?,\n" +
                "payment_method = ?\n" +
                "WHERE id = ?";

        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            statement.setString(2, String.valueOf(totalAmount));
            statement.setString(3, paymentMethod);
            statement.setString(4, id);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static List<Order> searchOrder(String query){
        String sql = "SELECT *\n" +
                "FROM [order]\n" +
                "JOIN member ON [order].[customer_id] = member.id\n" +
                "WHERE member.name LIKE ?\n" +
                "   OR [service_clerk_id] LIKE ?\n" +
                "   OR [table_id] LIKE ?\n" +
                "   OR [payment_method] LIKE ?\n" +
                "   OR [status] LIKE ?\n" +
                "   OR CONVERT(VARCHAR, CONVERT(DATE, [order].order_time)) LIKE ?\n" +
                "   OR CONVERT(VARCHAR, CONVERT(DATE, [order].paid_at)) LIKE ?\n" +
                "   OR [note] LIKE ?;\n";

        Connection connection = JDBCConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, "%" + query + "%");
            statement.setString(2, "%" + query + "%");
            statement.setString(3, "%" + query + "%");
            statement.setString(4, "%" + query + "%");
            statement.setString(5, "%" + query + "%");
            statement.setString(6, "%" + query + "%");
            statement.setString(7, "%" + query + "%");
            statement.setString(8, "%" + query + "%");

            try (ResultSet resultSet = statement.executeQuery()){
                List<Order> orders = new ArrayList<>();

                if (resultSet.next()){
                    String id = resultSet.getString("id");
                    int customerId = resultSet.getInt("customer_id");
                    int serviceClerkId = resultSet.getInt("service_clerk_id");
                    int tableId = resultSet.getInt("table_id");
                    LocalDateTime orderTime = resultSet.getObject("order_time", LocalDateTime.class);
                    String paymentMethod = resultSet.getString("payment_method").toUpperCase();
                    Float totalAmount = resultSet.getFloat("total_amount");
                    String status = resultSet.getString("status").toUpperCase();
                    LocalDateTime paidAt = resultSet.getObject("paid_at", LocalDateTime.class);
                    String note = resultSet.getString("note");

                    Order orderDetail = new Order();
                    orderDetail.setId(id);
                    orderDetail.setCustomerId(customerId);
                    orderDetail.setServiceClerkId(serviceClerkId);
                    orderDetail.setTableId(tableId);
                    orderDetail.setOrderTime(orderTime);
                    orderDetail.setPaymentMethod(PaymentMethod.valueOf(paymentMethod));
                    orderDetail.setTotalAmount(totalAmount);
                    orderDetail.setStatus(PaymentStatus.valueOf(status));
                    orderDetail.setPaidAt(paidAt);
                    orderDetail.setNote(note);

                    orders.add(orderDetail);

                }
                return orders;
            }
            catch (SQLException e){
                return null;
            }
        }
        catch (SQLException e){
            return null;
        }
    }

    public static List<Order> getOrderInvoice(){
        String sql = "SELECT * FROM [order] WHERE [status] IN ('completed', 'cancelled')";
        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()){
            List<Order> completedOrder = new ArrayList<>();
            while (resultSet.next()){
                String id = resultSet.getString("id");
                int customerId = resultSet.getInt("customer_id");
                int serviceClerkId = resultSet.getInt("service_clerk_id");
                int tableId = resultSet.getInt("table_id");
                LocalDateTime orderTime = resultSet.getObject("order_time", LocalDateTime.class);
                String paymentMethod = resultSet.getString("payment_method").toUpperCase();
                Float totalAmount = resultSet.getFloat("total_amount");
                String status = resultSet.getString("status").toUpperCase();
                LocalDateTime paidAt = resultSet.getObject("paid_at", LocalDateTime.class);
                String note = resultSet.getString("note");

                Order orderDetail = new Order();
                orderDetail.setId(id);
                orderDetail.setCustomerId(customerId);
                orderDetail.setServiceClerkId(serviceClerkId);
                orderDetail.setTableId(tableId);
                orderDetail.setOrderTime(orderTime);
                orderDetail.setPaymentMethod(PaymentMethod.valueOf(paymentMethod));
                orderDetail.setTotalAmount(totalAmount);
                orderDetail.setStatus(PaymentStatus.valueOf(status));
                orderDetail.setPaidAt(paidAt);
                orderDetail.setNote(note);

                completedOrder.add(orderDetail);

            }
            return  completedOrder;
        }
        catch (SQLException e){
            return null;
        }
    }
    public static int getTotalOrderNumber(){
        String sql = "SELECT COUNT(DISTINCT id) AS total_orders\n" +
                "FROM [order]";
        Connection connection = JDBCConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()){
            int totalOrder = 0;
            if (resultSet.next()){
                totalOrder = resultSet.getInt("total_orders");
            }
            return totalOrder;

        }
        catch (SQLException e){
            return 0;
        }
    }
    public static boolean addOrder(Order order){
        String sql = "INSERT INTO [order]\n" +
                "    ([id], [customer_id], [service_clerk_id], [table_id], [order_time], [expected_arrival_time], [payment_method], [total_amount], [status], [paid_at])\n" +
                "VALUES (?, ? , ? , ? , ?, ? , ? , ? , ? , ?)";
        Connection connection = JDBCConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, order.getId());
            statement.setInt(2, order.getCustomerId());
            statement.setInt(3, order.getServiceClerkId());
            statement.setInt(4, order.getTableId());
            statement.setTimestamp(5, Timestamp.valueOf(order.getOrderTime()));
            statement.setTimestamp(6, Timestamp.valueOf(order.getExpectedArrivalTime()));
            statement.setString(7, order.getPaymentMethod().name().toLowerCase());
            statement.setFloat(8, Math.round(order.getTotalAmount()));
            statement.setString(9, order.getStatus().name().toLowerCase());
            statement.setTimestamp(10, Timestamp.valueOf(order.getPaidAt()));

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            return false;
        }
    }

    public static List<Order> getOrdersByCustomerId(int customerId){
        String sql = "SELECT *\n" +
                "FROM [order]\n" +
                "WHERE [customer_id] = ? AND [status] != 'completed'";

        Connection connection = JDBCConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, String.valueOf(customerId));
            try (ResultSet resultSet = statement.executeQuery()){
                List<Order> orders = new ArrayList<>();
                while (resultSet.next()){
                    String id = resultSet.getString("id");
                    int serviceClerkId = resultSet.getInt("service_clerk_id");
                    int tableId = resultSet.getInt("table_id");
                    LocalDateTime orderTime = resultSet.getObject("order_time", LocalDateTime.class);
                    String paymentMethod = resultSet.getString("payment_method").toUpperCase();
                    Float totalAmount = resultSet.getFloat("total_amount");
                    String status = resultSet.getString("status").toUpperCase();
                    LocalDateTime paidAt = resultSet.getObject("paid_at", LocalDateTime.class);
                    LocalDateTime expectedDate = resultSet.getObject("expected_arrival_time", LocalDateTime.class);
                    String note = resultSet.getString("note");

                    Order orderDetail = new Order();
                    orderDetail.setId(id);
                    orderDetail.setCustomerId(customerId);
                    orderDetail.setServiceClerkId(serviceClerkId);
                    orderDetail.setTableId(tableId);
                    orderDetail.setOrderTime(orderTime);
                    orderDetail.setPaymentMethod(PaymentMethod.valueOf(paymentMethod));
                    orderDetail.setTotalAmount(totalAmount);
                    orderDetail.setStatus(PaymentStatus.valueOf(status));
                    orderDetail.setPaidAt(paidAt);
                    orderDetail.setExpectedArrivalTime(expectedDate);
                    orderDetail.setNote(note);

                    orders.add(orderDetail);

                }
                return  orders;
            } catch (SQLException e) {
                return null;
            }
        }
        catch (SQLException e){
            return null;
        }
    }

    public static boolean cancelOrder(String id){
        String sql = "UPDATE [order] SET status = 'cancelled' WHERE id = ?";

        Connection connection = JDBCConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, id);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }
}
