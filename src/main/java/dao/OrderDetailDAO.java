package main.java.dao;

import main.java.model.OrderDetail;
import main.java.util.JDBCConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailDAO {
    public static boolean addOrderDetail(OrderDetail orderDetail){
        String sql = "INSERT INTO [order_detail]\n" +
                "    ([id], [order_id], [product_id], [quantity], [unit_price], [sub_total])\n" +
                "VALUES (?, ?, ?, ?, ?, ?)";
        Connection connection = JDBCConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, orderDetail.getId());
            statement.setString(2, orderDetail.getOrderId());
            statement.setInt(3, orderDetail.getProductId());
            statement.setInt(4, orderDetail.getQuantity());
            statement.setFloat(5, Math.round(orderDetail.getUnitPrice()));
            statement.setFloat(6,Math.round(orderDetail.getSubTotal()));

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            return false;
        }
    }

    public static int getTotalOrderDetailNumber(){
        String sql = "SELECT COUNT(DISTINCT order_id) AS total_order_details\n" +
                "FROM [order_detail]";
        Connection connection = JDBCConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()){
            int totalOrderDetail = 0;
            if (resultSet.next()){
                totalOrderDetail = resultSet.getInt("total_order_details");
            }
            return totalOrderDetail;

        }
        catch (SQLException e){
            return 0;
        }
    }

    public static List<OrderDetail> findOrderDetailById(String id){
        String sql = "SELECT  *\n" +
                "FROM order_detail\n" +
                "WHERE order_detail.order_id = ?";
        Connection connection = JDBCConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()){
                List<OrderDetail> orderDetails = new ArrayList<>();
                while (resultSet.next()){
                    int productId = resultSet.getInt("product_id");
                    Float unitPrice = resultSet.getFloat("unit_price");
                    Float subTotal = resultSet.getFloat("sub_total");
                    int quantity = resultSet.getInt("quantity");

                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setProductId(productId);
                    orderDetail.setUnitPrice(unitPrice);
                    orderDetail.setSubTotal(subTotal);
                    orderDetail.setQuantity(quantity);

                    orderDetails.add(orderDetail);

                }
                return orderDetails;
            }
            catch (SQLException e){
                return null;
            }

        }
        catch (SQLException e){
            return null;
        }
    }
}
