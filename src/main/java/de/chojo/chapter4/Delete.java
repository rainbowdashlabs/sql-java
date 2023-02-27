package de.chojo.chapter4;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Delete {
    static DataSource dataSource;

    public static void main(String[] args) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("""
                     DELETE FROM player WHERE id = ?
                     """)) {
            stmt.setInt(1, 10);
            int changed = stmt.executeUpdate();
            System.out.printf("Deleted %d row%n", changed);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
