# Reading multiple entries from an ResultSet

This time we will use a query you should know already. It is the query we used in chapter 2 to select all players with
an id higher than 5. But this time we will make it a bit more configurable with the use of our `PreparedStatement`.

```sql
SELECT
	id,
	player_name
FROM
	player
WHERE id >= ?
  AND id <= ?
```

We set the first value to 5 and the second value to 10. That will give us a `ResultSet` which looks like this:

```      
        Row | id  | player_name
Cursor -> 0 |     |     
          1 | 5   | Matthias    
          2 | 6   | Lenny       
          3 | 7   | Summer      
          4 | 8   | Marry       
          5 | 9   | Milana      
          6 | 10  | Lexi        
```

Instead of checking whether there is a next row and read this we want to read as long as there is a next row. We can not
do this with an `if`, but we can do this with a `while`.

```java
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SelectMulti {
    static DataSource dataSource;

    public static void main(String[] args) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("""
                     SELECT id, player_name
                     FROM player
                     WHERE id >= ?
                       AND id <= ?
                     """)) {
            stmt.setInt(1, 5);
            stmt.setInt(2, 10);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("id"));
                System.out.println(resultSet.getString("player_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

The while will jump to the next row as long as there is one. It will also not fail if there is no row at all.

Of course instead of printing out your result you might want to create a list or a map to store them. We will look into
returning data in a later section.

