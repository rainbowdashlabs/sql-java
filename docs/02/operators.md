# Operators

We actually have a lot of operators in SQL and even more in our databases if we use one with an extended SQL flavour.

Some operators on this page may have shorter or different aliases in our databases. I will focus on the
intersection of the operators to keep it simple for now.

Let's start with the one which are equal in all databases we use

## Mathematical

- Add `+`
- Subtract `-`
- Divide`/`
- Multiply `*`
- Modulo `%`

You probably know these already, and you are best friends. Like in every other language we also have these in sql.

What you will need to keep in mind is that sql like java does some type conversion on its own.

Multiplying an integer with a decimal number will result in a decimal number. There are some build in functions and
other explicit type conversion which can change this, but for now just bear with the fact.

```sql
SELECT 1 * 1.0;
-> 1.0

SELECT 5 / 3;
-> 1

SELECT 5 / 2.0;
-> 2.5
```

All our databases have additional mathematical operators and build in functions like square root, absolute and more.
However, they are different in syntax. I will just link them here if you need something special.

[MySQL](https://dev.mysql.com/doc/refman/8.0/en/numeric-functions.html)
| [SQLite](https://www.sqlite.org/lang_corefunc.html) | [MariaDB](https://mariadb.com/kb/en/numeric-functions/)
| [PostgreSQL](https://www.postgresql.org/docs/9.3/functions-math.html)

## Logical

### AND/OR

The java 'and' (`&&`) operator becomes `AND` in sql and the 'or' (`||`) operator becomes `OR`.

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

Different from java we can check for equality with a simple `=`

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

Checking for null is a bit different. Our problem is that an equality check on null will return null.

```sql
SELECT NULL = NULL;
-> NULL
```

That is where we use the `is` keyword, which can be combined with the not keyword to facilitate a `!=` operator

```sql
SELECT NULL IS NULL;
-> TRUE

SELECT NULL IS NOT NULL;
-> FALSE
```

### Greater and Less

Everything which has a size is size comparable. This counts for string, numeric values, date and more.

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

Additionally, we have the between operator which basically checks if a value is between two different values.

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

## Text comparison and pattern matching

We often need to compare texts or part of them. Our databases already provide us some nice ways to do this.

### LIKE

The like operator uses a simple pattern matching syntax

- `%` is a wildcard for multiple characters
- `_` is a wildcard for one character

```sql
SELECT 'abcdef' LIKE 'abc'; -- (1)
-> FALSE

SELECT 'abcdef' LIKE 'abc%'; -- (2)
-> TRUE

SELECT 'abcdef' LIKE '__c%'; -- (3)
-> TRUE

SELECT 'abcdef' LIKE '%cde%'; -- (4)
-> TRUE

```

1. We check if the string is like abc, but we do not add a wildcard at the end
2. We check if the string starts with abc. We also add a wildcard in the end which matches all following characters.
3. We just check if the third char is a `c`. We also add a wildcard
4. We check if the string contains `cde` with two wildcards

**Note on case sensitivity**

In MySQL, SQLite and MariaDB the `LIKE` operator is **case-insensitive**.

PostgreSQL uses `LIKE` for **case-sensitive** and `ILIKE` for **case-insensitive** matching.

### Regex

In MySQL and MariaDB have the REGEXP operator. SQLite has this operator as well but does not have an implementation of it
by default. It will throw an error if used.

PostgreSQL uses the `~` operator for case-sensitive regex matching and `~*` for case-insensitive.

Notable is also that the REGEXP and `~` operators do not check if the whole string matches the expression. It just
checks for a subsequence.

The usage in general is the same.

```sql title="MariaDB and MySQL"
SELECT 'abcdef' REGEXP 'CDE'; -- (1)
-> TRUE

SELECT 'abcdef' REGEXP '[CDE]'; -- (2)
-> TRUE
```

1. Case-insensitive matching. We check if the string contains CDE
2. Case-insensitive matching. We check if the string contains any of `c`, `d` or `e`.

```sql title="PostgreSQL"
SELECT 'abcdef' ~ 'CDE'; -- (1)
-> FALSE

SELECT 'abcdef' ~* 'CDE'; -- (2)
-> TRUE

SELECT 'abcdef' ~* '[CDE]'; -- (3)
-> TRUE
```

1. Case-sensitive matching. We check if the string contains `CDE` with the correct casing.
2. Case-insensitive matching. We check if the string contains `CDE`
3. Case-insensitive matching. We check if the string contains any of `c`, `d` or `e`.
