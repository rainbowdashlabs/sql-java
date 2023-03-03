# Sortierte Indizes

Im Allgemeinen sind Indizes immer sortiert, weil sie so funktionieren.
Das können wir nutzen, um Suchvorgänge zu beschleunigen, bei denen wir eine Tabelle nach einem Wert sortieren wollen.
Wenn wir einen Index für die Spalte erstellen, nach der wir sortieren wollen, können wir die Sortierung direkt überspringen, denn die Datenbank schaut sich nur den bereits sortierten Index an.

Wahrscheinlich wollen wir unsere Nutzer nach der Menge des Geldes sortieren, das sie haben!

*Vergiss nicht, zuerst einige Daten in deine `money` Tabelle einzufügen c: Du musst eventuell noch mehr hinzufügen, wenn du willst, dass der Index verwendet wird.*

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
|:----------|:------|
| 1406      | 9994  |
| 1358      | 9993  |
| 1430      | 9989  |
| 178       | 9985  |
| 1113      | 9977  |

Wenn du möchtest, kannst du den Queryplan jetzt überprüfen, so wie wir es im vorherigen [Abschnitt](../query_planer.md) getan haben. 
Du wirst sehen, dass wir derzeit überhaupt keinen Index verwenden.
Das wird sich natürlich ändern, wenn wir einen Index für die Spalte `money` hinzufügen.

```sql
-- Wir verwenden dieses Mal CREATE INDEX statt CREATE UNIQUE INDEX
CREATE INDEX money_money_index
    -- Wir legen auch fest, dass wir den Index absteigend sortieren wollen. Die Standardeinstellung ist aufsteigend.
    ON money (money DESC);
```

Schau dir jetzt den Queryplan für diese Abfrage an.

```sql
SELECT player_id, money
FROM money
ORDER BY money DESC
LIMIT 5;
```

Du wirst sehen, dass wir tatsächlich einen Index-Scan verwenden.
Und wenn wir nur nach einem Wert sortieren, benutzen wir den Index auch, wenn wir in die andere Richtung sortieren.

```sql
SELECT player_id, money
FROM money
ORDER BY money
LIMIT 5;
```

Bei dieser Abfrage wird der Index in umgekehrter Reihenfolge verwendet.
Das funktioniert natürlich nicht mehr, wenn wir nach mehreren Werten sortieren.
Wenn du nach mehreren Werten sortierst, musst du auch einen Index hinzufügen, der die gleiche Reihenfolge der Spalten und die gleiche Sortierrichtung hat.
