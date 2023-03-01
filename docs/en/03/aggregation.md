# Aggregation

Beside storing and reading data, databases can perform numerous statistical actions on our data.

The most common operations are counting, min, max, sum and average calculation of values. that's why we will focus
on this. Your database has way more aggregates and especially postgres is very strong when it comes to aggregation.

For this section we will use our `friend_graph` again. In order to do a meaningful amount of operations on it, we will
need to add some more data to our graph.

We also will need to create some date in our `money` and `channel_subscription` table since this was probably deleted at
some point in the process.


<details>
<summary>Data creation</summary>

<details>
<summary>Postgres</summary>

```postgresql
INSERT INTO money (SELECT id, ROUND(RANDOM() * 10000) FROM player)
ON CONFLICT DO NOTHING;

INSERT INTO friend_graph
VALUES (1, 2),
       (1, 3),
       (1, 4),
       (4, 2),
       (4, 3),
       (4, 3)
ON CONFLICT DO NOTHING;

INSERT INTO channel_subscription
VALUES (1, 1),
       (1, 2),
       (2, 1),
       (2, 2),
       (2, 3),
       (3, 1)
ON CONFLICT DO NOTHING;
```

</details>


<details>
<summary>SqLite</summary>

```sqlite
INSERT INTO money
SELECT id, ROUND(RANDOM() * 10000)
FROM player
ON CONFLICT DO NOTHING;

INSERT INTO friend_graph
VALUES (1, 2),
       (1, 3),
       (1, 4),
       (4, 2),
       (4, 3),
       (4, 3)
ON CONFLICT DO NOTHING;

INSERT INTO friend_graph
VALUES (1, 1),
       (1, 2),
       (2, 1),
       (2, 2),
       (2, 3),
       (3, 1)
ON CONFLICT DO NOTHING;
```

</details>



<details>
<summary>MariaDB & MySQL</summary>

```mysql
INSERT IGNORE INTO money (SELECT id, ROUND(RAND() * 10000) FROM player);

INSERT IGNORE INTO friend_graph
VALUES (1, 2),
       (1, 3),
       (1, 4),
       (4, 2),
       (4, 3),
       (4, 3);

INSERT IGNORE INTO channel_subscription
VALUES (1, 1),
       (1, 2),
       (2, 1),
       (2, 2),
       (2, 3),
       (3, 1);
```

</details>


</details>

## Counting

Counting is one of the common cases in sql. We usually want to count entries which match a specific condition. For
example, we want to count all friends of the player with id 2. This is quite simple because all we do is call the
`count` function in a `SELECT` with a `WHERE` statement. Not much new here and you will recognize most of the stuff.

```postgresql
-- We always use an alias here since this avoids naming conflics when using the data in other queries.
-- If we would not set an alias the column would have the name of the function we call.
SELECT COUNT(1) AS friend_count
FROM friend_graph
WHERE player_id_1 = 2
   OR player_id_2 = 2;
```

We now know the amount of friends of player two, which is 3 for me with the data shown above. This works well
already, right?

But why are we using `count(1)`? The one inside our count is just a random value. It could be anything. Usually
people use a `*` there. Using the 1 has the advantage that the database directly know that we won't need any other
data beside the data we need for our `WHERE` clause. That's why I personally prefer always the number 1.

## Min, Max, Sum and Average

The `MIN`, `MAX`, `SUM` and `AVG` (average) aggregates work all the same way. Select your data and wrap the data you
want to calculate into an aggregate function.

Let's get the min value of our money table:

```postgresql
-- Again remember to use an alias.
SELECT MIN(money) AS min_money
FROM money
```

The cool stuff is that as long as we aggregate data the same rows we can use multiple aggregates the same time:

```postgresql
SELECT MIN(money) AS min_money,
       MAX(money) AS max_money,
       AVG(money) AS average_money,
       SUM(money) AS total_money
FROM money
```

Now we probably have all the information we probably need for our money table.

## Grouping

Now we probably not always want to simply count entries in a table. Grouping is essential for data operations and
aggregations. Grouping will group all entries which have the same value in a specified columns and allow aggregation
of the columns which are grouped together.

With that we can for example count how many channels a player has subscribed. For that all we need to do is `GROUP BY`
our player_id column and `COUNT` how many players are in each group. If we express this as a query it looks like this:

```postgresql
SELECT player_id, COUNT(1) AS channel_count
FROM channel_subscription
GROUP BY player_id;
```

| player\_id | channel\_count |
|:-----------|:---------------|
| 3          | 1              |
| 2          | 3              |
| 1          | 2              |

And now we can see that player 3 has subscribed to 1 channel while player 2 has subscribed to three channel. Notable
on grouping is that you can only select columns which are mentioned in your `GROUP BY` clause or inside an aggregate
function like sum and other stuff. It might not make sense, but we could for example count the sum of the channel
ids as well.

```postgresql
SELECT player_id, COUNT(1) AS channel_count, SUM(channel_id) AS channel_sum
FROM channel_subscription
GROUP BY player_id;
```

| player\_id | channel\_count | channel\_sum |
|:-----------|:---------------|:-------------|
| 3          | 1              | 1            |
| 2          | 3              | 6            |
| 1          | 2              | 3            |

If you tried to select the `channel_id` without the sum function you would get an error, because the database doesn't
know what to do with this column.

## Group by with another aggregate

When working with something like our `friend_graph` it might be hard to actually count all the friends of a player,
since the player can be in `player_id_1` or `player_id_2`. Sadly there is no good way to count them in a single
select statement. For this we will need three select statements. Two to count for each id and one more to combine
the counts of both. We will use `UNION` to combine both counts of our query, which we will wrap into a subquery and
after that we compute the sum of our two player counts. This is a more advanced topic, and we will use some
more stuff here which is not yet unknown. Maybe you will come back alter and fully understand it, or you will just use
this as a reference if you encounter the same problem again.

```postgresql
SELECT player_id_1 AS id, COUNT(1) AS friend_count
FROM friend_graph
GROUP BY player_id_1
-- UNION by default performs deduplication of entries. Since we don't want the we use ALL, which skips this step
UNION ALL
SELECT player_id_2 AS id, COUNT(1) AS friend_count
FROM friend_graph
GROUP BY player_id_2
```

Then query above gives us this table:

| id  | friend\_count |
|:----|:--------------|
| 4   | 2             |
| 2   | 1             |
| 1   | 3             |
| 3   | 2             |
| 4   | 2             |
| 2   | 2             |

All we actually need to do now is to `GROUP BY` our `id` and compute the `SUM` of our friend count.

```postgresql
SELECT id, SUM(friend_count)
-- This construct is called a subquery. Insted of directly reading a table we read the results of another query.
FROM (SELECT player_id_1 AS id, COUNT(1) AS friend_count
      FROM friend_graph
      GROUP BY player_id_1
      UNION ALL
      SELECT player_id_2 AS id, COUNT(1) AS friend_count
      FROM friend_graph
      GROUP BY player_id_2) counts -- this is an alias for our subquery.
-- We group our entries
GROUP BY id;
```

In the end we finally get our result:

| id  | sum |
|:----|:----|
| 3   | 2   |
| 4   | 4   |
| 2   | 3   |
| 1   | 3   |

These are the total counts we have. You don't understand it yet? Don't worry about it. If you need it you just come 
back again.
