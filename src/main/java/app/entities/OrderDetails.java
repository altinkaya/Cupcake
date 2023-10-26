package app.entities;

public class OrderDetails {

    private int orderNr;
    private int topId;
    private int bottomId;

    private int amount;

    public OrderDetails(int orderNr, int topId, int bottomId, int amount) {
        this.orderNr = orderNr;
        this.topId = topId;
        this.bottomId = bottomId;
        this.amount = amount;
    }

    public int getOrderNr() {
        return orderNr;
    }

    public int getTopId() {
        return topId;
    }

    public int getBottomId() {
        return bottomId;
    }

    public int getAmount() {
        return amount;
    }
}
