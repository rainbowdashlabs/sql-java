# Versioning

Having a database is one thing, but maintaining a database and aplying updates is another thing. I will show you two
ways to maintain your database version. One will be a self build, which is simple but solid the other one will be a
third party tool which is the most used in the industry.

## Building a SQL Updater

Now that we are connected we need to ensure that we will find the tables in our database we are looking for. Most people
do this in their code by writing very long table create statements. We won't do this. We will ship our required table
layout in a file in our plugin. It's considered the best practise to not include large sql statements in your code
directly

Create a file `dbsetup.sql` in your resources.
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
    String[] queries = setup.split(";$");
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

### Versioning

Versioning a database is hard and there are not many best practises, however I am using a system with setup, incremental
patches and migration files to accomplish this. This system is probably not required for most plugins, so you can skip
this if you are not interested.

If you want to keep track of your database version create a single table like:

```sql
CREATE TABLE IF NOT EXISTS plugin_name_db_version (
	version INT NOT NULL,
	patch   INT NOT NULL
);
```

This table should contain the version and patch version of your database.
You can check on a startup which database version your database has.
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

`setup` files contain a full new database setup. These are used if no database is present.
`patch` files are applied on the `setup` until the current version is reached.
If the major version needs to be changed, all patches will be applied first and then the `migrate` script is executed.
Of course, you need to store your required database version somewhere in your plugin.

## Flyway

[Flyway](https://flywaydb.org/) is a tool to apply database patches. It is probably the most common tool in the industry
and provides some additional features like migrate, clean, validate and repair. It also provides roling back and more,
but those features are only available in the paied version of flyway, which might make it less attractive.
