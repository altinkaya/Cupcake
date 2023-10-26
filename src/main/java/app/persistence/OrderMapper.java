package app.persistence;

import app.entities.Order;
import app.entities.OrderDetails;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderMapper {
    public static int createOrder(int userId, boolean status, int price, ConnectionPool connectionPool) throws DatabaseException {
        try (Connection connection = connectionPool.getConnection()) {
            String insertOrderSQL = "INSERT INTO `order` (user_id, status, price) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertOrderSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, userId);
            preparedStatement.setBoolean(2, status);
            preparedStatement.setInt(3, price);

            if (preparedStatement.executeUpdate() == 1) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Return the generated order number
                }
            }
        } catch (SQLException e) {
            String msg = "Der skete en fejl. Kan ikke oprette en ordre";
            throw new DatabaseException(msg);
        }
        throw new DatabaseException("Der skete en fejl. Kan ikke oprette en ordre");
    }



    public static void createOrderDetails(OrderDetails orderDetails, ConnectionPool connectionPool) throws DatabaseException {
        try (Connection connection = connectionPool.getConnection()) {
            String insertOrderDetailsSQL = "INSERT INTO orderdetails (ordernr, top_id, bottom_id, amount) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertOrderDetailsSQL);
            preparedStatement.setInt(1, orderDetails.getOrderNr());
            preparedStatement.setInt(2, orderDetails.getTopId());
            preparedStatement.setInt(3, orderDetails.getBottomId());
            preparedStatement.setInt(4, orderDetails.getAmount());

            if (preparedStatement.executeUpdate() != 1) {
                throw new DatabaseException("Failed to create order details.");
            }
        } catch (SQLException e) {
            String msg = "Der er sket en fejl. Prøv igen";
            throw new DatabaseException(msg);
        }
    }
    public static void createOrderDatabase(ConnectionPool connectionPool, Order order) throws DatabaseException {
        try (Connection connection = connectionPool.getConnection()) {
            String sql = "INSERT INTO `order` (user_id, status, price) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, order.getUserId());
            preparedStatement.setString(2, order.getStatus());
            preparedStatement.setInt(3, order.getPrice());

        } catch (SQLException e) {
            String msg = "Der skete en fejl. Kan ikke oprette en ordre";
            throw new DatabaseException(msg);
        }


    }

}
