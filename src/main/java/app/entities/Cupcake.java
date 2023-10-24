package app.entities;

public class Cupcake {
    private String topFlavor;
    private String bottomFlavor;

    private int amount;
    private int price;

    public Cupcake(String topFlavor, String bottomFlavor, int amount, int price) {
        this.topFlavor = topFlavor;
        this.bottomFlavor = bottomFlavor;
        this.amount = amount;
        this.price = price;
    }

    public String getTopFlavor() {
        return topFlavor;
    }

    public String getBottomFlavor() {
        return bottomFlavor;
    }

    public int getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Cupcake{" + "topFlavor=" + topFlavor +
                ", bottomFlavor=" + bottomFlavor +
                ", amount=" + amount +
                ", price=" + price + '}';
    }


}
