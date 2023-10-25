package app.persistence;

import app.entities.CupcakeBottom;
import app.entities.CupcakeTop;
import app.exceptions.DatabaseException;


import java.sql.*;
import java.util.HashMap;

public class CupcakeMapper {

    public static HashMap<Integer, CupcakeTop> topFlavors(ConnectionPool connectionPool) throws DatabaseException {
        HashMap<Integer, CupcakeTop> topFlavors = new HashMap<>();
        String sql = "SELECT * FROM cupcake_top";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String topFlavor = rs.getString("flavor");
                    int topFlavorPrice = rs.getInt("price");
                    topFlavors.put(id, new CupcakeTop(id, topFlavor, topFlavorPrice));
                }
            }
            return topFlavors;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static HashMap<Integer, CupcakeBottom> bottomFlavors(ConnectionPool connectionPool) throws DatabaseException {
        HashMap<Integer, CupcakeBottom> bottomFlavors = new HashMap<>();
        String sql = "SELECT * FROM cupcake_bottom";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String topFlavor = rs.getString("flavor");
                    int topFlavorPrice = rs.getInt("price");
                    bottomFlavors.put(id, new CupcakeBottom(id, topFlavor, topFlavorPrice));
                }
            }
            return bottomFlavors;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public static void addtop(String flavor, String price, ConnectionPool connectionPool) throws DatabaseException
    {
        String sql = "insert into \"cupcake_top\" (flavor, price) values (?,?)";

        try (Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setString(1, flavor);
                ps.setInt(2, Integer.parseInt(price));

                int rowsAffected =  ps.executeUpdate();
                if (rowsAffected != 1)
                {
                    throw new DatabaseException("Fejl ved oprettelse af ny variant");
                }
            }
        }
        catch (SQLException e)
        {
            String msg = "Der er sket en fejl. Prøv igen";
            if (e.getMessage().startsWith("ERROR: duplicate key value "))
            {
                msg = "denne variant findes allrede";
            }

            throw new DatabaseException(msg);
        }
    }

    public static void addbottom(String flavor, String price, ConnectionPool connectionPool) throws DatabaseException
    {
        String sql = "insert into \"cupcake_bottom\" (flavor, price) values (?,?)";

        try (Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setString(1, flavor);
                ps.setInt(2, Integer.parseInt(price));

                int rowsAffected =  ps.executeUpdate();
                if (rowsAffected != 1)
                {
                    throw new DatabaseException("Fejl ved oprettelse af ny variant");
                }
            }
        }
        catch (SQLException e)
        {
            String msg = "Der er sket en fejl. Prøv igen";
            if (e.getMessage().startsWith("ERROR: duplicate key value "))
            {
                msg = "denne variant findes allrede";
            }

            throw new DatabaseException(msg);
        }
    }


    public static void updateTop(int topId, String flavor, int price, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "UPDATE cupcake_top SET flavor = ?, price = ? WHERE id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, flavor);
            ps.setInt(2, price);
            ps.setInt(3, topId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Fejl i opdatering af top");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl i opdatering af top");
        }
    }

    public static void updateBottom(int topId, String flavor, int price, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "UPDATE cupcake_bottom SET flavor = ?, price = ? WHERE id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, flavor);
            ps.setInt(2, price);
            ps.setInt(3, topId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Fejl i opdatering af top");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl i opdatering af top");
        }
    }




}
