# Sortierte Indizes

Im Allgemeinen sind Indizes immer sortiert, weil sie so funktionieren. Wir können dies nutzen, um die Suche zu beschleunigen
wenn wir eine Tabelle nach einem Wert sortieren wollen. Wenn wir einen Index für die Spalte erstellen, nach der wir sortieren wollen, können wir
können wir die Sortierung direkt überspringen, denn die Datenbank schaut sich nur den bereits sortierten Index an.

Wahrscheinlich wollen wir unsere Nutzer nach der Menge des Geldes sortieren, das wir haben!

*Vergiss nicht, zuerst einige Daten in deine money-Tabelle einzufügen c: Du musst eventuell noch weitere Daten hinzufügen, wenn du möchtest, dass der Index 
verwendet werden soll.*

<Details>
<summary>Datengenerierung für Postgres</summary>

```sql
-- Tabelle löschen
DELETE
FROM money;

-- Wir müssen weitere Werte generieren, um die Verwendung des Index zu erzwingen
INSERT INTO player(player_name) (SELECT 'player_name' FROM GENERATE_SERIES(1, 1500));

-- Generiere einige zufällige money-Werte
INSERT INTO money (SELECT id, ROUND(RANDOM() * 10000) FROM player);
```

</details>

```sql
SELECT player_id, money
FROM money
ORDER BY money DESC
LIMIT 5;
```

| player_id | money |
|:-----------|:------|
| 1406 | 9994 |
| 1358 | 9993 |
| 1430 | 9989 |
| 178 | 9985 |
| 1113 | 9977 |

Wenn du willst, kannst du den Abfrageplan jetzt überprüfen, wie wir es im vorherigen [Abschnitt](../query_planer.md) getan haben. Du wirst sehen
dass wir derzeit überhaupt keinen Index verwenden. Das wird sich natürlich ändern, sobald wir einen Index für die Spalte money hinzufügen.

```sql
-- wir verwenden dieses Mal CREATE INDEX statt CREATE UNIQUE INDEX
CREATE INDEX money_money_index
    -- Wir legen auch fest, dass wir den Index absteigend sortieren wollen. Die Standardeinstellung ist aufsteigend.
    ON money (money DESC);
```

Schau dir jetzt den Abfrageplan für diese Abfrage an.

```sql
SELECT player_id, money
FROM money
ORDER BY money DESC
LIMIT 5;
```

Du wirst sehen, dass wir tatsächlich einen Index-Scan verwenden. Auch wenn wir nur nach einem Wert sortieren, verwenden wir den Index 
wenn wir in die andere Richtung bestellen.

```sql
SELECT player_id, money
FROM money
ORDER BY money
LIMIT 5;
```

Bei dieser Abfrage wird der Index in umgekehrter Reihenfolge verwendet. Natürlich funktioniert das nicht mehr, wenn wir nach mehreren Werten ordnen. 
Werten sortieren. Wenn du nach mehreren Werten sortierst, musst du einen Index hinzufügen, der der gleichen Reihenfolge der Spalten und der Sortier 
Richtung übereinstimmen.
