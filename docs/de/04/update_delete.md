# Aktualisieren und Löschen

Warum behandeln wir beides hier?
Weil sie auf der Datenbankseite im Wesentlichen dasselbe sind.
Ein `UPDATE` ändert eine bestehende Zeile und ein `DELETE` ebenso.
Wir verwenden auch den gleichen Weg, um sie an die Datenbank zu senden.

Anstelle von `executeQuery` rufen wir `executeUpdate` auf.
Es gibt auch eine Methode namens `execute`, die wir ebenfalls verwenden könnten, aber `executeUpdate` liefert direkt die Anzahl der geänderten Zeilen.

```java
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Delete {
    static DataSource dataSource;

    public static void main(String[] args) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("""
                     DELETE FROM player WHERE id = ?
                     """)) {
            stmt.setInt(1, 10);
            int changed = stmt.executeUpdate();
            System.out.printf("Gelöscht %d Zeile%n", geändert);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

Idealerweise sollten wir jetzt `Löschte 1 Zeile` ausgeben.
Das ist die Anzahl der Zeilen, die wir mit unserer Abfrage gelöscht haben.
Das ist eine gute Möglichkeit, um zu überprüfen, ob die Abfrage eine Auswirkung auf unsere Daten hatte.

Wenn wir diese Abfrage mit einem [Schlüsselwort `RETURNING`](../02/returning.md) kombinieren, können wir stattdessen `executeQuery` aufrufen und unsere Ergebnisse lesen, wie wir es im vorherigen Abschnitt getan haben.

```java
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeleteReturning {
    static DataSource dataSource;

    public static void main(String[] args) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("""
                     DELETE FROM player WHERE id = ? RETURNING player_name
                     """)) {
            stmt.setInt(1, 10);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) { // Wir könnten hier auch if verwenden, da wir nur eine Zeile erwarten.
                System.out.printf("Gelöschter Spieler %s%n", resultSet.getString("player_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```
