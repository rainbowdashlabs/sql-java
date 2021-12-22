# Naming conventions

There are no official naming conventions for sql, but there are some established conventions for naming.

More important than naming is consistency. Don't switch between different stiles.

More detailed information can be found [here](https://www.sqlshack.com/learn-sql-naming-conventions/).

User created names should be `snake_case` in general

## Database and Schema

Avoid databases and schemas which start with numbers. If you do this you will need to quote it every time, which can 
be really annoying

## Tables

- Tables should be named with singular terms. (user, role instead of users, roles)
- Use single words if possible

## Columns

- Avoid duplication of the table name. If you table is named `user` and has a id column name it `id` instead of
  `user_id`.
- Even if it is possible that a column has the same name as its type, this should be avoided.
### Primary key columns

Most common is `id`

### Foreign key column

If your column is part of a foreign key use the table name where the key references to.

Imagine a table `user(id, name)` and a table `money(user_id, amount)`.

The `user_id` column in `money` is a reference to the `id` column in `user`

### Dates

Dates should not onlybe named date, but have a descriptive name like `inserted_date` or `edit_date`

### Booleans

Boolean colums should have questioning names like `is_enabled`

## Keys and indices

- Prefixed with table names.

### Indices

Examples

```
<tablename>_<row.1>_<row.2>_<row.n>_index
<tablename>_<row.1>_<row.2>_<row.n>_uindex
```

- Contained rows
- suffixed with index if not unique
- suffixed with uindex if unique

### Primary key

```
<tablename>_pk
```

- Suffixed with `pk`

### Foreign keys

```
<origin_tablename>_<related_tablename>
<origin_tablename>_<related_tablename>_fk
```

- Contain related tablename
- suffixed with `fk`

## Views
- Views should be prefixed with `v_` to make the difference between a table and a view visible.
