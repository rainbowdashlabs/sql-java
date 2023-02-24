package de.chojo.chapter4;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PreparedStatementUsage {
    static DataSource dataSource;

    public static void main(String[] args) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT player_name FROM player WHERE id = ?")) {
            stmt.setInt(1, 10);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
