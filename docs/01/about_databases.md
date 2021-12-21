# Databases

## Common databases

We have a lot of different databases out there. They work in many different ways and are used for different use cases.
There is no ultimate database which can handle every type of data although some are very good with many different data
types.

When we speak about data types we usually differ between structured and unstructured data.

Some databases are specialised on searching in unstructured data like [Solr](https://solr.apache.org/)
or [ElasticSearch](https://www.elastic.co/elastic-stack/) which is most common used for searching and aggregating logs.
One more common database for unstructered data is [MongoDB](https://www.mongodb.com/). We call those databases document
based databases since we store data in documents.

On the other hand we have our databases for structured data. You might have heard of [MySQL](https://www.mysql.com/)
and [MariaDB](https://mariadb.org/) already. These two are siblings and MariaDB is developed on top of MySQL and can act
as a drop in replacement and can provide some enhancements. One more popular and more enterprise database
is [PostgreSQL](https://www.postgresql.org/) which is used in data science and projects with large datasets. One more
special database is [SQLite](https://www.sqlite.org) which is a serverless database without any running server. Its used
when the amount of data is small and the application should run without further setup steps. It is loaded into 
memory and therefore limited by the available memory on your system.

We will focus relational databases on this page. More precisely we will focus on MySql, MariaDB and PostgreSQL. SQLite
is quite common to MySQL.

## Is my data structured or unstructured?

In the most cases your data is structured. Many people tend to use databases for unstructured data because it seems less
work in the beginning. In general nearly all data which looks unstructured on first sight can be structured.

Don't use a document based database just because you want to dump your data somewehere. Start learning working with
relational data and make use of all the great benefits.

## Different Databases different flavours

When calling data from one of our databases we use [SQL (Structured Query Language)](https://en.wikipedia.org/wiki/SQL).

All our databases use SQL, but not the same SQL. Altough they will all have similar syntax they use
different [flavours](https://en.wikipedia.org/wiki/SQL#Procedural_extensions)
of SQL. Most of your queries will work the same in all databases or will just require small adjustments.

MySQL and SQLite use [SQL/PSM (SQL/Persistent Stored Module)](https://en.wikipedia.org/wiki/SQL/PSM) which is the most
standardized type of SQL

MariaDB uses [SQL/PSM](https://en.wikipedia.org/wiki/SQL/PSM) and [PL/SQL](https://en.wikipedia.org/wiki/PL/SQL) which
allows to write procedural statements with loops and other stuff. It has its origin in
the [OracleDB](https://www.oracle.com/database/).

PostgreSQL uses [PL/pgSQL](https://en.wikipedia.org/wiki/PL/pgSQL) which is a extension of
the [PL/SQL](https://en.wikipedia.org/wiki/PL/SQL) and enhances the general possibilities of SQL on a level which allows
highly complex operations. Some people say that you can write your complete backend in PL/pgSQL.

## Database or DBMS

When we speak of a database we usually mix up three things:

### DBMS

DBMS means [Database Management System](https://en.wikipedia.org/wiki/Database#Database_management_system).

These systems are designed to manage your databases.

Since SQL databases are for relational data our DBMS are actually RDBMS (Relational Database Management System).

### Database

A database is a database inside your DBMS. It will hold your data in tables, store procedures and do other stuff.

### Schema

Some databases like PostgreSQL have one more subdivision in a database
called [Schema](https://www.postgresqltutorial.com/postgresql-schema/).

Altough MariaDB and MySQL call their databases "Database" in statements they refer to them
as [schemas internally](https://www.tutorialspoint.com/difference-between-schema-and-database-in-mysql) as well.

A second subdivision into schemas has the advancement of better organization. It is common to access data through
different schemas, but uncommon to access data in different databases at the same time, although some RDBMS might allow
this.

### Structure of a RDBMS

In general we can visualize our structure as follows:

**MariaDB and MySQL**

```
RDBMS
├── Database/Schema
│   ├── Table
│   └── Table
└── Database/Schema
    ├── Table
    └── Table
```

**PostgreSQL**

```
RDBMS
├── Database
│   ├── Schema
│   │   ├── Table
│   │   └── Table
│   └── Schema
│       └── Table
└── Database
    └── Schema
        ├── Table
        └── Table
```
