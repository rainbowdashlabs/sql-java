# Data Consistency

Data consistency is key when it comes to databases. In an ideal world a database removes invalid data by itselfs and
ensures that only valid data gets inserted. Luckily all our databases give us the tools we need to acomplish those
ideal goals. They are called unique indices, constrains, foreign keys and more.

## Avoiding null values

When inserting a value into a table it can be always null in general. You can avoid this by marking a column as `NOT
NULL` when defining the table. This is especially important when you want to set default values or restrict values
on a column to unique values, because a null is always unique in SQL and column with a default value can still be null.

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

## Default Values

Default values are a pretty nice thing. They will always be used when you insert something and leave the column
empty. The value can be a static type like a number or something dynamic like the current date or time. It is a very
useful thing. There is also a special default value which is the auto increment, which we will look at in a moment.

But first we take a look at how default values work.

Remember our old player table we had? We save the online time manually when creating the player. But we can actually
assume that once we insert the player the first time it should be online as well. So lets set the `last_online` time
to the current time when we execute our insert statement.

*Remember to drop your table first*

```sql
CREATE TABLE player
(
    id          INTEGER,
    player_name TEXT NOT NULL,
    -- This will work in postgres as well. However using now() instead is recommended
    last_online TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

The `current_timestamp` will always return the current timestamp when we insert the data. In postgres you would
usually use `now()` instead.

Let's check that it actually works!

```sql
INSERT INTO player(id, player_name)
VALUES (1, 'Mike'),
       (2, 'Sarah'),
       (3, 'John'),
       (4, 'Lilly'),
       (5, 'Matthias'),
       (6, 'Lenny'),
       (7, 'Summer'),
       (8, 'Marry'),
       (9, 'Milana'),
       (10, 'Lexi')
```

Note that we only specify `id` and `player_name` this time and do not set the `last_online` column.

And now it is time to take a look at our data!

```sql
SELECT *
FROM player;
```

And we get:

| id  | player\_name | last\_online               |
|:----|:-------------|:---------------------------|
| 1   | Mike         | 2022-11-25 23:52:26.081797 |
| 2   | Sarah        | 2022-11-25 23:52:26.081797 |
| 3   | John         | 2022-11-25 23:52:26.081797 |
| 4   | Lilly        | 2022-11-25 23:52:26.081797 |
| 5   | Matthias     | 2022-11-25 23:52:26.081797 |
| 6   | Lenny        | 2022-11-25 23:52:26.081797 |
| 7   | Summer       | 2022-11-25 23:52:26.081797 |
| 8   | Marry        | 2022-11-25 23:52:26.081797 |
| 9   | Milana       | 2022-11-25 23:52:26.081797 |
| 10  | Lexi         | 2022-11-25 23:52:26.081797 |

It worked! Of course your times will be your current time. Of course, you can insert anything there like mentioned
already. It is a very nice way of ensuring that values are present every time you read from your row.

However, there is still a small flaw in our table.

Execute this.

```sql
INSERT INTO player(id, player_name, last_online)
VALUES (11, 'Jonathan', NULL);
```

Let's take a look at our new inserted player.

```sql
SELECT *
FROM player
WHERE id = 11;
```

| id  | player\_name | last\_online |
|:----|:-------------|:-------------|
| 11  | Jonathan     | null         |

As we can see last_online is null. But we want it to be always a value. That's why we whould add a `NOT NULL` here
as well.

Our table will look like this in the end.

*Remember to drop your table first*

```sql
CREATE TABLE player
(
    id          INTEGER,
    player_name TEXT      NOT NULL,
    -- This will work in postgres as well. However using now() instead is recommended
    last_online TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

Let's sum up what we have so far:

We can insert an id into our table, which can be still null, we will get to this a bit later. We also have a player
name which is not allowed to be null. When we insert a player the `last_online` column will be set by the database.

## Unique indice

Now we have another flaw in our table. We have our id which we assign to each player. This id should be unique, but
it is not how we already noticed. We can have multiple players with the same id currently.

That is where unique indices come in handy. Those ensure that all values in a column or a combination of multiple
values are unique. Lets start first with adding a unique index to ensure that our ids are unique.

```sql
CREATE UNIQUE INDEX player_id_uindex
    ON player (id);
```

The name doesn't really matter, but if you remember our naming conventions from the beginning you will probably
remember the naming scheme we use here. It is `<table>_<column>_uindex`.

Now execute this query twice:

```sql
INSERT INTO player(id, player_name)
VALUES (11, 'Jonathan');
```

It will work the first time, but it won't the second because we try to insert the id a second time.

Of course, we can also add more columns to our index, which will make a combination unique. An example would be that
a player can subscribe to a channel. But only one time. In that case the channel and player can occurr multiple
times in their column but the combination of the player and channel would have to be unique.

```sql
CREATE TABLE channel_subscription
(
    player_id  INTEGER NOT NULL,
    channel_id INTEGER NOT NULL
);

CREATE UNIQUE INDEX channel_subscription_player_id_channel_id_uindex
    ON channel_subscription (player_id, channel_id);
```

## Auto increment

## Sorted indice

## Conditional indice

## Foreign keys
