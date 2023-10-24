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
}
