# Vermeiden von Nullwerten

Wenn du einen Wert in eine Tabelle einfügst, kann er im Allgemeinen immer `NULL` sein.
Du kannst dies vermeiden, indem du eine Spalte bei der Definition der Tabelle als `NOT NULL` markierst.
Das ist besonders wichtig, wenn du Standardwerte festlegen oder die Werte einer Spalte auf eindeutige Werte beschränken willst, denn ein `NULL` ist in SQL immer eindeutig und eine Spalte mit einem Standardwert kann immer noch `NULL` sein.

Um den Wert `NULL` in einer Spalte zu verbieten, fügen wir einfach `NOT NULL` nach unserem Typ hinzu.

*Vergiss nicht, deine Tabelle zuerst zu löschen*

```sql
CREATE TABLE player (
    id          INTEGER,
    player_name TEXT NOT NULL,
    last_online TIMESTAMP
);
```

Wenn wir nun versuchen, einen `NULL` Wert in unsere `player_name` Spalte einzufügen, würden wir einen Fehler erhalten.

```sql
INSERT INTO player(id, player_name, last_online)
VALUES (1, NULL, NULL)
```

Wenn wir dies einfügen, erhalten wir eine nette Fehlermeldung, die uns sagt, dass der `player_name` nicht `NULL` sein kann!
