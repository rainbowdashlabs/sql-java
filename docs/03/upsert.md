# Upsert and Conflict Handling

Now we added a lot of ways to block input in our tables. We have primary keys, unique keys and foreign keys which
block input of invalid or duplicated data into our tables.

Failures of foreign keys are intended. Those should always be handled on the application side. However, failures on
unique keys can be handled by us. For that we use a construct which is called `UPSERT`. The name already hints that
this is a mix of `UPDATE` and `INSERT`. It basically says, insert and if something prevents me from inserting I want
to update the data.

Depending on the database you use the syntax is a bit different.

## Ignoring conflicts on input

### Postgres & SqLite

For postgres and SqLite we are going to insert Lexy once again and fail gracefully.

```postgresql
-- This is our usual insert statement
INSERT INTO player(player_name)
VALUES ('Lexy')
       -- Here we define where the conflict will appear. In this case we care about the conflict on the player name
ON CONFLICT (player_name)
    -- All we say here is that we wanna do nothing and discard the possible error.
    DO NOTHING;
```

### MariaDB & MySQL

For MariaDB and MySQL we can use the `INSERT IGNORE` syntax which will simply ignore errors which occur on an insert.

```mariadb
INSERT IGNORE INTO player(player_name)
VALUES ('Lexy');
```

## Upsert

The upsert statement is the more important part. We will use this to insert our player into our online table and 
refresh the last online time in case it exists already.

The paradigm is always: Try to insert and if I cant let me modify the conflicted row.

### Postges & SqLite 

For postgres and SqLite we are going to insert Lexy once again and this time update the online time in case a player 
with this name is already present. 

```postgresql
-- This is our usual insert statement
INSERT INTO player(player_name)
VALUES ('Lexy')
       -- Here we define where the conflict will appear. In this case we care about the conflict on the player name
ON CONFLICT (player_name)
    -- All we say here is that we wanna do nothing and discard the possible error.
    -- In SqLite we use CURRENT_TIMESTAMP here instead of now
    DO UPDATE SET last_online = NOW();
```

#### Exclude table

Let's assume we have a new row in our player table called age.

```postgresql
INSERT INTO player(player_name, age)
VALUES ('Lexy', 21)
ON CONFLICT (player_name)
    DO UPDATE SET age = 21;
```

If we wanted to upsert the age of lexy we would need to write the age twice in our query. This can get quite messy 
on larger queries. Luckily Postgres and SqLite have our back and provide a temporary table named `exclude` which holds 
the values we wanted to insert. So instead of the above we can simply write.

```postgresql
INSERT INTO player(player_name, age)
VALUES ('Lexy', 21)
ON CONFLICT (player_name)
    DO UPDATE SET age = excluded.age;
```

### MariaDB & MySQL

For MariaDB and MySQL we can use the `ON DUPLICATE KEY UPDATE` clause which allows us to change the values in the 
conflicted row.

```mariadb
INSERT INTO player(player_name)
VALUES ('Lexy')
-- We define that we want to do something if any duplicate appears on a key or unique index
ON DUPLICATE KEY UPDATE last_online = CURRENT_TIMESTAMP;
```
