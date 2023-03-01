# Upsert und Konfliktbehandlung

Jetzt haben wir eine Menge Möglichkeiten hinzugefügt, um Eingaben in unseren Tabellen zu blockieren. Wir haben Primärschlüssel, eindeutige Schlüssel und Fremdschlüssel, die
die die Eingabe von ungültigen oder doppelten Daten in unsere Tabellen blockieren.

Fehler bei Fremdschlüsseln sind beabsichtigt. Diese sollten immer auf der Anwendungsseite behandelt werden. Allerdings können Fehler bei
eindeutigen Schlüsseln können von uns behandelt werden. Dafür verwenden wir ein Konstrukt, das `UPSERT` genannt wird. Der Name deutet bereits an, dass
dass es sich um eine Mischung aus `UPDATE` und `INSERT` handelt. Im Grunde heißt es: Einfügen und wenn mich etwas am Einfügen hindert, möchte ich
die Daten zu aktualisieren.

Je nachdem, welche Datenbank du verwendest, ist die Syntax ein bisschen anders.

## Konflikte bei der Eingabe ignorieren

### Postgres & SqLite

Bei Postgres und SqLite fügen wir Lexy noch einmal ein und scheitern dabei gnädig.

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

Die Upsert-Anweisung ist der wichtigere Teil. Damit fügen wir unseren Spieler in unsere Online-Tabelle ein und 
die letzte Online-Zeit zu aktualisieren, falls sie bereits existiert.

Das Paradigma ist immer: Versuche einzufügen und wenn das nicht geht, lass mich die Zeile mit dem Konflikt ändern.

### Postges & SqLite 

Bei Postgres und SqLite fügen wir Lexy noch einmal ein und aktualisieren diesmal die Online-Zeit, falls ein Spieler 
mit diesem Namen bereits vorhanden ist. 

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

#### Tabelle ausschließen

Nehmen wir an, wir haben eine neue Zeile in unserer Spielertabelle namens Alter.

```postgresql
INSERT INTO player(player_name, age)
VALUES ('Lexy', 21)
ON CONFLICT (player_name)
    DO UPDATE SET age = 21;
```

Wenn wir das Alter von Lexy hochsetzen wollten, müssten wir das Alter zweimal in unsere Abfrage schreiben. Das kann bei größeren 
bei größeren Abfragen. Zum Glück sind Postgres und SqLite auf unserer Seite und stellen eine temporäre Tabelle namens `excluded` zur Verfügung, die 
die Werte enthält, die wir einfügen wollen. Anstelle der obigen Abfrage können wir also einfach schreiben.

```postgresql
INSERT INTO player(player_name, age)
VALUES ('Lexy', 21)
ON CONFLICT (player_name)
    DO UPDATE SET age = excluded.age;
```

### MariaDB & MySQL

Für MariaDB und MySQL können wir die Klausel `ON DUPLICATE KEY UPDATE` verwenden, mit der wir die Werte in der 
konfliktbehafteten Zeile zu ändern.

```mariadb
INSERT INTO player(player_name)
VALUES ('Lexy')
-- Wir definieren, dass wir etwas tun wollen, wenn ein Duplikat auf einem Schlüssel oder eindeutigen Index auftaucht
ON DUPLICATE KEY UPDATE last_online = CURRENT_TIMESTAMP;
```
