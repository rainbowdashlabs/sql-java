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

One exception where we would not use `execute` would be if we use a `RETURNING` clause to get the new created id for our user.

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
