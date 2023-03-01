# Was ist JDBC?

Ja, das ist wahrscheinlich die erste Frage, der wir nachgehen sollten.
JDBC bedeutet **J**ava **D**ata**b**ase **C**onnectivity.

JDBC ist Teil von Java und definiert die Komponenten, die für die Interaktion mit einer Datenbank über Java erforderlich sind.
Du findest die Schnittstellen dazu unter ``javax.sql`` und sie werden von ``java.sql`` ergänzt.
Wenn wir mit unserer Datenbank interagieren, verwenden wir in der Regel die in diesen Paketen enthaltenen Schnittstellen.
Sie enthalten verschiedene Schnittstellen, die definieren, wie eine `Connection`, `Statement`, `PreparedStatement`, `ResultSet`, `DataSource` und `Driver` zu implementieren sind.

## Verbindung

Die Verbindungsschnittstelle definiert die Methoden für eine Datenbankverbindung.
Sie ermöglicht es uns, Statements und PreparedStatements an unsere Datenbank zu senden.

## Statement und PreparedStatement

Die Schnittstellen Statement und PreparedStatement ermöglichen es uns, unsere SQL-Anweisung zu definieren und an die Datenbank zu senden.
Mit dem Prepared Statement können wir zusätzlich Variablen in unserer SQL-Abfrage setzen und unsere Eingaben escapen, um SQL-Injection zu vermeiden.

## ResultSet

Das ResultSet ist das, was wir erhalten, wenn wir Daten aus unserer Datenbank lesen.
Es ermöglicht den Zugriff auf die Werte der verschiedenen Spalten und geht durch die ausgewählten Zeilen.
Du kannst es dir als eine Tabelle mit den Zeilen und Spalten vorstellen, die wir in unserer Anweisung ausgewählt haben.
Sie enthält außerdem zusätzliche Metadaten über die von uns durchgeführte Operation.

## DataSource

Die DataSource stammt aus dem Paket `javax.sql`, das die neuere Java-SQL-Implementierung darstellt.
Sie ist der Nachfolger der Klasse `DriverManager` aus `java.sql`.
Seit Java 8 und der Einführung der Klasse `DataSource` wird die Verwendung des `DriverManager` nicht mehr empfohlen.

Anstatt wie der `DriverManager` eine einzelne `Connection` zu erstellen, soll die `DataSource` eine Verbindung bereitstellen und deren Gültigkeit sicherstellen.
Was die DataSource wirklich tut, hängt jedoch stark von ihrer Implementierung ab.
Manche Implementierungen stellen nur eine einzige Verbindung bereit und verwalten sie.
Andere wiederum stellen eine eigene Verbindung bereit, die offen gehalten und verwaltet wird.
Einige Implementierungen wie [HikariCP] (https://github.com/brettwooldridge/HikariCP) bieten eine DataSource an, die einen Pool von Verbindungen verwaltet.
Wir werden uns verschiedene DataSource-Implementierungen ansehen und uns schließlich für HikariCP entscheiden, da dies der neueste Stand der Technik für Verbindungsmanagement und Pooling ist.
Die meisten Datenbanken stellen nur einfache DataSource-Implementierungen zur Verfügung, die sich zwar gut zum Spielen eignen, aber nicht wirklich für den Einsatz in der Praxis geeignet sind.

## Treiber

Der Treiber ist das Herzstück einer jeden JDBC-Implementierung.
Er ist im Grunde der Übersetzer zwischen Java und deiner Datenbank.
Du wirst den Treiber wahrscheinlich nie direkt benutzen, aber es ist gut zu wissen, dass es ihn gibt und dass er wichtig ist.

Wenn du mehr wissen willst, sieh dir den [wikipedia-Artikel](https://en.wikipedia.org/wiki/Java_Database_Connectivity) an.
