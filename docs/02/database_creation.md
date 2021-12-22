# Database creation

Now that we know what we know what we can have in our database and how to name our stuff we can proceed to finally 
work with it.

Make sure that you are connected to your RDBMS. Use a ssh tunnel like I showed you in the [setup](../01/setup.md) 
ppage when you are connection to a remote database.

Database creation is actually very simple on all our databases.

## SQLite

In SQLite one database is equal to one file. By creating your sql file and entry you have already created your database.

## MySQL, MariaDB and PostgreSQL

To create our database we can use the `CREATE` command.

```sql
CREATE DATABASE sql_starter; -- (1)
```

1. We create a new database with the name `sql_starter`

Our desktop clients allow us to create the database via the ui.

**DataGrip**

Right click on your Database entry and select `New > Database`

Right click on your new database and select `Jump to query console` and select the default one.

**DBeaver**

Right click on your database entry and select `Create New Database`

Right click on your new database and select `SQL Editor > Open SQL Console`

**HeidiSQL**

TBD

### Postgres only

When using PostgreSQL everything we do will be located in the public schema by default.

This schema exists for compatibility with other databases since the schema subdivision is kinda unique.

If you want you can create a schema now to keep your public schema clean.

```sql
create schema my_schema;
```

**DataGrip**

Make a right click on your database and select `New > Schema`

The console in DataGrip is bound to a database and not to a schema by default.

Cou can change this by selecting the new schema at the top right of your console window.


**DBeaver**

Make a right click on your database and select `Create > Schema`

Right click on your new schema and select `SQL Editor > Open SQL Console`. Instead of being bound to the public 
schema your console is now bound to the new schema.

As an alternative you can also change the schema at the top of the console window click on `public@<database_name>` 
and select your new schema.

**HeidiSQL**

TBD


## Database configuration
The configuration of your database like encoding and timezone will be orientated on your system settings.

The options you can change on your database are numerous and I wont cover this here.

As a beginner the default settings should be sufficient for all what you want to do.
