package app.persistence;

import app.entities.Cupcake;
import app.entities.CupcakeBottom;
import app.entities.CupcakeTop;
import app.exceptions.DatabaseException;
import io.javalin.http.Context;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

}
