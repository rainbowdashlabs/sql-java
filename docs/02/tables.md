# Tables

Now that we have setup our database, learned about naming and the data types, we can finally create our first table.

Everything in our databases is stored in tables.

Creating and dropping is a very important skill. Of course there is much more, but we will take it slow for now.

# Creating a table

Creating a table is the same in all databases. Lets start with recreating the two tables we used earlier

**player**

The player table saves each player with a name and an id. We also save the last time the player was online.

| id  | player\_name | last\_online     |
|:----|:-------------|:-----------------|
| 1   | Mike         | 2022-05-11 00:00 |
| 2   | Sarah        | 2022-04-04 00:00 |
| 3   | john         | 2022-04-08 00:00 |
| 4   | Lilly        | 2022-04-01 00:00 |
| 5   | Matthias     | 2022-03-06 00:00 |
| 6   | Lenny        | 2022-03-08 00:00 |
| 7   | Summer       | 2022-05-22 00:00 |
| 8   | Marry        | 2022-06-04 00:00 |
| 9   | Milana       | 2022-02-12 00:00 |
| 10  | Lexi         | 2022-02-22 00:00 |

**friend_graph**

The friend graph is a bidirectional graph of friendships. We generally assume that if `player_1` is a friend of
`player_2`, that `player_2` is also a friend of `player_1`.

We just save the player ids here. The other information like the names are in the `player` table and we dont want to
store duplicated data.

| player_1 | player_2 |
|----------|----------|
| 1        | 2        |
| 2        | 3        |
| 4        | 3        |

Please try first to create the statments on your own based on the learnings from the datatype pages. You can refer to
the [datatype cheatsheet](../02/sql_datatypes.md). We dont care about the content at the moment.

Of course you can also use your desktop client of your choice to create the tables, but I highly recommend to learn 
the sql syntax as well, since this makes debugging a lot easier later.

The general syntax is:

<!-- @formatter:off -->
```sql
CREATE TABLE table_name
(
    col_name TYPE,
    col_name TYPE
)
```
<!-- @formatter:on --> 

<details>
<summary>Solution</summary>

To create those tables use these statements:

```sql
CREATE TABLE player
(
    id          INTEGER,
    player_name TEXT,
    last_online TIMESTAMP
);

CREATE TABLE friend_graph
(
    player_1 INTEGER,
    player_2 INTEGER
)
```

</details>

If you want to avoid conflicts you can use the `IF NOT EXISTS` keyword. This will only create the table if the name 
is not already in use.

<!-- @formatter:off -->

```sql
CREATE TABLE IF NOT EXISTS table_name
(
    col_name TYPE,
    col_name TYPE
)
```
<!-- @formatter:on --> 

# Deleting tables

Sometimes you might not need a table anymore. In this case we want to drop them.

```sql
DROP TABLE player;
```

We can use the `IF EXISTS` keyword here in order to avoid errors if the table does not exist anymore.

```sql
DROP TABLE IF EXISTS player;
```
