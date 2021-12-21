# SQL Datatypes

SQL has its own datatypes. Some of them are equal to the java types, some of them are not. To confuse us more MySQL,
MariaDB, SQLite and PostgreSQL support different data types. Some of them may behave differently altough they have the
same name.

I will provide a short list here which shows the most correct mapping of java datatypes to their sql counterpart.

However I will not cover all types, since some databases like PostgreSQL have very specific datatypes which are used
only in very special cases. I will only cover datatypes which might be important for you in the most cases.

Have a link at the documentation if you want to dive deeper.

[MySQL](https://dev.mysql.com/doc/refman/8.0/en/data-types.html) | [SQLite](https://www.sqlite.org/datatype3.html)
| [MariaDB](https://mariadb.com/kb/en/data-types/) | [PostgreSQL](https://www.postgresql.org/docs/9.5/datatype.html)

## Null

`NULL` in sql is the same like in java in all our databases. However we have no such think like a null pointer exception
in sql. Everytime you do something with null sql will try to do something with it. That is why performing logical
operations with null values can become tricky and errorprone like you have seen on the previous page.

## SQLite

SQLite has the smalles set of data types.

### INTEGER

Java: Integer, Long

An integer with 8 to 64 bits

### REAL

Java: float, double

Any number with a floating point. Stored in in 64 bits.

### TEXT

Java: String

Any text you want to store

### BLOB

Java: Anything binary

A blob is basically a binary representation of something. It will be returned in the exact same way than it was input.

### BOOLEAN

Java: Boolean

A boolean in sqlite is a integer internally.

### Date and Time

Java: Instant, LocalDate, LocalDateTime

SQLite stores dates and times as INTEGER with UNIX time, TEXT as serialized string or a REAL as julian day numbers.

You will have to chose one type and have to convert them everytime you use a date or datetime. I'd recommend UNIX time.
We will get to read and write later. Just bear with it for the moment.

## MariaDB

### Numeric types

Numeric types sometimes have a "size". this size is not directly a validation. It is most common the size of the number
and defines how much of that size is used to store the decimal. Sized never have a size which validates the input. It is
always about precision.

#### INTEGER

Java: Integer, Long

The integer in maria db are divided into several sizes. They will require different disk space depending on the size you
choose. Choose your integer type based on the maximum size you expect the values to be.

- [TINYINT](https://mariadb.com/kb/en/tinyint/): Between -128 and 127
- [SMALLINT](https://mariadb.com/kb/en/smallint/): -32,768 and 3,2767
- [MEDIUMINT](https://mariadb.com/kb/en/mediumint/): -8,288,608 and 8,388,607
- [INT](https://mariadb.com/kb/en/int/) or INTEGER: -2,147,483,648 and 2,147,483,647
- [BIGINT](https://mariadb.com/kb/en/bigint/): Every value larger than INT

#### [DECIMAL](https://mariadb.com/kb/en/decimal/)

Java: Double

A number with a "exact" fixed point.

#### [DOUBLE](https://mariadb.com/kb/en/double/)

Java: Double

A number with a double precision floating point

#### [FLOAT](https://mariadb.com/kb/en/float/)

Java: Float

A number with a single precision floating point

#### [BOOLEAN](https://mariadb.com/kb/en/boolean/)

Java: Boolean

Booleans are stored as tinyint internally.

### Text types

We have several string types which we need to choose based on the expected size of the value.

#### [CHAR](https://mariadb.com/kb/en/char/)

Java: String

A character is used for strings with the same size. E.g. Country Codes. Is has a maximum length of 255. The length needs
to be defined on creation. Characters will be padded with spaces if some characters are missing to reach the specified
length.

`!#SQL CHAR(length)`

#### TEXT

Java: String

The text type is divided into several sizes. The will disallow values larger than the maximum size.

- [TINYTEXT](https://mariadb.com/kb/en/tinytext/): Up to 255 chars
- [TEXT](https://mariadb.com/kb/en/text/): Up to 65,353 chars
- [MEDIUMTEXT](https://mariadb.com/kb/en/mediumtext/): Up to 16,777,215 chars
- [LONGTEXT](https://mariadb.com/kb/en/longtext/): Up to 4,294,967,295 chars

#### [VARCHAR](https://mariadb.com/kb/en/varchar/)

Java: String

The varchar is a string with a variable maximum size. The maximum size here is a bit more complex, since it depends on
the encoding of the database. Unlike CHAR is does not add any padding.

Theoretically the maximum size is 65,532 characters. If you use utf8 which requires 3 bytes per char you are down to
21,844 characters.

One advancement over TEXT is that varchar columns can be fully indexed while TEXT columns are truncated to a specified
length

#### [Enum](https://mariadb.com/kb/en/enum/)

Java: String or Enum name

The enum type is a special string which adds input validation to the column. Only values defined in the column can be
added.

```!#sql ENUM('value1','value2',...)```

### Binary types

#### BLOB

Java: Anything

The blob type is divided into several sizes. It is used to store any binary data you want.

- [TINYBLOB](https://mariadb.com/kb/en/tinyblob/): Up to 255 bytes
- [BLOB](https://mariadb.com/kb/en/blob/): Up to 65,353 bytes
- [MEDIUMBLOB](https://mariadb.com/kb/en/mediumblob/): Up to 16,777,215 bytes
- [LONGBLOB](https://mariadb.com/kb/en/longblob/): Up to 4,294,967,295 bytes aka 4GB

#### [BINARY](https://mariadb.com/kb/en/binary/)

Java: Anything

Stores a fixed length binary value.

```!#sql BINARY(length)```

### Date and Time

#### [DATE](https://mariadb.com/kb/en/date/)

Java: LocalDate

#### [TIME](https://mariadb.com/kb/en/time/)

Java: LocalTime

#### [DATETIME](https://mariadb.com/kb/en/datetime/)

Java: LocalDateTime

Combination of DATE and TIME. They will be stored in the current timezone. They will also validate that the time is
valid in the current timezone. Some times can be invalid due to dailight saving times also refered as winter and summer
time.

#### [TIMESTAMP](https://mariadb.com/kb/en/timestamp/)

Java: LocalDateTime

A timestamp will be converted to UTC on insertion and changed to the sessions timezone on read again.

#### [YEAR](https://mariadb.com/kb/en/year-data-type/)

Java: Integer or String

A year represents a year in two or four digit format. They are limited in range.

- Four digits (`YEAR(4)`): 1901 -> 2155 and 0000
- Two digits (`YEAR(2)`): 70 -> 69 representing 1970 -> 2069
