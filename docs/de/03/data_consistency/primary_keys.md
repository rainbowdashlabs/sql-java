# Primärschlüssel

Primärschlüssel sind eine besondere Art von eindeutigem Index. Normalerweise definieren wir sie beim Erstellen unserer Tabelle, indem wir eine Spalte
als Primärschlüssel. Nicht alle Tabellen brauchen oder können einen Primärschlüssel verwenden. Im Allgemeinen ist der Unterschied zwischen einem eindeutigen
Index und einem Primärschlüssel ist sehr gering. Der einzige Unterschied ist, dass der Primärschlüssel für einen speziellen Join verwendet werden kann
genannt `NATURAL JOIN`. Wir werden uns die Joins später ansehen, also denk jetzt noch nicht darüber nach.

Ein weiterer Unterschied ist, dass ein Primärschlüssel immer als `NOT NULL` markiert ist und eine Tabelle nur einen Primärschlüssel haben kann,
aber mehrere eindeutige Indizes.

Um unseren Primärschlüssel zu setzen, müssen wir nur unsere Spalte als Primärschlüssel markieren, indem wir `PRIMARY KEY`
nach dem Typ hinzufügen.

```sql
CREATE TABLE player
(
    id INTEGER PRIMARY KEY,
    player_name TEXT NOT NULL,
    last_online TIMESTAMP NOT NULL DEFAULT NOW()
);
```

Leider funktioniert diese einfache Methode nur mit einer einzigen Spalte als Primärschlüssel. Wenn wir mehrere Spalten als Primärschlüssel haben wollen
Primärschlüssel haben wollen, wird die Definition unserer Abfrage etwas komplexer. Werfen wir einen Blick auf unsere Tabelle `channel_subscription`
Tabelle von vorhin an. Wir hatten einen eindeutigen Index für player_id und channel_id und da beide Werte als nicht null markiert sind
und die Kombination eindeutig sein sollte, eignen sie sich perfekt für einen Primärschlüssel.

```sql
CREATE TABLE kanal_abonnement
(
    player_id INTEGER,
    channel_id INTEGER,
    CONSTRAINT channel_subscription_pk
        PRIMARY KEY (player_id, channel_id)
);
```

Die Einschränkung `NOT NULL` können wir jetzt sicher entfernen, da diese bereits durch den Primärschlüssel erzwungen wird.
Und das war's. Nicht viel komplexer, aber ein bisschen mehr Arbeit beim Schreiben. Achte auch auf die korrekte Benennung des
des Schlüssels, damit du ihn später identifizieren kannst.
