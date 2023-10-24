package app.persistence;

import app.entities.Cupcake;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class CupcakeMapper {

        public static HashMap<String,Integer> topFlavors(ConnectionPool connectionPool) throws DatabaseException {
            HashMap<String, Integer> topFlavors = new HashMap<>();
            String sql = "SELECT * FROM cupcake_top";
            try (Connection connection = connectionPool.getConnection()) {
                try (PreparedStatement ps = connection.prepareStatement(sql)) {

                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                            String topFlavor = rs.getString("flavor");
                            int topFlavorPrice = rs.getInt("price");
                            topFlavors.put(topFlavor, topFlavorPrice);
                        }
                    }
                    return topFlavors;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }

            public static HashMap<String,Integer> bottomFlavors(ConnectionPool connectionPool) throws DatabaseException {
                HashMap<String, Integer> bottomFlavors = new HashMap<>();
                String sqlBottom = "SELECT * FROM cupcake_bottom";
                try (Connection connection = connectionPool.getConnection()) {
                    try (PreparedStatement ps = connection.prepareStatement(sqlBottom)) {
                        ResultSet rs = ps.executeQuery();
                            while (rs.next()) {
                                String bottomFlavor = rs.getString("flavor");
                                int bottomFlavorPrice = rs.getInt("price");
                                bottomFlavors.put(bottomFlavor, bottomFlavorPrice);
                            }
                        }
                        return bottomFlavors;
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                public String generateDropdownsHTML(ConnectionPool connectionPool) throws DatabaseException {
                    HashMap<String,Integer> topFlavors = topFlavors(connectionPool);
                    HashMap<String,Integer> bottomFlavors = bottomFlavors(connectionPool);
                    StringBuilder html = new StringBuilder("<form id=\"dropdowns\" action=\"cart.html\" method=\"get\">\n");
                    html.append("<select name=\"topFlavor\">\n");
                    


                }
}
