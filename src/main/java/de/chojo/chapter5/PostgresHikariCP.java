package de.chojo.chapter5;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgresHikariCP {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        DataSource dataSource = createDataSource();
        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("SELECT 1");
        }
    }

    public static DataSource createDataSource() throws SQLException, ClassNotFoundException {
        // We load the driver class into the class path
        Class.forName("org.postgresql.Driver");
        // Create a new config
        HikariConfig config = new HikariConfig();
        // Set the url we used before already to connect to our database
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/db");
        // Insert our credentials
        config.setUsername("username");
        config.setPassword("password");
        // We define a maximum pool size.
        config.setMaximumPoolSize(5);
        // Define the min idle connections.
        config.setMinimumIdle(2);
        // Create a new DataSource based on our config
        return new HikariDataSource(config);
    }
}
