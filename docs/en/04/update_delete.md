# Update and Delete

Why do we handle both in here? Because they are essentially the same on the database side. An `UPDATE` changes an
existing
row and so does a `DELETE`. We also use the same way when dispatching them to the database.

Instead of calling `executeQuery` we call `executeUpdate`. There is a method called `execute` as well, which we could
use as well, but `executeUpdate` provides use the amount of changed rows directly.

```java
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Delete {
    static DataSource dataSource;

    public static void main(String[] args) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("""
                     DELETE FROM player WHERE id = ?
                     """)) {
            stmt.setInt(1, 10);
            int changed = stmt.executeUpdate();
            System.out.printf("Deleted %d row%n", changed);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

Ideally we should print `Deleted 1 row` now. That is the amount of rows we deleted with our query. This a great way to
check whether the query had an effect on our data.

If we combine this query with a [`RETURNING` keyword](dev/private/java/!tutorial/basicsql-pages/docs/en/02rivate/java/!tutorial/basicsql-pages/docs/en/02/returning.md) we can instead call `executeQuery` and read our
results like we did in the previous section.

```java
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeleteReturning {
    static DataSource dataSource;

    public static void main(String[] args) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("""
                     DELETE FROM player WHERE id = ? RETURNING player_name
                     """)) {
            stmt.setInt(1, 10);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) { // We could use if here as well since we do only expect one row.
                System.out.printf("Deleted player %s%n", resultSet.getString("player_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```
