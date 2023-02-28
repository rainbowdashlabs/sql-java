package de.chojo.chapter5;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReturnListMap {

    static DataSource dataSource;

    public static List<Player> playerByIdsAsList(int minId, int maxId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT id, player_name FROM player WHERE id >= ? AND id <= ?")) {
            stmt.setInt(1, minId);
            stmt.setInt(2, maxId);
            ResultSet resultSet = stmt.executeQuery();
            List<Player> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(new Player(resultSet.getInt("id"), resultSet.getString("player_name")));
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public static Map<Integer, Player> playerByIdsAsMap(int minId, int maxId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT id, player_name FROM player WHERE id >= ? AND id <= ?")) {
            stmt.setInt(1, minId);
            stmt.setInt(2, maxId);
            ResultSet resultSet = stmt.executeQuery();
            Map<Integer, Player> result = new HashMap<>();
            while (resultSet.next()) {
                Player player = new Player(resultSet.getInt("id"), resultSet.getString("player_name"));
                result.put(player.id(), player);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }

    record Player(int id, String name) {
    }
}
