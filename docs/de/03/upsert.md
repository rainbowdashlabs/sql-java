# Upsert und Konfliktbehandlung

Jetzt haben wir viele Möglichkeiten, Eingaben in unseren Tabellen zu blockieren.
Wir haben `Primary Keys`, `Unique Indices` und `Foreign Keys`, die die Eingabe von ungültigen oder doppelten Daten in unsere Tabellen blockieren.

Fehler bei `Foreign Keys` sind beabsichtigt.
Diese sollten immer auf der Anwendungsseite behandelt werden.
Fehler bei `Unique Indices` können jedoch von uns behandelt werden.
Dafür verwenden wir ein Konstrukt, das `UPSERT` genannt wird.
Der Name deutet bereits an, dass es sich um eine Mischung aus `UPDATE` und `INSERT` handelt.
Im Grunde heißt es: Einfügen und wenn mich etwas am Einfügen hindert, dann möchte ich die Daten aktualisieren.

Je nachdem, welche Datenbank du verwendest, ist die Syntax ein bisschen anders.

## Konflikte bei der Eingabe ignorieren

### Postgres & SqLite

Für Postgres und SqLite fügen wir Lexy erneut ein und scheitern dabei.

```postgresql
-- Dies ist unsere übliche Insert-Anweisung
INSERT INTO player(player_name)
VALUES ('Lexy')
       -- Hier legen wir fest, wo der Konflikt auftreten soll. In diesem Fall geht es um den Konflikt mit dem Spielernamen
ON CONFLICT (player_name)
    -- Hier sagen wir nur, dass wir nichts tun und den möglichen Fehler verwerfen wollen.
    DO NOTHING;
```

### MariaDB & MySQL

Für MariaDB und MySQL können wir die Syntax `INSERT IGNORE` verwenden, die Fehler, die beim Einfügen auftreten, einfach ignoriert.

```mariadb
INSERT IGNORE INTO player(player_name)
VALUES ('Lexy');
```

## Upsert

Die Upsert-Anweisung ist der wichtigere Teil.
Mit ihr fügen wir unseren Spieler in unsere Online-Tabelle ein und aktualisieren die letzte Online-Zeit, falls sie bereits existiert.

Das Paradigma ist immer: Versuche einzufügen und wenn das nicht geht, lass mich die Zeile mit dem Konflikt ändern.

### PostgeSQL & SqLite 

Für Postgres und SqLite fügen wir Lexy erneut ein und aktualisieren diesmal die Online-Zeit, falls bereits ein Spieler mit diesem Namen vorhanden ist. 

```postgresql
-- Dies ist unsere übliche Einfügeanweisung
INSERT INTO player(player_name)
VALUES ('Lexy')
       -- Hier legen wir fest, wo der Konflikt auftreten soll. In diesem Fall geht es um den Konflikt mit dem Spielernamen
ON CONFLICT (player_name)
    -- Hier sagen wir nur, dass wir nichts tun und den möglichen Fehler verwerfen wollen.
    -- In SqLite verwenden wir hier CURRENT_TIMESTAMP anstelle von now
    DO UPDATE SET last_online = NOW();
```

#### Excluded Table

Nehmen wir an, wir haben eine neue Zeile in unserer Spielertabelle namens Alter.

```postgresql
INSERT INTO player(player_name, age)
VALUES ('Lexy', 21)
ON CONFLICT (player_name)
    DO UPDATE SET age = 21;
```

Wenn wir das Alter von Lexy hochsetzen wollten, müssten wir das Alter zweimal in unsere Abfrage schreiben.
Das kann bei größeren Abfragen ziemlich unübersichtlich werden.
Zum Glück haben Postgres und SqLite eine temporäre Tabelle mit dem Namen `excluded`, die die Werte enthält, die wir einfügen wollten.
Wir können also stattdessen einfach schreiben.

```postgresql
INSERT INTO player(player_name, age)
VALUES ('Lexy', 21)
ON CONFLICT (player_name)
    DO UPDATE SET age = excluded.age;
```

### MariaDB & MySQL

Bei MariaDB und MySQL können wir die Klausel `ON DUPLICATE KEY UPDATE` verwenden, mit der wir die Werte in der Zeile mit dem Konflikt ändern können.

```mariadb
INSERT INTO player(player_name)
VALUES ('Lexy')
-- Wir legen fest, dass wir etwas tun wollen, wenn ein Duplikat auf einem Schlüssel oder eindeutigen Index auftaucht
ON DUPLICATE KEY UPDATE last_online = CURRENT_TIMESTAMP;
```
