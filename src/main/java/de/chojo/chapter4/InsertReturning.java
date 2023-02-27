package de.chojo.chapter4;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InsertReturning {
    static DataSource dataSource;

    public static void main(String[] args) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("""
                     INSERT INTO player(player_name) VALUES (?) RETURNING id
                     """)) {
            stmt.setString(1, "Lexi");
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                System.out.printf("Created user %s with id %d%n", "Lexi", resultSet.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
