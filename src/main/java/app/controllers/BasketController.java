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

        Basket basket;

        if (Objects.isNull(ctx.sessionAttribute("userBasket"))) {
            basket = new Basket();
            ctx.sessionAttribute("userBasket", basket);
        } else {
            basket = ctx.sessionAttribute("userBasket");
        }

        basket.addToBasket(cupcake);


        ctx.attribute("message", "Du har nu tilføjet "+ amount +" cupcake, med top: " + topMap.get(top).getFlavor() + " og bund: " + bottomMap.get(bottom).getFlavor() + " til din kurv.");
        ctx.render("frontpage.html");
        CupcakeController.dropDowns(ctx, connectionPool);
    }
    public static void removeCupcakeFromBasket(Context ctx){
        Basket basket = ctx.sessionAttribute("userBasket");
        int id = Integer.parseInt(ctx.formParam("id"));
        basket.removeFromBasket(id);
        ctx.attribute("basket", basket.getBasket());
        ctx.attribute("totalPrice", basket.getTotalPrice());
        ctx.attribute("username", ctx.sessionAttribute("username"));
        ctx.render("basket.html");

    }

    public static void showBasket(Context ctx){
        Basket basket = ctx.sessionAttribute("userBasket");
        if (Objects.isNull(basket)){
            basket = new Basket();
            ctx.sessionAttribute("userBasket", basket);
            ctx.attribute("basket", basket.getBasket());
            ctx.attribute("totalPrice", basket.getTotalPrice());

        } else {
        ctx.attribute("basket", basket.getBasket());
        ctx.attribute("totalPrice", basket.getTotalPrice());
        }

        ctx.attribute("username", ctx.sessionAttribute("username"));
        ctx.render("basket.html");
    }


}
