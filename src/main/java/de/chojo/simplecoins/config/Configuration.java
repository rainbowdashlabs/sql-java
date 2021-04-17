package de.chojo.simplecoins.config;

import de.chojo.simplecoins.config.elements.Database;
import org.bukkit.plugin.Plugin;

public class Configuration {
    private final Plugin plugin;

    private Database database;
    private String driver;

    public Configuration(Plugin plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        database = plugin.getConfig().getObject("database", Database.class, new Database());
        driver = plugin.getConfig().getString("driver");
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public String getDriver() {
        return driver;
    }

    public Database getDatabase() {
        return database;
    }
}