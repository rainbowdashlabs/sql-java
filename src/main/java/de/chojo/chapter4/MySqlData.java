package de.chojo.chapter4;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class MySqlData {
    public static void main(String[] args) throws SQLException {
        DataSource dataSource = createDataSource();
        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()){
            stmt.execute("SELECT 1");
        }
    }

    public static DataSource createDataSource() throws SQLException {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/db");
        dataSource.setUser("username");
        dataSource.setPassword("password");
        return dataSource;
    }
}
