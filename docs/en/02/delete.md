# Delete

After inserting all our data we only miss one basic functionality.
Sometimes updating or inserting data is not enough. We also need to delete data.

For this we use the `DELETE` keyword. The `DELETE` keyword is pretty similar to the [`SELECT` statement](dev/private/java/!tutorial/basicsql-pages/docs/en/02/select.md) we
already know.
We need to define `FROM` which table we want to delete and `WHERE` we want to delete those rows

With this in mind we can probably assume that the general `DELETE` statement would look like this:

```sql
DELETE
FROM table_name
WHERE column_1 = value_1; 
```

Let's try to write a statement which would delete the user with id 10 from our users table:



<details>
<summary>Solution</summary>

```sql
DELETE
FROM player
WHERE id = 10;
```

</details>

Now we just have one more problem...
Out friend graph and money table still contain references to the player with id 10.
To change this we need to delete those entries from these two tables as well.

Write two statements.

1. Delete all entries from friend_graph **where** the player 1 **or** player 2 has the id 10. (Do it in one statement.)
2. Delete the entry of player 10 from the `money` table (If you haven't created the money table, you can simply ignore it.)

<details>
<summary>Solution</summary>

   ```sql
DELETE
FROM friend_graph
WHERE player_1 = 10
   OR player_2 = 10;

DELETE
FROM money
WHERE id = 10;
```

</details>
   
Of course there is a better and more save solution to avoid "dead" entries in other tables.
We will learn this in another chapter.

Now that we know the statements for `SELECT`, `INSERT`, `UPDATE` and `DELETE` we are ready for the next chapter.
