# Treiber-Implementierungen

Jede Datenbank hat ihre eigene Treiberimplementierung. Wie bereits erwähnt, sind diese Treiber für die Übersetzung
zwischen Java und deiner Datenbank. Sie bilden Java-Datentypen auf ihre SQL-Typen ab und umgekehrt.

Du kannst diese Treiber normalerweise über Google finden, wenn du nach "<Datenbank> jdbc" suchst. Wahrscheinlich möchtest du auch ein
Build-Tool verwenden, um diese Treiber in deine Anwendung zu integrieren. Aber ich bin sicher, du weißt, wie man das macht.

Einige Treiber können für mehrere Datenbanken verwendet werden. Zum Beispiel kann der mysql-Treiber auch für mariadb verwendet werden. Von
Natürlich unterstützt er nicht den vollen Funktionsumfang von mariadb und du wirst Probleme bekommen, wenn du komplexere
Dinge,
aber es ist möglich, grundlegende Operationen durchzuführen.

Werfen wir einen Blick auf die Treiber, die du wahrscheinlich brauchen wirst. Ich zeige dir, wie du sie importierst und die Maven
Repository-Suche, wo du die neueste Version finden kannst. Alle Treiber implementieren die JDBC-Spezifikation.

## [PostgreSQL](https://mvnrepository.com/artifact/org.postgresql/postgresql)
**Aktuelle Version**

![Aktuellste Version](https://img.shields.io/maven-central/v/org.postgresql/postgresql)

### Maven
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>Version</version>
</dependency>
```

### Gradle
```kts
implementation("org.postgresql", "postgresql", "version")
```

## [MariaDB](https://mvnrepository.com/artifact/org.mariadb.jdbc/mariadb-java-client)
**Letzte Version**

![Neueste Version](https://img.shields.io/maven-central/v/org.mariadb.jdbc/mariadb-java-client)

### Maven
```xml
<Abhängigkeit>
    <groupId>org.mariadb.jdbc</groupId>
    <artifactId>mariadb-java-client</artifactId>
    <version>Version</version>
</dependency>
```

### Gradle
```kts
implementation("org.mariadb.jdbc", "mariadb-java-client", "version")
```

## [MySQL](https://mvnrepository.com/artifact/com.mysql/mysql-connector-j)
**Letzte Version**

![Neueste Version](https://img.shields.io/maven-central/v/com.mysql/mysql-connector-j)

### Maven
```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>Version</version>
</dependency>
```

### Gradle
```kts
implementation("com.mysql", "mysql-connector-j", "version")
```

## [SqLite](https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc)
**Letzte Version**

![Aktuellste Version](https://img.shields.io/maven-central/v/org.xerial/sqlite-jdbc)

### Maven
```xml
<dependency>
    <groupId>org.xerial</groupId>
    <artifactId>sqlite-jdbc</artifactId>
    <version>Version</version>
</dependency>
```

### Gradle
```kts
implementation("org.xerial", "sqlite-jdbc", "version")
```
