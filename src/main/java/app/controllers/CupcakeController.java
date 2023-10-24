package app.controllers;

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

    public static void dropDowns(Context ctx, ConnectionPool connectionPool)
    {

        try
        {
            HashMap<Integer, CupcakeTop> topFlavors = CupcakeMapper.topFlavors(connectionPool);
            List<CupcakeTop> toplist = new ArrayList<>(topFlavors.values());
            ctx.attribute("topFlavors", toplist);
            ctx.render("test.html");

        }
        catch (DatabaseException e)
        {
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");
        }
        try
        {
            HashMap<Integer, CupcakeTop> bottomFlavors = CupcakeMapper.topFlavors(connectionPool);
            List<CupcakeTop> bottomlist = new ArrayList<>(bottomFlavors.values());
            ctx.attribute("topFlavors", bottomlist);
            ctx.render("test.html");

        }
        catch (DatabaseException e)
        {
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");
        }
    }

}
