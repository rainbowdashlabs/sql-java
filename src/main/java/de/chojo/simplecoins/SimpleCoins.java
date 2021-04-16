package de.chojo.simplecoins;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import de.chojo.simplecoins.config.Configuration;
import de.chojo.simplecoins.config.elements.Database;
import de.chojo.simplecoins.listener.JoinAndLeaveListener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.mariadb.jdbc.MariaDbPoolDataSource;

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
            // We want to use a driver based on the config input for educational reasons.
            // you can see that bot methods return a DataSource. However the underlying implementation is different,
            // but we dont care as long as we get a DataSource back.
            if (config.getDriver().equalsIgnoreCase("mysql")) {
                dataSource = initMySQLDataSource();
            } else if (config.getDriver().equalsIgnoreCase("mariadb")) {
                dataSource = initMariaDBDataSource();
            } else {
                dataSource = initMySQLDataSource();
                getLogger().info(config.getDriver() + " is not a valid driver. Using mysql.");
            }
        } catch (SQLException e) {
            getLogger().log(Level.SEVERE, "Could not establish database connection", e);
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        try {
            // We initialize our database.
            initDb();
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

    private DataSource initMySQLDataSource() throws SQLException {
        // We create a new SQL connection pool data source for MySQL.
        // However this data source will work with MariaDB as well.
        MysqlDataSource dataSource = new MysqlConnectionPoolDataSource();
        Database database = config.getDatabase();
        // we set our credentials
        dataSource.setServerName(database.getHost());
        dataSource.setPassword(database.getPassword());
        dataSource.setPortNumber(database.getPort());
        dataSource.setDatabaseName(database.getDatabase());
        dataSource.setUser(database.getUser());

        // Test connection
        testDataSource(dataSource);

        // and return our stuff.
        return dataSource;
    }

    private DataSource initMariaDBDataSource() throws SQLException {
        // We create a new SQL connection pool data source for MariaDB
        MariaDbPoolDataSource dataSource = new MariaDbPoolDataSource();
        Database database = config.getDatabase();
        // we set our credentials
        dataSource.setServerName(database.getHost());
        dataSource.setPassword(database.getPassword());
        dataSource.setPortNumber(database.getPort());
        dataSource.setDatabaseName(database.getDatabase());
        dataSource.setUser(database.getUser());
        dataSource.setMaxPoolSize(20);
        // Test connection
        testDataSource(dataSource);

        // and return our stuff.
        return dataSource;
    }

    private void testDataSource(DataSource dataSource) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            if (!conn.isValid(1000)) {
                throw new SQLException("Could not establish database connection.");
            }
        }
    }

    private void initDb() throws SQLException, IOException {
        // first lets read our setup file.
        // This file contains statements to create our inital tables.
        // it is located in the resources.
        String setup;
        try (InputStream in = getClassLoader().getResourceAsStream("dbsetup.sql")) {
            setup = new String(in.readAllBytes());
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Could not read db setup file.", e);
            throw e;
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
