package app.entities;

import java.util.List;

public class User
{
    private int id;
    private String name;
    private String password;

    List<Cupcake> basket;

    public User(int id, String name, String password)
    {
        this.id = id;
        this.name = name;
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
