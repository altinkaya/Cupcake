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
        int price = Integer.parseInt(ctx.formParam("price"));

        Cupcake cupcake = new Cupcake(topFlavor, bottomFlavor, amount, price);
        Basket basket = ctx.sessionAttribute("userBasket");
        basket.addToBasket(cupcake);
        ctx.attribute("message", "Du har nu tilføjet en cupcake til din kurv");
        ctx.render("index.html");


    }


}
