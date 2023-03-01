# Views

Views are crucial for wrapping complex queries into some kind of "virtual table".
Views allow you to create something based on a query which will be dynamically created every time you call this view.

For example, you could simply wrap our friend count into a view and instead of selecting this cursed union code you
can simply select it like a table. Views are created with the `CREATE VIEW` keyword followed by a `SELECT` statement.

```postgresql
CREATE VIEW friend_count AS
SELECT player_id,
       SUM(friend_count)
FROM (SELECT player_id_1 AS player_id, COUNT(1) AS friend_count
      FROM friend_graph
      GROUP BY player_id_1
      UNION ALL
      SELECT player_id_2 AS player_id, COUNT(1) AS friend_count
      FROM friend_graph
      GROUP BY player_id_2) counts
GROUP BY player_id;
```

Once we executed this code all we need to do to read our friend count is to select everything from our 
`friend_count` view. However, our database doesn't differ between table and view when selecting. We will treat our 
view like a table.

```postgresql
SELECT player_id, sum
FROM friend_count;
```

| player\_id | sum |
|:-----------|:----|
| 3          | 2   |
| 4          | 4   |
| 2          | 3   |
| 1          | 3   |

Having views on a table is a good way to hide larger sql statements. Keep in mind that everytime the view is called 
the underlying query is called as well. This is why views can't really speed up your aggregation.

## Materialized views

**Postgres only**

If you are using postgres you are lucky, because postgres has something called materialized views. Those are actual 
tables created by a query. You define those the same way as views and just add the `MATERIALIZED` keyword:

```postgresql
CREATE MATERIALIZED VIEW friend_count_mat AS
SELECT player_id,
       SUM(friend_count)
FROM (SELECT player_id_1 AS player_id, COUNT(1) AS friend_count
      FROM friend_graph
      GROUP BY player_id_1
      UNION ALL
      SELECT player_id_2 AS player_id, COUNT(1) AS friend_count
      FROM friend_graph
      GROUP BY player_id_2) counts
GROUP BY player_id;
```

The big difference here is that this view gets not regenerated when it is read and this will indeed make your 
database queries much faster if you only need to aggregate your data from time to time.

A materialized view needs to be refreshed by you, which you can simply do by calling:

```postgresql
REFRESH MATERIALIZED VIEW friend_count_mat;
```

This will refresh your view with the query it used for creation.
