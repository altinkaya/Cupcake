package app;

import app.config.ThymeleafConfig;
import app.controllers.*;
import app.entities.CupcakeBottom;
import app.entities.CupcakeTop;
import app.entities.User;
import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CupcakeMapper;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

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

        app.post("/deleteFromBasket", ctx -> BasketController.removeCupcakeFromBasket(ctx));
        app.get("/createuser", ctx -> ctx.render("createuser.html"));
        app.post("/createuser",ctx -> UserController.createuser(ctx, connectionPool ));
        app.post("/logout", ctx -> UserController.logout(ctx));
        app.post("/checkout", ctx -> OrderController.checkout(ctx, connectionPool));

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
