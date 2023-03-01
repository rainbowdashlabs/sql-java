# Unique indices

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
a player can subscribe to a channel. But only one time. In that case the channel and player can occur multiple
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

Let's test this as well to see how it works.

```sql
INSERT INTO channel_subscription
VALUES (1, 1);
INSERT INTO channel_subscription
VALUES (1, 2);
INSERT INTO channel_subscription
VALUES (2, 1);
```

All of these work. Player 1 is now subscribed to channel 1 and 2 while player 2 is subscribed to channel 1. If any
of those players now tries to subscribe a channel again it will fail.

```sql
INSERT INTO channel_subscription
VALUES (1, 1);
```
