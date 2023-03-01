# Mehrere Einträge aus einem ResultSet lesen

Dieses Mal verwenden wir eine Abfrage, die du bereits kennen solltest.
Es ist die Abfrage, die wir in Kapitel 2 verwendet haben, um alle Spieler mit einer ID größer als 5 auszuwählen.
Aber dieses Mal machen wir sie mit unserem `PreparedStatement` ein bisschen konfigurierbarer.

```sql
SELECT
	id,
	player_name
FROM
	player
WHERE id >= ?
  AND id <= ?
```

Wir setzen den ersten Wert auf 5 und den zweiten Wert auf 10.
So erhalten wir ein `ResultSet`, das wie folgt aussieht:

```      
        Zeile | id | player_name
Cursor -> 0 | | |     
          1 | 5 | Matthias    
          2 | 6 | Lenny       
          3 | 7 | Summer      
          4 | 8 | Marry       
          5 | 9 | Milana      
          6 | 10 | Lexi        
```

Anstatt zu prüfen, ob es eine nächste Zeile gibt und diese zu lesen, wollen wir so lange lesen, wie es eine nächste Zeile gibt.
Wir können das nicht mit einem `if` machen, aber wir können es mit einem `while` machen.

```java
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SelectMulti {
    static DataSource dataSource;

    public static void main(String[] args) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("""
                     SELECT id, player_name
                     FROM player
                     WHERE id >= ?
                       AND id <= ?
                     """)) {
            stmt.setInt(1, 5);
            stmt.setInt(2, 10);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("id"));
                System.out.println(resultSet.getString("player_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

Die while-Funktion springt zur nächsten Zeile, solange es eine gibt.
Es schlägt auch nicht fehl, wenn es gar keine Zeile gibt.

Anstatt das Ergebnis auszudrucken, kannst du natürlich auch eine Liste oder eine Karte erstellen, um sie zu speichern.
Auf die Rückgabe von Daten werden wir in einem späteren Abschnitt eingehen.
