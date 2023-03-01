# Inserting data

For inserting data we will mainly use the `execute` method of our `PreparedStatement`. We will know that it worked when
we get no exception and since we usually only insert a single line, we are not really interested into the changed lines
as well.

```java
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Insert {
    static DataSource dataSource;

    public static void main(String[] args) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("""
                     INSERT INTO player(player_name) VALUES (?)
                     """)) {
            stmt.setString(1, "Lexi");
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

One exception where we would not use `execute` would be if we use a `RETURNING` [clause](dev/private/java/!tutorial/basicsql-pages/docs/en/02rivate/java/!tutorial/basicsql-pages/docs/en/02/returning.md) to get the
new created id for our user.

```java
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InsertReturning {
    static DataSource dataSource;

    public static void main(String[] args) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("""
                     INSERT INTO player(player_name) VALUES (?) RETURNING id
                     """)) {
            stmt.setString(1, "Lexi");
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                System.out.printf("Created user %s with id %d%n", "Lexi", resultSet.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

## Generated keys

For databases without an `RETURNING` clause (Looking at you MySQL) you can retrieve the generated keys in another way.
Our `PreparedStatement` not only allows us to execute, but also to check what our statement changed. If we provide that
we want the generated keys by using a flag, the database will return the generated keys when we execute the statement.
After that we can read those from the statement.

```java
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class InsertGeneratedKeys {
    static DataSource dataSource;

    public static void main(String[] args) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("""
                     INSERT INTO player(player_name) VALUES (?)
                     """, Statement.RETURN_GENERATED_KEYS)) { // Setting this flag here is very important
            stmt.setString(1, "Lexi");
            stmt.executeUpdate();
            // We get a new ResultSet here
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                // Since we do not know how the column is named we simply use the first one
                System.out.printf("Created user %s with id %d%n", "Lexi", keys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

The issue with that is that a key is only returned if it was explicitly identified as key. If that fails you can still
ask the database to explicitly return one or more columns.

```java
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InsertGeneratedKeysColumns {
    static DataSource dataSource;

    public static void main(String[] args) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("""
                     INSERT INTO player(player_name) VALUES (?)
                     """, new String[]{"id"})) { // We define that we want to get the value of id back
            stmt.setString(1, "Lexi");
            stmt.executeUpdate();
            // The rest is as usual
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                System.out.printf("Created user %s with id %d%n", "Lexi", keys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

