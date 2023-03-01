package de.chojo.chapter5;

import de.chojo.chapter5.threading.BukkitFutureResult;
import de.chojo.chapter5.threading.CompletableBukkitFuture;
import org.bukkit.plugin.Plugin;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ReturnOptionalAsync {
    static DataSource dataSource;
    static Plugin plugin;

    public static void main(String[] args) {
        playerByIdAsync(10)
                .whenComplete(player -> System.out.printf("Player %s%n", player));
    }

    public static BukkitFutureResult<Optional<Player>> playerByIdAsync(int id) {
        return CompletableBukkitFuture.supplyAsync(plugin, () -> playerById(id));
    }

    public static Optional<Player> playerById(int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT id, player_name FROM player WHERE id = ?")) {
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new Player(resultSet.getInt("id"), resultSet.getString("player_name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    record Player(int id, String name) {
    }
}
