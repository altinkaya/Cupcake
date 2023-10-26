package app.controllers;

import app.entities.CupcakeBottom;
import app.entities.CupcakeTop;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CupcakeMapper;
import app.persistence.UserMapper;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserController {

    public static void login(Context ctx, ConnectionPool connectionPool) {
        String name = ctx.formParam("username");
        String password = ctx.formParam("password");
        try {
            boolean status = UserMapper.login(name, password, connectionPool).getStatus();
            User user = UserMapper.login(name, password, connectionPool);
            ctx.sessionAttribute("currentUser", user);
            ctx.sessionAttribute("username", user.getName());
            ctx.sessionAttribute("status", user.getStatus());
            if (status) {
                ctx.redirect("/admin");
            } else {

                ctx.redirect("/frontpage");


            }
        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");
        }
    }

    public static void createuser(Context ctx, ConnectionPool connectionPool) {
        String name = ctx.formParam("username");
        String password1 = ctx.formParam("password1");
        String password2 = ctx.formParam("password2");
        int balance = Integer.parseInt(ctx.formParam("balance"));
        boolean admin = Boolean.parseBoolean(ctx.formParam("admin"));

        // Validering af passwords - at de to matcher
        if (password1.equals(password2)) {
            try {
                UserMapper.createuser(name, password1, balance, admin, connectionPool);
                ctx.attribute("message", "Du er nu oprette. Log på for at komme i gang.");
                ctx.render("index.html");

            } catch (DatabaseException e) {
                ctx.attribute("message", e.getMessage());
                ctx.render("createuser.html");
            }
        } else {
            ctx.attribute("message", "Dine password matcher ikke!");
            ctx.render("createuser.html");
        }

    }

    public static void logout(Context ctx) {
        // Invalidate session
        ctx.req().getSession().invalidate();
        ctx.render("index.html");
    }



    public static void searchUser(Context ctx, ConnectionPool connectionPool) {
        String id = ctx.queryParam("email");
        try {
            User user = UserMapper.searchUser(id, connectionPool);
            ctx.attribute("user", user);
            ctx.render("users.html");
        } catch (DatabaseException e) {
            ctx.attribute("kunne Ikke Finde Brugern", e.getMessage());
            ctx.render("users.html");
        }
    }

    public static void editBalance(Context ctx, ConnectionPool connectionPool) {
        String email = ctx.formParam("email");
        int newBalance = Integer.parseInt(ctx.formParam("newBalance"));

        try {
            User user = UserMapper.searchUser(email, connectionPool);

            if (user != null) {
                user.updateBalance(newBalance);
                UserMapper.updateBalance(user.getId(), user.getBalance(), connectionPool);
            }

            ctx.render("users.html", Map.of("user", user));
        } catch (DatabaseException e) {
            ctx.attribute("kunnne ikke rediger beløb", e.getMessage());
            ctx.render("users.html");
        }
    }
    }



