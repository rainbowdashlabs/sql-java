package de.chojo.simplecoins;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import de.chojo.simplecoins.config.Configuration;
import de.chojo.simplecoins.listener.JoinAndLeaveListener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

public class SimpleCoins extends JavaPlugin implements Listener {
    private Configuration config;
    private DataSource dataSource;

    @Override
    public void onLoad() {
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();
        config = new Configuration(this);

        try {
            dataSource = initDataSource();
        } catch (SQLException e) {
            getLogger().log(Level.SEVERE, "Could not establish database connection", e);
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        try {
            initDb();
        } catch (SQLException e) {
            getLogger().log(Level.SEVERE, "Could not init database.", e);
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onEnable() {
        JoinAndLeaveListener listener = new JoinAndLeaveListener(dataSource, this);
        getServer().getPluginManager().registerEvents(listener, this);
    }

    private DataSource initDataSource() throws SQLException {
        // We create a new MySQL connection pool data source
        MysqlDataSource dataSource = new MysqlConnectionPoolDataSource();
        // we set out credentials
        dataSource.setServerName(config.getHost());
        dataSource.setPassword(config.getPassword());
        dataSource.setPortNumber(config.getPort());
        dataSource.setDatabaseName(config.getDatabase());
        dataSource.setUser(config.getUser());

        // Test connection
        try (Connection conn = dataSource.getConnection()) {
            if (!conn.isValid(1000)) {
                throw new SQLException("Could not establish database connection.");
            }
        }

        // and return our stuff.
        return dataSource;
    }

    private void initDb() throws SQLException {
        // first lets read our setup file.
        // This file contains statements to create our inital tables.
        String setup;
        try (InputStream in = getClassLoader().getResourceAsStream("dbsetup.sql")) {
            setup = new String(in.readAllBytes());
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Could not read db setup file.", e);
            return;
        }
        // Mariadb can only handle a single query per statement. We need to split at ;.
        String[] queries = setup.split(";");
        // execute each query to the database.
        for (String query : queries) {
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.execute();
            }
        }
        getLogger().info("Database setup complete.");
    }
}
