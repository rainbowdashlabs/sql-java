# Vermeidung von Nullwerten

Wenn du einen Wert in eine Tabelle einfügst, kann er im Allgemeinen immer null sein. Du kannst dies vermeiden, indem du bei der Definition der Tabelle eine Spalte als `NOT
NULL` markierst, wenn du die Tabelle definierst. Das ist besonders wichtig, wenn du Standardwerte festlegen oder die Werte einer Spalte auf eindeutige Werte beschränken willst.
einer Spalte auf eindeutige Werte beschränken willst, denn eine Null ist in SQL immer eindeutig und eine Spalte mit einem Standardwert kann trotzdem Null sein.

Um eine Spalte als nicht löschbar zu kennzeichnen, fügen wir einfach `NOT NULL` nach unserem Typ hinzu.

*Vergiss nicht, deine Tabelle zuerst zu löschen*

```sql
CREATE TABLE player
(
    id INTEGER,
    player_name TEXT NOT NULL,
    last_online TIMESTAMP
);
```

Wenn wir nun versuchen, einen `NULL` Wert in unsere Spalte `player_name` einzufügen, würden wir eine Fehlermeldung erhalten.

```sql
INSERT INTO player(id, player_name, last_online)
VALUES (1, NULL, NULL)
```

Wenn wir dies einfügen, erhalten wir eine nette Fehlermeldung, die uns sagt, dass der `player_name` nicht null sein kann!
