# MariaDB

Die Query Planer von MariaDB und MySQL sind recht einfach zu verstehen. Eine ausführliche Beschreibung findest du in den 
docs von [MariaDB](https://mariadb.com/kb/en/explain/) und MySQL.

Wir beginnen mit unserer Grundabfrage von vorhin:

```sql
EXPLAIN
SELECT *
FROM player
WHERE id = 5;
```

Dies gibt zurück:


| id | select\_type | table | type | possible\_keys | key | key\_len | ref | rows | Extra |
|:----|:-------------|:-------|:-----|:---------------|:-----|:---------|:-----|:-----|:------------|
| 1 | SIMPLE | player | ALL | null | null | null | null | 10 | Using where |

Beginnen wir mit den einzelnen Spalten und schauen wir uns an, was sie eigentlich bedeuten:

- `id` -> Die Reihenfolge, wenn du mehrere Tabellen kombinierst (Das haben wir noch nicht gemacht)
- `select_type` -> Die Herkunft der ausgewählten Daten. Es gibt verschiedene Typen wie einfache oder Selects auf den 
  Schlüssel `PRIMARY`. Diese werden wir noch öfter sehen.
- `table` -> die Tabelle, aus der wir ausgewählt haben. In unserem Fall die Tabelle `player`.
- `Typ` -> Das ist wahrscheinlich unsere wichtigste Spalte. In dieser Spalte steht derzeit "all", was bedeutet, dass wir 
  alle Zeilen der Tabelle. Ideal wäre so etwas wie `index` oder `range`, was eine Verkürzung der Laufzeit bedeutet.
- `possible_keys` zeigt die Namen der Schlüssel in der Tabelle an, aus der wir lesen.
- `key` -> Der tatsächlich verwendete Schlüssel für unsere Abfrage.
- `key_len` -> die Länge des verwendeten Schlüssels, wenn wir mehrspaltige Schlüssel verwenden
- `ref` -> Die Referenz des Schlüsselwertes
- `rows` -> Eine Schätzung, wie viele Zeilen wir erwarten können.
- `Extra` -> einige zusätzliche Informationen. In unserem Fall sagt es uns, dass wir nach einer Where-Klausel suchen 

## Analysieren

Zusätzlich kann das Schlüsselwort `ANALYZE` dir einen Einblick in das geben, was tatsächlich vor sich geht. Damit wird die 
Abfrage aus und misst alles:

```sql
ANALYZE
SELECT *
FROM player
WHERE id = 5;
```

Ergebnisse in:

| id | select\_type | table | type | possible\_keys | key | key\_len | ref | rows | r\_rows | filtered | r\_filtered | Extra |
|:----|:-------------|:-------|:-----|:---------------|:-----|:---------|:-----|:-----|:--------|:---------|:------------|:------------|
| 1 | EINFACH | player | ALL | null | null | null | null | 10 | 10.00 | 100 | 10 | Mit wo |

Zusätzlich zu den vorherigen Schätzungen zeigen die Spalten mit dem Präfix `r_` die tatsächlichen Werte an.
