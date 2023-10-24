package app.controllers;

import app.entities.Basket;
import app.entities.Cupcake;
import app.entities.CupcakeBottom;
import app.entities.CupcakeTop;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CupcakeMapper;
import io.javalin.http.Context;

import java.util.HashMap;


public class BasketController {


    public static void addCupcakeToBasket(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        HashMap<Integer,CupcakeTop> top = CupcakeMapper.topFlavors(connectionPool);
        int topFlavor = Integer.parseInt(ctx.formParam("topFlavor"));
        HashMap<Integer, CupcakeBottom> bottom = CupcakeMapper.bottomFlavors(connectionPool);
        int bottomFlavor = Integer.parseInt(ctx.formParam("bottomFloavor"));
        int amount = Integer.parseInt(ctx.formParam("amount"));
        Cupcake cupcake = new Cupcake(top.get(topFlavor), bottom.get(bottomFlavor), amount);
        Basket basket = ctx.sessionAttribute("userBasket");
        basket.addToBasket(cupcake);
        ctx.attribute("message", "Du har nu tilføjet en cupcake til din kurv");
        ctx.render("test.html");
    }
    public static void removeCupcakeFromBasket(Cupcake cupcake, Context ctx){
        Basket basket = ctx.sessionAttribute("userBasket");
        basket.removeFromBasket(cupcake);

    }


}
