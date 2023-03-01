# Was ist JDBC?

Ja, das ist wahrscheinlich die erste Frage, der wir nachgehen sollten. JDBC bedeutet **J**ava **D**ata**b**ase **C**onnectivity.

JDBC ist Teil von Java und definiert die Komponenten, die für die Interaktion mit einer Datenbank über Java erforderlich sind. Du findest die
Schnittstellen davon findest du unter `javax.sql` und wird durch `java.sql` ergänzt. Wenn wir mit unserer Datenbank interagieren, verwenden wir normalerweise
Schnittstellen dieser Pakete verwenden. Sie enthalten verschiedene Schnittstellen, die definieren
wie Connection, Statement, PreparedStatement, ResultSet, DataSource und Driver zu implementieren sind.

## Verbindung

Die Verbindungsschnittstelle definiert die Methoden für eine Datenbankverbindung. Sie ermöglicht es uns, Statements und Prepared
Statements an unsere Datenbank zu senden.

## Statement und PreparedStatement

Die Schnittstellen Statement und PreparedStatement ermöglichen es uns, unsere SQL-Anweisung zu definieren und an die Datenbank zu senden. Die
PreparedStatement erlaubt es uns zusätzlich, Variablen in unserer SQL-Abfrage zu setzen und unsere Eingaben zu escapen, um eine SQL
Injektion zu vermeiden.

## ResultSet

Das ResultSet ist das, was wir erhalten, wenn wir Daten aus unserer Datenbank lesen. Es ermöglicht den Zugriff auf die Werte der verschiedenen Spalten und
die von uns ausgewählten Zeilen durchzugehen. Du kannst es dir als eine Tabelle mit den Zeilen und Spalten vorstellen, die wir in unserer Anweisung ausgewählt haben.
Sie enthält außerdem zusätzliche Metadaten über die von uns durchgeführte Operation.

## DataSource

Die DataSource stammt aus dem Paket `javax.sql`, das die neuere Java-SQL-Implementierung darstellt. Sie ist der Nachfolger
der Klasse `DriverManager` aus `java.sql`. Es wird nicht mehr empfohlen, den `DriverManager` seit java 8 und
die Einführung der Klasse `DataSource`.

Anstatt wie der `DriverManager` eine einzelne `Connection` zu erstellen, soll die `DataSource` eine
Verbindung bereitstellen und ihre Gültigkeit sicherstellen. Was die DataSource wirklich tut, hängt jedoch stark von ihrer Implementierung ab.
Manche Implementierungen stellen nur eine einzige Verbindung bereit und verwalten sie. Andere wiederum stellen eine eigene Verbindung bereit, die
offen gehalten und verwaltet wird. Einige Implementierungen wie [HikariCP](https://github.com/brettwooldridge/HikariCP) bieten eine
DataSource, die einen Pool von Verbindungen verwaltet. Wir werden uns verschiedene "DataSource"-Implementierungen ansehen und
Wir werden uns verschiedene Implementierungen von `DataSource` ansehen und uns schließlich für HikariCP entscheiden, da dies der Stand der Technik für Verbindungsmanagement und Pooling ist. Die meisten Datenbanken
Die meisten Datenbanken bieten nur einfache DataSource-Implementierungen, mit denen man zwar spielen kann, die aber nicht wirklich für den
Verwendung.

## Treiber

Der Treiber ist das Herzstück einer jeden JDBC-Implementierung. Er ist im Grunde der Übersetzer zwischen Java und deiner Datenbank. Du
wirst den Treiber wahrscheinlich nie direkt benutzen, aber es ist gut zu wissen, dass es ihn gibt und dass er wichtig ist.

Wenn du mehr wissen willst, schau dir den
den [wikipedia Artikel](https://en.wikipedia.org/wiki/Java_Database_Connectivity).
