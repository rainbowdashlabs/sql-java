# Operators

We actually have a lot of operators in SQL and even more in our databases if we use one with an extended SQL flavour.

Lets start with the one which are equal in all databases we use

## Mathematical

- Add `+`
- Subtract `-`
- Divide`/`
- Multiply `*`
- Modulo `%`

You probably know these already and you are best friends. Like in every other language we also have these in sql.

What you will need to keep in mind is that sql like java does some type conversion on its own.

Multiplying a integer with a decimal number will result in a decimal number. There are several build in functions and
other explicit type conversion which can change this, but for now just bear with the fact.

```sql
SELECT 1 * 1.0;
-> 1.0

SELECT 5 / 3;
-> 1

SELECT 5 / 2.0;
-> 2.5
```

All our databases have additional mathematical operators like squareroot, absolute and more. However they are different
in syntax. I will just link them here if you need something special.

[MySQL](https://dev.mysql.com/doc/refman/8.0/en/numeric-functions.html)
| [SQLite](https://www.sqlite.org/lang_corefunc.html) | [MariaDB](https://mariadb.com/kb/en/numeric-functions/)
| [PostgreSQL](https://www.postgresql.org/docs/9.3/functions-math.html)

## Logical

### AND and OR

The java and (`&&`) operator becomes `AND` in sql and the or (`||`) operator becomes `OR`.

We can also group our logical checks in groups with braces.

```sql
SELECT TRUE AND FALSE;
-> FALSE

SELECT FALSE OR (TRUE OR FALSE) AND TRUE;
-> TRUE
```

**BEWARE OF NULL**
Having a null value in a boolean comparison can result in unintended results.

```sql
NULL OR TRUE;
-> TRUE

NULL OR FALSE;
-> NULL

NULL AND TRUE;
-> NULL

NULL AND FALSE;
-> FALSE
```

### NOT Keyword

The not keyword will invert every boolean value. This is similar to the `!` in java.

```sql
SELECT NOT TRUE;
-> FALSE

SELECT NOT (FALSE AND TRUE);
-> TRUE

SELECT TRUE AND NOT FALSE;
-> TRUE
```

### Equality

#### EQUAL

Different than in java we can check for equality with a simple `=`

```sql
SELECT 1 = 2;
-> FALSE
```

#### NOT EQUAL

Not equality is obviously similar to equal, but we have two different ways here.

Using the `!=` operator

```sql
SELECT 1 != 2;
-> TRUE
```

Or using the NOT keyword from earlier

```sql
SELECT NOT 1 = 2;
-> TRUE
```

### IS and null equality

Checking for null is a bit different. Our problem is that a equality check on null will return null.

```sql
SELECT NULL = NULL;
-> NULL
```

That is where we use the `is` keyword, which can be combined with the not keyword to faciliate a `!=` operator

```sql
SELECT NULL IS NULL;
-> TRUE

SELECT NULL IS NOT NULL;
-> FALSE
```

### Greater and Less

Everything which has a size is size comparable. This counts for string, numeric values, dates and more.

Like in java we have the same operators here.

- Greater than `>`
- Greater than or equal to `>=`
- less than `<`
- Less than or equal to `<=`

```sql
SELECT 'abc' > 'ab';
-> TRUE

SELECT 5 >= 5;
-> TRUE

SELECT 5.0 < 5.01;
-> TRUE

SELECT 5.0 <= 5.0;
-> TRUE
```

### BETWEEN

Additionally we have the between operator which basically checks if a value is between two different values.

The lower and upper bounds are inclusive. The order doesn't matter.

```sql
~~
SELECT 1 BETWEEN 0 AND 5;
~~
-> TRUE

SELECT 0 BETWEEN 5 AND 0;
-> TRUE

SELECT 5 BETWEEN 0 AND 5;
-> TRUE
```

You can also use the `NOT` keyword

```sql
SELECT 1 NOT BETWEEN 0 AND 5;
-> FALSE
```
