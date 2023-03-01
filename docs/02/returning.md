# Returning changed data

**Applies to Postgres, SqLite and MariaDB**

When changing data via update or delete statements or inserting them, we can return the changed or inserted data.

For updates that means we get the now set values, which might not be helpful now, but there are mechanics in databases
which change data between update and write. For deletes it will give us the deleted entries, which are no longer present
in the database. This might be more helpful. Where this really comes to shine is for inserts. In the next chapter you
will learn how to automatically create ids or define default values for columns. Those are of course not set by you when
you insert data, but you can still get them via the `RETURNING` clause.

Using this feature is quite simple. When executing a `DELETE`, `UPDATE` or `INSERT` simply add a `RETURNING` after it
and list the columns you want to have returned.

```mariadb
DELETE
FROM player
WHERE id = 10
RETURNING id, player_name;
```

**Note:** Not all database clients understand that this query returns data. They will mostly not show any data here, but be sure that java can with the correct code.
