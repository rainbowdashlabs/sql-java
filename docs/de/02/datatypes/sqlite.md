# SQLite

SQLite hat den kleinsten Satz von Datentypen.

## INTEGER

Java: Integer, Long

Eine Ganzzahl mit 8 bis 64 Bits

## REAL

Java: Float, Double

Eine beliebige Zahl mit einem Fließkomma. Wird in 64 Bits gespeichert.

## TEXT

Java: String

Jeder beliebige Text, den du speichern möchtest

## BLOB

Java: Alles, was binär ist

Ein Blob ist im Grunde eine binäre Darstellung von etwas.
Er wird genau so zurückgegeben, wie er eingegeben wurde.

## BOOLEAN

Java: Boolean

Ein Boolean in Sqlite ist intern eine ganze Zahl.

## Datum und Uhrzeit

Java: Instant, LocalDate, LocalDateTime

SQLite speichert Datums- und Zeitangaben als INTEGER mit UNIX-Zeit, TEXT als serialisierte Zeichenkette oder als REAL als julianische Tageszahlen.
Du musst dich für einen Typ entscheiden und sie jedes Mal umwandeln, wenn du ein Datum oder eine Uhrzeit verwendest.
Ich würde die UNIX-Zeit empfehlen.
Wir werden später zum Lesen und Schreiben kommen. Halte dich im Moment einfach daran.
