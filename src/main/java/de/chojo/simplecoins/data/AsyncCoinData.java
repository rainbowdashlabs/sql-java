package de.chojo.simplecoins.data;

import de.chojo.simplecoins.async.BukkitAsyncAction;
import de.chojo.simplecoins.data.objects.CoinPlayer;
import de.chojo.simplecoins.data.util.PluginDataHolder;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.UUID;

/**
 * This class is equal to {@link CoinData}. It just wraps the queries into a {@link BukkitAsyncAction}.
 * <p>
 * The cleaner version with delegate is visible in {@link AsyncWrappedCoinData}
 */
public class AsyncCoinData extends PluginDataHolder {
    public AsyncCoinData(Plugin plugin, DataSource source) {
        super(plugin, source);
    }

    public BukkitAsyncAction<Boolean> addCoins(Player player, long amount) {
        return BukkitAsyncAction.supplyAsync(plugin(), () -> {
            try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO player_coins SET uuid  = ?, coins = ? ON DUPLICATE KEY UPDATE coins = coins + ?;")) {
                stmt.setString(1, player.getUniqueId().toString());
                stmt.setLong(2, amount);
                stmt.setLong(3, amount);
                stmt.execute();
                return true;
            } catch (SQLException e) {
                logSQLError("Could not add coins", e);
            }
            return false;
        });
    }

    public BukkitAsyncAction<Boolean> takeCoins(Player player, long amount) {
        return BukkitAsyncAction.supplyAsync(plugin(), () -> {
            try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE player_coins SET coins = coins - ? where uuid = ? and coins >= ?;"
            )) {
                stmt.setLong(1, amount);
                stmt.setString(2, player.getUniqueId().toString());
                stmt.setLong(3, amount);
                int updated = stmt.executeUpdate();
                return updated == 1;
            } catch (SQLException e) {
                logSQLError("Could not take coins from player.", e);
            }
            return false;
        });
    }

    public BukkitAsyncAction<Boolean> deleteCoins(Player player) {
        return BukkitAsyncAction.supplyAsync(plugin(), () -> {
            try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM player_coins where uuid = ?;"
            )) {
                stmt.setString(1, player.getUniqueId().toString());
                int updated = stmt.executeUpdate();
                return updated == 1;
            } catch (SQLException e) {
                logSQLError("Could not delete coins of player.", e);
            }
            return false;
        });
    }

    public BukkitAsyncAction<Boolean> setCoins(Player player, long amount) {
        return BukkitAsyncAction.supplyAsync(plugin(), () -> {
            try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
                    "REPLACE player_coins(uuid, coins)  VALUES(?,?);"
            )) {
                stmt.setString(1, player.getUniqueId().toString());
                stmt.setLong(2, amount);
                stmt.execute();
                return true;
            } catch (SQLException e) {
                logSQLError("Could not set coins", e);
            }
            return false;
        });
    }

    public BukkitAsyncAction<OptionalLong> getCoins(Player player) {
        return BukkitAsyncAction.supplyAsync(plugin(), () -> {
            try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
                    "select coins from player_coins where uuid = ?;"
            )) {
                stmt.setString(1, player.getUniqueId().toString());
                ResultSet resultSet = stmt.executeQuery();
                if (resultSet.next()) {
                    return OptionalLong.of(resultSet.getLong("coins"));
                }
                return OptionalLong.of(0);
            } catch (SQLException e) {
                logSQLError("Could not retrieve player coins.", e);
                return OptionalLong.empty();
            }
        });
    }

    public BukkitAsyncAction<Optional<List<CoinPlayer>>> getCoins() {
        return BukkitAsyncAction.supplyAsync(plugin(), () -> {
            try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
                    "SELECT uuid, coins from player_coins;"
            )) {
                ResultSet resultSet = stmt.executeQuery();
                List<CoinPlayer> coins = new ArrayList<>();
                while (resultSet.next()) {
                    coins.add(
                            new CoinPlayer(
                                    UUID.fromString(resultSet.getString("uuid")),
                                    resultSet.getLong("coins"))
                    );
                }
                return Optional.of(coins);
            } catch (SQLException e) {
                logSQLError("Could not fetch all player coins", e);
                return Optional.empty();
            }
        });
    }

    public BukkitAsyncAction<Optional<List<CoinPlayer>>> getTopCoins(int amount) {
        return BukkitAsyncAction.supplyAsync(plugin(), () -> {
            try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
                    "SELECT uuid, coins from player_coins ORDER BY coins DESC LIMIT ?;"
            )) {
                stmt.setInt(1, amount);
                ResultSet resultSet = stmt.executeQuery();
                List<CoinPlayer> coins = new LinkedList<>();
                while (resultSet.next()) {
                    coins.add(
                            new CoinPlayer(
                                    UUID.fromString(resultSet.getString("uuid")),
                                    resultSet.getLong("coins"))
                    );
                }
                return Optional.of(coins);
            } catch (SQLException e) {
                logSQLError("Could not fetch all player coins", e);
                return Optional.empty();
            }
        });
    }
}
