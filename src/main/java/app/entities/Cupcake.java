package app.entities;

public class Cupcake {
    private String topFlavor;
    private String bottomFlavor;

    private int amount;
    private int price;

    public Cupcake(CupcakeTop top, CupcakeBottom bottom, int amount) {
        this.topFlavor = top.getFlavor();
        this.bottomFlavor = bottom.getFlavor();
        this.amount = amount;
        this.price = (top.getPrice() + bottom.getPrice()) * amount;
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
        return "Cupcake{" +
                "topFlavor='" + topFlavor + '\'' +
                ", bottomFlavor='" + bottomFlavor + '\'' +
                ", amount=" + amount +
                ", price=" + price +
                '}';
    }


}
