# Auto increment

You remember how we always set the player id by ourselves? This doesn't feel right, right? How should we remember
which ID we used already, epsecially with our unique index now on our player id . So lets change this once again.
This time we will add a default value again, but in another way than we are used to do.

This default value is called auto increment and will simply count up every time we use it. This makes it impossible
to use an id twice and we no longer need to think about the id we used last.

Saddly this is a bit different depending on the database we use.

*No matter if you need or not, the auto increment column should be set as the primary key of the table in 99.9% of the
cases. There is simply no reason why not to do it. Some databases even require it to be a key or even the primary
key anyway.*

## Postgres

In postgres we can profit from SMALLSERIAL, SERIAL and BIGSERIAL. Those differ in the type and size they can return.

| Name        | Type     | Range                          |
|-------------|----------|--------------------------------|
| SMALLSERIAL | SMALLINT | 1 to 32,767                    |
| SERIAL      | INTEGER  | 1 to 2,147,483,647             |
| BIGSERIAL   | BIGINT   | 1 to 9,223,372,036,854,775,807 |

All we need to do in order to use them is swapping our `INTEGER` with the `SERIAL` datatype in our table. Note that
this value can be still set manually. Since it can also set to null we will mark it as NOT NULL aditionally.

*Remember to drop your table first*

```postgresql
CREATE TABLE player
(
    id          SERIAL    NOT NULL,
    player_name TEXT      NOT NULL,
    last_online TIMESTAMP NOT NULL DEFAULT NOW()
);
```

In postgres the auto increment does not need to be a key. However, it is highly recommended to use the auto increment
id as a primary key, because it is nearly always the right choice since your value is unique anyway and should be
not null.

```postgresql
CREATE TABLE player
(
    id          SERIAL PRIMARY KEY,
    player_name TEXT      NOT NULL,
    last_online TIMESTAMP NOT NULL DEFAULT NOW()
);
```

## SqLite

In order to create aan auto increment here we will need to use the primary key we learned in the
last section. What we basically do here is creating a primary key which values get supplied by an auto increment
sequence. Sounds complicated? Don't think about it. All you need to do is mark the column as a primary key and add
`AUTOINCREMENT` afterwards.

```sqlite
CREATE TABLE player
(
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    player_name TEXT      NOT NULL,
    last_online TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP
);
```

## MariaDB and MySQL

MariaDB and MySQL use the same syntax thankfully. Some engines require you to set the auto increment column as the
first column, so we are going to stick with this as well. Additionally, your auto increment column need to be part of
a key, so we are going to set it as a primary key, which is recommended anyway. In case you have taken a look at the
above example from SqLite you will notice that the only difference ist the underscore in `AUTO_INCREMENT`.

```mariadb
CREATE TABLE player
(
    id          INTEGER PRIMARY KEY AUTO_INCREMENT,
    player_name TEXT      NOT NULL,
    last_online TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

## Testing it

Now that we have set up our auto increment it is finally time to actually test how it works.

Now two of our three columns are populated by the database, which is great. The only job left for us in order to add
a new player is to insert its name into the table.

```sql
INSERT INTO player(player_name)
VALUES ('Mike'),
       ('Sarah'),
       ('John'),
       ('Lilly'),
       ('Matthias'),
       ('Lenny'),
       ('Summer'),
       ('Marry'),
       ('Milana'),
       ('Lexi');
```

Let's check what we ended up with:

```sql
SELECT id, player_name, last_online
FROM player;
```

| id  | player\_name | last\_online               |
|:----|:-------------|:---------------------------|
| 1   | Mike         | 2022-11-26 12:32:39.021491 |
| 2   | Sarah        | 2022-11-26 12:32:39.021491 |
| 3   | John         | 2022-11-26 12:32:39.021491 |
| 4   | Lilly        | 2022-11-26 12:32:39.021491 |
| 5   | Matthias     | 2022-11-26 12:32:39.021491 |
| 6   | Lenny        | 2022-11-26 12:32:39.021491 |
| 7   | Summer       | 2022-11-26 12:32:39.021491 |
| 8   | Marry        | 2022-11-26 12:32:39.021491 |
| 9   | Milana       | 2022-11-26 12:32:39.021491 |
| 10  | Lexi         | 2022-11-26 12:32:39.021491 |

Looks nice! Auto increment ids always start with 1. They make 1 steps by default, but can be changed to make larger
steps. I will not dive into this here, but I am sure google has some great examples for you.

And that's it already. We mastered auto increments on our table and no longer need to think about the unique ids we
need for our users.
