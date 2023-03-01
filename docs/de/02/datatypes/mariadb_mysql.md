# MariaDB und MySQL

MariaDB und MySQL sind sich sehr ähnlich, wenn es um Datentypen geht. Es gibt jedoch einige kleine Unterschiede.

## Numerische Typen

Numerische Typen haben manchmal eine "Größe".
Diese Größe ist nicht direkt eine Validierung.
Sie ist meistens die Größe der Zahl und legt fest, wie viel von dieser Größe zum Speichern der Dezimalzahl verwendet wird.
Sized haben nie eine Größe, die die Eingabe validiert.
Es geht immer um die Genauigkeit.

### INTEGER

Java: Integer, Long

Die Ganzzahlen in MariaDB sind in verschiedene Größen unterteilt.
Je nachdem, welche Größe du wählst, benötigen sie unterschiedlich viel Speicherplatz.
Wähle deinen Integer-Typ anhand der maximalen und minimalen Größe, die du für die Werte erwartest.

- TINYINT: Zwischen -128 und 127 - [MariaDB](https://mariadb.com/kb/en/tinyint/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/integer-types.html)
- SMALLINT: -32.768 und 32.767 - [MariaDB](https://mariadb.com/kb/en/smallint/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/integer-types.html)
- MEDIUMINT: -8.288.608 und 8.388.607 - [MariaDB](https://mariadb.com/kb/en/mediumint/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/integer-types.html)
- INT oder INTEGER: -2.147.483.648 und 2.147.483.647 - [MariaDB](https://mariadb.com/kb/en/int/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/integer-types.html)
- BIGINT: Jeder Wert größer als INT - [MariaDB](https://mariadb.com/kb/en/bigint/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/integer-types.html)

### DECIMAL

Java: Double | [MariaDB](https://mariadb.com/kb/en/decimal/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/fixed-point-types.html)

Eine Zahl mit einem "exakten" Fixpunkt.

### DOUBLE

Java: Double | [MariaDB](https://mariadb.com/kb/en/double/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/floating-point-types.html)

Eine Zahl mit doppelter Fließkommapräzision

### FLOAT

Java: Float | [MariaDB](https://mariadb.com/kb/en/float/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/floating-point-types.html)

Eine Zahl mit einfacher Fließkomma-Präzision

### BOOLEAN

Java: Boolean | [MariaDB](https://mariadb.com/kb/en/boolean/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/other-vendor-data-types.html)

Boolesche Werte werden intern als tinyint gespeichert.

## Texttypen

Es gibt verschiedene Texttypen, die wir je nach der erwarteten Größe des Wertes auswählen müssen.

### CHAR

Java: String | [MariaDB](https://mariadb.com/kb/en/char/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/char.html)

Ein Zeichen wird für Strings mit der gleichen Größe verwendet. Z.B. Länder-Codes.
Es hat eine maximale Länge von 255. Die Länge muss bei der Erstellung festgelegt werden.
Die Zeichen werden mit Leerzeichen aufgefüllt, wenn einige Zeichen fehlen, um die angegebene Länge zu erreichen.

`CHAR(Länge)`

### TEXT

Java: String

Der Texttyp ist in verschiedene Größen unterteilt. Sie lassen keine Werte zu, die größer als die maximale Größe sind.

- TINYTEXT: Bis zu 255 Zeichen - [MariaDB](https://mariadb.com/kb/en/tinytext/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/blob.html)
- TEXT: Bis zu 65.353 Zeichen - [MariaDB](https://mariadb.com/kb/en/text/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/blob.html)
- MEDIUMTEXT: Bis zu 16.777.215 Zeichen - [MariaDB](https://mariadb.com/kb/en/mediumtext/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/blob.html)
- LONGTEXT: Bis zu 4.294.967.295 Zeichen - [MariaDB](https://mariadb.com/kb/en/longtext/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/blob.html)

### VARCHAR

Java: String | [MariaDB](https://mariadb.com/kb/en/varchar/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/char.html)

VARCHAR ist ein String mit einer variablen Maximalgröße.
Die maximale Größe ist hier etwas komplexer, da sie von der Kodierung der Datenbank abhängt.
Im Gegensatz zu CHAR wird hier kein Padding hinzugefügt.

Theoretisch beträgt die maximale Größe 65.532 Zeichen.
Wenn du utf8 verwendest, das bis zu 4 Byte pro Zeichen benötigt, kommst du im schlimmsten Fall auf 21.844 Zeichen.

Ein Vorteil gegenüber TEXT ist, dass varchar-Spalten vollständig indiziert werden können, während TEXT-Spalten auf eine bestimmte Länge gekürzt werden.

### Enum

Java: String oder Enum name | [MariaDB](https://mariadb.com/kb/en/enum/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/enum.html)

Der Enum-Typ ist ein spezieller String, der der Spalte eine Eingabevalidierung hinzufügt. Es können nur Werte hinzugefügt werden, die in der Spalte definiert sind.

```ENUM('Wert1','Wert2',...)```

### JSON

Java: String | [MariaDB](https://mariadb.com/kb/en/json-data-type/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/json.html)

**MySQL**
JSON wird als LONGTEXT-Spalte gespeichert, aber für einen besseren Zugriff in ein internes Format umgewandelt.

**MariaDB**

JSON ist ein Alias für LONGTEXT und fügt eine Prüfung hinzu, die beim Einfügen eine gültige JSON-Syntax sicherstellt. JSON ist ein normaler String in MariaDB.

### SET

Java: String | **[Nur MySQL](https://dev.mysql.com/doc/refman/8.0/en/set.html)**

Ähnlich wie ein Enum kann ein Set nur vordefinierte Eingaben enthalten, die durch `,` getrennt sind.

Bei einem Set wie diesem:

```sql
SET ('a', 'b', 'c', 'd')
```

Wir können mehrere Werte haben wie:

- `''`
- `'a'`
- `'a,b'`
- `'a,c'`
- ...

## Binäre Typen

### BLOB

Java: Anything

Der Blob-Typ ist in mehrere Größen unterteilt.
Er wird verwendet, um beliebige Binärdaten zu speichern.

- TINYBLOB: Bis zu 255 Bytes - [MariaDB](https://mariadb.com/kb/en/tinyblob/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/blob.html)
- BLOB: Bis zu 65.353 Bytes - [MariaDB](https://mariadb.com/kb/en/blob/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/blob.html)
- MEDIUMBLOB: Bis zu 16.777.215 Bytes - [MariaDB](https://mariadb.com/kb/en/mediumblob/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/blob.html)
- LONGBLOB: Bis zu 4.294.967.295 Bytes alias 4GB - [MariaDB](https://mariadb.com/kb/en/longblob/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/blob.html)

### BINARY

Java: Beliebig | [MariaDB](https://mariadb.com/kb/en/binary/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/binary-varbinary.html)

Speichert einen binären Wert mit fester Länge.

BINARY(length)````

## Datum und Uhrzeit

### DATE

Java: LocalDate | [MariaDB](https://mariadb.com/kb/en/date/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/datetime.html)

### ZEIT

Java: LocalTime | [MariaDB](https://mariadb.com/kb/en/time/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/time.html)

### DATETIME

Java: LocalDateTime | [MariaDB](https://mariadb.com/kb/en/datetime/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/datetime.html)

Kombination aus DATE und TIME.
Sie werden in der aktuellen Zeitzone gespeichert.
Sie überprüfen auch, ob die Uhrzeit in der aktuellen Zeitzone gültig ist.
Einige Zeiten können aufgrund der Sommerzeit ungültig sein, die auch als Winter- und Sommerzeit bezeichnet wird.

### TIMESTAMP

Java: LocalDateTime | [MariaDB](https://mariadb.com/kb/en/timestamp/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/datetime.html)

Ein Zeitstempel wird beim Einfügen in UTC umgewandelt und beim gegenseitigen Lesen in die Zeitzone der Sitzung geändert.

### YEAR

Java: Integer oder String | [MariaDB](https://mariadb.com/kb/en/year-data-type/) | [MySQL](https://dev.mysql.com/doc/refman/8.0/en/year.html)

Ein Jahr steht für eine Jahreszahl im zwei- oder vierstelligen Format. Ihr Bereich ist begrenzt.

- Vier Ziffern (`YEAR(4)`): 1901 -> 2155 und 0000
- Zwei Ziffern (`JAHR(2)`): 70 -> 69 für 1970 -> 2069
