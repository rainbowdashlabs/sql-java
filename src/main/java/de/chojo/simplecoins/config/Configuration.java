package de.chojo.simplecoins.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

public class Configuration {
    private final Plugin plugin;


    private Configuration() {
        throw new UnsupportedOperationException("This is a utility class.");
    }

    public Configuration(Plugin plugin) {
        this.plugin = plugin;
    }

    public String getHost() {
        return getDatabaseSection().getString("host");
    }

    public String getUser() {
        return getDatabaseSection().getString("user");
    }

    public String getPassword() {
        return getDatabaseSection().getString("password");
    }

    public String getDatabase() {
        return getDatabaseSection().getString("database");
    }

    public int getPort() {
        return getDatabaseSection().getInt("port");
    }

    private ConfigurationSection getDatabaseSection() {
        return plugin.getConfig().getConfigurationSection("database");
    }
}
