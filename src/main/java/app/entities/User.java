package app.entities;

public class User
{
    private int id;
    private String name;
    private String password;
    private int balance;
    private Boolean status;

    public User(int id, String name, String password, boolean status)
    {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.status = status;
        this.password = password;

    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public Boolean getStatus(){ return status; }

    public String getPassword()
    {
        return password;
    }

    @Override
    public String toString()
    {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
