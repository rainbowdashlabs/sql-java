package de.chojo.chapter4;


import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgresData {
    public static void main(String[] args) throws SQLException {
        DataSource dataSource = createDataSource();
        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()){
            stmt.execute("SELECT 1");
        }
    }

    public static DataSource createDataSource() throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl("jdbc:postgres://localhost:5432/db");
        dataSource.setUser("username");
        dataSource.setPassword("password");
        return dataSource;
    }
}
