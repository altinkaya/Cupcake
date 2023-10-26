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

public class CupcakeController {

    public static void dropDowns(Context ctx, ConnectionPool connectionPool) {

        try {
            HashMap<Integer, CupcakeTop> topFlavors = CupcakeMapper.topFlavors(connectionPool);
            List<CupcakeTop> toplist = new ArrayList<>(topFlavors.values());
            ctx.attribute("topFlavors", toplist);
            ctx.attribute("username", ctx.sessionAttribute("username"));
            ctx.render("frontpage.html");


        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");
        }
        try {
            HashMap<Integer, CupcakeBottom> bottomFlavors = CupcakeMapper.bottomFlavors(connectionPool);
            List<CupcakeBottom> bottomlist = new ArrayList<>(bottomFlavors.values());
            ctx.attribute("bottomFlavors", bottomlist);
            ctx.render("frontpage.html");

        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");
        }
    }

    public static void addtop(Context ctx, ConnectionPool connectionPool) {
        String flavor = ctx.formParam("flavor");
        String price = ctx.formParam("price");
        try {
            CupcakeMapper.addtop(flavor, price, connectionPool);
            ctx.attribute("message", "du har du oprret en ny variant");
            ctx.render("cake.html");

        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("cake.html");
        }
        ctx.redirect("/cake");
    }


    public static void addbottom(Context ctx, ConnectionPool connectionPool) {
        String flavor = ctx.formParam("flavor_bottom");
        String price = ctx.formParam("price_bottom");
        try {
            CupcakeMapper.addbottom(flavor, price, connectionPool);
            ctx.attribute("message", "du har du oprret en ny variant");
            ctx.render("cake.html");

        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("cake.html");
        }
        ctx.redirect("/cake");
    }


    public static void updateTop(Context ctx, ConnectionPool connectionPool) {
        int topId = Integer.parseInt(ctx.formParam("top_id"));
        String flavor = ctx.formParam("top_name"); // Ændret fra "top_name"
        int price = Integer.parseInt(ctx.formParam("top_price")); // Hent prisen fra formularen

        try {
            // Opdater "top" i din mapper ved hjælp af de modtagne data
            CupcakeMapper.updateTop(topId, flavor, price, connectionPool);

            // Hent alle "tops" fra din mapper som en HashMap
            HashMap<Integer, CupcakeTop> topFlavors = CupcakeMapper.topFlavors(connectionPool);

            // Send "topFlavors" til Thymeleaf-skabelonen for at vise listen af "tops"
            ctx.attribute("topFlavors", topFlavors);
            ctx.redirect("/cake");
        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("error.html");
        }
    }


    public static void updatebottom(Context ctx, ConnectionPool connectionPool) {
        int topId = Integer.parseInt(ctx.formParam("top_id"));
        String flavor = ctx.formParam("top_name"); // Ændret fra "top_name"
        int price = Integer.parseInt(ctx.formParam("top_price")); // Hent prisen fra formularen

        try {
            // Opdater "top" i din mapper ved hjælp af de modtagne data
            CupcakeMapper.updateTop(topId, flavor, price, connectionPool);

            // Hent alle "tops" fra din mapper som en HashMap
            HashMap<Integer, CupcakeTop> topFlavors = CupcakeMapper.topFlavors(connectionPool);

            // Send "topFlavors" til Thymeleaf-skabelonen for at vise listen af "tops"
            ctx.attribute("topFlavors", topFlavors);
            ctx.redirect("/cake");
        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("error.html");
        }
    }


    public static void deleteTop(Context ctx, ConnectionPool connectionPool)
    {
        int topId = Integer.parseInt(ctx.pathParam("id")); // Antager, at du bruger sti-parametre (path parameters) i din rute
        try
        {
            // Implementér sletningslogikken for top i din HashMap
            CupcakeMapper.deleteTop(topId, connectionPool);

            // Hent alle "tops" fra din HashMap
            HashMap<Integer, CupcakeTop> topFlavors = CupcakeMapper.topFlavors(connectionPool);

            // Send "topFlavors" til Thymeleaf-skabelonen for at vise listen af "tops"
            ctx.attribute("topFlavors", topFlavors);

            // Omdiriger brugeren til "/tops" eller en anden relevant side
            ctx.redirect("/cake");
        }
        catch (DatabaseException e)
        {
            ctx.attribute("message", e.getMessage());
            ctx.render("error.html");
        }
    }



    public static void deleteBottom(Context ctx, ConnectionPool connectionPool)
    {
        int bottomId = Integer.parseInt(ctx.pathParam("id")); // Antager, at du bruger sti-parametre (path parameters) i din rute
        try
        {
            // Implementér sletningslogikken for top i din HashMap
            CupcakeMapper.deleteBottom(bottomId, connectionPool);

            // Hent alle "tops" fra din HashMap
            HashMap<Integer, CupcakeBottom> bottomFlavors = CupcakeMapper.bottomFlavors(connectionPool);

            // Send "topFlavors" til Thymeleaf-skabelonen for at vise listen af "tops"
            ctx.attribute("topFlavors", bottomFlavors);

            // Omdiriger brugeren til "/tops" eller en anden relevant side
            ctx.redirect("/cake");
        }
        catch (DatabaseException e)
        {
            ctx.attribute("message", e.getMessage());
            ctx.render("error.html");
        }
    }





}

