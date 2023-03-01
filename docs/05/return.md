# Return Types

Now we learned countless ways to read and write data. What is missing are methods and return types which indicate
whether our operation was a success. For that we have different options that we will look into here. You want to have
some kind of return type in any case.

## Optionals

Optionals are a java class. They are prefered when a call returns 0 or 1 results. They can be constructed by
calling `Optional.of()`, `Optional.ofNullable()` or `Optional.empty()`.

```java
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ReturnOptional {
    static DataSource dataSource;

    public static void main(String[] args) {
        System.out.printf("%s%n", playerById(10));
    }

    public static Optional<Player> playerById(int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT id, player_name FROM player WHERE id = ?")) {
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new Player(resultSet.getInt("id"), resultSet.getString("player_name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    record Player(int id, String name) {
    }
}
```

## List and Map

When returning multiple entities it is applicable to return them as a list. Of course a map would be possible as well,
but in most of the cases you will notice that objects in a list is usually sufficent.

```java
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReturnListMap {

    static DataSource dataSource;

    public static List<Player> playerByIdsAsList(int minId, int maxId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT id, player_name FROM player WHERE id >= ? AND id <= ?")) {
            stmt.setInt(1, minId);
            stmt.setInt(2, maxId);
            ResultSet resultSet = stmt.executeQuery();
            List<Player> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(new Player(resultSet.getInt("id"), resultSet.getString("player_name")));
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public static Map<Integer, Player> playerByIdsAsMap(int minId, int maxId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT id, player_name FROM player WHERE id >= ? AND id <= ?")) {
            stmt.setInt(1, minId);
            stmt.setInt(2, maxId);
            ResultSet resultSet = stmt.executeQuery();
            Map<Integer, Player> result = new HashMap<>();
            while (resultSet.next()) {
                Player player = new Player(resultSet.getInt("id"), resultSet.getString("player_name"));
                result.put(player.id(), player);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }

    record Player(int id, String name) {
    }
}
```

You will notice that we create our collection after we query our data. That causes that we do not create a collection
when there is an error in our query. Of course, we still create one even if we have zero results, but that is something
we can ignore.

## Boolean

Booleans can be used to identify whether our query changed data or not. That means we can use it for `INSERT`, `DELETE`
and `UPDATE` queries.

```java
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReturnBoolean {
    static DataSource dataSource;

    public static boolean createPlayer() {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("""
                     INSERT INTO player(player_name) VALUES (?)
                     """)) {
            stmt.setString(1, "Lexi");
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
```

Now our method will return true when the player was created. We always know whether it failed to create or not. The same
works for delete and update, but those have some other way which might give you more insights in some situations.

## Row Count

The row count is nearly the same method as we did for the boolean. Instead of checking that our value is larger than 0
we simply return it. That way we know how many entries we updated or deleted with our query.

```java
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReturnRowCount {
    static DataSource dataSource;

    public static int deleteOldPlayers() {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("""
                     DELETE FROM player WHERE last_online < now() - '1 year'
                     """)) {
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
```

Our method will delete all players which were not online during the last year. It returns how many players were deleted
afterwards.

## Cheat Sheet

| Operation | Results | Type              |
|-----------|---------|-------------------|
| Read      | 0-1     | Optional          |
| Read      | >0      | List/Map          |
| Insert    |         | boolean           |
| Delete    |         | boolean/row count |
| Update    |         | boolean/row count |
