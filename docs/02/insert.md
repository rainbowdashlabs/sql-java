# INSERT

Now that we have created our tables we want to add data to them.

Lets start with our players

| id  | player\_name | last\_online     |
|:----|:-------------|:-----------------|
| 1   | Mike         | 2022-05-11 00:00 |
| 2   | Sarah        | 2022-04-04 00:00 |
| 3   | john         | 2022-04-08 00:00 |
| 4   | Lilly        | 2022-04-01 00:00 |
| 5   | Matthias     | 2022-03-06 00:00 |
| 6   | Lenny        | 2022-03-08 00:00 |
| 7   | Summer       | 2022-05-22 00:00 |
| 8   | Marry        | 2022-06-04 00:00 |
| 9   | Milana       | 2022-02-12 00:00 |
| 10  | Lexi         | 2022-02-22 00:00 |

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
VALUES (1, 'Mike', TIMESTAMP('2022-05-11 00:00')),
       (2, 'Sarah', TIMESTAMP('2022-04-04 00:00')),
       (3, 'john', TIMESTAMP('2022-04-08 00:00')),
       (4, 'Lilly', TIMESTAMP('2022-04-02 00:00')),
       (5, 'Matthias', TIMESTAMP('2022-03-06 00:00')),
       (6, 'Lenny', TIMESTAMP('2022-03-08 00:00')),
       (7, 'Summer', TIMESTAMP('2022-05-22 00:00')),
       (8, 'Marry', TIMESTAMP('2022-06-04 00:00')),
       (9, 'Milana', TIMESTAMP('2022-02-12 00:00')),
       (10, 'Lexi', TIMESTAMP('2022-02-22 00:00'));
```
**PostgreSQL**
```sql
INSERT INTO player(id, player_name, last_online)
VALUES (1, 'Mike', '2022-05-11 00:00'::TIMESTAMP),
       (2, 'Sarah', '2022-04-04 00:00'::TIMESTAMP),
       (3, 'john', '2022-04-08 00:00'::TIMESTAMP),
       (4, 'Lilly', '2022-04-01 00:00'::TIMESTAMP),
       (5, 'Matthias', '2022-03-06 00:00'::TIMESTAMP),
       (6, 'Lenny', '2022-03-08 00:00'::TIMESTAMP),
       (7, 'Summer', '2022-05-22 00:00'::TIMESTAMP),
       (8, 'Marry', '2022-06-04 00:00'::TIMESTAMP),
       (9, 'Milana', '2022-02-12 00:00'::TIMESTAMP),
       (10, 'Lexi', '2022-02-22 00:00'::TIMESTAMP);
```
**SqLite**
```sql
INSERT INTO player(id, player_name, last_online)
VALUES (1, 'Mike', CAST(STRFTIME('%s', '2022-05-11 00:00') AS INTEGER)),
       (2, 'Sarah', CAST(STRFTIME('%s', '2022-04-04 00:00') AS INTEGER)),
       (3, 'John', CAST(STRFTIME('%s', '2022-04-08 00:00') AS INTEGER)),
       (4, 'Lilly', CAST(STRFTIME('%s', '2022-04-02 00:00') AS INTEGER)),
       (5, 'Matthias', CAST(STRFTIME('%s', '2022-03-06 00:00') AS INTEGER)),
       (6, 'Lenny', CAST(STRFTIME('%s', '2022-03-08 00:00') AS INTEGER)),
       (7, 'Summer', CAST(STRFTIME('%s', '2022-05-22 00:00') AS INTEGER)),
       (8, 'Marry', CAST(STRFTIME('%s', '2022-06-04 00:00') AS INTEGER)),
       (9, 'Milana', CAST(STRFTIME('%s', '2022-02-12 00:00') AS INTEGER)),
       (10, 'Lexi', CAST(STRFTIME('%s', '2022-02-22 00:00') AS INTEGER));

```
  
</details>

Lets to the same with the friend_graph

| player\_1 | player\_2 |
|:----------|:----------|
| 1         | 2         |
| 2         | 3         |
| 4         | 3         |
| 5         | 3         |
| 7         | 2         |
| 6         | 1         |
| 6         | 2         |
| 1         | 10        |
| 4         | 10        |

<details>
<summary>Solution</summary>

```sql
INSERT INTO friend_graph(player_1, player_2)
VALUES (1, 2),
       (2, 3),
       (4, 3),
       (5, 3),
       (7, 2),
       (6, 1),
       (6, 2),
       (1, 10),
       (4, 10);
```

</details>

## Conflicts
You may have noticed that until now we can insert the players as often as we want and our ids will no longer be 
unique. We will deal with this later.
