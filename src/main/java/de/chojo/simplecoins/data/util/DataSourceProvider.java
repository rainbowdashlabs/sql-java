package de.chojo.simplecoins.data.util;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import de.chojo.simplecoins.config.elements.Database;
import org.bukkit.plugin.Plugin;
import org.mariadb.jdbc.MariaDbPoolDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceProvider {
    public static DataSource initMySQLDataSource(Plugin plugin, Database database) throws SQLException {
        // We create a new SQL connection pool data source for MySQL.
        // However this data source will work with MariaDB as well.
        MysqlDataSource dataSource = new MysqlConnectionPoolDataSource();
        // we set our credentials
        dataSource.setServerName(database.getHost());
        dataSource.setPassword(database.getPassword());
        dataSource.setPortNumber(database.getPort());
        dataSource.setDatabaseName(database.getDatabase());
        dataSource.setUser(database.getUser());

        // Test connection
        testDataSource(plugin, dataSource);

        // and return our stuff.
        return dataSource;
    }

    public static DataSource initMariaDBDataSource(Plugin plugin, Database database) throws SQLException {
        // We create a new SQL connection pool data source for MariaDB
        MariaDbPoolDataSource dataSource = new MariaDbPoolDataSource();
        // we set our credentials
        dataSource.setUrl("mariadb://%s:%s/%s".formatted(database.getHost(), database.getPort(), database.getDatabase()));
        dataSource.setPassword(database.getPassword());
        dataSource.setUser(database.getUser());
        // Test connection
        testDataSource(plugin, dataSource);

        // and return our stuff.
        return dataSource;
    }

    private static void testDataSource(Plugin plugin, DataSource dataSource) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            if (!conn.isValid(1000)) {
                throw new SQLException("Could not establish database connection.");
            }
        }
        if (plugin != null) {
            plugin.getLogger().info("§2Database connection established.");
        }
    }
}
