# Checks

**Only for MySQL, MariaDB and PostgreSQL**

Checks are also very important when ensuring that a value in a column meets certain requirements.

You already know some checks.
Every time we used `CONSTRAINT` we added a check. For example in our [foreign key](foreign_keys.md) or in our [primary key](../03/data_consistency/primary_keys.md).
`NOT NULL` is an inline check as well.
But there is actually more! We can also add manual checks ourselves.

Time to enhance our money table even more, by adding a check which ensures that money is always equal or greater than 0, which will ensure that we never have a negative amount of money.

```sql
CREATE TABLE money
(
    player_id INT PRIMARY KEY,
    money     DECIMAL DEFAULT 0 NOT NULL,
    CONSTRAINT money_player_player_id_id_fk
        FOREIGN KEY (player_id) REFERENCES player (id)
            ON DELETE CASCADE,
    -- We add another constraint. The name should use something like
    -- check_<table>_<check_name>
    CONSTRAINT check_money_negative
        -- and define a custom check which ensures that money is greater or equal to 0
        CHECK ( money >= 0)
);
```

Let's try to insert something invalid:

```sql
INSERT INTO money (player_id, money) VALUES (1, -10);
```

It fails! Our check works and prevents us from adding negative values to our table! Of course this works with updates as well.
