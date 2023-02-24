package de.chojo.chapter4;

import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

public class SqLiteData {
    public static void main(String[] args) throws SQLException {
        DataSource dataSource = createDataSource();
    }

    public static DataSource createDataSource() throws SQLException {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        // This will create a database in memory without data persistence
        dataSource.setUrl("jdbc:sqlite::memory:");
        // This will create a dabatase and store it in a file called data.db
        dataSource.setUrl("jdbc:sqlite:data.db");
        return dataSource;
    }
}
