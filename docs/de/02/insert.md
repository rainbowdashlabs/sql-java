# INSERT

Now that we have created our tables we want to add data to them.
Let's start with our players

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

To proceed you need to know how to convert a string into a timestamp.
This is different depending on the database you use:

- Postgres: `'2022-05-11 00:00'::TIMESTAMP`\
  We can simply cast a string to a timestamp
- MariaDB/MySQL: `timestamp('2022-05-11 00:00')`\
  We use the timestamp function to parse our string
- SqLite: `CAST(STRFTIME('%s', '2022-05-11 00:00') AS INTEGER)`\
  We save timestamps as epoch seconds since sqlite doesn't really have a timestamp type

Now try to recreate the table from above with the previously mentioned methods and statements.
Usually the time can be omitted when it is set to 00:00.
  
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

Lets to the same with the friend_graph.
Try to insert following values into the `friend_graph` table.

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

## Create tables with content

You can also use an alternative syntax to directly create a table with content.

Let's say we want to create a table with money of the players.
The table should contain the id and a fixed amount of money for each player for now.

```sql
CREATE TABLE money AS 
    SELECT id, 1000.0 AS money
    FROM player

```

This will create a table like this:

| id  | money |
|:----|:------|
| 1   | 1000  |
| 2   | 1000  |
| 3   | 1000  |
| 4   | 1000  |
| 5   | 1000  |
| 6   | 1000  |
| 7   | 1000  |
| 8   | 1000  |
| 9   | 1000  |
| 10  | 1000  |

This method has some stuff you have to take care of.
The database will decide about the data type of the column.
When we use `1000.0` we get a numeric type.
When we use `1000` we will get an integer type.
It is also important to use an alias on newly created columns which have only a type.
Use the `as` keyword here.
If you don't define an alias the column will have some fallback default value which is usually not what you want. 


## Conflicts
You may have noticed that until now we can insert the players as often as we want and our ids will no longer be unique.
We will deal with this later.
