# Standardwerte

Standardwerte sind eine ziemlich nette Sache. Sie werden immer verwendet, wenn du etwas einfügst und die Spalte
leer lässt. Der Wert kann ein statischer Typ wie eine Zahl oder etwas Dynamisches wie das aktuelle Datum oder die Uhrzeit sein. Das ist eine sehr
nützliche Sache. Es gibt auch einen speziellen Standardwert, nämlich das automatische Erhöhen, das wir uns gleich ansehen werden.

Aber zuerst schauen wir uns an, wie Standardwerte funktionieren.

Erinnerst du dich noch an unsere alte Spielertabelle, die wir hatten? Wir sparen uns die Online-Zeit, wenn wir den Spieler manuell erstellen. Aber wir können eigentlich
davon ausgehen, dass der Spieler auch online sein sollte, wenn wir ihn das erste Mal einfügen. Also setzen wir die "last_online"-Zeit
auf die aktuelle Zeit, wenn wir unsere Einfügeanweisung ausführen.

*Vergiss nicht, deine Tabelle zuerst zu löschen.

```sql
CREATE TABLE player
(
    id INTEGER,
    player_name TEXT NOT NULL,
    -- Dies funktioniert auch in Postgres. Es wird jedoch empfohlen, stattdessen now() zu verwenden
    last_online TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

Der `current_timestamp` gibt immer den aktuellen Zeitstempel zurück, wenn wir die Daten einfügen. In Postgres würdest du
normalerweise stattdessen `now()` verwenden.

Lass uns überprüfen, ob es tatsächlich funktioniert!

```sql
INSERT INTO player(id, player_name)
VALUES (1, 'Mike'),
       (2, 'Sarah'),
       (3, 'John'),
       (4, 'Lilly'),
       (5, 'Matthias'),
       (6, 'Lenny'),
       (7, 'Summer'),
       (8, 'Marry'),
       (9, 'Milana'),
       (10, 'Lexi')
```

Beachte, dass wir dieses Mal nur `id` und `player_name` angeben und die Spalte `last_online` nicht setzen.

Und nun ist es an der Zeit, einen Blick auf unsere Daten zu werfen!

```sql
SELECT id, player_name, last_online
FROM player;
```

Und wir erhalten:

| id  | player\_name | last\_online               |
|:----|:-------------|:---------------------------|
| 1   | Mike         | 2022-11-25 23:52:26.081797 |
| 2   | Sarah        | 2022-11-25 23:52:26.081797 |
| 3   | John         | 2022-11-25 23:52:26.081797 |
| 4   | Lilly        | 2022-11-25 23:52:26.081797 |
| 5   | Matthias     | 2022-11-25 23:52:26.081797 |
| 6   | Lenny        | 2022-11-25 23:52:26.081797 |
| 7   | Summer       | 2022-11-25 23:52:26.081797 |
| 8   | Marry        | 2022-11-25 23:52:26.081797 |
| 9   | Milana       | 2022-11-25 23:52:26.081797 |
| 10  | Lexi         | 2022-11-25 23:52:26.081797 |

Es hat geklappt! Natürlich werden deine Zeiten deine aktuelle Zeit sein. Natürlich kannst du dort alles einfügen, was du
bereits erwähnt. Das ist eine sehr schöne Methode, um sicherzustellen, dass die Werte jedes Mal vorhanden sind, wenn du aus deiner Zeile liest.

Allerdings gibt es noch einen kleinen Fehler in unserer Tabelle.

Führe dies aus.

```sql
INSERT INTO player(id, player_name, last_online)
VALUES (11, 'Jonathan', NULL);
```

Werfen wir einen Blick auf unseren neu eingefügten Spieler.

```sql
SELECT id, player_name, last_online
FROM player
WHERE id = 11;
```

| id  | player_name | last_online |
|:----|:------------|:------------|
| 11  | Jonathan    | null        |

Wie wir sehen können, ist last_online null. Wir wollen aber, dass es immer ein Wert ist. Deshalb würden wir hier ein `NOT NULL` hinzufügen
hinzufügen.

Unsere Tabelle wird am Ende so aussehen.

*Vergiss nicht, deine Tabelle zuerst zu löschen*

```sql
CREATE TABLE player
(
    id INTEGER,
    player_name TEXT NOT NULL,
    -- Dies funktioniert auch in Postgres. Es wird jedoch empfohlen, stattdessen now() zu verwenden
    last_online TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

Fassen wir zusammen, was wir bis jetzt haben:

Wir können eine ID in unsere Tabelle einfügen, die auch null sein kann, dazu kommen wir später. Außerdem haben wir einen Spielernamen
Namen, der nicht null sein darf. Wenn wir einen Spieler einfügen, wird die Spalte "last_online" von der Datenbank gesetzt.
