package de.chojo.simplecoins;

import de.chojo.simplecoins.config.Configuration;
import de.chojo.simplecoins.config.elements.Database;
import de.chojo.simplecoins.data.util.DataSourceProvider;
import de.chojo.simplecoins.data.util.DbSetup;
import de.chojo.simplecoins.listener.JoinAndLeaveListener;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;

public class SimpleCoins extends JavaPlugin implements Listener {
    private Configuration config;
    private DataSource dataSource;

    @Override
    public void onLoad() {
        ConfigurationSerialization.registerClass(Database.class);

        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();
        config = new Configuration(this);

        try {
            // We want to use a driver based on the config input for educational reasons.
            // you can see that bot methods return a DataSource. However the underlying implementation is different,
            // but we dont care as long as we get a DataSource back.
            if (config.getDriver().equalsIgnoreCase("mysql")) {
                dataSource = DataSourceProvider.initMySQLDataSource(this, config.getDatabase());
            } else if (config.getDriver().equalsIgnoreCase("mariadb")) {
                dataSource = DataSourceProvider.initMariaDBDataSource(this, config.getDatabase());
            } else {
                dataSource = DataSourceProvider.initMySQLDataSource(this, config.getDatabase());
                getLogger().info(config.getDriver() + " is not a valid driver. Using mysql.");
            }
        } catch (SQLException e) {
            getLogger().log(Level.SEVERE, "Could not establish database connection", e);
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        try {
            // We initialize our database.
            DbSetup.initDb(this, dataSource);
        } catch (SQLException | IOException e) {
            getLogger().log(Level.SEVERE, "Could not init database.", e);
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onEnable() {
        JoinAndLeaveListener listener = new JoinAndLeaveListener(dataSource, this);
        getServer().getPluginManager().registerEvents(listener, this);
    }
}
