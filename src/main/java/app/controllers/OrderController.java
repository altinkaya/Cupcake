package app.controllers;

import app.entities.Basket;
import app.entities.Order;
import app.entities.OrderDetails;
import app.entities.User;
import app.persistence.ConnectionPool;
import app.services.OrderService;
import io.javalin.http.Context;

import java.util.List;
import java.util.Map;

public class OrderController {
    public static OrderService orderService;
    private ConnectionPool connectionPool;

    public OrderController(OrderService orderService, ConnectionPool connectionPool) {
        this.orderService = orderService;
        this.connectionPool = connectionPool;
    }

    // Confirm an order
    public void confirmOrder(Context ctx) {
        int orderNumber = Integer.parseInt(ctx.formParam("orderNumber"));

        Order order = orderService.getOrder(orderNumber);
        orderService.confirmOrder(order);
        ctx.attribute("message", "Order confirmed successfully.");
        ctx.render("confirmation.html");
    }

    // Get order status
    public void getOrderStatus(Context ctx) {
        int orderNumber = Integer.parseInt(ctx.pathParam("orderNumber"));

        boolean orderStatus = orderService.getOrderStatus(orderNumber);
        ctx.render("order_status.html", Map.of("orderStatus", orderStatus));
    }

    // Generate an invoice
    public static void generateInvoice(Context ctx, ConnectionPool connectionPool) {
        int orderNumber = Integer.parseInt(ctx.pathParam("orderNumber"));

        Order order = orderService.getOrder(orderNumber);
        List<OrderDetails> orderDetails = orderService.getOrderDetails(orderNumber);
        String invoice = orderService.generateInvoice(order, orderDetails);
        ctx.render("invoice.html", Map.of("invoice", invoice));
    }

    public static void checkout(Context ctx, ConnectionPool connectionPool){
        User user = ctx.sessionAttribute("currentUser");
        Basket basket = ctx.sessionAttribute("userBasket");

        Order order = new Order(user.getId(),"Betalt" ,basket.getTotalPrice());




    }


}

