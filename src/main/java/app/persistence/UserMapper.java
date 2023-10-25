package app.persistence;

import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserMapper
{

    public static User login(String email, String password, ConnectionPool connectionPool) throws DatabaseException
    {
        String sql = "select * from \"users\" where email=? and password=?";

        try (Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setString(1, email);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();
                if (rs.next())
                {
                    int id = rs.getInt("id");
                    boolean status = rs.getBoolean("admin");
                    return new User(id, email, password,status);
                } else
                {
                    throw new DatabaseException("Fejl i login. Prøv igen.");
                }
            }

        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

    }

    public static void createuser(String name, String password,int balance,boolean admin, ConnectionPool connectionPool) throws DatabaseException
    {
        String sql = "insert into \"users\" (email, password,balance,admin) values (?,?,?,?)";

        try (Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setString(1, name);
                ps.setString(2, password);
                ps.setInt(3, balance);
                ps.setBoolean(4, admin);
                int rowsAffected =  ps.executeUpdate();
                if (rowsAffected != 1)
                {
                    throw new DatabaseException("Fejl ved oprettelse af ny bruger");
                }
            }
        }
        catch (SQLException e)
        {
            String msg = "Der er sket en fejl. Prøv igen";
            if (e.getMessage().startsWith("ERROR: duplicate key value "))
            {
                msg = "Brugernavnet findes allerede. Vælg et andet";
            }

            throw new DatabaseException(msg);
        }
    }



}