package app.entities;

import java.util.ArrayList;
import java.util.List;

public class Basket {
    private static List<Cupcake> basket;

    public Basket() {

        this.basket = new ArrayList<>();
    }

    public void addToBasket(Cupcake cupcake) {
        basket.add(cupcake);
    }

    public void removeFromBasket(Cupcake cupcake) {
        basket.remove(cupcake);
    }

    public List<Cupcake> getBasket() {
        return basket;
    }


    @Override
    public String toString() {
        return "Basket{" + basket + "}";

    }

}
