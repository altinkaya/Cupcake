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
import java.util.Objects;


public class BasketController {


    public static void addCupcakeToBasket(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        HashMap<Integer,CupcakeTop> topMap = CupcakeMapper.topFlavors(connectionPool);
        HashMap<Integer, CupcakeBottom> bottomMap = CupcakeMapper.bottomFlavors(connectionPool);

        int top = Integer.parseInt(ctx.formParam("top"));
        int bottom = Integer.parseInt(ctx.formParam("bottom"));
        int amount = Integer.parseInt(ctx.formParam("amount"));

        Cupcake cupcake = new Cupcake(topMap.get(top), bottomMap.get(bottom), amount);
        Basket basket = new Basket();

        basket.addToBasket(cupcake);

        basket.getBasket().forEach(System.out::println);

        ctx.attribute("message", "Du har nu tilføjet en cupcake til din kurv");
        ctx.render("test.html");
    }
    public static void removeCupcakeFromBasket(Cupcake cupcake, Context ctx){
        Basket basket = ctx.sessionAttribute("userBasket");
        basket.removeFromBasket(cupcake);

    }


}
