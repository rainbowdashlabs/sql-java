# Datasource Basics

To connect to a database we use jdbc urls. They are the most reliable way and allow a high amount of customization. They
follow a unified format which looks like that:

`jdbc:<database>://url:port/database`

For now, I will show you how to create a DataSource for each database. We will switch to HikariCP later, which will no
longer require us to define a datasource of a specific type.

For every database we will first initialize the data source of the required type, set the url. If required we set
username and password. This will be enough for now and is enough to connect.

This is the minimal setup and there is a ton of configuration stuff you can do with those urls. I will link the
documentation for the parameter for each database.

Each class will have one method which creates a DataSource and a main method which calls this method. Since this is not
a software architecture tutorial, but probably a beginner guide, I will note that this is not a best practice in terms
of structure.

## Postgres

[Parameter](https://jdbc.postgresql.org/documentation/use/)

```java
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

public class PostgresData {
    public static void main(String[] args) throws SQLException {
        DataSource dataSource = createDataSource();
    }

    public static DataSource createDataSource() throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl("jdbc:postgres://localhost:5432/db");
        dataSource.setUser("username");
        dataSource.setPassword("password");
        return dataSource;
    }
}
```

## MySQL

[Parameter](https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-configuration-properties.html)

```java
import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

public class MySqlData {
    public static void main(String[] args) throws SQLException {
        DataSource dataSource = createDataSource();
    }

    public static DataSource createDataSource() throws SQLException {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/db");
        dataSource.setUser("username");
        dataSource.setPassword("password");
        return dataSource;
    }
}
```

## MariaDB

[Parameter](https://mariadb.com/kb/en/about-mariadb-connector-j/#optional-url-parameters)

```java
import org.mariadb.jdbc.MariaDbPoolDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

public class MariaDbData {
    public static void main(String[] args) throws SQLException {
        DataSource dataSource = createDataSource();
    }

    public static DataSource createDataSource() throws SQLException {
        MariaDbPoolDataSource dataSource = new MariaDbPoolDataSource();
        dataSource.setUrl("jdbc:mariadb://localhost:3306/db");
        dataSource.setUser("username");
        dataSource.setPassword("password");
        return dataSource;
    }
}
```

## SqLite

```java
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
```


