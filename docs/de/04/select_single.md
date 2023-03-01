# Reading an entry from a ResultSet

Let's take a look at our result set.
If we execute the previous query: `SELECT player_name FROM player WHERE id = ?` it would probably look something like this.

```
        Row | player_name
Cursor -> 0 |
          1 | 'Lexi'
```

You can see that we have a `Cursor`.
This cursor is pointing at row 0 or more specifically at the row before the first row.
That means that if we would read something from the `ResultSet` now, we would get an error because there is simply not data to read.
In order to check if there is another row in our `ResultSet` we can use `ResultSet#next`.
This method does three things:

1. Checks whether there is another row it could move to.
2. Moves to the next row if there is one
3. Returns a boolean which is true when there was a new row.

Let's assume we have called `ResultSet#next`. Now our `ResultSet` would look like this:

```
        Row | player_name
          0 |
Cursor -> 1 | 'Lexi'
```

You can see that the cursor moved one row further, and we are now on a row with data.
That also means that we are now able to read something from our current row.
We do that with the methods of `ResultSet` which start with `get`.
In our case we want to read a `String`, that means we will use the `ResultSet#getString()` method.
You will notice that there are two versions of this method.
This is because we can define the column we want to select in two ways.
The first version would be by its name `resultSet.getString("player_name")`.
This version is generally preferred since it will not break if you change the select statement and is more readable.
The second version would be the column id.
Those ids start at 1 again.
That means that we would need to select the first column in our case `resultSet.getString(1)`.

Now it is time to translate all this into some fine code:

```java
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReadResultSet {
    static DataSource dataSource;

    public static void main(String[] args) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT player_name, id FROM player WHERE id = ?")) {
            stmt.setInt(1, 10);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                System.out.println(resultSet.getString("player_name"));
                System.out.println(resultSet.getString(1));
            } else {
                System.out.println("Not entry returned");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

You can see that we use `if` to determine whether there is another row or not.
This is important, because we will get a nasty error if we try to read a row when there is none.
After we used `next()` to confirm that there is another row we attempt to read the value of `player_name` and the first column.
Both are the same of course.

If you had selected multiple columns, you could of course read all those as well.

## Closing a ResultSet

If you take a close look at the `ResultSet`, you will notice that it is an `AutoClosable` as well.
You might question: "Does it mean I have to close it as well?".
The answer to that is "No".
But this is a huge exception in general.
A `ResultSet` is bound to the `Statement` or `PreparedStatement` it was retrieved from.
That means that once the statement is closed the connected `ResultSet` will be closed as well.
But that also means that returning the ResultSet would not work since it is already closed after we returned it.
You always need to read the values inside the `try` block.
