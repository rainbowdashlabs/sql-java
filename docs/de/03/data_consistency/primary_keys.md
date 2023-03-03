# Primary Keys

`Primary Keys` sind eine besondere Art von eindeutigem Index.
Normalerweise legen wir sie bei der Erstellung unserer `Primary Key` fest, indem wir eine Spalte als `Primary Key` markieren.
Nicht alle Tabellen brauchen oder können einen `Primary Key` besitzen.
Im Allgemeinen ist der Unterschied zwischen einem eindeutigen Index und einem `Primary Key` sehr gering.
Der einzige Unterschied besteht darin, dass der `Primary Key` für einen speziellen `JOIN` namens `NATURAL JOIN` verwendet werden kann.
Wir werden uns später mit Joins beschäftigen, also denk jetzt noch nicht darüber nach.

Ein weiterer Unterschied ist, dass ein `Primary Key` immer als `NOT NULL` markiert ist und eine Tabelle nur einen `Primary Key`, aber mehrere eindeutige Indizes haben kann.

Um unseren `Primary Key` zu setzen, müssen wir nur unsere Spalte als `Primary Key` markieren, indem wir `PRIMARY KEY` nach dem Typ hinzufügen.

```sql
CREATE TABLE player
(
    id INTEGER PRIMARY KEY,
    player_name TEXT NOT NULL,
    last_online TIMESTAMP NOT NULL DEFAULT NOW()
);
```

Leider funktioniert diese einfache Methode nur mit einer einzigen Spalte als `Primary Key`.
Wenn wir mehrere Spalten als `Primary Key` verwenden wollen, wird die Definition unserer Abfrage etwas komplexer.
Schauen wir uns unsere Tabelle `channel_subscription` von vorhin an.
Wir hatten einen eindeutigen Index für `player_id` und `channel_id` und da beide Werte als `NOT NULL` markiert sind und die Kombination eindeutig sein sollte, eignen sie sich perfekt für einen `Primary Key`.

```sql
CREATE TABLE channel_subscription (
    player_id  INTEGER,
    channel_id INTEGER,
    CONSTRAINT channel_subscription_pk
        PRIMARY KEY (player_id, channel_id)
);
```

Die Einschränkung `NOT NULL` können wir jetzt entfernen, da diese bereits durch den `Primary Key` erzwungen wird.
Und das war's.
Nicht viel komplexer, aber ein bisschen mehr Arbeit beim Schreiben.
Achte auch auf die korrekte Benennung des Schlüssels, damit du ihn später identifizieren kannst.
