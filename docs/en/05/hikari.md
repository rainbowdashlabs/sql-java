# HikariCP and connection pooling

You might notice that we open our connection as a resource, which will inevitable close the connection once we leave our
try block. That is not really what we want. Opening connections is expensive, and ideally we would keep those
connections and reuse them. Implementing this is not the responsibility of JDBC and that is why connection pooling
frameworks exist.

That is where [HikariCP](https://github.com/brettwooldridge/HikariCP) comes into use. HikariCP wraps connections into their own connection. Instead of closing the
connection when `close()` is called they move the connection back into a pool and will return it again when we request a
connection from our datasource. Since we used a `DataSource` before switching to Hikari is no problem, because it
creates a `DataSource` as well. All we need to change is drop our url we defined for our JDBC driver to connect into
our `HikariDataSource` instead and define a pool size.

To use Hikari we need to import it first:

**Latest version**

![Latest version](https://img.shields.io/maven-central/v/com.zaxxer/HikariCP)

**Gradle**

```kts
implementation("com.zaxxer", "HikariCP", "version")
```

**Maven**

```xml

<dependency>
    <groupId>com.zaxxer</groupId>
    <artifactId>HikariCP</artifactId>
    <version>version</version>
</dependency>
```

Once we imported HikariCP we need to create our DataSource.

```java
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgresHikariCP {
    public static void main(String[] args) throws SQLException {
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
        config.setJdbcUrl("jdbc:postgres://localhost:5432/db");
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
```

And now we will use pooled connections whenever we request a connection from our datasource.

## Choose the correct pool and idle amount

Most applications will work fine with 3 to 5 connections per pool. You always need to ask how many parallel connections
you will need. You might also add some monitoring for it if you need. The idle connection should be set to 1 at least,
but two usually is better.

## More configuration

We are just scraping the surface when using HikariCP. There are tons of customization we can do on it and I highly
recommend taking a look at the [documentation](https://github.com/brettwooldridge/HikariCP#gear-configuration-knobs-baby).
