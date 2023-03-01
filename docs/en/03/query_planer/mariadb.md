# MariaDB

The query planer of MariaDB and MySQL are quite simple to understand.
An in depth description can be found in the docs of [MariaDB](https://mariadb.com/kb/en/explain/) and MySQL.

We start with our basic query from earlier:

```sql
EXPLAIN
SELECT *
FROM player
WHERE id = 5;
```

This returns:


| id  | select\_type | table  | type | possible\_keys | key  | key\_len | ref  | rows | Extra       |
|:----|:-------------|:-------|:-----|:---------------|:-----|:---------|:-----|:-----|:------------|
| 1   | SIMPLE       | player | ALL  | null           | null | null     | null | 10   | Using where |

Let's start with each column and take a look at what they actually mean:

- `id` -> The sequence when you combine multiple tables (We didn't do this yet)
- `select_type` -> The origin of the selected data. There are different types like simple or selects on the 
  `PRIMARY` key. We will see those more often
- `table` -> the table we selected from. In our case the `player` table
- `type` -> That's probably our most important column. This column currently shows `all` which means we are reading 
  all rows of the table. Ideally there would be something like `index` or `range` which means a reduction in runtime.
- `possible_keys` shows the names of the key in the table we are reading from.
- `key` ->  The actual used key for our query.
- `key_len` -> the length of the used key when we use multi-column keys
- `ref` -> The reference of the key value
- `rows` -> An estimation of how many rows we can expect.
- `Extra` -> some additional information. In our case it tells us that we are searching by a where clause 

## Analyze

Additionally, the `ANALYZE` keyword can give you an insight about the actual stuff going on.
This will execute the query and measure everything:

```sql
ANALYZE
SELECT *
FROM player
WHERE id = 5;
```

Results in:

| id  | select\_type | table  | type | possible\_keys | key  | key\_len | ref  | rows | r\_rows | filtered | r\_filtered | Extra       |
|:----|:-------------|:-------|:-----|:---------------|:-----|:---------|:-----|:-----|:--------|:---------|:------------|:------------|
| 1   | SIMPLE       | player | ALL  | null           | null | null     | null | 10   | 10.00   | 100      | 10          | Using where |

Additionally, to the previous estimations, columns prefixed with `r_` are showing the real values.
