package app.controllers;

import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import io.javalin.http.Context;

import java.util.List;
import java.util.Map;

public class OrderController {

    private ConnectionPool connectionPool;

    public OrderController(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public static void checkout(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        User user = ctx.sessionAttribute("currentUser");
        Basket basket = ctx.sessionAttribute("userBasket");

        Order order = new Order(user.getId(), "Betalt", basket.getTotalPrice());

        int newOrderId = OrderMapper.createOrderDatabase(connectionPool, order);

        for (Cupcake c : basket.getBasket())
        {
            OrderMapper.createOrderDetailsDatabase(newOrderId, c.getTopId(), c.getBottomId(), c.getAmount(), connectionPool);
        }
        ctx.render("checkout.html");
    }



}


