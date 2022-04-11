# INSERT

Now that we have created our tables we want to add data to them.

Lets start with our players

| id  | player_name | last_online      |
|-----|-------------|------------------|
| 1   | Mike        | 2022-05-11 00:00 |
| 2   | Sarah       | 2022-04-04 00:00 |
| 3   | John        | 2022-04-08 00:00 |
| 4   | Lilly       | 2022-04-02 00:00 |

In order to insert data we will need to use the `INSERT` statement.

The general syntax to insert one row into a table is:

```sql
INSERT INTO table_name(column_1, column_2)
VALUES (value_1, value_2)
```

You can also insert multiple rows at once with:

```sql
INSERT INTO table_name(column_1, column_2)
VALUES (value_1, value_2),
       (value_1, value_2),
       (value_1, value_2);
```

To proceed you need to know how to convert a string into a timestamp. This is different depending on the database you
use:

- Postgres: `'2022-05-11 00:00'::TIMESTAMP`\
  We can simply cast a string to a timestamp
- MariaDB/MySQL: `timestamp('2022-05-11 00:00')`\
  We use the timestamp function to parse our string
- SqLite: `CAST(STRFTIME('%s', '2022-05-11 00:00') AS INTEGER)`\
  We save timestamps as epoch seconds since sqlite doesnt really has a timestamp type
  
<details>
<summary>Solution</summary>

**MariaDB/MySQL**
```sql
INSERT INTO player(id, player_name, last_online)
VALUES (1, 'Mike', timestamp('2022-05-11 00:00')),
       (2, 'Sarah', timestamp('2022-04-04 00:00')),
       (3, 'john', timestamp('2022-04-08 00:00')),
       (4, 'Lilly', timestamp('2022-04-02 00:00'));
```
**PostgreSQL**
```sql
INSERT INTO player(id, player_name, last_online)
VALUES (1, 'Mike', '2022-05-11 00:00'::TIMESTAMP),
       (2, 'Sarah', '2022-04-04 00:00'::TIMESTAMP),
       (3, 'john', '2022-04-08 00:00'::TIMESTAMP),
       (4, 'Lilly', '2022-04-02 00:00'::TIMESTAMP);
```
**SqLite**
```sql
INSERT INTO player(id, player_name, last_online)
VALUES (1, 'Mike', CAST(STRFTIME('%s', '2022-05-11 00:00') AS INTEGER)),
       (2, 'Sarah', CAST(STRFTIME('%s', '2022-04-04 00:00') AS INTEGER)),
       (3, 'john', CAST(STRFTIME('%s', '2022-04-08 00:00') AS INTEGER)),
       (4, 'Lilly', CAST(STRFTIME('%s', '2022-04-02 00:00') AS INTEGER));
```
  
</details>

Lets to the same with the friend_graph

| player_1 | player_2 |
|----------|----------|
| 1        | 2        |
| 2        | 3        |
| 4        | 3        |


| col 1 l |     | dgdjhfgjhdfg         | sgdsfgh         |
|---------|-----|----------------------|-----------------|
|         |     |                      | sdfgoshdfglsghf |
|         |     | asldfkjsldkfgl;sdhgf |                 |
|         |     |                      |                 |
|         |     |                      |                 |



<details>
<summary>Solution</summary>

```sql
INSERT INTO friend_graph(player_1, player_2)
VALUES (1, 2),
       (2, 3),
       (4, 3)
```

</details>

## Conflicts
You may have noticed that until now we can insert the players as often as we want and our ids will no longer be 
unique. We will deal with this later.
