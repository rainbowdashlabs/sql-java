# Namenskonvention

Es gibt keine offiziellen Namenskonventionen für Sql, aber es gibt einige etablierte Konventionen für die Namensgebung.
Noch wichtiger als die Namensgebung ist die Konsistenz.
Wechsle nicht zwischen verschiedenen Stilen.
Ausführlichere Informationen findest du [hier](https://www.sqlshack.com/learn-sql-naming-conventions/).
Von Nutzern erstellte Namen sollten in der Regel `snake_case` sein

## Datenbank und Schema

Vermeide Datenbanken und Schemas, die mit Zahlen beginnen. Du musst sie sonst jedes Mal in Anführungszeichen setzen, und das kann 
sehr lästig sein kann

## Tabellen

- Tabellen sollten mit singulären Begriffen benannt werden. (Nutzer, Rolle anstelle von Nutzer, Rollen)
- Verwende wenn möglich einzelne Wörter

## Spalten

- Vermeide die Verdoppelung des Tabellennamens. Wenn deine Tabelle `user` heißt und eine id-Spalte hat, nenne sie `id` statt `user_id`.
- Auch wenn es möglich ist, dass eine Spalte den gleichen Namen wie ihren Typ hat, sollte dies vermieden werden.
### Primärschlüsselspalten

Am häufigsten ist "id".

### Foreign key column

Wenn deine Spalte Teil eines Foreign keys ist, verwende den Namen der Tabelle, auf die sich der Schlüssel bezieht.
Stell dir eine Tabelle "Nutzer(id, Name)" und eine Tabelle "money(user_id, amount)" vor.
Die Spalte `user_id` in `money` ist ein Verweis auf die Spalte `id` in `user`.

### Daten

Datumsangaben sollten nicht nur als Datum bezeichnet werden, sondern einen beschreibenden Namen wie "Einfügedatum" oder "Änderungsdatum" haben.

### Boolesche Werte

Boolesche Spalten sollten fragende Namen wie "is_enabled" haben.

## Schlüssel und Indizes

- Mit vorangestellten Tabellennamen.

### Indizes

Beispiele

```
<tabellenname>_<Zeile.1>_<Zeile.2>_<Zeile.n>_index
<tabellenname>_<Zeile.1>_<Zeile.2>_<Zeile.n>_uindex
```

- Enthaltene Zeilen
- mit Index angehängt, wenn nicht eindeutig
- Suffix mit uindex, wenn eindeutig

### Primärschlüssel

```
<tabellenname>_pk
```

- Suffix mit `pk`

### Foreign keys

```
<Herkunfts_tabellenname>_<verwandter_tabellenname>
<Herkunfts_tabellenname>_<verwandter_tabellenname>_fk
```

- Enthält den Namen der zugehörigen Tabelle
- mit der Endung `fk`

## Views
- Views sollten mit dem Präfix `v_` versehen werden, um den Unterschied zwischen einer Tabelle und einem View sichtbar zu machen.
