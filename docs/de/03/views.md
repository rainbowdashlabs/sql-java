# Views

Views sind wichtig, um komplexe Abfragen in eine Art "virtuelle Tabelle" zu verpacken.
Mit Views kannst du etwas erstellen, das auf einer Abfrage basiert und jedes Mal dynamisch erstellt wird, wenn du diese View aufrufst.

Du könntest zum Beispiel einfach unsere Freundesanzahl in eine Ansicht packen und statt diesen verfluchten Gewerkschaftscode auszuwählen, kannst du sie einfach wie eine Tabelle auswählen.
Views werden mit dem Schlüsselwort `CREATE VIEW` erstellt, gefolgt von einer `SELECT`-Anweisung.

```postgresql
CREATE VIEW friend_count AS
SELECT player_id,
       SUM(friend_count)
FROM (SELECT player_id_1 AS player_id, COUNT(1) AS friend_count
      FROM friend_graph
      GROUP BY player_id_1
      UNION ALL
      SELECT player_id_2 AS player_id, COUNT(1) AS friend_count
      FROM friend_graph
      GROUP BY player_id_2) zählt
GROUP BY player_id;
```

Sobald wir diesen Code ausgeführt haben, müssen wir nur noch die Freundesanzahl aus unserer Ansicht `friend_count` auslesen.
Allerdings unterscheidet unsere Datenbank bei der Auswahl nicht zwischen Tabelle und View.
Wir müssen unseren View wie eine Tabelle behandeln.

```postgresql
SELECT player_id, sum
FROM friend_count;
```

| player_id | sum |
|:-----------|:----|
| 3 | 2 |
| 4 | 4 |
| 2 | 3 |
| 1 | 3 |

Views auf eine Tabelle sind eine gute Möglichkeit, um größere SQL-Anweisungen zu verstecken.
Denke daran, dass bei jedem Aufruf der View auch die zugrunde liegende Abfrage aufgerufen wird.
Aus diesem Grund können Views deine Aggregation nicht wirklich beschleunigen.

## Materialisierte Ansichten

**Nur Postgres**

Wenn du Postgres verwendest, hast du Glück, denn Postgres verfügt über sogenannte materialisierte Ansichten.
Das sind echte Tabellen, die von einer Abfrage erstellt werden.
Du definierst sie genauso wie die Views und fügst einfach das Schlüsselwort `MATERIALIZED` hinzu:

```postgresql
CREATE MATERIALIZED VIEW friend_count_mat AS
SELECT player_id,
       SUM(friend_count)
FROM (SELECT player_id_1 AS player_id, COUNT(1) AS friend_count
      FROM friend_graph
      GROUP BY player_id_1
      UNION ALL
      SELECT player_id_2 AS player_id, COUNT(1) AS friend_count
      FROM friend_graph
      GROUP BY player_id_2) zählt
GROUP BY player_id;
```

Der große Unterschied ist, dass diese Ansicht nicht neu generiert wird, wenn sie gelesen wird, und das macht deine Datenbankabfragen in der Tat viel schneller, wenn du deine Daten nur von Zeit zu Zeit aggregieren musst.

Eine materialisierte Ansicht muss von dir aktualisiert werden, was du ganz einfach durch einen Aufruf tun kannst:

```postgresql
REFRESH MATERIALIZED VIEW friend_count_mat;
```

Dadurch wird deine Ansicht mit der Abfrage aktualisiert, die sie bei der Erstellung verwendet hat.
