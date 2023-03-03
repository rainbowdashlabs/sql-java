# Sorted Indices

In general indices are always sorted, because that's how they work.
We can use this to speed up searches where we want to sort a table based on a value.
With creating an index on the column we want to sort by we can directly skip the sort, because the database will just look at the already sorted index.

We most probably want to sort our users by the amount of money we have!

*Remember to insert some data into your money table first c: You might need to add some more if you want the index to be used.*

<details>
<summary>Data generation for postgres</summary>

```sql
-- clear table
DELETE
FROM money;

-- We need to generate some more values to force the index usage
INSERT INTO player(player_name) (SELECT 'player name' FROM GENERATE_SERIES(1, 1500));

-- Generate some random money values
INSERT INTO money (SELECT id, ROUND(RANDOM() * 10000) FROM player);
```

</details>

```sql
SELECT player_id, money
FROM money
ORDER BY money DESC
LIMIT 5;
```

| player\_id | money |
|:-----------|:------|
| 1406       | 9994  |
| 1358       | 9993  |
| 1430       | 9989  |
| 178        | 9985  |
| 1113       | 9977  |

If you want you can check the query plan now as we did in the previous [section](../query_planer.md). 
You will see that we currently use no index at all.
This will of course change once we add an index on the money column.

```sql
-- we use CREATE INDEX instead of CREATE UNIQUE INDEX this time
CREATE INDEX money_money_index
    -- We also define that we want to sort the index in an descending order. Ascending is the default.
    ON money (money DESC);
```

Check the query plan for this query now.

```sql
SELECT player_id, money
FROM money
ORDER BY money DESC
LIMIT 5;
```

Now you will see that we indeed use an index scan.
Also, if we only order by one value we are even using the index when we order the other direction.

```sql
SELECT player_id, money
FROM money
ORDER BY money
LIMIT 5;
```

This query will use the index in a reversed order.
Of course this won't work anymore when we order by multiple values.
If you sort by multiple values you will need to add an index matching the same order of columns and sort direction as well.
