# HikariCP und Verbindungspooling

Du hast vielleicht bemerkt, dass wir unsere Verbindung als Ressource öffnen, die die Verbindung zwangsläufig schließt, sobald wir unseren Try-Block verlassen.
Das ist nicht wirklich das, was wir wollen.
Das Öffnen von Verbindungen ist teuer, und idealerweise würden wir diese Verbindungen behalten und wiederverwenden.
Dies zu implementieren ist nicht die Aufgabe von JDBC und deshalb gibt es Connection Pooling Frameworks.

Hier kommt [HikariCP](https://github.com/brettwooldridge/HikariCP) zum Einsatz.
HikariCP wickelt Verbindungen in ihre eigene Verbindung ein.
Anstatt die Verbindung zu schließen, wenn `close()` aufgerufen wird, verschiebt es die Verbindung zurück in einen Pool und gibt sie erneut zurück, wenn wir eine Verbindung von unserer DataSource anfordern.
Da wir vor dem Wechsel zu Hikari eine DataSource verwendet haben, ist das kein Problem, denn Hikari erstellt auch eine DataSource.
Alles, was wir ändern müssen, ist, dass wir die URL, die wir für unseren JDBC-Treiber definiert haben, weglassen und uns stattdessen mit unserer `HikariDataSource` verbinden und eine Poolgröße definieren.

Um Hikari zu verwenden, müssen wir es zuerst importieren:

**Aktuelle Version**

![Neueste Version](https://img.shields.io/maven-central/v/com.zaxxer/HikariCP)

**Gradle**

**Kkts
implementation("com.zaxxer", "HikariCP", "version")
```

**Maven**

``xml

<dependency>
    <groupId>com.zaxxer</groupId>
    <artifactId>HikariCP</artifactId>
    <version>Version</version>
</dependency>
```

Nachdem wir HikariCP importiert haben, müssen wir unsere DataSource erstellen.

```java
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgresHikariCP {
    public static void main(String[] args) throws SQLException {
        DataSource dataSource = createDataSource();
        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("SELECT 1");
        }
    }

    public static DataSource createDataSource() throws SQLException, ClassNotFoundException {
        // Wir laden die Treiberklasse in den Klassenpfad
        Class.forName("org.postgresql.Driver");
        // Erstelle eine neue Konfiguration
        HikariConfig config = new HikariConfig();
        // Lege die URL fest, die wir bereits für die Verbindung zu unserer Datenbank verwendet haben
        config.setJdbcUrl("jdbc:postgres://localhost:5432/db");
        // Einfügen unserer Anmeldedaten
        config.setUsername("username");
        config.setPassword("password");
        // Wir legen eine maximale Poolgröße fest.
        config.setMaximumPoolSize(5);
        // Wir legen die minimalen Leerlaufverbindungen fest.
        config.setMinimumIdle(2);
        // Erstelle eine neue DataSource basierend auf unserer Konfiguration
        return new HikariDataSource(config);
    }
}
```

Und jetzt werden wir gepoolte Verbindungen verwenden, wenn wir eine Verbindung von unserer DataSource anfordern.

## Wähle den richtigen Pool und die Leerlaufmenge

Die meisten Anwendungen funktionieren gut mit 3 bis 5 Verbindungen pro Pool.
Du musst dich immer fragen, wie viele parallele Verbindungen du brauchst.
Du kannst auch ein Monitoring dafür einrichten, wenn du es brauchst.
Die Idle-Verbindung sollte mindestens auf 1 gesetzt werden, aber zwei sind normalerweise besser.

## Weitere Konfiguration

Wir kratzen nur an der Oberfläche, wenn wir HikariCP verwenden.
Es gibt tonnenweise Möglichkeiten zur Anpassung und ich empfehle dir, einen Blick in die [Dokumentation](https://github.com/brettwooldridge/HikariCP#gear-configuration-knobs-baby) zu werfen.
