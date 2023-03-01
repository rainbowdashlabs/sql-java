# Datasource Basics

To connect to a database we use jdbc urls.
They are the most reliable way and allow a high amount of customization.
They follow a unified format which looks like that:

`jdbc:<database>://url:port/database`

For now, I will show you how to create a DataSource for each database.
We will switch to HikariCP later, which will no longer require us to define a datasource of a specific type.

For every database we will first initialize the data source of the required type, set the url.
If required we set username and password.
This will be enough for now and is enough to connect.

This is the minimal setup and there is a ton of configuration stuff you can do with those urls.
I will link the documentation for the parameter for each database.

Each class will have one method which creates a DataSource and a main method which calls this method.
Once we have created our datasource we will retrieve a connection from it as a resource, create a statement, execute it and let the statement and connection being closed automatically again.
Since this is not a software architecture tutorial, but probably a beginner guide, I will note that this is not a best practice in terms of structure.

From now on I will always assume that a DataSource is present.
I will not show the creation of it.

## Class loading issues.

In general java will try to find the `Driver` implementation for the specified database.
For postgres it will try to find the driver implementation for the postgres jdbc driver.
Normally this class would already be loaded if we use the postgres data source directly, but in some example we will have late we won't use the postgres data source directly, but only the url to specify the database we want to connect to.
In order to force java to load our `Driver` class we will need to use load the class via `Class.forName(String)`.

This could look like this:

```java
public class ClassForName {
    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver not found.", e);
        }
    }
}
```

`org.postgresql.Driver` is where the Driver class is located for postgres.
Your IDE can easily show you all available implementations.
The class implementing the `Driver` interface is usually named `Driver` as well.

_Note:_ When you are using relocation you will of course need to load the relocated class.

## Postgres

[Parameter](https://jdbc.postgresql.org/documentation/use/)

```java
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

public class PostgresData {
    public static void main(String[] args) throws SQLException {
        DataSource dataSource = createDataSource();
        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
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
        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
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
        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
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
```

## SqLite

SqLite does not have additional url parameter

```java
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

public class SqLiteData {
    public static void main(String[] args) throws SQLException {
        DataSource dataSource = createDataSource();
        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("SELECT 1");
        }
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

