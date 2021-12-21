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

I will show you the different datatypes on the next pages for each database. You may skip the databases you not care
about.
