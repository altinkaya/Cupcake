package app.controllers;

import app.entities.Basket;
import app.entities.Cupcake;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CupcakeMapper;
import io.javalin.http.Context;

public class BasketController {


    public static void addCupcakeToBasket(Context ctx, ConnectionPool connectionPool) {
        String topFlavor = ctx.formParam("topFlavor");
        String bottomFlavor = ctx.formParam("bottomFlavor");
        int amount = Integer.parseInt(ctx.formParam("amount"));

        try {
        Cupcake cupcake = CupcakeMapper.createCupcake(topFlavor, bottomFlavor, amount, connectionPool);
        Basket basket = ctx.sessionAttribute("userBasket");
        basket.addToBasket(cupcake);
        ctx.attribute("message", "Du har nu tilføjet en cupcake til din kurv");
        ctx.render("index.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");
        }



    }


}
