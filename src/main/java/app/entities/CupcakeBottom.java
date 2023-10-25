package app.entities;

public class CupcakeBottom {
    private int id;
    private String flavor;
    private int price;

    public CupcakeBottom(int id, String flavor, int price) {
        this.id = id;
        this.flavor = flavor;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getFlavor() {
        return flavor;
    }

    public int getPrice() {
        return price;
    }
}
