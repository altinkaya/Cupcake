package app;

import app.config.ThymeleafConfig;
import app.controllers.BasketController;
import app.controllers.CupcakeController;
import app.controllers.UserController;
import app.entities.CupcakeBottom;
import app.entities.CupcakeTop;
import app.entities.User;
import app.controllers.OrderController;
import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CupcakeMapper;
import app.persistence.UserMapper;
import app.services.OrderService;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Map;

import static app.controllers.OrderController.orderService;

public class Main
{

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "cupcake";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    public static void main(String[] args)
    {
        // Initializing Javalin and Jetty webserver

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            JavalinThymeleaf.init(ThymeleafConfig.templateEngine());
        }).start(7070);

        // Routing

        app.get("/", ctx -> ctx.render("index.html"));
        app.post("/login", ctx -> UserController.login(ctx, connectionPool));
        app.get("/frontpage", ctx -> CupcakeController.dropDowns(ctx, connectionPool));
        app.post("/frontpage", ctx -> CupcakeController.dropDowns(ctx, connectionPool));
        app.post("/addToBasket", ctx -> BasketController.addCupcakeToBasket(ctx, connectionPool));



        app.get("/basket", ctx -> BasketController.showBasket(ctx));
        app.post("/generateInvoice", ctx -> OrderController.generateInvoice(ctx, connectionPool));

        app.post("/deleteFromBasket", ctx -> BasketController.removeCupcakeFromBasket(ctx));
        app.get("/createuser", ctx -> ctx.render("createuser.html"));
        app.post("/createuser",ctx -> UserController.createuser(ctx, connectionPool ));
        app.post("/logout", ctx -> UserController.logout(ctx));


            //låser siden så man kun kan tilgå den med admin retigheder
        app.get("/admin", ctx -> {
            Boolean loggedIn = ctx.sessionAttribute("status");
            if (loggedIn != null && loggedIn) {
                ctx.render("admin.html");
            } else {
                ctx.redirect("/");
            }
        });




        app.post("/addtop", ctx -> {
            CupcakeController.addtop(ctx, connectionPool);
            ctx.redirect("/cake"); // Omdiriger brugeren tilbage til "cake"-siden
        });

        app.post("/addbottom", ctx -> {
            CupcakeController.addbottom(ctx, connectionPool);
            ctx.redirect("/cake"); // Omdiriger brugeren tilbage til "cake"-siden
        });


        app.get("/cake", ctx -> {
            // Hent alle "tops" fra din mapper og send dem til skabelonen som en HashMap
            HashMap<Integer, CupcakeTop> topFlavors = CupcakeMapper.topFlavors(connectionPool);
            ctx.attribute("topFlavors", topFlavors);

            HashMap<Integer, CupcakeBottom> bottomFlavors = CupcakeMapper.bottomFlavors(connectionPool);
            ctx.attribute("bottomFlavors", bottomFlavors);

            // Tjek om brugeren er logget ind
            Boolean loggedIn = ctx.sessionAttribute("status");
            if (loggedIn != null && loggedIn) {
                ctx.render("cake.html");
            } else {
                ctx.redirect("/");
            }
        });


        app.get("/ordre", ctx -> {
            Boolean loggedIn = ctx.sessionAttribute("status");
            if (loggedIn != null && loggedIn) {
                ctx.render("ordre.html");
            } else {
                ctx.redirect("/");
            }
        });


        app.get("/edit-top/{id}", ctx -> {
            int topId = Integer.parseInt(ctx.pathParam("id"));
            HashMap<Integer, CupcakeTop> topFlavors = CupcakeMapper.topFlavors(connectionPool);
            CupcakeTop selectedTop = topFlavors.get(topId);

            if (selectedTop != null) {
                ctx.attribute("selectedTop", selectedTop);
                ctx.render("edit-top.html"); // Opret en ny Thymeleaf-skabelon til redigeringssiden
            } else {
                // Håndter fejl, f.eks. ved at omdirigere til en fejlskabelon
                ctx.attribute("message", "Toppen blev ikke fundet.");
                ctx.render("error.html");
            }
        });



        app.post("/update-top", ctx -> {
            int topId = Integer.parseInt(ctx.formParam("top_id"));
            String flavor = ctx.formParam("top_name");
            int price = Integer.parseInt(ctx.formParam("top_price"));

            try {
                // Opdater "top" i din database eller mapper ved hjælp af de modtagne data
                CupcakeMapper.updateTop(topId, flavor, price, connectionPool);
                ctx.redirect("/cake"); // Omdiriger brugeren til listen af "tops"
            } catch (DatabaseException e) {
                ctx.attribute("message", e.getMessage());
                ctx.render("error.html");
            }
        });

        app.get("/edit-bottom/{id}", ctx -> {
            int bottomId = Integer.parseInt(ctx.pathParam("id"));
            HashMap<Integer, CupcakeBottom> bottomFlavors = CupcakeMapper.bottomFlavors(connectionPool);
            CupcakeBottom selectedBottom = bottomFlavors.get(bottomId);

            if (selectedBottom != null) {
                ctx.attribute("selectedTop", selectedBottom);
                ctx.render("edit-top.html"); // Opret en ny Thymeleaf-skabelon til redigeringssiden
            } else {
                // Håndter fejl, f.eks. ved at omdirigere til en fejlskabelon
                ctx.attribute("message", "Toppen blev ikke fundet.");
                ctx.render("error.html");
            }
        });



        app.post("/update-bottom", ctx -> {
            int bottomId = Integer.parseInt(ctx.formParam("bottom_id"));
            String flavor = ctx.formParam("bottom_name");
            int price = Integer.parseInt(ctx.formParam("bottom_price"));

            try {
                // Opdater "top" i din database eller mapper ved hjælp af de modtagne data
                CupcakeMapper.updateBottom(bottomId, flavor, price, connectionPool);
                ctx.redirect("/cake"); // Omdiriger brugeren til listen af "tops"
            } catch (DatabaseException e) {
                ctx.attribute("message", e.getMessage());
                ctx.render("error.html");
            }
        });


        app.post("/delete-top/{id}", ctx -> {
            CupcakeController.deleteTop(ctx, connectionPool);
        });


        app.post("/delete-bottom/{id}", ctx -> {
            CupcakeController.deleteBottom(ctx, connectionPool);
        });


        app.post("/confirmOrder", ctx -> {
            int orderNumber = Integer.parseInt(ctx.formParam("orderNumber"));

            OrderService orderService = new OrderService(connectionPool);
            Order order = orderService.getOrder(orderNumber);
            orderService.confirmOrder(order);

            // Handle successful order confirmation, e.g., display a success message to the user
            ctx.attribute("message", "Order confirmed successfully.");
            ctx.render("confirmation.html");
        });

        app.get("/checkOrderStatus", ctx -> {
            int orderNumber = Integer.parseInt(ctx.pathParam("orderNumber"));

            OrderService orderService = new OrderService(connectionPool);
            boolean orderStatus = orderService.getOrderStatus(orderNumber);

            // Handle order status, e.g., render a template with the status
            ctx.render("order_status.html", Map.of("orderStatus", orderStatus));
        });

        app.get("/generateInvoice", ctx -> {
            try {
                // Retrieving the data needed for creating the order
                Basket basket = ctx.sessionAttribute("basket"); // stored in the session
                User user = ctx.sessionAttribute("user"); // stored in the session
                int orderNumber = Integer.parseInt(ctx.formParam("orderNumber"));

                // Create an instance of OrderService with the connectionPool
                OrderService orderService = new OrderService(connectionPool);

                // Create an order from the basket (if not already created)
                int userId = user.getId();
                orderService.createOrderFromBasket(basket, userId);

                // Retrieve the order from the database using the orderNumber
                Order order = orderService.getOrder(orderNumber);

                if (order != null) {
                    // You can also retrieve the order details or any other relevant data
                    List<OrderDetails> orderDetails = orderService.getOrderDetails(orderNumber);
                    String invoice = orderService.generateInvoice(order, orderDetails);

                    // Handle invoice generation, e.g., display the invoice to the user
                    ctx.render("invoice.html", Map.of("invoice", invoice));
                } else {
                    // Handle the case when the order is not found
                    ctx.attribute("message", "Order not found.");
                    ctx.render("error.html");
                }
            } catch (DatabaseException e) {
                // Handle any database-related exceptions
                ctx.attribute("message", e.getMessage());
                ctx.render("error.html");
            }
        });



        app.post("/updateUser", ctx -> {
            int userId = Integer.parseInt(ctx.formParam("id"));
            int newBalance = Integer.parseInt(ctx.formParam("newBalance"));

            try {
                // Opdater brugerens saldo i din database eller mapper med de modtagne data
                UserMapper.updateBalance(userId, newBalance, connectionPool);
                ctx.redirect("/users"); // Omdiriger brugeren til brugerlisten
            } catch (DatabaseException e) {
                ctx.attribute("message", e.getMessage());
                ctx.render("error.html");
            }
        });

        app.post("/searchUser", ctx -> {
            String email = ctx.formParam("email");

            User user = UserMapper.searchUser(email, connectionPool);

            if (user != null) {
                ctx.attribute("user", user);
            } else {
                ctx.attribute("user", null);
            }

            ctx.render("users.html"); // Dette vil vise resultaterne på den samme side.
        });

        app.get("/users", ctx -> {
            ctx.render("users.html");
        });




    }


}
