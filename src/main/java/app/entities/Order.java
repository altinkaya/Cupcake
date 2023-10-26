package app.entities;

public class Order {

    private int orderNr;
    private int userId;
    private boolean status;
    private int price;

    public Order(int orderNr, int userId, boolean status, int price) {
        this.orderNr = orderNr;
        this.userId = userId;
        this.status = status;
        this.price = price;
    }

    public int getOrderNr() {
        return orderNr;
    }

    public int getUserId() {
        return userId;
    }

    public boolean isStatus() {
        return status;
    }

    public int getPrice() {
        return price;
    }
}
