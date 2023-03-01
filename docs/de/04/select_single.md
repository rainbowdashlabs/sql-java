# Lesen eines Eintrags aus einem ResultSet

Werfen wir einen Blick auf unsere Ergebnismenge. Wenn wir die vorherige Abfrage ausführen: `SELECT player_name FROM player WHERE id = ?` würde es
würde es wahrscheinlich ungefähr so aussehen.

```
      Zeile | player_name
Cursor -> 0 |
          1 | 'Lexi'
```

Du siehst, dass wir einen "Cursor" haben. Dieser Cursor zeigt auf Zeile 0 oder genauer gesagt auf die Zeile vor der ersten Zeile.
Zeile. Das bedeutet, dass wir, wenn wir jetzt etwas aus dem `ResultSet` lesen würden, einen Fehler bekommen würden, weil es einfach
keine Daten zum Lesen gibt. Um zu prüfen, ob es eine weitere Zeile in unserem `ResultSet` gibt, können wir `ResultSet#next` verwenden. Diese Methode
tut drei Dinge:

1. Sie prüft, ob es eine weitere Zeile gibt, zu der sie wechseln kann.
2. Sie geht in die nächste Reihe, wenn es eine gibt.
3. Gibt einen booleschen Wert zurück, der true ist, wenn es eine neue Zeile gab.

Nehmen wir an, wir haben `ResultSet#next` aufgerufen. Dann würde unser `ResultSet` wie folgt aussehen:

```
      Zeile | player_name
          0 |
Cursor -> 1 | 'Lexi'
```

Du siehst, dass sich der Cursor eine Zeile weiter bewegt hat und wir uns jetzt in einer Zeile mit Daten befinden. Das bedeutet auch, dass wir jetzt in der Lage sind
in der Lage sind, etwas aus unserer aktuellen Zeile zu lesen. Das machen wir mit den Methoden von `ResultSet`, die mit `get` beginnen. In unserem
Fall wollen wir eine Zeichenkette lesen, also benutzen wir die Methode `ResultSet#getString()`. Du wirst feststellen, dass es
zwei Versionen dieser Methode gibt. Das liegt daran, dass wir die Spalte, die wir auswählen wollen, auf zwei Arten definieren können. Die erste
Version ist der Name `resultSet.getString("player_name")`. Diese Version wird im Allgemeinen bevorzugt, da sie nicht
nicht abbricht, wenn du die Select-Anweisung änderst, und sie ist besser lesbar. Die zweite Version ist die Spalten-ID. Diese Ids
beginnt wieder bei 1. Das bedeutet, dass wir die erste Spalte auswählen müssen, in unserem Fall `resultSet.getString(1)`.

Jetzt ist es an der Zeit, all dies in einen schönen Code zu übersetzen:

```java
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReadResultSet {
    static DataSource dataSource;

    public static void main(String[] args) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT player_name, id FROM player WHERE id = ?")) {
            stmt.setInt(1, 10);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                System.out.println(resultSet.getString("player_name"));
                System.out.println(resultSet.getString(1));
            } else {
                System.out.println("Kein Eintrag zurückgegeben");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

Du kannst sehen, dass wir `if` verwenden, um festzustellen, ob es eine weitere Zeile gibt oder nicht. Das ist wichtig, denn wir bekommen einen
Fehler, wenn wir versuchen, eine Zeile zu lesen, obwohl es keine gibt. Nachdem wir mit `next()` bestätigt haben, dass es eine weitere Zeile gibt, versuchen wir
versuchen wir, den Wert von "player_name" und der ersten Spalte zu lesen. Beide sind natürlich identisch.

Wenn du mehrere Spalten ausgewählt hättest, könntest du natürlich auch alle anderen auslesen.

## Schließen eines ResultSets

Wenn du dir das `ResultSet` genau ansiehst, wirst du feststellen, dass es auch ein `AutoClosable` ist. Du könntest dich fragen: "
Heißt das, dass ich es auch wieder schließen muss?". Die Antwort darauf ist "Nein". Aber das ist eine große Ausnahme im Allgemeinen.
Ein `ResultSet` ist an das `Statement` oder `PreparedStatement` gebunden, von dem es abgerufen wurde. Das bedeutet, dass sobald die
Anweisung geschlossen wird, wird auch das damit verbundene `ResultSet` geschlossen. Das bedeutet aber auch, dass die Rückgabe des ResultSet
nicht funktionieren würde, da es bereits geschlossen ist, nachdem wir es zurückgegeben haben. Du musst die Werte immer innerhalb des `try`-Blocks lesen
