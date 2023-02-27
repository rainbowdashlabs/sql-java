# What is JDBC

Yeah, that's probably the first question we should look into. JDBC means **J**ava **D**ata**b**ase **C**onnectivity.

JDBC is part of java and defines the components required to interact with a database via java. You will find the
interfaces of it located at `javax.sql` and is supplements by `java.sql`. Usually when we interact with our database we
will use interfaces contained in those packages. They contain several interfaces which define
how to implement a Connection, Statement, PreparedStatement, ResultSet, DataSource and Driver.

## Connection

The connection interface defines the methods for a database connection. It allows us to send statments and prepared
statements to our database.

## Statement and PreparedStatement

The statement and prepared statement interfaces allow us to define our SQL statement and send it to the database. The
prepared statement allows us additionally to set variables in our SQL query and escapes our input to avoid SQL
injection.

## ResultSet

The ResultSet is what we get when we read data from our database. It allows to access the value of different columns and
going through the rows we selected. You can imagine it as a table with the rows and colums we selected in our statement.
It also contains additional metadata about the operation we performed.

## DataSource

The DataSource is from the `javax.sql` package, which represents the newer java sql implementation. It is the successor
of the `DriverManager` class from `java.sql`. It is no longer recommended to use the `DriverManager` since java 8 and
the introduction of the `DataSource` class.

Instead of creating a single `Connection` like the `DriverManager` does the `DataSource` is supposed to provide a
connection and ensure its validity. However, what the DataSource really does highly depend on the implementation of it.
Some implementations only provide a single connection and manage it. Some others provide a custom connection which is
kept open and maintained. Some implementations like [HikariCP](https://github.com/brettwooldridge/HikariCP) provide a
DataSource which maintains a pool of connections. We will look into different `DataSource` implementations and
ultimately end up with HikariCP, which is the state of the art way for connection management and pooling. Most databases
only provide basic `DataSource` implementations which are good for playing around but not really made for real world
use.

## Driver

The driver is the heart of every JDBC implementation. It is basically the translator between java and your database. You
will most likely never use the Driver directly, but it is good to know it is there and that it is important.

If you want to know more take a look at
the [wikipedia article](https://en.wikipedia.org/wiki/Java_Database_Connectivity).



