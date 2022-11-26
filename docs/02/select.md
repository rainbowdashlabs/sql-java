# Select

The `SELECT` statement is another very important statement. Now that we have inserted data in our tables we want to read
them as well.

The `SELECT` statement always returns a so called result set. This set contains all our requested data. Notice that 
altough that it is called a "set", this doesnt man that the results are unique in any way.

## Basic select

The pure basic select statement is quite simple:

```sql
SELECT id,
       player_name
FROM player;
```

This statement will give you the content of `id` and `name` of the table `player`.

| id  | player\_name |
|:----|:-------------|
| 1   | Mike         |
| 2   | Sarah        |
| 3   | John         |
| 4   | Lilly        |
| 5   | Matthias     |
| 6   | Lenny        |
| 7   | Summer       |
| 8   | Marry        |
| 9   | Milana       |
| 10  | Lexi         |

If you ever see or write a statement like this you might select way more than you need. Usage of plain select statement
can be generally discourraged. In 99% of these cases there are better and more refined statements.

> **Read what you need**

This is in general always a good thing to remember. Never read more data from the database than you need. The following
keywords will help doing this.

## Column Alias

Using the `as` keyword we can rename columns when we select them

```sql
SELECT id,
       player_name as name
FROM player;
```

Returns:

| id  | name     |
|:----|:---------|
| 1   | Mike     |
| 2   | Sarah    |
| 4   | Lilly    |
| 5   | Matthias |
| 6   | Lenny    |
| 7   | Summer   |
| 8   | Marry    |
| 9   | Milana   |
| 10  | Lexi     |
| 3   | John     |

The column `player_name` is now names `name` in our result set

## WHERE

The where statement might be the most important keyword in sql. The where keyword evaluates to a boolean for each row
and defines whether a row gets returned or not.

