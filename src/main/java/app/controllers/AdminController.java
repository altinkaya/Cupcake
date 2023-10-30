package app.controllers;

import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.*;
import io.javalin.http.Context;

import java.util.*;

public class AdminController {

    public static void updateUser(Context ctx, ConnectionPool connectionPool) {
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
    }

    public static void searchUser(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        String email = ctx.formParam("email");

        User user = UserMapper.searchUser(email, connectionPool);

        if (user != null) {
            ctx.attribute("user", user);
        } else {
            ctx.attribute("user", null);
        }

        ctx.render("users.html"); // Dette vil vise resultaterne på den samme side.
    }


    public static void users(Context ctx, ConnectionPool connectionPool) {
        Boolean loggedIn = ctx.sessionAttribute("status");
        if (loggedIn != null && loggedIn) {
            ctx.render("users.html");
        } else {
            ctx.redirect("/");
        }
    }


    public static void admin(Context ctx, ConnectionPool connectionPool) {
        Boolean loggedIn = ctx.sessionAttribute("status");
        if (loggedIn != null && loggedIn) {
            ctx.render("admin.html");
        } else {
            ctx.redirect("/");
        }
    }

    public static void cake(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
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
    }

    public static void ordre(Context ctx, ConnectionPool connectionPool) {
        Boolean loggedIn = ctx.sessionAttribute("status");
        if (loggedIn != null && loggedIn) {
            ctx.render("ordre.html");
        } else {
            ctx.redirect("/");
        }
    }

    public static void editTop(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
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
    }

    public static void updateTop(Context ctx, ConnectionPool connectionPool) {
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
    }

    public static void editBottom(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
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
    }

    public static void updateBottom(Context ctx, ConnectionPool connectionPool) {
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
    }


    public static void getOrders(Context ctx, ConnectionPool connectionPool) throws DatabaseException {

        Boolean loggedIn = ctx.sessionAttribute("status");
        if (loggedIn != null && loggedIn) {

            User user = ctx.sessionAttribute("currentUser");
            List<Order> orders = AdminMapper.getOrders(user.getId(), connectionPool);
            ctx.attribute("username", ctx.sessionAttribute("username"));
            ctx.attribute("balance", user.getBalance());
            ctx.attribute("orders", orders);
            ctx.render("order_admin.html");
        } else {
            ctx.redirect("/");
        }
    }

    public static void getOrderDetails(Context ctx, ConnectionPool connectionPool) throws DatabaseException {

        Boolean loggedIn = ctx.sessionAttribute("status");
        if (loggedIn != null && loggedIn) {
            int orderNr = Integer.parseInt(ctx.formParam("orderNr"));

            List<Cupcake> orderDetail = AdminMapper.getOrderDetails(orderNr, connectionPool);
            ctx.attribute("orderDetail", orderDetail);
            ctx.attribute("username", ctx.sessionAttribute("username"));
            User user = ctx.sessionAttribute("currentUser");
            ctx.attribute("balance", user.getBalance());
            ctx.render("orderedit_admin.html");
        } else {
            ctx.redirect("/");
        }
    }

    public static void getUsersAndOrders(Context ctx, ConnectionPool connectionPool) throws DatabaseException {

        Boolean loggedIn = ctx.sessionAttribute("status");
        if (loggedIn != null && loggedIn) {

            Map<User, List<Order>> usersAndOrders = AdminMapper.getUsersAndOrders(connectionPool);
            ctx.attribute("usersAndOrders", usersAndOrders);
            ctx.render("users_admin.html");

            usersAndOrders.forEach((user, orders) -> {
                orders.sort(Comparator.comparing(Order::getStatus));
            });

            // Send de sorteret data til skabelonen
            ctx.attribute("usersAndOrders", usersAndOrders);

            ctx.render("users_admin.html");

        } else {
            ctx.redirect("/");
        }


    }


    public static void deleteorders(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        int orderNr = Integer.parseInt(ctx.pathParam("orderNr"));
                OrderMapper.deleteOrder(orderNr,connectionPool);
                ctx.redirect("/users_admin");

    }









}







