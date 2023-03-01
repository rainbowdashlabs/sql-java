# Indizes und Ausdrücke

Im vorherigen Abschnitt haben wir bereits gesehen, dass wir Ausdrücke für unsere Abfragen verwenden können. Auf diese Weise können wir eine Menge
in unseren Tabellen jetzt mit einigen konkreteren Beispielen!

Zuerst müssen wir unsere Spielertabelle aufräumen, um fortzufahren.
Entweder löschst du alle Dummy-Einträge, die wir in den letzten Abschnitten mit der folgenden Abfrage erstellt haben, oder du erstellst sie von Grund auf neu.

```sql
DELETE
FROM player
WHERE player_name = 'Spielername';
```

## Erstellen eines eindeutigen Indexes ohne Berücksichtigung der Groß-/Kleinschreibung

Die erste Aufgabe besteht darin, einen Index für den Spielernamen zu erstellen, der die Groß- und Kleinschreibung nicht berücksichtigt. Wir wollen vermeiden, dass mehrere Spieler
Wir wollen vermeiden, dass mehrere Spieler denselben Namen haben, und brauchen daher eine Prüfung, die sicherstellt, dass wir den Namen nicht zweimal einfügen. Außerdem wollen wir
sicherstellen, dass der Name nicht in einem anderen Gehäuse existiert.

Zu diesem Zweck verwenden wir wieder einen Ausdruck in unserem Index. Die Abfrage wird auch dieses Mal eindeutig sein. Ein Fallstrick
ist, dass nicht alle Datenbanken hier gleich funktionieren. MariaDB unterstützt Ausdrücke nicht direkt, sondern nur über
generierte Spalten, während MySQL verlangt, dass wir eine Teilzeichenkette verwenden oder unseren Wert casten. PostgreSQL und SqLite akzeptieren den
einfachste Ansatz.

### PostgreSQL, SqLite

Hier erstellen wir lediglich einen neuen Index, der die klein geschriebene Version unseres Spielernamens enthält.

```postgresql
CREATE UNIQUE INDEX player_name_uindex 
    ON player ((LOWER(player_name)))
```

### MySQL

MySQL verlangt, dass wir den substring-Aufruf substring verwenden oder den von lower zurückgegebenen Wert casten. Wir indizieren nur die
ersten 50 Zeichen unseres Namens. Das sollte für die Länge ausreichen. Wenn du auf Nummer sicher gehen willst, kannst du eine
[check](../data_consistency/checks.md) hinzufügen, um sicherzustellen, dass die Namen nicht länger als 50 Zeichen sind.

```mysql
CREATE UNIQUE INDEX player_name_uindex 
    ON player ((SUBSTRING(LOWER(player_name), 1, 50)));
```

### MariaDB

Leider unterstützt MariaDB keine direkten Ausdrücke in Indizes. Stattdessen erstellen wir eine generierte Spalte und verwenden den
Wert dieser Spalte, um unseren Index zu erstellen. Das ist wahrscheinlich der komplexeste Weg, der möglich ist.

Dazu verwenden wir den Befehl alter table, den wir uns noch nicht angeschaut haben, aber es sollte ziemlich klar sein, was er bewirkt.

```mariadb
ALTER TABLE player
    ADD COLUMN name_lower TEXT GENERATED ALWAYS AS (LOWER(player_name)) STORED;

CREATE UNIQUE INDEX player_name_uindex 
    ON player (name_lower)
```

### Testen

Schauen wir uns an, wie und ob dies tatsächlich funktioniert. Wir werden versuchen, Lexy noch einmal gegenseitig in unsere Tabelle einzufügen. Idealerweise würde dies
scheitern, da wir bereits einen Spieler namens Lexy haben.

```postgresql
INSERT INTO player(player_name)
VALUES ('LEXY');
INSERT INTO player(player_name)
VALUES ('lexy');
```

Du wirst auch sehen, dass beide Abfragen unabhängig von der Groß- und Kleinschreibung fehlschlagen. Das ist das Ergebnis unserer Abfrage, bei der die Groß- und Kleinschreibung nicht berücksichtigt wird.

## Einen Index einer bidirektionalen Beziehung erstellen

Wir können auch einen anderen Indexausdruck verwenden, um sicherzustellen, dass Freundschaftsbeziehungen in beide Richtungen existieren und nicht
einmal für jede Richtung existieren.

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

````postgresql
DELETE FROM friend_graph;
-- Erfolg
INSERT INTO friend_graph VALUES (1,2);
-- Fehlschlag
INSERT INTO friend_graph VALUES (2,1);
```

Wenn alles wie erwartet funktioniert, können wir die erste ID einfügen, aber nicht die zweite, da der kombinierte Wert der 
beiden IDs aufgrund der XOR-Verknüpfung gleich sind. Jetzt haben wir sichergestellt, dass eine Freundschaftsverbindung nur in einer Richtung existiert 
und jetzt in beiden.
