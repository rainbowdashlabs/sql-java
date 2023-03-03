# Namenskonvention

Es gibt keine offiziellen Namenskonventionen für SQL, aber es gibt einige etablierte Konventionen für die Namensgebung.
Noch wichtiger als die Namensgebung ist die Konsistenz.
Wechsle nicht zwischen verschiedenen Stilen.
Ausführlichere Informationen findest du [hier](https://www.sqlshack.com/learn-sql-naming-conventions/).
Von Nutzern erstellte Namen sollten in der Regel `snake_case` sein

## Datenbank und Schema

Vermeide Datenbanken und Schemas, die mit Zahlen beginnen. Du musst sie sonst jedes Mal in Anführungszeichen setzen, und das kann 
sehr lästig sein kann

## Tabellen

- Tabellen sollten mit singulären Begriffen benannt werden. (Nachricht, Rolle anstelle von Nachrichten, Rollen)
- Verwende wenn möglich einzelne Wörter

## Spalten

- Vermeide die Verdoppelung des Tabellennamens. Wenn deine Tabelle `user` heißt und eine id-Spalte hat, nenne sie `id` statt `user_id`.
- Auch wenn es möglich ist, dass eine Spalte den gleichen Namen wie ihren Typ hat, sollte dies vermieden werden.

### Primary Key Spalte

Am häufigsten ist `id` für die Spalte des Primary Keys.

### Foreign key Spalte

Wenn deine Spalte Teil eines Foreign Keys ist, verwende den Namen der Tabelle, auf die sich der Schlüssel bezieht.
Stell dir eine Tabelle `User(id, Name)` und eine Tabelle `money(user_id, amount)` vor.
Die Spalte `user_id` in `money` ist ein Verweis auf die Spalte `id` in `user`.

### Daten

Datumsangaben sollten nicht nur als `date` bezeichnet werden, sondern einen beschreibenden Namen wie `created` oder `changed` haben.

### Boolesche Werte

Boolesche Spalten sollten fragende Namen wie `is_enabled` oder `enabled` haben.

## Schlüssel und Indizes

Sollten mit dem Tabellennamen beginnen.

### Indizes

Beispiele

```
<tabellenname>_<Spalte.1>_<Spalte.2>_<Spalte.n>_index
<tabellenname>_<Spalte.1>_<Spalte.2>_<Spalte.n>_uindex
```

- Enthaltene Spalten
- Suffix mit `index`, wenn nicht eindeutig
- Suffix mit `uindex`, wenn eindeutig

### Primary Key

```
<tabellenname>_pk
```

- Suffix mit `pk`

### Foreign keys

```
<Herkunfts_tabellenname>_<verwandter_tabellenname>
<Herkunfts_tabellenname>_<verwandter_tabellenname>_fk
<Herkunfts_tabellenname>_<verwandter_tabellenname>_<herukunft_spalte>_<verwandte_spalte>_fk
```

- Enthält den Namen der zugehörigen Tabelle
- mit der Endung `fk`

Foreign key `player_id` in `money` zu `id` in `player`:\
`money_user_player_id_id_fk`

## Views
- Views sollten mit dem Präfix `v_` versehen werden, um den Unterschied zwischen einer Tabelle und einem View sichtbar zu machen.
