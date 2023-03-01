# Prepared Statement

Du hast vielleicht bemerkt, dass wir im vorherigen Abschnitt keine vorbereitete Anweisung verwendet haben.
Das war das erste und letzte Mal, dass du in diesem Lehrgang eine "Anweisung" gesehen hast.
Von nun an werden wir immer vorbereitete Anweisungen verwenden.
Warum?
Weil es nur Vorteile hat, ein `PreparedStatement` anstelle eines `Statements` zu verwenden.

1. Sie verhindern [SQL-Injection](https://www.w3schools.com/sql/sql_injection.asp)
2. Sie ermöglichen die Stapelverarbeitung von Abfragen
3. Code mit Parametern ist einfacher zu erstellen
4. Sie sind vorkompiliert und ermöglichen Caching auf der Datenbankseite.
5. Beschleunigt die Kommunikation über ein binäres Nicht-SQL-Protokoll

Eine ausführlichere Erklärung findest du unter [Baeldung](https://www.baeldung.com/java-statement-preparedstatement)

Ein `PreparedStatement` wird von der `Connection` auf die gleiche Weise wie ein `Statement` abgerufen.

```java
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PreparedStatementUsage {
    static DataSource dataSource;

    public static void main(String[] args) {
        try /*(1)*/(Connection conn = dataSource.getConnection(); //(2)
             PreparedStatement stmt = /*(3)*/ conn.prepareStatement( 
                     /*(4)*/ "SELECT player_name FROM player WHERE id = ?")) {
            stmt.setInt(1, 10); //(5)
            ResultSet resultSet = stmt.executeQuery(); //(6)
            // hier kommt mehr
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

1. Deklariere unseren try with resources-Block
2. Rufe eine Verbindung von unserer DataSource ab
3. Erstelle eine neue vorbereitete Anweisung
4. Definiere unsere Abfrage mit einem Platzhalter -> `id = ?`
5. Setze den ersten Parameter in unserer Abfrage auf den Wert 10
6. Führe die Abfrage aus


Gehen wir es Schritt für Schritt durch.

1. Deklariere unseren try with resources-Block
2. Rufe eine Verbindung von unserer DataSource ab
3. Erstelle eine neue vorbereitete Anweisung
4. Definiere unsere Abfrage mit einem Platzhalter -> `id = ?`
5. Setze den ersten Parameter in unserer Abfrage auf den Wert 10
6. Führe die Abfrage aus

Die Werte in einer vorbereiteten Anweisung beginnen mit dem Index 1.
Du musst sie nicht in der richtigen Reihenfolge setzen, aber du musst sie alle setzen.
