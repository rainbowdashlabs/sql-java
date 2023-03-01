# Primary Keys

Primary keys are a special type of unique index.
We usually define them when creating our table, by marking a column as primary key.
Not all tables will need or be able to use a primary key.
In general the difference between a unique index and a primary key is very small.
The only difference is that the primary key can be used for a special join called `NATURAL JOIN`.
We will look at joins later, so don't think about it yet.

Another difference is that a primary key is always marked as `NOT NULL` and a table can only have one primary key, but multiple unique indices.

In order to set our primary key, all we need to do is mark our column as the primary key by adding `PRIMARY KEY` after the type.

```sql
CREATE TABLE player
(
    id          INTEGER PRIMARY KEY,
    player_name TEXT      NOT NULL,
    last_online TIMESTAMP NOT NULL DEFAULT NOW()
);
```

Sadly this simple way only works with a single column as a primary key.
If we want to have multiple columns as our primary key it will be a bit more complex when defining our query.
Let's take a look at our `channel_subscription` table from earlier.
We had a unique index on `player_id` and `channel_id` and since both values are marked as not null and the combination should be unique they are prefect for a primary key.

```sql
CREATE TABLE channel_subscription
(
    player_id  INTEGER,
    channel_id INTEGER,
    CONSTRAINT channel_subscription_pk
        PRIMARY KEY (player_id, channel_id)
);
```

Not that we can now safely remove the `NOT NULL` restriction since those are enforced by the primary key already.
And that's it.
Not that much more complex, but a bit more work to write.
Also take care about the correct naming of the key in order to identify it later.
