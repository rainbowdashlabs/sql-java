package de.chojo.chapter4;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InsertGeneratedKeysColumns {
    static DataSource dataSource;

    public static void main(String[] args) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("""
                     INSERT INTO player(player_name) VALUES (?)
                     """, new String[]{"id"})) { // We define that we want to get the value of id back
            stmt.setString(1, "Lexi");
            stmt.executeUpdate();
            // The rest is as usual
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                System.out.printf("Created user %s with id %d%n", "Lexi", keys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
