package app.persistence;

import app.entities.*;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public static List<Order> getOrders(int id, ConnectionPool connectionPool) {
        List<Order> orders = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection()) {
            String sql = "SELECT * FROM \"orders\" WHERE user_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int orderNr = rs.getInt("order_nr");
                int userId = rs.getInt("user_id");
                String status = rs.getString("status");
                int price = rs.getInt("price");
                Order order = new Order(orderNr, userId, status, price);
                orders.add(order);
            }
            return orders;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static List<Cupcake> getOrderDetails(int orderNr, ConnectionPool connectionPool) {
        List<Cupcake> orderDetails = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection()) {
            String sql = "SELECT ctop.flavor AS top_flavor, ctop.price AS top_price, cbottom.flavor AS bottom_flavor, cbottom.price AS bottom_price, amount\n" +
                    "FROM ordersdetails od\n" +
                    "JOIN cupcake_top ctop ON od.top_id = ctop.id\n" +
                    "JOIN cupcake_bottom cbottom ON od.bottom_id = cbottom.id\n" +
                    "WHERE od.order_nr = ?";

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, orderNr);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String cTopFlavor = rs.getString("top_flavor");
                int cTopPrice = rs.getInt("top_price");
                String cBottomFlavor = rs.getString("bottom_flavor");
                int cBottomPrice = rs.getInt("bottom_price");
                int amount = rs.getInt("amount");
                Cupcake cupcake = new Cupcake(new CupcakeTop(cTopFlavor,cTopPrice), new CupcakeBottom(cBottomFlavor,cBottomPrice), amount);
                orderDetails.add(cupcake);
            }
            return orderDetails;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteOrder(int orderNr, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "DELETE FROM orders WHERE order_nr = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderNr);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Fejl i sletning af ordre");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl i sletning af ordre");
        }
    }



}
