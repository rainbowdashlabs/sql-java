# MySQL

Der Query Planer von MySQL ist recht einfach zu verstehen.
Eine ausführliche Beschreibung findest du in den Dokumentationen von [MySQL](https://dev.mysql.com/doc/refman/8.0/en/explain.html).

Wir beginnen mit unserer Basisabfrage von vorhin:

```sql
EXPLAIN
SELECT *
FROM player
WHERE id = 5;
```

Dies gibt zurück:


| id  | select\_type | table  | type | possible\_keys | key  | key\_len | ref  | rows | Extra       |
|:----|:-------------|:-------|:-----|:---------------|:-----|:---------|:-----|:-----|:------------|
| 1   | SIMPLE       | player | ALL  | null           | null | null     | null | 10   | Using where |

Beginnen wir mit den einzelnen Spalten und schauen wir uns an, was sie eigentlich bedeuten:

- `id` -> Die Reihenfolge, wenn du mehrere Tabellen kombinierst (Das haben wir noch nicht gemacht)
- `select_type` -> Die Herkunft der ausgewählten Daten. Es gibt verschiedene Typen wie einfache oder Selects auf den Schlüssel `PRIMARY`. Diese werden wir noch öfter sehen.
- `table` -> die Tabelle, aus der wir ausgewählt haben. In unserem Fall die Tabelle `player`.
- `Typ` -> Das ist wahrscheinlich unsere wichtigste Spalte. In dieser Spalte steht derzeit "all", was bedeutet, dass wir alle Zeilen der Tabelle. Ideal wäre so etwas wie `index` oder `range`, was eine Verkürzung der Laufzeit bedeutet.
- `possible_keys` zeigt die Namen der Schlüssel in der Tabelle an, aus der wir lesen.
- `key` -> Der tatsächlich verwendete Schlüssel für unsere Abfrage.
- `key_len` -> die Länge des verwendeten Schlüssels, wenn wir mehrspaltige Schlüssel verwenden
- `ref` -> Die Referenz des Schlüsselwertes
- `rows` -> Eine Schätzung, wie viele Zeilen wir erwarten können.
- `Extra` -> einige zusätzliche Informationen. In unserem Fall sagt es uns, dass wir nach einer Where-Klausel suchen 

## Analysieren

Mit dem Schlüsselwort "ANALYZE" kannst du dir außerdem einen Überblick über die tatsächlichen Vorgänge verschaffen.
Damit wird die Abfrage ausgeführt und alles gemessen:

```sql
EXPLAIN ANALYZE
SELECT *
FROM player
WHERE id = 5;
```

Das Ergebnis ist:

```
-> Filter: (player.id = 5) (cost=1.25 rows=1) (actual time=0.033..0.042 rows=1 loops=1)
    -> Table scan on player (cost=1.25 rows=10) (actual time=0.023..0.037 rows=10 loops=1)
```

Dieses Format ist dem Format, das [postgres](postgres.md#analyze) verwendet, sehr ähnlich.
Sieh dir das erst einmal an.
