# Indizes und Ausdrücke

Im vorherigen Abschnitt haben wir bereits gesehen, dass wir Ausdrücke für unsere Abfragen verwenden können.
Damit können wir eine Menge Dinge in unseren Tabellen korrigieren, jetzt mit einigen konkreteren Beispielen!

Zuerst müssen wir unsere `player` Tabelle aufräumen, um fortzufahren.
Entweder löschst du alle Dummy-Einträge, die wir in den letzten Abschnitten mit der folgenden Abfrage erstellt haben, oder du erstellst sie von Grund auf neu.

```sql
DELETE
FROM player
WHERE player_name = 'player_name';
```

## Erstellen eines eindeutigen Indexes ohne Berücksichtigung der Groß-/Kleinschreibung

Die erste Aufgabe besteht darin, einen Index für den `player_name` zu erstellen, der die Groß- und Kleinschreibung nicht berücksichtigt.
Wir wollen vermeiden, dass mehrere Spieler denselben Namen haben, und brauchen daher eine Prüfung, die sicherstellt, dass wir den Namen nicht zweimal einfügen.
Außerdem wollen wir sicherstellen, dass es den Namen nicht mit einer anderen Schreibweise gibt.

Zu diesem Zweck verwenden wir erneut einen Ausdruck in unserem Index.
Auch dieses Mal ist die Abfrage eindeutig.
Ein Problem dabei ist mal wieder, dass nicht alle Datenbanken gleich funktionieren.
MariaDB unterstützt Ausdrücke nicht direkt, sondern nur über generierte Spalten, während MySQL möchte, dass wir eine Teilzeichenkette verwenden oder unseren Wert umwandeln.
PostgreSQL und SqLite akzeptieren den einfachsten Ansatz.

### PostgreSQL, SqLite

Hier erstellen wir lediglich einen neuen Index, der die klein geschriebene Version unserer `player_name` Spalte enthält.

```postgresql
CREATE UNIQUE INDEX player_name_uindex
    ON player ((LOWER(player_name)))
```

### MySQL

MySQL verlangt, dass wir die `substring` Funktion verwenden oder den von lower zurückgegebenen Wert casten.
Wir werden nur die ersten 50 Zeichen unseres Namens indizieren.
Das sollte für die Länge ausreichen.
Wenn du auf Nummer sicher gehen willst, kannst du einen [Check](../data_consistency/checks.md) hinzufügen, um sicherzustellen, dass die Namen nicht länger als 50 Zeichen sind.

```mysql
CREATE UNIQUE INDEX player_name_uindex 
    ON player ((SUBSTRING(LOWER(player_name), 1, 50)));
```

### MariaDB

Leider unterstützt MariaDB keine direkten Ausdrücke in Indizes.
Stattdessen erstellen wir eine generierte Spalte und verwenden den Wert dieser Spalte, um unseren Index zu erstellen.
Das ist wahrscheinlich der komplexeste Weg, der möglich ist.

Dazu verwenden wir den Befehl `ALTER TABLE`, den wir uns noch nicht angeschaut haben, aber es sollte ziemlich klar sein, was er bewirkt.

```mariadb
ALTER TABLE player
    ADD COLUMN name_lower TEXT GENERATED ALWAYS AS (LOWER(player_name)) STORED;

CREATE UNIQUE INDEX player_name_uindex 
    ON player (name_lower)
```

### Testen

Schauen wir uns an, wie und ob dies tatsächlich funktioniert.
Wir werden versuchen, Lexy erneut in unsere Tabelle einzufügen.
Im Idealfall würde dies fehlschlagen, da wir bereits einen Spieler namens Lexy haben.

```postgresql
INSERT INTO player(player_name)
VALUES ('LEXY');
INSERT INTO player(player_name)
VALUES ('lexy');
```

Du wirst auch sehen, dass beide Abfragen unabhängig von der Groß- und Kleinschreibung fehlschlagen.
Das ist das Ergebnis unserer Abfrage, bei der die Groß- und Kleinschreibung nicht berücksichtigt wird.

## Einen Index einer bidirektionalen Beziehung erstellen

Wir können auch einen anderen Indexausdruck verwenden, um sicherzustellen, dass Freundschaftsbeziehungen in beide Richtungen bestehen und nicht nur einmal für jede Richtung existieren können.

Diesmal werden wir XOR verwenden, um einen Schlüssel für unsere Freundschaftsbeziehungen zu erstellen.

**Postgres**

```postgresql
CREATE UNIQUE INDEX friend_graph_relation_uindex 
    ON friend_graph ((player_id_1 # player_id_2));
```

**MySQL & MariaDB**

```mysql
CREATE UNIQUE INDEX friend_graph_relation_uindex
    ON friend_graph ((player_id_1 ^ player_id_2));
```

**SqLite**

SqLite hat keinen XOR-Operator, aber wir können "einfach" unser eigenes XOR erstellen.

```sqlite
CREATE UNIQUE INDEX friend_graph_relation_uindex 
    ON friend_graph (((player_id_1 | player_id_2) - (player_id_1 & player_id_2)));
```

Zeit zu testen, ob unsere Indizes tatsächlich funktionieren!

Zuerst löschen wir die Tabelle, um nicht versehentlich widersprüchliche Einträge einzufügen.

```postgresql
DELETE FROM friend_graph;
-- Erfolg
INSERT INTO friend_graph VALUES (1,2);
-- Fehlschlag
INSERT INTO friend_graph VALUES (2,1);
```

Wenn alles wie erwartet funktioniert, können wir die erste einfügen, aber nicht die zweite, da der kombinierte Wert der beiden IDs aufgrund des XORs gleich ist.
Jetzt haben wir sichergestellt, dass eine Freundschaftsverbindung nur in einer Richtung existiert und jetzt in beiden.
