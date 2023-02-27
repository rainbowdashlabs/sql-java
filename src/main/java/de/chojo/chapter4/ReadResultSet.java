package de.chojo.chapter4;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReadResultSet {
    static DataSource dataSource;

    public static void main(String[] args) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT player_name FROM player WHERE id = ?")) {
            stmt.setInt(1, 10);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                System.out.println(resultSet.getString("player_name"));
                System.out.println(resultSet.getString(1));
            } else {
                System.out.println("Not entries returned");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
