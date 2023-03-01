# MariaDB and MySQL

MariaDB and MySQL are very similar when it comes to datatypes. However, there are some small differences.

## Numeric types

Numeric types sometimes have a "size".
This size is not directly a validation.
It is most common the size of the number and defines how much of that size is used to store the decimal.
Sized never have a size which validates the input.
It is always about precision.

### INTEGER

Java: Integer, Long

The integer in MariaDB are divided into several sizes.
They will require different disk space depending on the size you choose.
Choose your integer type based on the maximum and minimum size you expect the values to be.

- TINYINT: Between -128 and 127 - [MariaDB](https://mariadb.com/kb/en/tinyint/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/integer-types.html)
- SMALLINT: -32,768 and 32,767 - [MariaDB](https://mariadb.com/kb/en/smallint/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/integer-types.html)
- MEDIUMINT: -8,288,608 and 8,388,607 - [MariaDB](https://mariadb.com/kb/en/mediumint/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/integer-types.html)
- INT or INTEGER: -2,147,483,648 and 2,147,483,647 - [MariaDB](https://mariadb.com/kb/en/int/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/integer-types.html)
- BIGINT: Every value larger than INT - [MariaDB](https://mariadb.com/kb/en/bigint/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/integer-types.html)

### DECIMAL

Java: Double | [MariaDB](https://mariadb.com/kb/en/decimal/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/fixed-point-types.html)

A number with an "exact" fixed point.

### DOUBLE

Java: Double | [MariaDB](https://mariadb.com/kb/en/double/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/floating-point-types.html)

A number with a double precision floating point

### FLOAT

Java: Float | [MariaDB](https://mariadb.com/kb/en/float/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/floating-point-types.html)

A number with a single precision floating point

### BOOLEAN

Java: Boolean | [MariaDB](https://mariadb.com/kb/en/boolean/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/other-vendor-data-types.html)

Booleans are stored as tinyint internally.

## Text types

We have several string types which we need to choose based on the expected size of the value.

### CHAR

Java: String | [MariaDB](https://mariadb.com/kb/en/char/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/char.html)

A character is used for strings with the same size. E.g. Country Codes.
It has a maximum length of 255. The length needs to be defined on creation.
Characters will be padded with spaces if some characters are missing to reach the specified length.

`CHAR(length)`

### TEXT

Java: String

The text type is divided into several sizes. They will disallow values larger than the maximum size.

- TINYTEXT: Up to 255 chars - [MariaDB](https://mariadb.com/kb/en/tinytext/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/blob.html)
- TEXT: Up to 65,353 chars - [MariaDB](https://mariadb.com/kb/en/text/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/blob.html)
- MEDIUMTEXT: Up to 16,777,215 chars - [MariaDB](https://mariadb.com/kb/en/mediumtext/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/blob.html)
- LONGTEXT: Up to 4,294,967,295 chars - [MariaDB](https://mariadb.com/kb/en/longtext/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/blob.html)

### VARCHAR

Java: String | [MariaDB](https://mariadb.com/kb/en/varchar/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/char.html)

The varchar is a string with a variable maximum size.
The maximum size here is a bit more complex, since it depends on the encoding of the database.
Unlike CHAR is does not add any padding.

Theoretically the maximum size is 65,532 characters.
If you use utf8 which requires up to 4 bytes per char you are down to 21,844 characters in the worst case.

One advancement over TEXT is that varchar columns can be fully indexed while TEXT columns are truncated to a specified length.

### Enum

Java: String or Enum name | [MariaDB](https://mariadb.com/kb/en/enum/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/enum.html)

The enum type is a special string which adds input validation to the column. Only values defined in the column can be added.

```ENUM('value1','value2',...)```

### JSON

Java: String | [MariaDB](https://mariadb.com/kb/en/json-data-type/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/json.html)

**MySQL**
JSON is stored as a LONGTEXT column but converted into an internal format for better access.

**MariaDB**

JSON is an alias for LONGTEXT and adds a check that ensures a valid json syntax on insertion. JSON is a normal string in MariaDB.

### SET

Java: String | **[MySQL only](https://dev.mysql.com/doc/refman/8.0/en/set.html)**

Similar to an enum a set can only contain predefined input which are separated by `,`.

Given a set like this:

```sql
SET ('a', 'b', 'c', 'd')
```

We can have several values like:

- `''`
- `'a'`
- `'a,b'`
- `'a,c'`
- ...

## Binary types

### BLOB

Java: Anything

The blob type is divided into several sizes.
It is used to store any binary data you want.

- TINYBLOB: Up to 255 bytes - [MariaDB](https://mariadb.com/kb/en/tinyblob/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/blob.html)
- BLOB: Up to 65,353 bytes - [MariaDB](https://mariadb.com/kb/en/blob/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/blob.html)
- MEDIUMBLOB: Up to 16,777,215 bytes - [MariaDB](https://mariadb.com/kb/en/mediumblob/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/blob.html)
- LONGBLOB: Up to 4,294,967,295 bytes aka 4GB - [MariaDB](https://mariadb.com/kb/en/longblob/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/blob.html)

### BINARY

Java: Anything | [MariaDB](https://mariadb.com/kb/en/binary/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/binary-varbinary.html)

Stores a fixed length binary value.

```BINARY(length)```

## Date and Time

### DATE

Java: LocalDate | [MariaDB](https://mariadb.com/kb/en/date/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/datetime.html)

### TIME

Java: LocalTime | [MariaDB](https://mariadb.com/kb/en/time/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/time.html)

### DATETIME

Java: LocalDateTime | [MariaDB](https://mariadb.com/kb/en/datetime/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/datetime.html)

Combination of DATE and TIME.
They will be stored in the current timezone.
They will also validate that the time is valid in the current timezone.
Some times can be invalid due to daylight saving times also referred as winter- and summertime.

### TIMESTAMP

Java: LocalDateTime | [MariaDB](https://mariadb.com/kb/en/timestamp/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/datetime.html)

A timestamp will be converted to UTC on insertion and changed to the sessions timezone on read again.

### YEAR

Java: Integer or String | [MariaDB](https://mariadb.com/kb/en/year-data-type/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/year.html)

A year represents a year in two or four digit format. They are limited in range.

- Four digits (`YEAR(4)`): 1901 -> 2155 and 0000
- Two digits (`YEAR(2)`): 70 -> 69 representing 1970 -> 2069
