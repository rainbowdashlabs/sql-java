# Prepared Statement

You may have noticed that we did not use a prepared statement in the previous section. That was the first and last time
that you have seen a `Statement` in this tutorial. From now on we will always use prepared statements. Why? Because it
has only advantages using a `PreparedStatement` instead of a `Statement`

1. They prevent [SQL injection](https://www.w3schools.com/sql/sql_injection.asp)
2. They allow batch execution of queries
3. Code with parameters is easier to ready
4. They are precompiled and allow caching on the database side.
5. Speeds up communication through a non-SQL binary protocol

Read a more detailed explanation at [Baeldung](https://www.baeldung.com/java-statement-preparedstatement)

A `PreparedStatement` is retrieved from the `Connection` the same way as a `Statement`.

```java
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PreparedStatementUsage {
    static DataSource dataSource;

    public static void main(String[] args) {
        try /*(1)*/(Connection conn = dataSource.getConnection(); //(2)
             PreparedStatement stmt = /*(3)*/ conn.prepareStatement( 
                     /*(4)*/ "SELECT player_name FROM player WHERE id = ?")) {
            stmt.setInt(1, 10); //(5)
            ResultSet resultSet = stmt.executeQuery(); //(6)
            // here comes more
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

1. Declare our try with resources block
2. Retrieve a connection from our datasource
3. Create a new prepared statement
4. Define our query with a placeholder -> `id = ?`
5. Set the first parameter in our query to the value 10
6. Execute the query


Let's go through it step by step.

1. Declare our try with resources block
2. Retrieve a connection from our datasource
3. Create a new prepared statement
4. Define our query with a placeholder -> `id = ?`
5. Set the first parameter in our query to the value 10
6. Execute the query

Values in a prepared statement start with index 1. You will not have to set them in the correct order, but you have to
set them all.
