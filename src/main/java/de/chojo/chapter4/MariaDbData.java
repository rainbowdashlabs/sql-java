package de.chojo.chapter4;

import org.mariadb.jdbc.MariaDbPoolDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class MariaDbData {
    public static void main(String[] args) throws SQLException {
        DataSource dataSource = createDataSource();
        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()){
            stmt.execute("SELECT 1");
        }
    }

    public static DataSource createDataSource() throws SQLException {
        MariaDbPoolDataSource dataSource = new MariaDbPoolDataSource();
        dataSource.setUrl("jdbc:mariadb://localhost:3306/db");
        dataSource.setUser("username");
        dataSource.setPassword("password");
        return dataSource;
    }
}
