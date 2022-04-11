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

## Cheatsheet for quick reference

| **Java Type** | **Range/Length**                 | **MySQL / MariaDB** | **SQLite**           | **PostgreSQL**   |
|:--------------|:---------------------------------|:--------------------|:---------------------|:-----------------|
| String        | Fixed < 255 with padding         | CHAR                |                      | CHAR(Up to 1 GB) |
| String        | < 255                            | TINYTEXT, VARCHAR   | TEXT                 | TEXT, VARCHAR    |
| String        | < 65,353                         | TEXT, VARCHAR       | TEXT                 | TEXT, VARCHAR    |
| String        | < 16,777,215                     | MEDIUMTEXT          | TEXT                 | TEXT, VARCHAR    |
| String        | < 4,294,967,295                  | LONGTEXT            | TEXT                 | TEXT, VARCHAR    |
| String        | unlimited                        |                     | TEXT                 | TEXT, VARCHAR    |
| Integer/Short | -128 and 127                     | TINYINT             | INTEGER              | SMALLINT         |
| Integer/Short | -32,768 and 32,767               | SMALLINT            | INTEGER              | SMALLINT         |
| Integer       | -8,288,608 and 8,388,607         | MEDIUMINT           | INTEGER              | INTEGER          |
| Integer       | -2,147,483,648 and 2,147,483,647 | INT(INTEGER)        | INTEGER              | INTEGER          |
| Long          |                                  | BIGINT              | INTEGER (64 bit max) | BIGINT           |
| Double        | exact fixed point                | DECIMAL             | REAL                 | DECIMAL(NUMERIC) |
| Double        | double precision                 | DOUBLE              | REAL                 | DOUBLE           |
| Float         | single precision                 | FLOAT               | REAL                 |                  |
| Boolean       |                                  | BOOLEAN             | BOOLEAN(INTEGER)     | BOOLEAN          |
| Bytes         | Fixed < 255 with padding         | BINARY              |                      |                  |
| Bytes         | < 255                            | TINYBLOB, VARBINARY | BLOB                 | BYTEA            |
| Bytes         | < 65,353                         | BLOB, VARBINARY     | BLOB                 | BYTEA            |
| Bytes         | < 16,777,215                     | MEDIUMBLOB          | BLOB                 | BYTEA            |
| Bytes         | < 4,294,967,295                  | LONGBLOB            | BLOB                 | BYTEA            |
| LocalDate     |                                  | DATE                | TEXT, REAL, INTEGER  | DATE             |
| LocalTime     |                                  | TIME                | TEXT, REAL, INTEGER  | TIME             |
| LocalDateTime |                                  | TIMESTAMP           | TEXT, REAL, INTEGER  | TIMESTAMPTZ      |
| Timestamp     |                                  | TIMESTAMP           | TEXT, REAL, INTEGER  | TIMESTAMP        |

## Null

`NULL` in sql is the same like in java in all our databases. However we have no such think like a null pointer exception
in sql. Everytime you do something with null sql will try to do something with it. That is why performing logical
operations with null values can become tricky and errorprone like you have seen on the previous page.

I will show you the different datatypes on the next pages for each database. You may skip the databases you not care
about.