Remember the [logical operators](../02/operators.md#logical) earlier in the chapter.

```sql
SELECT column_x,
       column_y
FROM my_table
WHERE column_z = value_a; --(1)
```

1. The condition can be anything. It can be also multiple conditions or checks combined with `AND` or `OR`. The column
   you check does not need to be contained in the select statement itself.

Try to select all players with an id greater than 5.

In the end you should have a table like this:

| id  | player\_name | last\_online               |
|:----|:-------------|:---------------------------|
| 6   | Lenny        | 2022-03-08 00:00:00.000000 |
| 7   | Summer       | 2022-05-22 00:00:00.000000 |
| 8   | Marry        | 2022-06-04 00:00:00.000000 |
| 9   | Milana       | 2022-02-12 00:00:00.000000 |
| 10  | Lexi         | 2022-02-22 00:00:00.000000 |

<details>
<summary>Solution</summary>

```sql
SELECT id,
       player_name,
       last_online
FROM player
WHERE id > 5;
```

</details>

All we need here is a simple check on the id column.

We can also get more specific here. Lets add one more check for all names which start with the letter `m`. (case
insensitive)

| id  | player\_name | last\_online               |
|:----|:-------------|:---------------------------|
| 8   | Marry        | 2022-06-04 00:00:00.000000 |
| 9   | Milana       | 2022-02-12 00:00:00.000000 |

<details>
<summary>Solution</summary>

**MySQL, MariaDB, SqLite**

```sql
SELECT id,
       player_name,
       last_online
FROM player
WHERE id > 5
  AND player_name LIKE 'm%';
```

The `LIKE` keyword compares case insensitive. the `%` is a wildcard for an infinit amount of other characters.

**PostgreSQL**

```sql
SELECT id,
       player_name,
       last_online
FROM player
WHERE id > 5
  AND player_name ILIKE 'm%';
```

Unlike the other databases the `LIKE` is case sensitive in Postgres. Thats why we need to use `ILIKE` here.


</details>

You can continue chaining more conditions or try an `OR` instead of the `AND` and look how the output changes.

## ORDER BY

Currently, our players are retrieved in the order the were inserted into the database. This might not only be the order
we want.

Lets say we want our players in the order the were last seen with the most recent players first and the inactive players
last. We can do this by using the `ORDER BY` keyword on the `last_online` column.

The general syntax is:

```sql
SELECT column_x,
       column_y
FROM my_table
ORDER BY column_x [DESC|ASC]; --(1)
```

1. The ASC (ascending) or DESC (descending) clause defines the sorting order. ASC is default.

Try to define a query to sort the player table by `last_online` in descending order.

We want this table in the end:

| id  | player\_name | last\_online               |
|:----|:-------------|:---------------------------|
| 8   | Marry        | 2022-06-04 00:00:00.000000 |
| 7   | Summer       | 2022-05-22 00:00:00.000000 |
| 1   | Mike         | 2022-05-11 00:00:00.000000 |
| 3   | John         | 2022-04-08 00:00:00.000000 |
| 2   | Sarah        | 2022-04-04 00:00:00.000000 |
| 4   | Lilly        | 2022-04-01 00:00:00.000000 |
| 6   | Lenny        | 2022-03-08 00:00:00.000000 |
| 5   | Matthias     | 2022-03-06 00:00:00.000000 |
| 10  | Lexi         | 2022-02-22 00:00:00.000000 |
| 9   | Milana       | 2022-02-12 00:00:00.000000 |

<details>
<summary>Solution</summary>

```sql
SELECT id,
       player_name,
       last_online
FROM player
ORDER BY last_online DESC;
```

</details>


The `ORDER BY` clause defines the column we want to sort by. The `DESC` keyword tells SQL that we want a descending
order. If we not define an order the elements will always be in ascending order.

We can also sort by multiple columns. In this case they will be first sorted by the first column and every value with an
equal value will be sorted by the second value.

Let's sort by the first char of the name and id as a second example.

To get the first character of a string we need to use a functions again.

`substr(player_name, 1, 1)` returns the first character of the `player_name`.

In the end we want a table like this:

| id  | player\_name | last\_online               |
|:----|:-------------|:---------------------------|
| 3   | John         | 2022-04-08 00:00:00.000000 |
| 4   | Lilly        | 2022-04-01 00:00:00.000000 |
| 6   | Lenny        | 2022-03-08 00:00:00.000000 |
| 10  | Lexi         | 2022-02-22 00:00:00.000000 |
| 1   | Mike         | 2022-05-11 00:00:00.000000 |
| 5   | Matthias     | 2022-03-06 00:00:00.000000 |
| 8   | Marry        | 2022-06-04 00:00:00.000000 |
| 9   | Milana       | 2022-02-12 00:00:00.000000 |
| 2   | Sarah        | 2022-04-04 00:00:00.000000 |
| 7   | Summer       | 2022-05-22 00:00:00.000000 |

<details>
<summary>Solution</summary>

```sql
SELECT id,
       player_name,
       last_online
FROM player
ORDER BY SUBSTR(player_name, 1, 1), id;
```

</details>

You can see that we first sort by the first character of the name and all names with the same first character are sorted
by id.

## LIMIT

The `LIMIT` keyword is also really important to avoid overly large reads.

The general syntax is:

```sql
SELECT column_x,
       column_y
FROM my_table
LIMIT [n |ALL];
```

The `LIMIT` keyword restricts the amount of your results to the entered number `n`. As an alternativ you can use
`ALL` or `NULL` to disable the parameter. Thats something frameworks use sometimes to disable a limit.

Earlier we already sorted the players by the most recent online times. Lets try to only get the last 5 most recent
players by adding the limit clause to our previous query.

*Hint: The limit is always the last parameter of your query.*

| id  | player\_name | last\_online               |
|:----|:-------------|:---------------------------|
| 8   | Marry        | 2022-06-04 00:00:00.000000 |
| 7   | Summer       | 2022-05-22 00:00:00.000000 |
| 1   | Mike         | 2022-05-11 00:00:00.000000 |
| 3   | John         | 2022-04-08 00:00:00.000000 |
| 2   | Sarah        | 2022-04-04 00:00:00.000000 |

<details>
<summary>Solution</summary>

```sql
SELECT id,
       player_name,
       last_online
FROM player
ORDER BY last_online DESC
LIMIT 5;
```

</details>

## OFFSET

The `OFFSET` keyword is often used to facilitate some kind of pagination in results. Thats also why it is often combined
with limit.

The `OFFSET` keyword skips the first `n` lines of the result sets.

The general syntax is:

```sql
SELECT column_x,
       column_y
FROM my_table
OFFSET n;
```

Lets try to enhance our query from the limit part even more. We already got the first 5 entries. Now we want to get 
the next 5 entries by adding a `OFFSET` of `5` while keeping the `LIMIT` and `ORDER BY` keywords.

*Hint: The `OFFSET` keyword is directly located before the `LIMIT` keyword*

| id  | player\_name | last\_online               |
|:----|:-------------|:---------------------------|
| 4   | Lilly        | 2022-04-01 00:00:00.000000 |
| 6   | Lenny        | 2022-03-08 00:00:00.000000 |
| 5   | Matthias     | 2022-03-06 00:00:00.000000 |
| 10  | Lexi         | 2022-02-22 00:00:00.000000 |
| 9   | Milana       | 2022-02-12 00:00:00.000000 |

<details>
<summary>Solution</summary>

```sql
SELECT id,
       player_name,
       last_online
FROM player
ORDER BY last_online DESC
OFFSET 5
LIMIT 5;
```

</details>

Whats basically happening here is:

First we order the whole table by the `last_online` column. After this we skip the first `5` rows and read the next 
`5` rows. It is important to remember that we first need to sort the whole table currently. That can be very costly 
on large tables. Luckily there are ways to make this faster. We will cover this later.

## Summing Up

We have now learned the four most important keywords for searching, sorting and limiting the retrieved data. The 
keywords have to be used in a specific order and cant be altered in a free way.

The general syntax for this is:

```sql
SELECT column_x,
       column_y
FROM my_table
WHERE condition
ORDER BY column_z DESC
OFFSET n
LIMIT m;
```
