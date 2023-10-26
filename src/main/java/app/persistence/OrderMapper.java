package app.persistence;

import app.entities.Order;
import app.entities.OrderDetails;
import app.exceptions.DatabaseException;

import java.sql.*;

public class OrderMapper {

    public static int createOrderDatabase(ConnectionPool connectionPool, Order order) throws DatabaseException {
        int newOrderId = 0;
        try (Connection connection = connectionPool.getConnection()) {
            String sql = "INSERT INTO \"orders\" (user_id, status, price) VALUES (?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, order.getUserId());
            ps.setString(2, order.getStatus());
            ps.setInt(3, order.getPrice());
            int rs = ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
               newOrderId = generatedKeys.getInt(1);
            }

            return newOrderId;


        } catch (SQLException e) {
            String msg = "Der skete en fejl. Kan ikke oprette en ordre";
            throw new DatabaseException(msg);
        }

    }

    public static void createOrderDetailsDatabase(int newOrderId, int topId, int bottomId, int amount, ConnectionPool connectionPool) throws DatabaseException {
        try (Connection connection = connectionPool.getConnection()) {
            String sql = "INSERT INTO \"ordersdetails\" (order_nr, top_id, bottom_id, amount) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, newOrderId);
            ps.setInt(2, topId);
            ps.setInt(3, bottomId);
            ps.setInt(4, amount);
            ps.executeUpdate();
        } catch (SQLException e) {
            String msg = "Der er sket en fejl. Prøv igen";
            throw new DatabaseException(msg);
        }
    }
}
