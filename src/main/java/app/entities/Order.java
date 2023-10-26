package app.entities;

public class Order {

    private int orderNr;
    private int userId;
    private String status;
    private int price;

    public Order(int orderNr, int userId, String status, int price) {
        this.orderNr = orderNr;
        this.userId = userId;
        this.status = status;
        this.price = price;
    }
    public Order(int userId, String status, int price) {
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

    public String getStatus() {
        return status;
    }

    public int getPrice() {
        return price;
    }
}
