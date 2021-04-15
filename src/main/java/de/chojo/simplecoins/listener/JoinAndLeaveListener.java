package de.chojo.simplecoins.listener;

import de.chojo.simplecoins.data.CoinData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import javax.sql.DataSource;
import java.util.OptionalLong;

public class JoinAndLeaveListener implements Listener {
    private CoinData data;
    private Plugin plugin;

    public JoinAndLeaveListener(DataSource source, Plugin plugin) {
        this.plugin = plugin;
        this.data = new CoinData(plugin, source);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (data.addCoins(event.getPlayer(), 10)) {
            event.getPlayer().sendMessage("You received 10 coins.");
        }
        OptionalLong coins = data.getCoins(event.getPlayer());
        if (coins.isPresent()) {
            event.getPlayer().sendMessage("You have currently " + coins.getAsLong() + " coins.");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (data.takeCoins(event.getPlayer(), 5)) {
            plugin.getLogger().info("Withdraw 5 coins from " + event.getPlayer().getName() + ".");
            return;
        }
        plugin.getLogger().info("Player has not sufficient coins.");
    }
}
