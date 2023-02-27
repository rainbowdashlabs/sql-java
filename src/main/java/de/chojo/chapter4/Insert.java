package de.chojo.chapter4;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Insert {
    static DataSource dataSource;

    public static void main(String[] args) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("""
                     INSERT INTO player(player_name) VALUES (?)
                     """)) {
            stmt.setString(1, "Lexi");
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
