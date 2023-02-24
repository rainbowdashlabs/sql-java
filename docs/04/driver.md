# Driver Implementations

Every database has its own driver implementation. Like mentioned before, those drivers are responsible to translate
between java and your database. They map java datatypes to their sql types and vice versa.

You can usually find those drivers via Google when you search for `<database> jdbc`. You also probably want to use a
build tool to integrate those drivers into your application. But I am sure you know how to do this.

Some drivers are operable for multiple databases. For example the mysql driver can be used for mariadb as well. Of
course, it does not support the full feature set of mariadb, and you will encounter issues when doing more complex
stuff,
but it is possible to perform basic operations.

Let's take a look at the Drivers you will probably need. I will show you the way to import them and link the maven
repository search, where you can find the latest version. All drivers implement the JDBC specification.

## [PostgreSQL](https://mvnrepository.com/artifact/org.postgresql/postgresql)

### Maven
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>version</version>
</dependency>
```

### Gradle
```kts
implementation("org.postgresql", "postgresql", "version")
```

## [MariaDB](https://mvnrepository.com/artifact/org.mariadb.jdbc/mariadb-java-client)

### Maven
```xml
<dependency>
    <groupId>org.mariadb.jdbc</groupId>
    <artifactId>mariadb-java-client</artifactId>
    <version>version</version>
</dependency>
```

### Gradle
```kts
implementation("org.mariadb.jdbc", "mariadb-java-client", "version")
```

## [MySQL](https://mvnrepository.com/artifact/com.mysql/mysql-connector-j)

### Maven
```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>version</version>
</dependency>
```

### Gradle
```kts
implementation("com.mysql", "mysql-connector-j", "version")
```

## [SqLite](https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc)

### Maven
```xml
<dependency>
    <groupId>org.xerial</groupId>
    <artifactId>sqlite-jdbc</artifactId>
    <version>version</version>
</dependency>
```

### Gradle
```kts
implementation("org.xerial", "sqlite-jdbc", "version")
```





