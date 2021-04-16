package de.chojo.simplecoins.listener;

import de.chojo.simplecoins.data.AsyncCoinData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import javax.sql.DataSource;

public class AsyncJoinAndLeaveListener implements Listener {
    private final AsyncCoinData data;
    private final Plugin plugin;

    public AsyncJoinAndLeaveListener(DataSource source, Plugin plugin) {
        this.plugin = plugin;
        this.data = new AsyncCoinData(plugin, source);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        data.addCoins(event.getPlayer(), 10).queue(result -> {
            if (result) {
                event.getPlayer().sendMessage("You received 10 coins.");
            }
            data.getCoins(event.getPlayer()).queue(coins -> {
                if (coins.isPresent()) {
                    event.getPlayer().sendMessage("You have currently " + coins.getAsLong() + " coins.");
                }
            });
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        data.takeCoins(event.getPlayer(), 5).queue(result -> {
            if (result) {
                plugin.getLogger().info("Withdraw 5 coins from " + event.getPlayer().getName() + ".");
                return;
            }
            plugin.getLogger().info("Player has not sufficient coins.");
        });
    }
}
