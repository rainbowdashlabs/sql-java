package de.chojo.chapter5;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

public class TimestampConversion {
    static DataSource dataSource;

    public static void main(String[] args) {
        System.out.printf("%s%n", playerById(10));
    }

    public static Optional<Player> playerById(int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT id, player_name, last_online FROM player WHERE id = ?")) {
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new Player(resultSet.getInt("id"),
                        resultSet.getString("player_name"),
                        resultSet.getTimestamp("last_online").toInstant()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    record Player(int id, String name, Instant instant) {
    }
}
