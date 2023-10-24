package app.entities;

public class Cupcake {
    private String topFlavor;
    private String bottomFlavor;
    private int price;

    public Cupcake(String topFlavor, String bottomFlavor, int price) {
        this.topFlavor = topFlavor;
        this.bottomFlavor = bottomFlavor;
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

    @Override
    public String toString() {
        return "Cupcake{" +
                "topFlavor='" + topFlavor + '\'' +
                ", bottomFlavor='" + bottomFlavor + '\'' +
                ", price=" + price +
                '}';
    }


}
