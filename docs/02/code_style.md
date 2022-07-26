# Code Style

SQL code should be easily readable. If you use the naming conventions provided earlier this will happen nearly by
itself. Don't think about the actual code for now. You don't need to understand it yet.

I will disable syntax highlighting here to make it more clear.

## Keywords

All SQL keywords should be `UPPER_CASE` this will make clear which words are names and which are keywords.

```
-- Bad
select col from my_table;

-- Good
SELECT col FROM my_table;
```

## Quote only if required

Many people tend to quote a lot which isn't needed. If you use a good naming scheme you will probably never need to
quote your stuff.

```
-- Bad
SELECT `col` FROM `my_table`;

-- Good
SELECT col FROM my_table;
```

## Line breaks are good

Line breaks will make larger queries way more readable. A good rule is to precede each keyword with a line break. You
may add more line breaks based on your own choice

```
-- Bad
SELECT col1, col2 FROM my_table WHERE col1 = 'something' AND col2 = 'else' LIMIT 1;

-- Good
SELECT col1,
    col2
FROM my_table
WHERE col1 = 'something'
    AND col2 = 'else'
LIMIT 1;

-- Even better
SELECT 
    col1,
    col2
FROM
    my_table
WHERE
    col1 = 'something'
    AND col2 = 'else'
LIMIT 1;
```

## Meaningful alias

Give your table a meaningful alias. Choose a descriptive shorter name instead of single characters like `x`, `y` or `z`

## Use common table expressions (CTE) instead of subqueries

CTE can make your complex code way more readable.

Take a look at this subquery example which shows the friend count of all players which were seen in the last 10 days.

<!-- @formatter:off --> 
```sql
SELECT -- (4)
       id,
       COALESCE(friend.count, 0)
FROM ( -- (1)
         SELECT id
         FROM player
         WHERE last_online > NOW() - '10 DAYS':: INTERVAL
     ) active
         LEFT JOIN ( -- (3)
    SELECT user_id, COUNT(1) AS friend_count
    FROM ( -- (2)
             SELECT player_1 AS player_id
             FROM friend_graph
             UNION ALL
             SELECT player_2 AS player_id
             FROM friend_graph
         ) flat_friend_graph
    GROUP BY user_id
) friend
                   ON active.id = friend.player_id
```
<!-- @formatter:on --> 

1. We select all players which were online in the past 10 days.
2. Our friend graph table is a unidirectional graph which contains one entry per friendship We use a UNION ALL to append
   the `player_2` column on the `player_1` column.
3. We calculate the friend count. We group by the used_id and count how often every user id occurs.
4. We combine our friend count with the active players and get a table with the friend count of all active players.

When we use CTEs it would look like this.

<!-- @formatter:off --> 
```sql
WITH active_players AS ( -- (1)
    SELECT id
    FROM player
    WHERE last_online > NOW() - INTERVAL '10 DAYS'
),
flat_friend_graph AS ( -- (2)
    SELECT player_1 AS player_id
    FROM friend_graph
    UNION ALL
    SELECT player_2 AS player_id
    FROM friend_graph
),
friend_count AS ( -- (3)
    SELECT user_id, COUNT(1) AS friend_count
    FROM flat_friend_graph
    GROUP BY user_id
)
SELECT -- (4)
       id,
       COALESCE(friend.count, 0)
FROM active_players active
LEFT JOIN friend_count friend 
    ON active.id = friend.player_id
```
<!-- @formatter:on --> 

1. We select all players which were online in the past 10 days.
2. Our friend graph table is a unidirectional graph which contains one entry per friendship We use a UNION ALL to append
   the `player_2` column on the `player_1` column.
3. We calculate the friend count. We group by the used_id and count how often every user id occurs.
4. We combine our friend count with the active players and get a table with the friend count of all active players.
