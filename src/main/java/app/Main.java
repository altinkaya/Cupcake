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
import java.util.Map;

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
        app.get("/orders", ctx -> OrderController.getOrders(ctx, connectionPool));
        app.post("/orders", ctx -> OrderController.getOrders(ctx, connectionPool));
        app.post("/orderDetail", ctx -> OrderController.getOrderDetails(ctx, connectionPool));
        app.get("/basket", ctx -> BasketController.showBasket(ctx));
        app.post("/checkout", ctx -> OrderController.checkout(ctx, connectionPool));
        app.post("/deleteFromBasket", ctx -> BasketController.removeCupcakeFromBasket(ctx));
        app.get("/createuser", ctx -> ctx.render("createuser.html"));
        app.post("/createuser",ctx -> UserController.createuser(ctx, connectionPool ));
        app.post("/logout", ctx -> UserController.logout(ctx));
        app.post("/delete-top/{id}", ctx -> {CupcakeController.deleteTop(ctx, connectionPool);});
        app.post("/delete-bottom/{id}", ctx -> {CupcakeController.deleteBottom(ctx, connectionPool);});
        app.post("/updateUser", ctx -> AdminController.updateUser(ctx, connectionPool));
        app.post("/searchUser", ctx -> AdminController.searchUser(ctx, connectionPool));
        app.get("/users", ctx -> AdminController.users(ctx, connectionPool));
        app.get("/admin", ctx -> AdminController.admin(ctx, connectionPool));
        app.post("/addtop", ctx -> CupcakeController.addtop(ctx, connectionPool));
        app.post("/addbottom", ctx -> CupcakeController.addbottom(ctx, connectionPool));
        app.get("/cake", ctx ->AdminController.cake(ctx, connectionPool));
        app.get("/ordre", ctx -> AdminController.ordre(ctx,connectionPool));
        app.get("/edit-top/{id}", ctx -> AdminController.editTop(ctx, connectionPool));
        app.post("/update-top", ctx -> AdminController.updateTop(ctx,connectionPool));
        app.get("/edit-bottom/{id}", ctx -> AdminController.editBottom(ctx, connectionPool));
        app.post("/update-bottom", ctx -> AdminController.updateBottom(ctx, connectionPool));

    }


}
