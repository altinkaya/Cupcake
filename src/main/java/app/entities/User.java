package app.entities;

public class User {
    private int id;
    private String name;
    private String password;
    private int balance;
    private boolean status;

    public User(int id, String name, String password, boolean status) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.balance = balance;
        this.status = status;
    }

    public User(String name, int balance) {
        this.name = name;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }

    public boolean getStatus() {
        return status;
    }

    public String getPassword() {
        return password;
    }

    public void updateBalance(int newBalance) {
        this.balance = newBalance;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                ", status=" + status +
                '}';
    }
}
