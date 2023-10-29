package app.controllers;

import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.persistence.UserMapper;
import io.javalin.http.Context;

import java.util.List;
import java.util.Map;

public class OrderController {

    private ConnectionPool connectionPool;

    public OrderController(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public static void checkout(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        List<Cupcake> chekoutBasket;

        User user = ctx.sessionAttribute("currentUser");
        Basket basket = ctx.sessionAttribute("userBasket");


        Order order = new Order(user.getId(), "Betalt", basket.getTotalPrice());

        if(user.getBalance() < basket.getTotalPrice()) {
            ctx.attribute("message", "Du har ikke nok penge på din konto");
            ctx.render("basket.html");
            return;
        }
        int newOrderId = OrderMapper.createOrderDatabase(connectionPool, order);

        for (Cupcake c : basket.getBasket())
        {
            OrderMapper.createOrderDetailsDatabase(newOrderId, c.getTopId(), c.getBottomId(), c.getAmount(), connectionPool);
        }
        user.updateBalance(user.getBalance() - basket.getTotalPrice());
        UserMapper.updateBalance(user.getId(), user.getBalance(), connectionPool);
        ctx.sessionAttribute("currentUser", user);
        ctx.attribute("totalPrice", basket.getTotalPrice());
        chekoutBasket = basket.getBasket();
        ctx.attribute("checkoutBasket", chekoutBasket);

        ctx.sessionAttribute("userBasket", new Basket());
        ctx.attribute("username", ctx.sessionAttribute("username"));
        ctx.attribute("balance", user.getBalance());



        ctx.render("checkout.html");

    }


    public static void getOrders(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        User user = ctx.sessionAttribute("currentUser");
        List<Order> orders = OrderMapper.getOrders(user.getId(), connectionPool);
        ctx.attribute("username", ctx.sessionAttribute("username"));
        ctx.attribute("balance", user.getBalance());
        ctx.attribute("orders", orders);
        ctx.render("orders.html");
    }

    public static void getOrderDetails(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        int orderNr = Integer.parseInt(ctx.formParam("orderNr"));

        List<Cupcake> orderDetail = OrderMapper.getOrderDetails(orderNr, connectionPool);
        ctx.attribute("orderDetail", orderDetail);
        ctx.attribute("username", ctx.sessionAttribute("username"));
        User user = ctx.sessionAttribute("currentUser");
        ctx.attribute("balance", user.getBalance());
        ctx.render("orderDetail.html");
    }
}


