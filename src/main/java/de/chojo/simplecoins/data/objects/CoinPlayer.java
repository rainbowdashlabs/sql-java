package de.chojo.simplecoins.data.objects;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class CoinPlayer {
    private final long amount;
    private final OfflinePlayer offlinePlayer;

    public CoinPlayer(UUID uuid, long amount) {
        this.offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        this.amount = amount;
    }

    public long getAmount() {
        return amount;
    }

    public OfflinePlayer getOfflinePlayer() {
        return offlinePlayer;
    }
}