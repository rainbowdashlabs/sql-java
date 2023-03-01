# Return Types

Jetzt haben wir unzählige Möglichkeiten kennengelernt, Daten zu lesen und zu schreiben. Was fehlt, sind Methoden und Rückgabetypen, die anzeigen
ob unsere Operation erfolgreich war. Dafür gibt es verschiedene Möglichkeiten, die wir uns hier ansehen werden. Du willst auf jeden Fall
auf jeden Fall eine Art von Rückgabewert haben.

*# Optionals

Optionals sind eine Java-Klasse. Sie werden bevorzugt, wenn ein Aufruf 0 oder 1 Ergebnisse zurückgibt. Sie können konstruiert werden durch
Aufruf von `Optional.of()`, `Optional.ofNullable()` oder `Optional.empty()` erstellt werden.

```java
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ReturnOptional {
    static DataSource dataSource;

    public static void main(String[] args) {
        System.out.printf("%s%n", playerById(10));
    }

    public static Optional<Player> playerById(int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT id, player_name FROM player WHERE id = ?")) {
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new Player(resultSet.getInt("id"), resultSet.getString("player_name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    record Player(int id, String name) {
    }
}
```

## Liste und Karte

Wenn mehrere Entitäten zurückgegeben werden, ist es sinnvoll, sie als Liste zurückzugeben. Natürlich wäre auch eine Map möglich,
aber in den meisten Fällen wirst du feststellen, dass eine Liste von Objekten ausreichend ist.

```java
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReturnListMap {

    static DataSource dataSource;

    public static List<Player> playerByIdsAsList(int minId, int maxId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT id, player_name FROM player WHERE id >= ? AND id <= ?")) {
            stmt.setInt(1, minId);
            stmt.setInt(2, maxId);
            ResultSet resultSet = stmt.executeQuery();
            List<Player> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(new Player(resultSet.getInt("id"), resultSet.getString("player_name")));
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public static Map<Integer, Player> playerByIdsAsMap(int minId, int maxId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT id, player_name FROM player WHERE id >= ? AND id <= ?")) {
            stmt.setInt(1, minId);
            stmt.setInt(2, maxId);
            ResultSet resultSet = stmt.executeQuery();
            Map<Integer, Player> result = new HashMap<>();
            while (resultSet.next()) {
                Player player = new Player(resultSet.getInt("id"), resultSet.getString("player_name"));
                result.put(player.id(), player);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }

    record Player(int id, String name) {
    }
}
```

Du wirst feststellen, dass wir unsere Sammlung erstellen, nachdem wir unsere Daten abgefragt haben. Das bedeutet, dass wir keine Sammlung erstellen
wenn ein Fehler in unserer Abfrage auftritt. Natürlich erstellen wir auch dann eine Sammlung, wenn wir keine Ergebnisse haben, aber das ist etwas
können wir ignorieren.

## Boolesch

Boolesche Werte können verwendet werden, um festzustellen, ob unsere Abfrage Daten verändert hat oder nicht. Das heißt, wir können sie für `INSERT`, `DELETE`
und `UPDATE` Abfragen verwenden.

```java
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReturnBoolean {
    static DataSource dataSource;

    public static boolean createPlayer() {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("""
                     INSERT INTO player(player_name) VALUES (?)
                     """)) {
            stmt.setString(1, "Lexi");
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
```

Jetzt wird unsere Methode true zurückgeben, wenn der Spieler erstellt wurde. Wir wissen immer, ob die Erstellung fehlgeschlagen ist oder nicht. Das Gleiche
funktioniert auch für das Löschen und Aktualisieren, aber für diese Methoden gibt es andere Möglichkeiten, die dir in manchen Situationen mehr Aufschluss geben können.

## Row Count

Die Zeilenzählung ist fast die gleiche Methode wie bei den booleschen Werten. Anstatt zu prüfen, ob unser Wert größer als 0 ist
geben wir ihn einfach zurück. Auf diese Weise wissen wir, wie viele Einträge wir mit unserer Abfrage aktualisiert oder gelöscht haben.

```java
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReturnRowCount {
    static DataSource dataSource;

    public static int deleteOldPlayers() {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("""
                     DELETE FROM player WHERE last_online < now() - '1 year'
                     """)) {
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
```

Unsere Methode löscht alle Spieler, die im letzten Jahr nicht online waren. Sie gibt zurück, wie viele Spieler gelöscht wurden
danach.

## Spickzettel

| Vorgang | Ergebnisse | Typ |
|-----------|---------|-------------------|
| Lesen | 0-1 | Optional |
| Lesen | >0 | Liste/Karte |
| Einfügen | | boolesch |
| Löschen | | Boolesche/Zeilenanzahl |
| Aktualisieren | Boolesche/Zeilenanzahl |
