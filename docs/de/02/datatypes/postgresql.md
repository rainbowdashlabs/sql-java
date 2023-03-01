# PostgreSQL

PostgreSQL hat viel mehr spezialisierte und flexiblere Datentypen. Das macht ihre Erstellung in manchen Fällen etwas komplexer.

Wir schauen uns nur die allgemeineren Typen an und lassen Dinge wie [geometrische](https://www.postgresql.org/docs/14/datatype-geometric.html), [Textsuche](https://www.postgresql.org/docs/14/datatype-textsearch.html) oder [Bereich](https://www.postgresql.org/docs/14/rangetypes.html) Typen aus.

## Numerische Typen

Numerische Typen haben manchmal eine "Größe".
Diese Größe ist nicht direkt eine Validierung.
Sie ist meistens die Größe der Zahl und legt fest, wie viel von dieser Größe zum Speichern der Dezimalzahl verwendet wird.
Sized haben nie eine Größe, die die Eingabe validiert.
Es geht immer um die Genauigkeit.

### INTEGER

Java: Integer, Long | [Dokumentation](https://www.postgresql.org/docs/current/datatype-numeric.html#DATATYPE-INT)

Die Integer in PostgreSQL sind in verschiedene Größen unterteilt.
Je nachdem, welche Größe du wählst, benötigen sie unterschiedlich viel Speicherplatz.
Wähle deinen Integer-Typ anhand der maximalen und minimalen Größe, die du für die Werte erwartest.

- SMALLINT: -32.768 und 32.767
- INTEGER: -2.147.483.648 und 2.147.483.647
- BIGINT: Jeder Wert, der größer ist als INT

### DECIMAL

Java: Double | [Dokumentation](https://www.postgresql.org/docs/current/datatype-numeric.html#DATATYPE-NUMERIC-DECIMAL)

Eine Zahl mit einem "exakten" Festkomma.

### DOUBLE

Java: Double | [Dokumentation](https://www.postgresql.org/docs/current/datatype-numeric.html#DATATYPE-FLOAT)

Eine Zahl mit doppelter Fließkommapräzision

### BOOLEAN

Java: Boolean | [Dokumentation](https://www.postgresql.org/docs/current/datatype-boolean.html)

Boolesche Werte werden intern als tinyint gespeichert.

## Texttypen

Es gibt verschiedene Texttypen, die wir je nach der erwarteten Größe des Wertes auswählen müssen.

### CHAR

Java: String | [Dokumentation](https://www.postgresql.org/docs/current/datatype-character.html)

Ein Zeichen wird für Strings mit der gleichen Größe verwendet. Z.B. Ländercodes.
Die Länge muss bei der Erstellung festgelegt werden.
Zeichen werden mit Leerzeichen aufgefüllt, wenn einige Zeichen fehlen, um die angegebene Länge zu erreichen.
Die maximale Größe eines CHAR beträgt etwa 1 GB. Wenn du mehr brauchst, verwende TEXT oder VARCHAR

`CHAR(Länge)`

### TEXT

Java: String | [Dokumentation](https://www.postgresql.org/docs/current/datatype-character.html)

Der Texttyp in Postgres erlaubt die Speicherung von Texten mit unbegrenzter, variabler Länge.
Das ist ein großer Unterschied zu anderen Datenbanken, die mehrere Texttypen mit unterschiedlichen Maximallängen haben.

### VARCHAR

Java: String | [Dokumentation](https://www.postgresql.org/docs/current/datatype-character.html)

VARCHAR ist ein String mit einer variablen maximalen Länge.
Im Gegensatz zu CHAR wird kein Padding hinzugefügt.

Im Gegensatz zu anderen Datenbanken ist ein varchar ohne eine explizite Begrenzung unbegrenzt und entspricht dem TEXT im Verhalten.
Theoretisch beträgt die maximale Größe 65.532 Zeichen.
Wenn du utf8 verwendest, das bis zu 4 Byte pro Zeichen benötigt, kommst du im schlimmsten Fall auf 21.844 Zeichen.

Ein Vorteil gegenüber TEXT ist, dass varchar-Spalten vollständig indiziert werden können, während TEXT-Spalten auf eine bestimmte Länge gekürzt werden.

### Enum

Java: String oder Enum name | [Dokumentation](https://www.postgresql.org/docs/current/datatype-enum.html)

Im Gegensatz zu MariaDB und MySQL werden die Enum-Werte nicht in der Tabelle, sondern in einem eigenen Typ definiert.
Postgres erlaubt es, eigene Typen über den Befehl create type hinzuzufügen.

```sql
CREATE TYPE mood AS enum ('happy', 'angry', 'sad')
```

So kannst du ihn in mehreren Tabellen oder an zahlreichen anderen Stellen verwenden, ohne ihn jedes Mal gegenseitig zu definieren. Außerdem ist eine Iteration und explizite Reihenfolge möglich.

### JSON und JSONB

Java: String | [Dokumentation](https://www.postgresql.org/docs/current/datatype-json.html)

JSON ist die primitive Nur-Text-Darstellung, die einer Spalte für json eine Syntaxvalidierung hinzufügt.
Sie ermöglicht auch die Verwendung einer großen Anzahl von json [spezifischen Operatoren](https://www.postgresql.org/docs/current/functions-json.html).

JSONB ist eine binäre Darstellung des Objekts selbst.
Im Gegensatz zu JSON ist es möglich, Indizes auf Werte bestimmter Schlüssel zu erstellen.
Dadurch kann Postgres eine Art dokumentenbasiertes Datenbankverhalten bieten.

## Binäre Typen

### BINÄR (BYTEA)

Java: Anything | [Dokumentation](https://www.postgresql.org/docs/current/datatype-binary.html)

Speichert binäre Daten als Bytes oder Hex. Der BYTEA-Typ benötigt kein Größenargument.

## Datum und Uhrzeit

### DATE

Java: LocalDate | [Dokumentation](https://www.postgresql.org/docs/current/datatype-datetime.html)

### TIME

Java: LocalTime | [Dokumentation](https://www.postgresql.org/docs/current/datatype-datetime.html)

### TIMESTAMP

Java: LocalDateTime | [Dokumentation](https://www.postgresql.org/docs/current/datatype-datetime.html)

Postgres verfügt nicht über einen eigenen DATETIME-Typ.
Stattdessen gibt es TIMESTAMP, einen normalen Zeitstempel ohne Zeitzone, und TIMESTAMPTZ, einen Zeitstempel mit einer Zeitzone.

### INTERVAL

Java: String | [Dokumentation](https://www.postgresql.org/docs/current/datatype-datetime.html#DATATYPE-INTERVAL-INPUT))

Als besonderen Typ hat Postgres den Typ INTERVAL.
Dieser Typ ermöglicht die einfache Erstellung von Intervallen durch das Parsen von Strings.

```sql
SELECT now() - '10 HOURS'::interval; -- (1)

SELECT now() - '10 HOURS 10 MINUTES'::interval; -- (2)

SELECT now() - '1 D 10 H 10 M'::interval; -- (3)
```

1. Die aktuelle Zeit minus 10 Stunden
2. Die aktuelle Zeit minus 10 Stunden und 10 Minuten
3. Die aktuelle Zeit minus 1 Tag, 10 Stunden und 10 Minuten.

## Arrays

Alle Postgres-Datentypen können als [Array](https://www.postgresql.org/docs/current/arrays.html) verwendet werden.

Arrays werden als `Typ[]` deklariert.
Sie haben eine Menge zusätzlicher [Operatoren](https://www.postgresql.org/docs/current/functions-array.html) für Vergleiche oder Änderungen.

## Zusammengesetzte Typen

PostgreSQL erlaubt die Erstellung von [zusammengesetzten Typen](https://www.postgresql.org/docs/current/rowtypes.html), die im Wesentlichen Klassen sind und die Erstellung von Objekten ermöglichen.
Wie Enums werden sie mit dem Befehl create type erstellt

```sql
CREATE TYPE person AS (name text, alter INT)
```

Diese Typen können auch als Spaltentyp verwendet werden.
Um ein Objekt eines zusammengesetzten Typs zu erstellen, verwenden wir den ROW-Ausdruck.

```sql
ROW
('Some Name', 10)
```

Der Name des zusammengesetzten Typs spielt keine Rolle.
Es geht nur um die richtigen Typen in der richtigen Reihenfolge.
