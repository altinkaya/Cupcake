package app.persistence;

import app.entities.Cupcake;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CupcakeMapper {

    public static Cupcake createCupcake(String topFlavor, String bottomFlavor, int amount, ConnectionPool connectionPool) throws DatabaseException {
        int price = 0;
        String sqlTop = "SELECT price FROM cupcake_top WHERE flavor=?";
        String sqlBottom = "SELECT price FROM cupcake_bottom WHERE flavor=?";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sqlTop)) {
                ps.setString(1, topFlavor);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    price += rs.getInt("top_price");
                } else {
                    throw new DatabaseException("Fejl i oprrettelse af cupcake. Prøv igen.");
                }
            }
            try (PreparedStatement ps = connection.prepareStatement(sqlBottom)) {
                ps.setString(1, bottomFlavor);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    price += rs.getInt("bottom_price");
                } else {
                    throw new DatabaseException("Fejl i oprrettelse af cupcake. Prøv igen.");
                }
            }
            return new Cupcake(topFlavor, bottomFlavor, amount, price);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
}
