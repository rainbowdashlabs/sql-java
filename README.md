# Content

- [Introduction / What can I expect.](#introduction--what-can-i-expect)
- [What are databases?](#what-are-databases)
- [Prerequisite](#prerequisite)
    * [Driver Implementation](#driver-implementation)
        + [Different Database -> Different Driver and Queries](#different-database---different-driver-and-queries)
    * [Async and Synced calling](#async-and-synced-calling)
    * [Connection Pooling and HikariCP](#connection-pooling-and-hikaricp)
    * [Read everything](#read-everything)
- [Try With Resources](#try-with-resources)
- [Setting up a Connection](#setting-up-a-connection)
- [Building Tables](#building-tables)
    * [Creating a table](#creating-a-table)
    * [Data consistency](#data-consistency)
        + [Default](#default)
        + [not null](#not-null)
        + [Primary keys](#primary-keys)
        + [Constrains and Indices](#constrains-and-indices)
- [Setting up your database](#setting-up-your-database)
    * [Sidenote on versioning](#sidenote-on-versioning)
- [Best practises](#best-practises)
    * [Prepared Statements](#prepared-statements)
    * [Using optional](#using-optional)
    * [Returning boolean](#returning-boolean)
- [Working with data](#working-with-data)
    * [Writing data](#writing-data)
        + [Insert](#insert)
        + [Upsert](#upsert)
    * [Updating data](#updating-data)
        + [Replace](#replace)
        + [Update](#update)
    * [Deleting data](#deleting-data)
    * [Reading Data](#reading-data)
        + [ResultSet](#resultset)
        + [Select Single Row](#select-single-row)
        + [Select Multiple Rows.](#select-multiple-rows)
        + [Select only what you need](#select-only-what-you-need)
- [Nice to know](#nice-to-know)
    * [Sorted indices and EXPLAIN](#sorted-indices-and-explain)
- [Conclusions](#conclusions)
- [Further Stuff](#further-stuff)

# Introduction / What can I expect.

Databases are often crucial when it comes to storing data.\
This tutorial aims to get you ready to store your data in your MariaDB or MySQL database.

We will start with some general preconditions we need to clarify and make some preparations.\
After this we will look into the DataSource and build our first connection to our database.\
To continue with reading and writing data to the database we will need to introduce some best practices. This will make
it easier for you to work with your database.\
After this we can finally come to write and read some data and will do this at the example of a pretty simple coins
plugin, which gives and takes coins.

After reading this guide you will have basic knowledge how to develop an application with a stable and good database
implementation.

Databases are a really large topic. We can't cover everything here. You might think this is much, but we are just
scraping the surface.

# What are databases?

As you delve into the depths of programming, you will come across times when you realize that it would be a lot more
convenient for both the user, and the developer to store data in tables like you see in spreadsheets instead of creating
all sorts of wrapper objects, hashmaps of hashmaps and so on. This is where databases come in handy.\
SQL databases are designed to store structural data effectively. They do this by storing data in tables like this:

| uuid | coins |
| :--- | :--- |
| 0006b9ed-2592-461c-aa82-be3e7efe6006 | 70 |
| 14a13f3a-535c-45e8-aec7-6eea5b90f9d5 | 20 |
| 2917b45f-e1ab-42a8-953b-a30124d18a8b | 96 |
| 3f7141c7-5355-4244-b70c-79034c365db5 | 54 |
| 5395581d-fc92-470b-9de8-90e5ef415b2c | 85 |

Looking at the image, I think you might be seeing how this comes in handy. Data is stored neatly as entries or rows of
data. Each kind of data is classified under each column.\
The data is structured in columns. Why am I always say that the data is structured? Because there is unstructured data
as well. That's something where you would use so called noSQL database, although the term "noSQL" is a bit misleading,
since it is a very broad field, but that's something for another time.

Since the data is structured SQL databases can do lot more than just storing data. They can define rules for values in a
column or even validate combination of columns. They can also link columns in different tables together and delete
dropped rows when the linked row is deleted. This is also a great way to keep data consistency, but we are already going
into the topic without clarifying some things. Let's get started with some prerequisites.

# Prerequisite

In order to get this done properly we will need some things to prepare and clarify.

## Driver Implementation

Of course spigot has some kind of mysql database driver included, however this driver was never really intended to be
used by the public and is also pretty old (As of 14.4.2021 the version is 5.1.49. Latest is 8.0.23). So you will be good
with getting your own version of it.\
Also your plugin will break whenever the driver path changes, or the driver is relocated. In fact the spigot way doesn't
work with paper anymore, and I want to provide a general best practise on this page.\
You can find the latest mysql driver [here](https://mvnrepository.com/artifact/mysql/mysql-connector-java/latest).\
Throw this in your gradle, maven or whatever and don't forget to shade and relocate this.

We are using a standard mysql connector, however if you use MariaDB you can also use the MariaDB driver directly. You
will find the latest version [here](https://mvnrepository.com/artifact/org.mariadb.jdbc/mariadb-java-client/latest).
Note: The ConnectionPool implementation is a bit different there. The class is called `MariaDbPoolDataSource`.

### Different Database -> Different Driver and Queries

Every database has its own database driver. Nearly every database has some different syntax. If you want to support
different databases you will probably need different queries for each database and a different database driver. If you
want to make your life a bit easier but support multiple database I would recommend HikariCP to you.

If you are looking for the driver for a database simply google for `<database name> jdbc`.

Note: Not all database drivers have an own implementation for connection pooling.\
Postgres for example will remove it in a future release. In this case you have to use HikariCP anyway.\
This is mostly done, because the database driver should not be responsible for implementing a proper connection pooling
management.

## Async and Synced calling

The examples will all use synced requests. You always want to use async requests on a production system to avoid that
the database requests slow down your server. You will find more about about
this [here](https://www.spigotmc.org/wiki/asynchronously-working-with-a-database/). \
The repository linked at the end also contains an example implementation how to use a database async in bukkit. You
never want to execute any sql functions inside your main thread. This is the baddest idea you can have. I promise that
it will cause TPS loss on your server. The database has no fixed return time. At some point you may have to wait for
connections to be freed or network latency slows down your requests.

## Connection Pooling and HikariCP

In our example we will use a pooled data source provided by the mysql driver, which is the easiest way, however it has
some drawbacks. For example the mysql connection pool does not have any way to restrict how many connections you can
open, but your database will. If you use too many Threads to call your database it will fail at some point.\
Frameworks like [HikariCP](https://github.com/brettwooldridge/HikariCP) can help you with managing your connections and
improve your database connection.\
If you want to know more about connection pooling with HikariCP you can look at
this [thread](https://www.spigotmc.org/threads/480002/).

### Read everything

I know this is a much to read, but please take your time and go through everything.

# Try With Resources

A `Connection`, `Statement` (`PreparedStatement`) and `ResultSet` are `AutoCloseable`.\
This means that they are closeable, but can be also closed automatically (obviously...).

When you open a connection this connection will stay open until it is closed.\
A statement needs cache until it is closed.\
A Result set is also cached until it is closed, or the statement is closed.

If you are missing to close it you will have a memory leak, and you will block connections and/or cache.\
You could also run out of free connections at some point.

To avoid this you want to use a `Try-with-Resources`.\
This ensures that all closeables are closed when you leave the code block.

Here is some "pseudo code" which shows you the advantage of an auto closeable.

The old and wrong way shown pretty much everywhere looks like this. (don't look to long at it. Its wrong anyway...)

``` java
try {
    Connection conn = getConnection();
    PreparedStatement stmt = conn.prepareStatement("SELECT some from stuff");
    stmt.setSomething(1, something);
    ResultSet rs = stmt.exeuteQuery();
    // do something with the ResultSet

    // The following part is missed most of the time. Many ppl forget to close their stuff.
    conn.close();
    stmt.close(); // Closing the Statement closes the ResultSet of the statement as well. 
} catch (SQLException e){
    e.printStackstrace(); // This should be replaced with a propper logging solution. don't do this.
}
```

With `AutoCloseable` you don't have to bother about closing your stuff anymore.\
We will also use a DataSource named source which we cached somewhere inside our class
(No we don't get it via a static variable from somewhere. This is bad design...)

``` java
try (Connection conn = source.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT some from stuff")) {
    ResultSet rs = stmt.exeuteQuery();
    stmt.setSomething(1, something);
    ResultSet rs = stmt.exeuteQuery();
    // do something with the ResultSet
} catch (SQLException e) {
    e.printStackstrace(); // This should be replaced with a propper logging solution. don't do this.
}
```

You can see, that we don't close our stuff here, because we don't need it. Any object you assign inside the
braces `(...)`
of the try block `try (...) {...}` will be closed when you exit the code block.\
This will return the connection to our connection pool, free the blocked memory for the result cache and statement. Now
we are happy and ready for the next request.\
Obviously object assigned inside the braces need to be of type `AutoCloseable`.\
(Hint: Many more classes are auto closeable. Like input and output streams for example. Keep a look at the stuff you are
using and use try with resources wherever you can.)

One more addition here. The result set is also an auto closeable, but we don't create it inside the try braces. It will
still be closed. Let's take a look at the ResultSet documentation.
> A ResultSet object is automatically closed when the Statement object that generated it is closed, re-executed, or used to retrieve the next result from a sequence of multiple results.
>
[Source](https://docs.oracle.com/javase/7/docs/api/java/sql/ResultSet.html)

That's it. That's Try-with-Resources. Your connection, statement and result set are freed when you exit the code block,
and you don't have to care about it anymore.

# Setting up a Connection

With the knowledge about Try-with-Resources we can get serious now. Time to connect to our database.\
Hopefully you have imported the database driver your want to use.

First we need to create our DataSource. Like mentioned before: Every database driver has other class names and classes.
Not all database driver implement connection pooling, however the MySQL and MariaDB driver implement connection
pooling.\
To create a data source for one of these database simply create a new instance of the connection pools:\
MySQL:

``` java
MysqlDataSource dataSource = new MysqlConnectionPoolDataSource();
```

MariaDB

``` java
MariaDbPoolDataSource dataSource = new MariaDbPoolDataSource();
```

To connect to your database you need the connection data like you always need. Load it from your config. Don't hardcode
anything like this. This is very bad practise.

You config will contain something like this:

```yaml
database:
  host: localhost # host of your database
  port: 3306 # default port for MariaDB and MySQL
  database: db # name of your database. A database server can contain multiple databases
  user: username
  password: passy
```

Now we need to configure our DataSource. Both DataSources provide the same methods:

``` java
Database database = config.getDatabase();
// we set out credentials
dataSource.setServerName(database.getHost());
dataSource.setPassword(database.getPassword());
dataSource.setPortNumber(database.getPort());
dataSource.setDatabaseName(database.getDatabase());
dataSource.setUser(database.getUser());
```

_Note: Every database driver implementation contains some reasonable default values. If you don't set a port, the
default port of the database is used. The default host is always "localhost". The default value depends on the database
driver type._

_Note for MariaDB driver: While the mysql DataSource doesn't implement a max connection count the DataSource for MariaDB
does implement it. You can set it via:_

``` java
dataSource.setMaxPoolSize(8); // Default value is 8. 8 connections should be more then enough for most plugins.
```

Now we want to test our connection.\
Please note that we use Try-with-Resources here and everywhere from now on.

``` java
private void testDataSource(DataSource dataSource) throws SQLException {
    try (Connection conn = dataSource.getConnection()) {
        if (!conn.isValid(1000)) {
            throw new SQLException("Could not establish database connection.");
        }
    }
}
```

This snippet will create a connection to the database and will wait 1000 ms for a response from the database.\
If the connection is not valid we throw an exception to stop our process, if the connection can't be established an
error will be thrown by the database driver anyway.

When nothing went wrong we now have a working connection pool to our database.

You probably want to wrap the code above inside a method and let this method return a `DataSource` class.

``` java
private DataSource initMySQLDataSource() throws SQLException {
    MysqlDataSource dataSource = new MysqlConnectionPoolDataSource();
    // set credentials

    // Test connection
    testDataSource(dataSource);
    return dataSource;
}
```

You call this method in your `onEnable()` method and cache the returned datasource in a
field `private DataSource dataSource` in your plugin class.

# Building Tables

Now that we have our database connected we need to store our data somewhere. Like mentioned at the beginning SQL
databases store data in tables, but databases can do a lot more than just storing data in tables. They can validate and
pre organize your data to yield results faster and provide data consistency.

We will take a look at different methods in this section.

## Creating a table

To create a table we need to define the columns with a name, and the required data type. You can find the
types [here](https://www.w3schools.com/sql/sql_datatypes.asp). Some of these types have a `(size)`
parameter. For String data types this defines the maximum size of the data written in this column, however for numeric
types it does NOT. `INT(2)` won't restrict the integer value written to this column in any way. This is wrong knowledge
spread out widely. See the [documentation](https://dev.mysql.com/doc/refman/5.7/en/numeric-type-attributes.html) if you
want to know more.

_Note: Other databases might have different types. Read the documentation of your database._

_Note 2: Some types have different names than the java data types. `Bigint` in SQL is equal to a `long` in Java for
example._

There are some best practises which data type you have to choose:

- String with always the same size (E.g. UUID)-> `CHAR(size)` (Max 255 chars)
- String with known max size (E.g. Player names) -> `VARCHAR(size)` (Max 65,535)
- Text of unknown length -> `TEXT` (~32,700 character)
- Text which is expected to be large -> `MEDIUMTEXT` (16,777,215 characters) or `LONGTEXT` (4,294,967,295 characters)
- UUID can also stored ad `BINARY(16)` (See "Nice to know" section for more)

I would recommend to take a dive into the documentation.

An example table to store some coins for a player would look like this:

``` sql
-- always make sure to use "if not exists" to avoid errors when the table is alread defined.
CREATE TABLE IF NOT EXISTS player_coins
(
    -- A uuid has 36 characters.
    -- That's why we define our uuid column with a size of 36.
    -- We also say that this column should never be null.
    uuid  CHAR(36)         NOT NULL,
    -- We create a coin column with a bigint. This is equal to a long in java.
    -- We also say that this column should never be null.
    -- If you just insert a new uuid into this table the coin column will be 0 by default.
    coins BIGINT DEFAULT 0 NOT NULL,
    -- we create a primary key "coins_pk" on the uuid column.
    -- This means that a value in the uuid column can be only one time in the column.
    primary key (uuid)
);
```

You may have noticed some other parameter there than just the column type. We will come to this in a moment.

## Data consistency

SQL tables are not just tables. They have some kind of their own mind and can decide which data can be inserted and how
it should be inserted.

### Default

The `default` keyword is pretty useful.\
If you look on our statement above you will notice that `coins` has a default value.\
This means that whenever we insert a new uuid to this table without inserting a value into the `coins` column, this
value will be 0.

### not null

The `not null` keyword should be obvious. It prevents you or someone else to insert a `null` value into this column.\
This is pretty useful as well.\
You never want to have some coins in your table with a `null` uuid or an uuid with `null` coins.

### Primary keys

Our `primary key (uuid)` creates a primary key for the uuid column.\
This has two advantages:

- A UUID can't exist more than one time in this table.
- An index is created on the uuid column which speeds up your search for a specific uuid.

The first advantage is called constraint. The second advantage is called index. A primary key is both in one.

### Constrains and Indices

A table can only have one primary key, but you maybe want to have more constraints to have better data consistency and
faster searches.\
That's why we can create own constraints and indices for our table.

To show you this we need a more complex table:

```sql
CREATE TABLE IF NOT EXISTS player_boosts
(
    uuid     CHAR(36)  NOT NULL,
    boost_id INT       NOT NULL,
    until    TIMESTAMP NOT NULL,
    CONSTRAINT player_boosts_constraint
        UNIQUE (uuid, boost_id)
);

CREATE INDEX IF NOT EXISTS player_boosts_until_index
    ON player_boosts (until ASC);
```

The booster table contains all users with active boosters and a `boost_id`. It also contains a timestamp when this
booster will run out.

Since we have more than one booster probably we want to have an uuid multiple times in this table, but we don't want the
same player with the same booster more than one time in this table. That's why our `CONSTRAINT` is a combination
of `uuid` and `boost_id`. This means that the combination of these two columns must be unique in this table.

The `CONSTRAINT` could be replaced by a primary key in the current case. I just used it here because I wanted to show
you the syntax for it. When you have more complex tables you probably will need more constraints next to the primary
key.

_Note: The names for the constraints and indices can be chosen freely, however it's recommended to use useful names._

However something other interesting is happening here. We create an index on the `until` column. This index is sorted
which meant that we already have this table in a sorted form by the `until` timestamp. We will have the lowest timestamp
at the top and the timestamps in the far future at the end of the table.

Since we probably want to see which boosters will run out shortly we want to select the most recent booster which end.
We can to this by ordering the table by until and then select the next x entries.

As a query this would look like this (don't worry. Everything here will be explained later. A further explanation is
added at the end of the article.):

``` sql
EXPLAIN select
	uuid,
	boost_id
from
	player_boosts
where until < date_sub(now(), INTERVAL 1 HOUR);
```

| id | select\_type | table | type | possible\_keys | key | key\_len | ref | rows | Extra |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| 1 | SIMPLE | player\_boosts | range | player\_boosts\_until\_index | player\_boosts\_until\_index | 4 | NULL | 1 | Using where; Using index |

Since we already have the table sorted by until the database will automatically search in the index instead of the table
itself.

# Setting up your database

Now that we are connected we need to ensure that we will find the tables in our database we are looking for.\
Most people do this in their code by writing very long table create statements. We won't do this.\
We will ship our required table layout in a file in our plugin.\
It's considered the best practise to not include large sql statements in your code directly. This may change with java
15, which allow quoteblocks now, but we know that it will be a long time until we can use java 15 in production for
minecraft server, so we stick with the "old fashioned" way.

Create a file `dbsetup.sql` in your resources.\
We now write all statements to create our tables in this file.

``` sql
CREATE TABLE IF NOT EXISTS something
(
    [...]
);

CREATE TABLE IF NOT EXISTS somewhat
(
    [...]
);
[...]
```

Please notice that we end each statement with a `;` this is required to know where the statement ends. We also use
the `IF NOT EXISTS` keyword everywhere, otherwise our setup would fail on the next startup.

Now we need to execute this in our database.\
For this we will create a `initDb()` method in our plugin and call it after our datasource assignment.

This method will read our `dbsetup.sql` file and execute the statements one by one into our database.\
Please note that this method will throw a SQLException whenever something went wrong.\
This will abort the setup, since there is no sense to run our plugin without a properly initialized database.

``` java
private void initDb() throws SQLException, IOException {
    // first lets read our setup file.
    // This file contains statements to create our inital tables.
    // it is located in the resources.
    String setup;
    try (InputStream in = getClassLoader().getResourceAsStream("dbsetup.sql")) {
        // Java 9+ way
        setup = new String(in.readAllBytes());
        // Legacy way
        setup = new BufferedReader(new InputStreamReader(in)).lines().collect(Collectors.joining("\n"));
    } catch (IOException e) {
        getLogger().log(Level.SEVERE, "Could not read db setup file.", e);
        throw e;
    }
    // Mariadb can only handle a single query per statement. We need to split at ;.
    String[] queries = setup.split(";");
    // execute each query to the database.
    for (String query : queries) {
        // If you use the legacy way you have to check for empty queries here.
        if (query.isBlank()) continue;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.execute();
        }
    }
    getLogger().info("§2Database setup complete.");
}
```

After our script is executed all tables should be created, and we are ready to go.

## Sidenote on versioning

Versioning a database is hard and there are not many best practises, however I am using a system with setup, incremental
patches and migration files to accomplish this. This system is probably not required for most plugins, so you can skip
this if you are not interested.

If you want to keep track of your database version create a single table like:

```sql
CREATE TABLE IF NOT EXISTS plugin_name_db_version
(
    version int not null,
    patch   int not null
);
```

This table should contain the version and patch version of your database.\
You can check on a startup which database version your database has.\
Now you can include incremental patch files in your plugin with a structure like this:

```yaml
database/1/setup.sql
database/1/patch_1.sql
database/1/patch_2.sql
database/1/patch_3.sql
database/1/migrate.sql
database/2/setup.sql
database/2/patch_1.sql
database/2/patch_2.sql
```

`setup` files contain a full new database setup. These are used if no database is present.\
`patch` files are applied on the `setup` until the current version is reached.\
If the major version needs to be changed, all patches will be applied first and then the `migrate` script is executed.\
Of course you need to store your required database version somewhere in your plugin.

# Best practises

While working with databases I found some best practises I want to share with you.

## Prepared Statements

Using prepared statements is crucial when writing user input into your database.\
Prepared statements will protect you from SQLInjection. They will also improve performance because the database can
cache these statements better.\
They will also ensure that your value is interpreted correctly. So your string stays a string, and your integer stays a,
integer.

You can see a prepared statement below. don't think about the SQL query itself for now.\
You may notice the `?` inside the query. These are placeholders for the actual values.\
These values are set by the `set...()` methods provided by the PreparedStatement object.\
You need to choose the correct method to set your value, which is pretty straight forward. Then you enter the number of
the `?` you want to replace and add the value you want to set there. The index of the placeholders is not zero-based and
instead starts at 1.

``` java
public boolean someMethod(Object obj1, Object obj2) {
    try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
            "INSERT INTO player_coins(uuid, coins) VALUES(?, ?);"
    )) {
        stmt.setSomething(1, obj1);
        stmt.setSomething(2, obj2);
        stmt.execute();
        return true;
    } catch (SQLException e) {
        logSQLError("Something went wrong.", e);
    }
    return false;
}
```

## Using optional

The Optional class is pretty useful when you work with databases.

When you request data from a database you have several possible outcomes:

- We found data
- We didn't found data
- An error occurred

You don't want to deliver false data in case of an error or when no data is returned.\
The Optional indicates that a value can be returned by this function, but it also says that its possible that no value
is returned.\
The "old" way would be to return `null` or a default value, however this is bad style, and you will probably forget to
perform a null check.

The Optional can be used very simple:

``` java
// A empty optional. The old null value.
Optional.empty()
// wraps the value into an optional
Optional.of(anyValue) 
// wraps a value which might be null. If the value is null this is equal to Optional.empty()
Optional.ofNullable(anyValue)
```

There are some special Optionals designed for specific types like `OptionalLong`, `OptionalInt`, `OptionalDouble`
and `OptionalBoolean`.

In general the whole think would look like this:

``` java
public OptionalLong getCoins(Player player) {
    try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
            "select coins from player_coins where uuid = ?;"
    )) {
        stmt.setString(1, player.getUniqueId().toString());
        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()) {
            return OptionalLong.of(resultSet.getLong("coins"));
        }
        return OptionalLong.empty();
    } catch (SQLException e) {
        logSQLError("Could not retrieve player coins.", e);
        return OptionalLong.empty();
    }
}
```

If we get an error or found nothing in the database we return `OptionalLong.empty()`. This will signalize that we don't
have or could not retrieve any data.\
If we found data we wrap it into an Optional.

## Returning boolean

If we don't return any data we still want to know if our transaction was a success in this case we just return false in
the case of an exception and true otherwise.\
This will prevent us from sending a confirm message if something went wrong.

``` java
public boolean someMethod(Object obj1, Object obj2) {
    try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
            "INSERT INTO player_coins(uuid, coins) VALUES(?, ?);"
    )) {
        stmt.setSomething(1, obj1);
        stmt.setSomething(2, obj2);
        stmt.execute();
        return true;
    } catch (SQLException e) {
        logSQLError("Something went wrong.", e);
    }
    return false;
}
```

# Working with data

Now we can finally come to the cool part.\
Time to play with some data!\
This section will cover all basic operations you will need to start working with your database.

For space reasons I will only show you the prepared statements, without any java code.

Our table looks like this:

``` sql
CREATE TABLE IF NOT EXISTS player_coins_2
(
    uuid  CHAR(36)         NOT NULL,
    coins BIGINT DEFAULT 0 NOT NULL,
    PRIMARY KEY (uuid)
);
```

No value is allowed to be `null` and every `uuid` can be only one time in this table.

## Writing data

Writing data is essential. What should we read if we don't write?\
So lets start with writing. We have three different ways: `INSERT`, `UPSERT` and `REPLACE`

### Insert

The `INSERT` statement is probably one of the most common, and most simple pattern.\
You insert some data into some columns of a table.

Remember the constraints from earlier? You will need to insert the data in the correct way otherwise they will prevent
you from inserting data. The insert will fail if we violate any rules of the table.

So lets insert some data:

``` java
public boolean addCoins(Player player, long amount) {
    try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
            "INSERT INTO player_coins(uuid, coins) VALUES(?, ?)")) {
        stmt.setString(1, player.getUniqueId().toString());
        stmt.setLong(2, amount);
        stmt.execute();
        return true;
    } catch (SQLException e) {
        logSQLError("Could not add coins", e);
    }
    return false;
}
```

This is super convenient for the beginning.\
However basic inserts are not very common because you normally have some kind of logic in your table.\
This insert would only work one time because we have the primary key.\
If we wouldn't have the primary key we would have to count the coins in every entry for each player, which would be way
more work than just updating it, right?\
That's where we come to the upsert.

### Upsert

The upsert is a mix of `INSERT` and `UPDATE`.

``` java
public boolean addCoins(Player player, long amount) {
    try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
            "INSERT INTO player_coins(uuid, coins) VALUES(?, ?) ON DUPLICATE KEY UPDATE coins = coins + ?;")) {
        stmt.setString(1, player.getUniqueId().toString());
        stmt.setLong(2, amount);
        stmt.setLong(3, amount);
        stmt.execute();
        return true;
    } catch (SQLException e) {
        logSQLError("Could not add coins", e);
    }
    return false;
}
```

You can see that this is quite similar to the `INSERT` statement from above. This time we added something at the end.\
`ON DUPLICATE KEY UPDATE`\
This means that if we encounter a duplicate key (in our case the uuid) we want to update the entry otherwise we just
insert it.\
We are doing this by setting the value of the `coins` column to the current coin count plus the value we want to
add `coins = coins + ?`

You probably never want to just insert data.

### Replace

`REPLACE` is like a `DELETE` and `INSERT`. It will first delete your old row, if a conflict with the primary key exists,
and then insert the new data. Since this is an actual `INSERT`, this will also create a new id if you use an auto
increment column. Also have in mind that you can get duplicated entries if you don't setup your primary/unique keys
correctly.

``` java
public boolean setCoins(Player player, long amount) {
    try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
            "REPLACE player_coins(uuid, coins) VALUES(?, ?);"
    )) {
        stmt.setString(1, player.getUniqueId().toString());
        stmt.setLong(2, amount);
        stmt.execute();
        return true;
    } catch (SQLException e) {
        logSQLError("Could not set coins", e);
    }
    return false;
}
```

## Updating data

Updating data is as important as inserting data.

Most time you want to update data instead of insert new data.

### Update

We could also do the same as above with an `UPDATE`. In this way we would only update the data if we have an entry for
our key.

``` java
public boolean setCoins(Player player, long amount) {
    try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
            "UPDATE player_coins SET coins = ? WHERE uuid = ?"
    )) {
        stmt.setLong(1, amount);
        stmt.setString(2, player.getUniqueId().toString());
        return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
        logSQLError("Could not set coins", e);
    }
    return false;
}
```

You see something new here. The `WHERE` clause defines which columns we want to update. This time we say that we want to
update the coins on a specific uuid.\
Our return value changed as well. The method `executeUpdate()` returns us the number of rows we updated. If the result
is 0 we don't updated anything, so we return false.

This mechanic can be used to make some nice thread save take operations without checking if a player has sufficient
coins.

``` java
public boolean takeCoins(Player player, long amount) {
    try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
            "UPDATE player_coins SET coins = coins - ? WHERE uuid = ? AND coins >= ?;"
    )) {
        stmt.setLong(1, amount);
        stmt.setString(2, player.getUniqueId().toString());
        stmt.setLong(3, amount);
        int updated = stmt.executeUpdate();
        return updated == 1;
    } catch (SQLException e) {
        logSQLError("Could not take coins from player.", e);
    }
    return false;
}
```

This method will take coins from a player with a specific uuid if the player has enough coins.\
If we get an updated row we know that the player had enough money.\
Use this whenever you want to take a something from a player if he has enough credits. If you check first whether the
player has enough credits or not and take it if he has, you lose thread safety.

## Deleting data

Sometimes you need to delete data from your tables.

``` java
public boolean deleteCoins(Player player) {
    try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
            "DELETE FROM player_coins WHERE uuid = ?;"
    )) {
        stmt.setString(1, player.getUniqueId().toString());
        int updated = stmt.executeUpdate();
        return updated == 1;
    } catch (SQLException e) {
        logSQLError("Could not delete coins of player.", e);
    }
    return false;
}
```

A `DELETE` statement is also a type of update. That's why we can use the same mechanism, like earlier, to check if we
deleted the player.\
If you execute a `DELETE` without `WHERE` your whole table will be wiped which would have the same effect as `TRUNCATE`.

## Reading Data

Now that you can manage your data in your database it's finally time to read it.

### ResultSet

Whenever you request data via a `SELECT` statement you will get a `ResultSet`.\
To get the `ResultSet` you need to use the `executeQuery()` method on your statement.

A result set contains the rows you requested. The rows are selected by a pointer. When you get the result set this
pointer will be before the first row.

To move the pointer to the next row you have to call the `next()` method on your `ResultSet`. This method does not only
move the pointer forward it also tells you if there is a row.\
If next returns `false` no row can be read.

### Select Single Row

If you select a row via a primary key like our uuid you will get only one row.

``` java
public OptionalLong getCoins(Player player) {
    try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
            "SELECT coins FROM player_coins WHERE uuid = ?;"
    )) {
        stmt.setString(1, player.getUniqueId().toString());
        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()) {
            return OptionalLong.of(resultSet.getLong("coins"));
        }
        return OptionalLong.of(0);
    } catch (SQLException e) {
        logSQLError("Could not retrieve player coins.", e);
        return OptionalLong.empty();
    }
}
```

We check if our `ResultSet` contains a row with the `next()` method, if so we get the column with the name coins. A `*`
selector should be avoided. It's unreadable for other people reading your code and bad practise.

### Select Multiple Rows.

If we want to select multiple rows we have to do it a bit different.

``` java
public Optional<List<CoinPlayer>> getCoins() {
    try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
            "SELECT uuid, coins FROM player_coins;"
    )) {
        ResultSet resultSet = stmt.executeQuery();
        List<CoinPlayer> coins = new ArrayList<>();
        while (resultSet.next()) {
            coins.add(
                    new CoinPlayer(
                            UUID.fromString(resultSet.getString("uuid")),
                            resultSet.getLong("coins"))
            );
        }
        return Optional.of(coins);
    } catch (SQLException e) {
        logSQLError("Could not fetch all player coins", e);
        return Optional.empty();
    }
}
```

You see several new things here.\
First we select both columns (We are not using `*`, although we select all columns).\
Then we loop through the ResultSet with a while which will move the pointer to the next row until we hit the end of the
ResultSet.\
Also please note that the Result is wrapped into a CoinPlayer. This is way cleaner than just returning a map or
something else. Especially when you want to return multiple rows, you should wrap them into an object.

### Select only what you need

There are only very rare cases where you will need the complete table. In the most cases you pull the table to do some
sorting on it locally, however the database can do this as well.\
Don't select what you don't need.

Let's say we want to get the top x player with the most coins.\
Of course you can pull the complete table, sort it in java and get a sublist of the x first entries.\
But this is way easier:

``` java
public Optional<List<CoinPlayer>> getTopCoins(int amount) {
    try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
            "SELECT uuid, coins FROM player_coins ORDER BY coins DESC LIMIT ?;"
    )) {
        ResultSet resultSet = stmt.executeQuery();
        List<CoinPlayer> coins = new LinkedList<>();
        while (resultSet.next()) {
            coins.add(
                    new CoinPlayer(
                            UUID.fromString(resultSet.getString("uuid")),
                            resultSet.getLong("coins"))
            );
        }
        return Optional.of(coins);
    } catch (SQLException e) {
        logSQLError("Could not fetch all player coins", e);
        return Optional.empty();
    }
}
```

The `ORDER BY ... DESC` clause sorts the whole table by coins in an descending order.\
The `LIMIT` keyword will only return the x player with the most coins.

When reading the result set you already have the x top players in a sorted list.

# Nice to know

This section will contain nice to knows which didn't found its own place in the article.

## Sorted indices and EXPLAIN

Remember our player coins from previously? I inserted some data:

| uuid | coins |
| :--- | :--- |
| 0006b9ed-2592-461c-aa82-be3e7efe6006 | 70 |
| 14a13f3a-535c-45e8-aec7-6eea5b90f9d5 | 20 |
| 2917b45f-e1ab-42a8-953b-a30124d18a8b | 96 |
| 3f7141c7-5355-4244-b70c-79034c365db5 | 54 |
| 5395581d-fc92-470b-9de8-90e5ef415b2c | 85 |
| 66c4189f-5245-4831-b71c-4346ff8a408d | 30 |
| 6e1fa064-8e68-4c6f-8b81-1ee085ef322b | 223 |
| 817d6f02-45c7-4ac6-8520-08d5aa71e0ce | 10 |
| 8d85120b-d576-4abe-924c-0eee16e16aa5 | 60 |
| 979a0446-30b7-492b-90db-67c481596742 | 78 |
| a0f1d4ed-215c-4c17-bb6c-27f40ecfa21b | 11 |
| a9fc9610-1ec0-4501-8778-e2a85da2a580 | 50 |
| bcfa3ceb-cd93-4bab-9f5c-993cab7f9aad | 40 |
| d6a721d5-89ef-4d1b-9e1b-226e32847ad9 | 80 |
| f222744e-6cce-424b-b00a-450bdad4eb07 | 21 |

To speed up your search from the top coins example even more you could create an index which is already presorted. When
you do this, your database don't have to sort anymore and can retrieve the results directly by looking into the index,
currently when do something like this:

``` sql
SELECT
	uuid,
	coins
FROM
	player_coins
ORDER BY coins DESC
LIMIT 10;
```

We read the whole table, sort it by coins and then take the first 10 rows. Your database can tell you this as well.\
Run this query:

``` sql
EXPLAIN SELECT
	uuid,
	coins
FROM
	player_coins
ORDER BY coins DESC
LIMIT 10;
```

The `EXPLAIN` keyword shows you what the database will do, currently our explain looks like this:

| id | select\_type | table | type | possible\_keys | key | key\_len | ref | rows | Extra |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| 1 | SIMPLE | player\_coins | ALL | NULL | NULL | NULL | NULL | 15 | Using filesort |

Type shows us that we are using all entries in the table. Rows say that we are reading 15 rows, which are all rows in
the table, and we are using filesort which is not pretty fast.

Now execute this query:

``` sql
CREATE INDEX player_coins_coins_index
	ON player_coins (coins DESC);
```

This will create a sorted index on the table sorted by coins in descending order. You remember that we used a `ORDER BY`
clause in our query previously. This query will now read the index instead of the table itself, which is quite fast,
because it just takes the first x entries from the index instead of reading all values sorting them and then taking the
first x entries.

You can check this by using the `EXPLAIN` statement from above again.

| id | select\_type | table | type | possible\_keys | key | key\_len | ref | rows | Extra |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| 1 | SIMPLE | player\_coins | index | NULL | player\_coins\_coins\_index | 8 | NULL | 10 | Using index |

You can see that some things changed:

- The type changed from `ALL` to `index`, which means that we are only searching the index instead of the whole table.
- The key changed from `null` to `player_coins_coins_index` which shows us, that we're using the index we previously
  created.
- The rows count went down from `15` to `10` which means we only read the lines we want to return instead of all rows in
  the table. Instead of all we use the index as search type and extra mentions that we are using an index instead of the
  table.
- Any `extra` changed from `Using filesort` to `Using index`.

This is great. Our index works perfectly! Instead of reading the whole table we read the index, and we just read a part
of the index since the index is sorted.

## UUID in database

UUIDs can be stored as `BINARY(16)` or `CHAR(36)`. Consequently a char UUID requires more space than a binary UUID.\
Your goal should be to keep your columns as small as possible. Also binary data can be indexed better. A binary UUID
will also keep your index smaller, which is important because the database will try to cache the indices and load them
into memory. This is only possible as long as the index is small enough, so it is always a good practice to keep your
indexed columns as small as possible.

You can use these functions to convert your `UUID` to `byte[]` and your `byte[]` to `UUID`:

``` java
public static byte[] convert(UUID uuid) {
    return ByteBuffer.wrap(new byte[16])
            .putLong(uuid.getMostSignificantBits())
            .putLong(uuid.getLeastSignificantBits())
            .array();
}

public static UUID convert(byte[] bytes) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
    return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
}
```

You can use these methods like this:
``` java
// Convert UUID to bytes
PreparedStatement stmt = conn.prepareStatement("...");
stmt.setBytes(?, UUIDConverter.convert(player.getUniqueId()));

// Convert bytes to UUID
ResultSet rs = stmt.executeQuery();
UUID uuid = UUIDConverter.convert(rs.getBytes("uuid"));

```

# Conclusions

You now have the tools to create some reasonable table layout for your application, However you just read the basics.
There is so much more out there to learn when it comes to SQL databases.

Databases can do a lot of work faster and better than your application. Remember to always use the constrains to keep
data consistency and indices on columns you want to search. Try to always do as much presorting and filtering in your
query instead of reading the data and sort it outside the database.

And please don't forget to use prepared statements and a datasource. These will make your life so much easier.

If you want to learn some more things you may want to check out the further reading section below.

# Further Stuff

A sample implementation with all shown Queries and Stuff can be
found [here](https://github.com/RainbowDashLabs/BasicSQLPlugin). An async implementation can be found in the repository
as well. Contributions welcome.

A guide how to work with HikariCP can be found [here](https://www.spigotmc.org/threads/480002/)

An article how to work with databases asynchronously in bukkit can be
found [here](https://www.spigotmc.org/wiki/asynchronously-working-with-a-database/).