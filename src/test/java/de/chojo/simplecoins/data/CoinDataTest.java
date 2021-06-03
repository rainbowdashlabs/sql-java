package de.chojo.simplecoins.data;

import de.chojo.simplecoins.config.elements.Database;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.MariaDbPoolDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

class CoinDataTest {

    @BeforeAll
    static void initDb() throws SQLException {
        MariaDbPoolDataSource dataSource = new MariaDbPoolDataSource();
        // we set our credentials
        dataSource.setServerName("localhost");
        dataSource.setPassword("");
        dataSource.setPortNumber(3306);
        dataSource.setDatabaseName("test");
        dataSource.setUser("root");
        dataSource.setMaxPoolSize(20);
    }

    @Test
    void addCoins() {

    }

    @Test
    void takeCoins() {
    }

    @Test
    void deleteCoins() {
    }

    @Test
    void setCoins() {
    }

    @Test
    void getCoins() {
    }

    @Test
    void testGetCoins() {
    }

    @Test
    void getTopCoins() {
    }
}