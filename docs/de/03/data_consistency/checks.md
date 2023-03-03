# Checks

**Nur für MySQL, MariaDB und PostgreSQL**

Validierungen sind auch sehr wichtig, um sicherzustellen, dass ein Wert in einer Spalte bestimmte Anforderungen erfüllt.

Du kennst bereits einige Prüfungen.
Jedes Mal, wenn wir `CONSTRAINT` verwendet haben, haben wir eine Validierung hinzugefügt. Zum Beispiel in unserem [foreign key](foreign_keys.md) oder in unserem [primary key](primary_keys.md).
`NICHT NULL` ist ebenfalls eine Inlinevalidierung.
Aber es gibt noch mehr! Wir können auch selbst manuelle Validierungen hinzufügen.

Es ist an der Zeit, unsere `money` Tabelle noch weiter zu verbessern, indem wir eine Prüfung hinzufügen, die sicherstellt, dass `money` immer gleich oder größer als 0 ist, wodurch sichergestellt wird, dass wir nie einen negativen Geldbetrag haben.

```sql
CREATE TABLE money (
    player_id INT PRIMARY KEY,
    money     DECIMAL DEFAULT 0 NOT NULL,
    CONSTRAINT geld_spieler_spieler_id_fk
        FOREIGN KEY (player_id) REFERENCES player (id)
            ON DELETE CASCADE,
    -- Wir fügen eine weitere Einschränkung hinzu. Der Name sollte in etwa so lauten
    -- check_<tabelle>_<check_name>
    CONSTRAINT check_money_negative
        -- und eine eigene Prüfung definieren, die sicherstellt, dass money größer oder gleich 0 ist
        CHECK ( money >= 0)
);
```

Versuchen wir nun, etwas Ungültiges einzufügen:

```sql
INSERT INTO money (player_id, money) VALUES (1, -10);
```

Das schlägt fehl! Unsere Prüfung funktioniert und verhindert, dass wir negative Werte in unsere Tabelle einfügen! Natürlich funktioniert das auch bei `UPDATE`s.
