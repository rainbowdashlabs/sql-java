# PostgreSQL

PostgreSQL has a lot more specialised and more flexible datatypes. This makes their creation a bit more complex in some cases.

We will just take a look at the more general types and will skip stuff like [geometric](https://www.postgresql.org/docs/14/datatype-geometric.html), [text search](https://www.postgresql.org/docs/14/datatype-textsearch.html) or [range](https://www.postgresql.org/docs/14/rangetypes.html) types.

## Numeric types

Numeric types sometimes have a "size".
This size is not directly a validation.
It is most common the size of the number and defines how much of that size is used to store the decimal.
Sized never have a size which validates the input.
It is always about precision.

### INTEGER

Java: Integer, Long | [Documentation](https://www.postgresql.org/docs/current/datatype-numeric.html#DATATYPE-INT)

The integer in PostgreSQL are divided into several sizes.
They will require different disk space depending on the size you choose.
Choose your integer type based on the maximum and minimum size you expect the values to be.

- SMALLINT: -32,768 and 32,767
- INTEGER: -2,147,483,648 and 2,147,483,647
- BIGINT: Every value larger than INT

### DECIMAL

Java: Double | [Documentation](https://www.postgresql.org/docs/current/datatype-numeric.html#DATATYPE-NUMERIC-DECIMAL)

A number with an "exact" fixed point.

### DOUBLE

Java: Double | [Documentation](https://www.postgresql.org/docs/current/datatype-numeric.html#DATATYPE-FLOAT)

A number with a double precision floating point

### BOOLEAN

Java: Boolean | [Documentation](https://www.postgresql.org/docs/current/datatype-boolean.html)

Booleans are stored as tinyint internally.

## Text types

We have several string types which we need to choose based on the expected size of the value.

### CHAR

Java: String | [Documentation](https://www.postgresql.org/docs/current/datatype-character.html)

A character is used for strings with the same size. E.g. Country Codes.
The length needs to be defined on creation.
Characters will be padded with spaces if some characters are missing to reach the specified length.
The maximum size of a CHAR is about 1 GB. If you need more use TEXT or VARCHAR

`CHAR(length)`

### TEXT

Java: String | [Documentation](https://www.postgresql.org/docs/current/datatype-character.html)

The text type in postgres allows storing of texts of unlimited variable length.
This is a large difference to other databases which have several text types with different max length.

### VARCHAR

Java: String | [Documentation](https://www.postgresql.org/docs/current/datatype-character.html)

The VARCHAR is a string with a variable maximum size.
The maximum defined size of a VARCHAR is 10485760.
Unlike CHAR is does not add any padding.
Unlike other databases a VARCHAR without an explicit limit is unlimited and equal to the TEXT in behaviour.

One advancement over TEXT is that varchar columns can be fully indexed while TEXT columns are truncated to a specified length.

### Enum

Java: String or Enum name | [Documentation](https://www.postgresql.org/docs/current/datatype-enum.html)

Differently to MariaDB and MySQL the enum values are not defined in the table but in its own type.
Postgres allows to add own types via the create type command.

```sql
CREATE TYPE mood AS enum ('happy', 'angry', 'sad')
```

This allows to use it in multiple tables or at numerous other places without defining it every time again. It also allows iteration and explicit ordering.

### JSON and JSONB

Java: String | [Documentation](https://www.postgresql.org/docs/current/datatype-json.html)

JSON is the primitive text only representation which adds syntax validation to a column for json.
It also allows to use a large number of json [specific operators](https://www.postgresql.org/docs/current/functions-json.html).

JSONB is a binary representation of the object itself.
Unlike JSON it allows to create indices on values of specific keys.
This enables postgres to provide some kind of document based database behaviour.

## Binary types

### BINARY (BYTEA)

Java: Anything | [Documentation](https://www.postgresql.org/docs/current/datatype-binary.html)

Stores binary data as bytes or hex. The BYTEA type takes no size argument.

## Date and Time

### DATE

Java: LocalDate | [Documentation](https://www.postgresql.org/docs/current/datatype-datetime.html)

### TIME

Java: LocalTime | [Documentation](https://www.postgresql.org/docs/current/datatype-datetime.html)

### TIMESTAMP

Java: LocalDateTime | [Documentation](https://www.postgresql.org/docs/current/datatype-datetime.html)

Postgres does not have a dedicated DATETIME type.
Instead, it has a TIMESTAMP which is basically a normal timestamp without timezone and TIMESTAMPTZ which is a timestamp with a timezone.

### INTERVAL

Java: String | [Documentation](https://www.postgresql.org/docs/current/datatype-datetime.html#DATATYPE-INTERVAL-INPUT))

As a special type postgres has the INTERVAL type.
This type allows simple creation of intervals by parsing strings.

```sql
SELECT now() - '10 HOURS'::interval; -- (1)

SELECT now() - '10 HOURS 10 MINUTES'::interval; -- (2)

SELECT now() - '1 D 10 H 10 M'::interval; -- (3)
```

1. The current time minus 10 hours
2. The current time minus 10 hours and 10 minutes
3. The current time minus 1 day 10 hours and 10 minutes.

## Arrays

All postgres datatypes can be used as an [array](https://www.postgresql.org/docs/current/arrays.html).

Arrays are declared as `type[]`.
They have a lot of extra [operators](https://www.postgresql.org/docs/current/functions-array.html) for comparison or modification.

## Composite Types

PostgreSQL allows creation of [composite types](https://www.postgresql.org/docs/current/rowtypes.html) which are essentially classes and allow the creation of objects.
Like enums they are created with the create type command

```sql
CREATE TYPE person AS (name text, age INT)
```

These types can be used as a column type as well.
To create a composite type object we use the ROW expression.

```sql
ROW
('Some Name', 10)
```

The name of the composite type doesn't matter.
It is only about the correct types in the correct order.
