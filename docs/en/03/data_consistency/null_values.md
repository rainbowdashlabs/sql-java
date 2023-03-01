# Avoiding null values

When inserting a value into a table it can be always null in general.
You can avoid this by marking a column as `NOT NULL` when defining the table.
This is especially important when you want to set default values or restrict values on a column to unique values, because a null is always unique in SQL and column with a default value can still be null.

In order to mark a column as not nullable all we do is add `NOT NULL` after our type.

*Remember to drop your table first*

```sql
CREATE TABLE player
(
    id          INTEGER,
    player_name TEXT NOT NULL,
    last_online TIMESTAMP
);
```

When we now try to insert a `NULL` value into our `player_name` column we would get an error now.

```sql
INSERT INTO player(id, player_name, last_online)
VALUES (1, NULL, NULL)
```

When we insert this, we will get a nice error telling us that the `player_name` can not be null!
