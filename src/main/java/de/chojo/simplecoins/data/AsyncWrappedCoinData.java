package de.chojo.simplecoins.data;

import de.chojo.simplecoins.async.BukkitAsyncAction;
import de.chojo.simplecoins.data.objects.CoinPlayer;
import de.chojo.simplecoins.data.util.PluginDataHolder;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

/**
 * A clean implementation example if you want to change your data class into a async class.
 */
public class AsyncWrappedCoinData {

    private final CoinData delegate;
    private final Plugin plugin;

    /**
     * Create a new {@link PluginDataHolder} with a datasource to server connections and a plugin for logging.
     *
     * @param plugin plugin for logging
     * @param source source to provide connections.
     */
    public AsyncWrappedCoinData(Plugin plugin, DataSource source) {
        this.plugin = plugin;
        delegate = new CoinData(plugin, source);
    }

    public BukkitAsyncAction<Boolean> addCoins(Player player, long amount) {
        return BukkitAsyncAction.supplyAsync(plugin, () -> delegate.addCoins(player, amount));
    }

    public BukkitAsyncAction<Boolean> takeCoins(Player player, long amount) {
        return BukkitAsyncAction.supplyAsync(plugin, () -> delegate.takeCoins(player, amount));
    }

    public BukkitAsyncAction<Boolean> deleteCoins(Player player) {
        return BukkitAsyncAction.supplyAsync(plugin, () -> delegate.deleteCoins(player));
    }

    public BukkitAsyncAction<Boolean> setCoins(Player player, long amount) {
        return BukkitAsyncAction.supplyAsync(plugin, () -> delegate.setCoins(player, amount));
    }

    public BukkitAsyncAction<OptionalLong> getCoins(Player player) {
        return BukkitAsyncAction.supplyAsync(plugin, () -> delegate.getCoins(player));
    }

    public BukkitAsyncAction<Optional<List<CoinPlayer>>> getCoins() {
        return BukkitAsyncAction.supplyAsync(plugin, () -> delegate.getCoins());
    }

    public BukkitAsyncAction<Optional<List<CoinPlayer>>> getTopCoins(int amount) {
        return BukkitAsyncAction.supplyAsync(plugin, () -> delegate.getTopCoins(amount));
    }
}
