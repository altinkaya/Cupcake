package app.entities;

public class Cupcake {
    private String topFlavor;
    private String bottomFlavor;

    private int amount;
    private int price;
    private int id;
    private static int counter = 0;

    private int topId;
    private int bottomId;


    public Cupcake(CupcakeTop top, CupcakeBottom bottom, int amount) {
        this.topFlavor = top.getFlavor();
        this.bottomFlavor = bottom.getFlavor();
        this.amount = amount;
        this.price = (top.getPrice() + bottom.getPrice()) * amount;
        this.id = counter++;
        this.topId = top.getId();
        this.bottomId = bottom.getId();

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

    public int getId() {
        return id;


    }

    public int getTopId() {
        return topId;
    }

    public int getBottomId() {
        return bottomId;
    }

}
