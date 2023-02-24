# Joins

Especially after hearing about normalization and splitting data into different tables you might ask now how we are 
connecting this data when we need data from multiple tables at the same time.

Joins allow us to connect tables based on keys in the table. That's another reason why it is important to have unique 
identifiers for entity. Without those joining tables is quite a pain.

For joins we differ between `LEFT JOIN` (which is the same as `JOIN` and `RIGHT JOIN` just inversed) 
and `INNER JOIN`. Some more special joins are `CROSS JOIN` and `FULL OUTER JOIN` (This one is not supported by all 
databases. We won't cover it here).

## Left Join

The left join is the most common join. It is in general the join you will use the most and will suffice a lot of use 
cases. What it does is basically adding the right table on the left table.

```postgresql
-- We use a normal select statement
SELECT *
-- We select our first table
FROM left_table l
    -- We declare a left join and add our right table 
LEFT JOIN right_table r 
    -- We define which values are used to define our join key.
    ON l.id = r.id
```

Fine. That's quite abstract... Let's see how this looks with our data. For now we are going to add the money next to 
our player names, because ids are quite hard to read.

```postgresql
SELECT player_name, money
FROM player p 
LEFT JOIN money m ON p.id = m.player_id
ORDER BY p.id
```

| player\_name | money |
|:-------------|:------|
| Lexy         | 4117  |
| John         | 7795  |
| Milana       | 9843  |
| Mike         | 4570  |
| Lenny        | 984   |
| Marry        | 2570  |
| Summer       | 1858  |
| Lilly        | 3602  |
| Lexi         | 6057  |
| Matthias     | 6244  |
| Sarah        | 268   |

You see that we now have our money values nicely next to our player names. Of course, you could also add the id next 
to it as well if you need it.

The right join is basically the same. The only difference is how the database will handle values which are not 
present in one of the tables. This gets quite clear when we join the channel table instead.

```postgresql
SELECT player_name, channel_id
FROM player
LEFT JOIN channel_subscription cs ON player.id = cs.player_id;
```

| player\_name | channel\_id |
|:-------------|:------------|
| Mike         | 1           |
| Mike         | 2           |
| Sarah        | 1           |
| Sarah        | 2           |
| Sarah        | 3           |
| John         | 1           |
| Lexi         | null        |
| Matthias     | null        |
| Marry        | null        |
| Lenny        | null        |
| Lilly        | null        |
| Lexy         | null        |
| Milana       | null        |
| Summer       | null        |

You can see here that the values for players which have no entry in the `channel_subcription` table are simply `null`.

If we join the other way around:

```postgresql
SELECT player_name, channel_id
FROM player
RIGHT JOIN channel_subscription cs ON player.id = cs.player_id;
```

| player\_name | channel\_id |
|:-------------|:------------|
| Mike         | 1           |
| Mike         | 2           |
| Sarah        | 1           |
| Sarah        | 2           |
| Sarah        | 3           |
| John         | 1           |

Only entries present in the `channel_subscription` are now shown since this is our reference table.

## Inner Join

The `INNER JOIN` is exactly what you think it is. It will join all data where the keys are present in both tables. 
This is even more restrictive than the `LEFT JOIN` and `RIGHT JOIN`. When using the `INNER JOIN` we don't have to 
care about missing values in our table, but we might not get all the data of our first table.

```postgresql
SELECT player_name, channel_id
FROM player
INNER JOIN channel_subscription cs ON player.id = cs.player_id;
```

| player\_name | channel\_id |
|:-------------|:------------|
| Mike         | 1           |
| Mike         | 2           |
| Sarah        | 1           |
| Sarah        | 2           |
| Sarah        | 3           |
| John         | 1           |


The `INNER JOIN` here is basically the same as our `RIGHT JOIN` above. But instead of chosing the table with the 
more sparse data we just use the smalles subset of our tables.
