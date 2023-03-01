# Foreign Keys

We already know about [primary keys](primary_keys.md) but there is at least one more important key type. This is
called the foreign key. All it does is checking if a value in a column is present in a column of another table.
Sounds complicated? Trust me it isn't!

Do you remember our money problem back in chapter 1 where we deleted a player, but his money was still listed in the
money table? We needed to delete it manually back then. Foreign keys allow us to do something cooler instead.

Let's redefine our money table and add a foreign key this time on our new nice player table with the auto increment
and all the other cool stuff.

Of course, we will use all our previous knowledge here as well. Let's sum up quickly what we want to achieve:

- Every player should only appear once -> Primary key on player_id
- The money should be 0 initially -> Default to 0
- Only players which are present in our player table should have a money amount listed -> Foreign key from money.
  player_id to player.id
- When a player gets deleted we also want to delete the money entry -> On delete cascade the deletion on other tables.

Let's get into action!

```sql
CREATE TABLE money
(
    -- We name id player_id because it references the column id in the player table.
    -- Since this is the primary key we do not need to set it NOT NULL
    player_id INT PRIMARY KEY,
    -- We define our money column and set the default value to 0.
    money     DECIMAL DEFAULT 0 NOT NULL,
    -- We define our foreign key name. The naming schema is simply 
    -- <curr_table>_<target_table>_<curr_col>_<target_col>_fk
    -- Again names do not matter, but need to be unique, so it is a common practice to name them like this.
    CONSTRAINT money_player_player_id_id_fk
        -- We define a foreign key on our player_id column and bin it to the id column of the player table.
        FOREIGN KEY (player_id) REFERENCES player (id)
            -- In case the id is deleted in our player table we want to delete
            ON DELETE CASCADE
);
```

And that's already it. Time to play around. We currently have a bunch of players in our player table already:

| id  | player\_name | last\_online               |
|:----|:-------------|:---------------------------|
| 1   | Mike         | 2022-11-26 12:32:39.021491 |
| 2   | Sarah        | 2022-11-26 12:32:39.021491 |
| 3   | John         | 2022-11-26 12:32:39.021491 |
| 4   | Lilly        | 2022-11-26 12:32:39.021491 |
| 5   | Matthias     | 2022-11-26 12:32:39.021491 |
| 6   | Lenny        | 2022-11-26 12:32:39.021491 |
| 7   | Summer       | 2022-11-26 12:32:39.021491 |
| 8   | Marry        | 2022-11-26 12:32:39.021491 |
| 9   | Milana       | 2022-11-26 12:32:39.021491 |
| 10  | Lexi         | 2022-11-26 12:32:39.021491 |

**Validate that we can not insert unknown players**

```sql
INSERT INTO money(player_id)
VALUES (11);
```

This fails because we have no player in our player table with id 11. The foreign key prevents us from inserting
unknown players.

**Validate that we can insert known players**

```sql
INSERT INTO money(player_id)
VALUES (10);

SELECT player_id, money
FROM money
WHERE player_id = 10;
```

| player\_id | money |
|:-----------|:------|
| 10         | 0     |

Seems like it worked! We have added a player with id 10 which is the id of Lexi and the money was set to 0
automatically.

**Validate that money entry gets deleted when we delete a player**

```sql
DELETE
FROM player
WHERE id = 10;

SELECT player_id, money
FROM money
WHERE player_id = 10;
```

Now we get nothing when reading the money table. That's great! Lexis money was deleted the moment we deleted the
entry from the player table.

## More complex foreign keys

Now we have a good understanding of a simple foreign key on a single column, but we have a more complex task to
solve. We still have our friend graph, which can still contain friendship connections of non-existent players. That
is a problem we want to solve now, and it will be a bit more complex.

Our last table was quite simple for every entry in the player table we only could have one entry in our money table.
But the friend_graph contains multiple entries for a single player and even in two columns and not only in one!

Just in case that you no longer know how our table looks like:

| player\_1 | player\_2 |
|:----------|:----------|
| 1         | 2         |
| 2         | 3         |
| 4         | 3         |
| 5         | 3         |
| 7         | 2         |
| 6         | 1         |
| 6         | 2         |
| 1         | 10        |
| 4         | 10        |

We can't use a primary key for player 1 OR 2, but we still can use a primary key for player 1 AND 2.

```postgresql
CREATE TABLE friend_graph
(
    player_id_1 INT,
    player_id_2 INT,
    -- We define our primary key
    CONSTRAINT friend_graph_pk
        PRIMARY KEY (player_id_1, player_id_2),
    -- We define our reference again and define the delete
    CONSTRAINT friend_graph_player_player_id_1_id_fk
        FOREIGN KEY (player_id_1) REFERENCES player (id)
            ON DELETE CASCADE,
    -- We define our reference again and define the delete
    CONSTRAINT friend_graph_player_player_id_2_id_fk
        FOREIGN KEY (player_id_2) REFERENCES player (id)
            ON DELETE CASCADE
);
```

And that is it already. Still pretty simple. Instead of creating a foreign key on a single column we just create two
for each column.

Feel free to validate that it works with similar tests like we used above!

We still have an issue here in terms of consistency. We can have a duplicate entry here since player 1 can be
a friend with player 2 and player 2 can be a friend with player 1. That is not prevented by the primary key.

| player\_1 | player\_2 |
|:----------|:----------|
| 1         | 2         |
| 2         | 1         |

There are multiple ways to solve this issue. We could either always insert the lower id into player_1 and the higher
into player_2 or use an XOR on both ids in order to create a unique key for the friendship. For now, we won't bother
with it, because we lack the knowledge for both.
