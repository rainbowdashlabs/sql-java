# Grundlagen der DataSource

Um eine Verbindung zu einer Datenbank herzustellen, verwenden wir jdbc urls.
Sie sind die zuverlässigste Methode und ermöglichen ein hohes Maß an Anpassungsfähigkeit.
Sie folgen einem einheitlichen Format, das wie folgt aussieht:

jdbc:<Datenbank>://url:port/datenbank

Im Folgenden zeige ich dir, wie du eine DataSource für jede Datenbank erstellst.
Später werden wir zu HikariCP wechseln, wo wir keine DataSource eines bestimmten Typs mehr definieren müssen.

Für jede Datenbank werden wir zunächst die Datenquelle des gewünschten Typs initialisieren und die URL festlegen.
Falls erforderlich, legen wir auch den Benutzernamen und das Passwort fest.
Das reicht für den Moment aus, um eine Verbindung herzustellen.

Das ist die minimale Einrichtung und es gibt eine Menge Konfigurationsmöglichkeiten, die du mit diesen URLs machen kannst.
Ich werde die Dokumentation der Parameter für jede Datenbank verlinken.

Jede Klasse hat eine Methode, die eine DataSource erstellt, und eine Hauptmethode, die diese Methode aufruft.
Sobald wir unsere DataSource erstellt haben, rufen wir eine Verbindung als Ressource ab, erstellen eine Anweisung, führen sie aus und lassen die Anweisung und die Verbindung wieder automatisch schließen.
Da es sich hier nicht um ein Softwarearchitektur-Tutorial handelt, sondern eher um eine Anleitung für Anfänger, möchte ich anmerken, dass dies keine Best Practice in Bezug auf die Struktur ist.

Von nun an werde ich immer davon ausgehen, dass eine DataSource vorhanden ist.
Ich werde nicht zeigen, wie sie erstellt wird.

## Probleme beim Laden von Klassen.

Im Allgemeinen versucht Java, die Implementierung des Treibers für die angegebene Datenbank zu finden.
Für Postgres wird es versuchen, die Treiberimplementierung für den Postgres-Jdbc-Treiber zu finden.
Normalerweise würde diese Klasse bereits geladen werden, wenn wir die Postgres-Datenquelle direkt verwenden, aber in einigen Beispielen werden wir später nicht die Postgres-Datenquelle direkt verwenden, sondern nur die URL, um die Datenbank anzugeben, mit der wir uns verbinden wollen.
Um Java zu zwingen, unsere "Driver"-Klasse zu laden, müssen wir die Klasse über "Class.forName(String)" laden.

Das könnte folgendermaßen aussehen:

```java
public class ClassForName {
    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Treiber nicht gefunden.", e);
        }
    }
}
```

In `org.postgresql.Driver` befindet sich die Treiberklasse für Postgres.
Deine IDE kann dir leicht alle verfügbaren Implementierungen zeigen.
Die Klasse, die die Schnittstelle `Driver` implementiert, heißt normalerweise auch `Driver`.

Hinweis:_ Wenn du Relocation verwendest, musst du natürlich auch die relocated Klasse laden.

## Postgres

[Parameter](https://jdbc.postgresql.org/documentation/use/)

```java
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

public class PostgresData {
    public static void main(String[] args) throws SQLException {
        DataSource dataSource = createDataSource();
        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("SELECT 1");
        }
    }

    public static DataSource createDataSource() throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/db");
        dataSource.setUser("username");
        dataSource.setPassword("password");
        return dataSource;
    }
}
```

## MySQL

[Parameter](https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-configuration-properties.html)

```java
import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

public class MySqlData {
    public static void main(String[] args) throws SQLException {
        DataSource dataSource = createDataSource();
        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("SELECT 1");
        }
    }

    public static DataSource createDataSource() throws SQLException {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/db");
        dataSource.setUser("username");
        dataSource.setPassword("password");
        return dataSource;
    }
}
```

## MariaDB

[Parameter](https://mariadb.com/kb/en/about-mariadb-connector-j/#optional-url-parameters)

```java
import org.mariadb.jdbc.MariaDbPoolDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

public class MariaDbData {
    public static void main(String[] args) throws SQLException {
        DataSource dataSource = createDataSource();
        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("SELECT 1");
        }
    }

    public static DataSource createDataSource() throws SQLException {
        MariaDbPoolDataSource dataSource = new MariaDbPoolDataSource();
        dataSource.setUrl("jdbc:mariadb://localhost:3306/db");
        dataSource.setUser("username");
        dataSource.setPassword("password");
        return dataSource;
    }
}
```

## SqLite

SqLite hat keinen zusätzlichen url-Parameter

```java
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

public class SqLiteData {
    public static void main(String[] args) throws SQLException {
        DataSource dataSource = createDataSource();
        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("SELECT 1");
        }
    }

    public static DataSource createDataSource() throws SQLException {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        // Damit wird eine Datenbank im Speicher ohne Datenpersistenz erstellt
        dataSource.setUrl("jdbc:sqlite::memory:");
        // Dies erstellt eine Datenbank und speichert sie in einer Datei namens data.db
        dataSource.setUrl("jdbc:sqlite:data.db");
        return dataSource;
    }
}
```
