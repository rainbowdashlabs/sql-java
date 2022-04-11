# Update

Now we can insert and read data. But what if we want to update already inserted data?

Thats where the `UPDATE` statements comes to use. To update data we will also need to use the `WHERE` statement
introduced in the earlier [chapter](../02/select.md#where).

Remember our already pretty known table of players.

| id  | player\_name | last\_online               |
|:----|:-------------|:---------------------------|
| 1   | Mike         | 2022-05-11 00:00:00.000000 |
| 2   | Sarah        | 2022-04-04 00:00:00.000000 |
| 3   | John         | 2022-04-08 00:00:00.000000 |
| 4   | Lilly        | 2022-04-01 00:00:00.000000 |
| 5   | Matthias     | 2022-03-06 00:00:00.000000 |
| 6   | Lenny        | 2022-03-08 00:00:00.000000 |
| 7   | Summer       | 2022-05-22 00:00:00.000000 |
| 8   | Marry        | 2022-06-04 00:00:00.000000 |
| 9   | Milana       | 2022-02-12 00:00:00.000000 |
| 10  | Lexi         | 2022-02-22 00:00:00.000000 |

We are currently saving the last time the player has logged in in the `last_online` column.

When Lexy is online again we might need to update the `last_online` value for her again. We have now two options. The
first one is deleting the entry and inserting a new one. This is very dirty and also not a good practice. Thats why we
use the `UPDATE` statement and define in the `WHERE` clause where we want to update and what we want to update.

The general syntax is:

```sql
UPDATE player
SET column_x = value_x,
    column_y = value_y
WHERE condition
```

## Basic Update

With this knowledge, let's try to update the `last_login` time of Lexy based on her id `10` to the current time.

We gonna need a built in function again to retrieve the current time.

- SqLite: `CAST(STRFTIME('%s', 'NOW') AS INTEGER)` we dont have timestamps. We use the current time as unix timestamp.
- MariaDB/MySQL: `current_timestamp()` returns the current timestamp. You can also use just `current_timestamp` which is
  a constant for the current transaction.
- PostgreSQL: `now()` returns the current timestamp

<details>
<summary>Solution</summary>

**SqLite**

```sql
UPDATE player
SET last_online = CAST(strftime('%s', 'NOW') AS INTEGER)
WHERE id = 10;
```

**MariaDB/MySQL/PostgreSQL**

```sql
UPDATE player
SET last_online = CURRENT_TIMESTAMP
WHERE id = 10;
```

**PostgreSQL**

```sql
UPDATE player
SET last_online = NOW()
WHERE id = 10;
```

</details>

If you now select the entry of Lexy with:

```sql
SELECT id,
       player_name,
       last_online
FROM player
WHERE id = 10
```

You should get a timestamp with the current time in her `last_online` column.

## Update with current value

Of course we can also use the current value of the column we want to update.

Remember our money table we created in the [insert chapter](../02/insert.md#create-tables-with-content)? We need this
now again.

So lets say we want to take 600 of our currency from lexy, but only if she has at least 600.

The syntax for this is quite the same then the one earlier. We just reference the column value itself

```sql
UPDATE player
SET column_x = value_x,
    column_y = value_y + column_y
WHERE condition
```

Try to remove the money and adjust the condition with the check for the money

```sql
UPDATE money
SET money = money - 600
WHERE id = 10
  AND money >= 600
```

Lets check what changed:

```sql
SELECT id, money
FROM money
WHERE id = 10
```

| id  | money |
|:----|:------|
| 10  | 400   |

We can now see that Lexy has only 400 of our currency. 600 less than initally. If we execute our update again this 
value will still be the same. This mechanic can be really usefull if you want to be sure that the player really has 
the amount of money and directly withdraw the money.
