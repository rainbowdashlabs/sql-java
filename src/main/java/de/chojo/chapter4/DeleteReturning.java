package de.chojo.chapter4;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeleteReturning {
    static DataSource dataSource;

    public static void main(String[] args) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("""
                     DELETE FROM player WHERE id = ? RETURNING player_name
                     """)) {
            stmt.setInt(1, 10);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                System.out.printf("Deleted player %s%n", resultSet.getString("player_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
