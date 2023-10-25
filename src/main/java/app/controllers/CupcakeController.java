package app.controllers;

import app.entities.CupcakeBottom;
import app.entities.CupcakeTop;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CupcakeMapper;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CupcakeController {

    public static void dropDowns(Context ctx, ConnectionPool connectionPool)
    {

        try
        {
            HashMap<Integer, CupcakeTop> topFlavors = CupcakeMapper.topFlavors(connectionPool);
            List<CupcakeTop> toplist = new ArrayList<>(topFlavors.values());
            ctx.attribute("topFlavors", toplist);
            ctx.attribute("username", ctx.sessionAttribute("username"));
            ctx.render("frontpage.html");


        }
        catch (DatabaseException e)
        {
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");
        }
        try
        {
            HashMap<Integer, CupcakeBottom> bottomFlavors = CupcakeMapper.bottomFlavors(connectionPool);
            List<CupcakeBottom> bottomlist = new ArrayList<>(bottomFlavors.values());
            ctx.attribute("bottomFlavors", bottomlist);
            ctx.render("frontpage.html");

        }
        catch (DatabaseException e)
        {
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");
        }
    }

}
