package app.services;

import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;

import java.util.List;

public class OrderService {
    private ConnectionPool connectionPool;

    public OrderService(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void createOrderFromBasket(Basket basket, int userId) throws DatabaseException {
        try {
            // Calculate the total price based on the contents of the basket
            int totalPrice = calculateTotalPrice(basket);

            // Create an order using the createOrder method in OrderMapper
            OrderMapper.createOrder(userId, true, totalPrice, connectionPool);

        } catch (DatabaseException e) {
            // Handle any database-related exceptions
            throw e;
        }
    }


    private int calculateTotalPrice(Basket basket) {
        int totalPrice = 0;

        for (Cupcake cupcake : basket.getBasket()) {
            totalPrice += cupcake.getPrice();
        }

        return totalPrice;
    }

    private int createOrderRecord(int userId, boolean status, int price) throws DatabaseException {
        try {
            return OrderMapper.createOrder(userId, status, price, connectionPool);
        } catch (DatabaseException e) {
            throw e;
        }
    }


    // cupcake.getTopId(), cupcake.getBottomId() or cupcake.getTopFlavor(), cupcake.getBottomFlavor() ????
    private void createOrderDetails(int orderNumber, Basket basket) throws DatabaseException {
        try {
            for (Cupcake cupcake : basket.getBasket()) {
                // Create order details records in the database
                OrderDetails orderDetails = new OrderDetails(orderNumber, cupcake.getTopId(), cupcake.getBottomId(), cupcake.getAmount());
                OrderMapper.createOrderDetails(orderDetails, connectionPool);
            }
        } catch (DatabaseException e) {
            throw e;
        }
    }



    public Order getOrder(int orderNumber) {

        return null;
    }

    public void confirmOrder(Order order) {
    }

    public boolean getOrderStatus(int orderNumber) {
        return true;
    }

    public List<OrderDetails> getOrderDetails(int orderNumber) {
        return null;
    }

    public String generateInvoice(Order order, List<OrderDetails> orderDetails) {
        return null;
    }
}


