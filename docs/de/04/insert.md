# Daten einfügen

Zum Einfügen der Daten werden wir hauptsächlich die Methode `execute` unseres `PreparedStatement` verwenden. Wir wissen, dass es funktioniert hat, wenn
Wir wissen, dass es funktioniert hat, wenn wir keine Ausnahme erhalten. Da wir normalerweise nur eine einzige Zeile einfügen, sind wir nicht wirklich an den geänderten Zeilen interessiert.
auch nicht.

```java
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Insert {
    static DataSource dataSource;

    public static void main(String[] args) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("""
                     INSERT INTO player(player_name) VALUES (?)
                     """)) {
            stmt.setString(1, "Lexi");
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

Eine Ausnahme, in der wir `execute` nicht verwenden würden, wäre, wenn wir eine `RETURNING` [Klausel](../02/returning.md) verwenden, um die
neu erstellte ID für unseren Nutzer zu erhalten.

```java
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InsertReturning {
    static DataSource dataSource;

    public static void main(String[] args) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("""
                     INSERT INTO player(player_name) VALUES (?) RETURNING id
                     """)) {
            stmt.setString(1, "Lexi");
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                System.out.printf("Created user %s with id %d%n", "Lexi", resultSet.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

## Generierte Schlüssel

Bei Datenbanken ohne `RETURNING`-Klausel (siehe MySQL) kannst du die generierten Schlüssel auf andere Weise abrufen.
Unser `PreparedStatement` ermöglicht uns nicht nur die Ausführung, sondern auch die Überprüfung, was unsere Anweisung geändert hat. Wenn wir angeben, dass
wir die generierten Schlüssel mit einem Flag angeben, gibt die Datenbank die generierten Schlüssel zurück, wenn wir die Anweisung ausführen.
Danach können wir sie aus der Anweisung lesen.

```java
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class InsertGeneratedKeys {
    static DataSource dataSource;

    public static void main(String[] args) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("""
                     INSERT INTO player(player_name) VALUES (?)
                     """, Statement.RETURN_GENERATED_KEYS)) { // Dieses Flag hier zu setzen ist sehr wichtig
            stmt.setString(1, "Lexi");
            stmt.executeUpdate();
            // Wir erhalten hier ein neues ResultSet
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                // Da wir nicht wissen, wie die Spalte benannt ist, verwenden wir einfach die erste Spalte
                System.out.printf("Created user %s with id %d%n", "Lexi", keys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

Das Problem dabei ist, dass ein Schlüssel nur zurückgegeben wird, wenn er explizit als Schlüssel identifiziert wurde. Wenn das nicht klappt, kannst du immer noch
die Datenbank bitten, eine oder mehrere Spalten explizit zurückzugeben.

```java
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InsertGeneratedKeysColumns {
    static DataSource dataSource;

    public static void main(String[] args) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("""
                     INSERT INTO player(player_name) VALUES (?)
                     """, new String[]{"id"})) { // Wir definieren, dass wir den Wert von id zurückbekommen wollen
            stmt.setString(1, "Lexi");
            stmt.executeUpdate();
            // Der Rest ist wie üblich
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                System.out.printf("Created user %s with id %d%n", "Lexi", keys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```
