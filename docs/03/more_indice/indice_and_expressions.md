# Indice and expressions

In the previous section we already saw that we can use expressions for our queries. Doing this we can fix a lot of
stuff in our tables now with some more concrete examples!

First we need to clean up our player table in order to proceed.
Either delete all dummy entries we created in the last sections with the query below or recreate it from scratch.

```sql
DELETE
FROM player
WHERE player_name = 'player name';
```

## Creating a unique case insensitive index

First task will be to create a case-insensitive index for our player name. We want to avoid that multiple players
have the same name, and therefore we need a check which ensures that we don't insert the name twice. We also want to
ensure that the name does not exist with another casing.

To do this we will once again use an expression in our index. The query will also be unique this time. One pitfall
here is that not all databases work the same here. MariaDB does not support expressions directly, but only via
generated columns, while MySQL wants us to use a substring or cast our value. PostgreSQL and SqLite accept the most
simple approach.

### PostgreSQL, SqLite

All we do here is creating a new index which contains the lower case version of our player name.

```postgresql
CREATE UNIQUE INDEX player_name_uindex 
    ON player ((LOWER(player_name)))
```

### MySQL

MySQL requires us to use the substring call substring or cast the value returned from lower. We will just index the
first 50 characters of our name. This should suffice for the length. If you want to be safe, you can add a
[check](../data_consistency/checks.md) to ensure that names are no longer than 50 characters.

```mysql
CREATE UNIQUE INDEX player_name_uindex 
    ON player ((SUBSTRING(LOWER(player_name), 1, 50)));
```

### MariaDB

Sadly MariaDB does not directly support expressions in indices. Instead, we create a generated column and use the
value of this column to create our index. This is probably the most complex way possible.

For this we will use the alter table command which we didn't look into yet, but it should be quite clear what it does.

```mariadb
ALTER TABLE player
    ADD COLUMN name_lower TEXT GENERATED ALWAYS AS (LOWER(player_name)) STORED;

CREATE UNIQUE INDEX player_name_uindex 
    ON player (name_lower)
```

### Testing

Let's see how and if this actually works. We are going to try to insert Lexy once again into our table. Ideally this
would fail since we already have a player named Lexy.

```postgresql
INSERT INTO player(player_name)
VALUES ('LEXY');
INSERT INTO player(player_name)
VALUES ('lexy');
```

You will also see that both queries fail regardless of the casing. That's the result of our case-insensitive query.

## Creating an index of a bidirectional relation

We can also use another index expression to ensure that friend relations are in both direction and can not exist
one time for each direction.

This time we will use XOR in order to create a key for our friend relations.

**Postgres**

```postgresql
CREATE UNIQUE INDEX friend_graph_relation_uindex 
    ON friend_graph ((player_id_1 # player_id_2));
```

**MySQL & MariaDB**

```mysql
CREATE UNIQUE INDEX friend_graph_relation_uindex
    ON friend_graph ((player_id_1 ^ player_id_2));
```

**SqLite**

SqLite does not have a XOR operator, but we can "easily" build our own XOR.

```sqlite
CREATE UNIQUE INDEX friend_graph_relation_uindex 
    ON friend_graph (((player_id_1 | player_id_2) - (player_id_1 & player_id_2)));
```

Time to test if our indices actually work!

We are going to clear the table first in order to not accidently insert conflicting entries.

```postgresql
DELETE FROM friend_graph;
-- Success
INSERT INTO friend_graph VALUES (1,2);
-- Failure
INSERT INTO friend_graph VALUES (2,1);
```

If everything work as expected we can insert the first one, but not the second one since the combined value of the 
two ids are the same because of the XOR. Now we have ensured that a friend connection exists only on one direction 
and now in both.
