package app.controllers;

import app.entities.CupcakeBottom;
import app.entities.CupcakeTop;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CupcakeMapper;
import app.persistence.UserMapper;
import io.javalin.http.Context;

import java.util.HashMap;

public class AdminController {

    public static void updateUser(Context ctx, ConnectionPool connectionPool)
    {
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


    public static void users(Context ctx, ConnectionPool connectionPool)
    {
        Boolean loggedIn = ctx.sessionAttribute("status");
        if (loggedIn != null && loggedIn) {
            ctx.render("users.html");
        } else {
            ctx.redirect("/");
        }
    }
    public static void admin (Context ctx, ConnectionPool connectionPool)
    {
        Boolean loggedIn = ctx.sessionAttribute("status");
        if (loggedIn != null && loggedIn) {
            ctx.render("admin.html");
        } else {
            ctx.redirect("/");
        }
    }
    public static void cake (Context ctx, ConnectionPool connectionPool) throws DatabaseException {
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
    public static void ordre (Context ctx, ConnectionPool connectionPool)
    {
        Boolean loggedIn = ctx.sessionAttribute("status");
        if (loggedIn != null && loggedIn) {
            ctx.render("ordre.html");
        } else {
            ctx.redirect("/");
        }
    }

    public static void editTop (Context ctx, ConnectionPool connectionPool) throws DatabaseException {
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
    public static void updateTop (Context ctx, ConnectionPool connectionPool)
    {
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

    public static void updateBottom (Context ctx, ConnectionPool connectionPool)
    {
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



    }
