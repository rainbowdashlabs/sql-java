# Postgres

Der Postgres Query Planner ist extrem leistungsfähig und bietet einen hohen Detailgrad über die ausgeführten Aufgaben. Knotenpunkte werden
durch Einrückungen im Abfrageplan dargestellt. Die Knoten werden vom inneren zum äußeren Knoten ausgeführt. Dies ist
noch nicht wichtig, da unsere Abfragepläne vorerst sehr einfach sind und wahrscheinlich nicht einmal so viele Knoten haben werden.

Um einen tieferen Einblick in die Postgres-Abfragepläne zu bekommen, schau dir diese [Seite](https://www.postgresql.org/docs/current/using-explain.html) an.

Wir beginnen mit unserer Basisabfrage von vorhin:

```sql
EXPLAIN
SELECT *
FROM player
WHERE id = 5;
```

Dies gibt zurück:

```
Seq Scan on player (cost=0.00..24.12 rows=6 width=44)
  Filter: (id = 5)
```

Schauen wir uns nun an, was wir hier sehen und heben wir die wichtigsten Dinge hervor:

Erster Knoten:

- `Seq Scan` bedeutet, dass wir über die gesamte Tabelle iterieren. das ist der Knotentyp.
- on player" ist die Tabelle, die wir uns ansehen.
- `cost=0.00..24.12` die Kosten sind die Rechenleistung, die die Datenbank voraussichtlich benötigt. Höhere Zahlen bedeuten
  längere Zeiten. Die erste Zahl ist die Planungszeit. Die zweite Zahl ist die Zeit, die zum Lesen der Daten benötigt wird.
- Bei `rows=6` erwartet die Datenbank aufgrund der internen Statistiken, dass bis zu 6 Zeilen zurückgegeben werden.
- `width=44` ist die Menge der erwarteten Daten in Bytes

Zweiter Knoten:

- `Filter:` ist der Typ unseres nächsten Knotens. Ein Filter-Knoten.
- `(id = 5)` ist der Filter selbst.

Versuchen wir nun, ein komplexeres Beispiel zu erstellen. Du wirst hier einige neue Schlüsselwörter sehen, aber mach dir darüber erst einmal keine Gedanken:

```sql
SELECT p1.id, p1.player_name, p2.id, p2.player_name
FROM friend_graph f
         LEFT JOIN player p1 ON f.player_1 = p1.id
         LEFT JOIN player p2 ON f.player_2 = p2.id
         WHERE f.spieler_1 = 2 OR f.spieler_2 = 2; 
```

Das sieht vielleicht ein bisschen chaotisch aus, ist aber eigentlich ganz einfach zu verstehen:

```
Hash Right Join (cost=74.06..123.89 rows=734 width=72)
  Hash Cond: (p2.id = f.player_2)
  -> Seq Scan on player p2 (cost=0.00..21.30 rows=1130 width=36)
  -> Hash (cost=72.44..72.44 rows=130 width=40)
        -> Hash Right Join (cost=44.19..72.44 rows=130 width=40)
              Hash Cond: (p1.id = f.player_1)
              -> Seq Scan on player p1 (cost=0.00..21.30 rows=1130 width=36)
              -> Hash (cost=43.90..43.90 rows=23 width=8)
                    -> Seq Scan on friend_graph f (cost=0.00..43.90 rows=23 width=8)
                          Filter: ((spieler_1 = 2) OR (spieler_2 = 2))
```

Wir gehen es Schritt für Schritt durch und beginnen mit dem innersten Knoten.

```
                    -> Seq Scan on friend_graph f (cost=0.00..43.90 rows=23 width=8)
                          Filter: ((spieler_1 = 2) OR (spieler_2 = 2))
```

Wir kennen diesen Knoten bereits aus dem vorherigen Plan. ein einfacher Scan über die gesamte Tabelle mit einer Bedingung. Der einzige 
Unterschied ist, dass wir in unserem Filter auf zwei Bedingungen prüfen und nicht nur auf eine.

```
        -> Hash Right Join (cost=44.19..72.44 rows=130 width=40)
              Hash Cond: (p1.id = f.player_1)
              -> Seq Scan on player p1 (cost=0.00..21.30 rows=1130 width=36)
              -> Hash (cost=43.90..43.90 rows=23 width=8)
```

Wir müssen uns diese Knoten gemeinsam ansehen. Wir fügen unsere Tabelle zusammen. Das bedeutet im Grunde, dass wir einfach 
weitere Spalten an unsere ursprüngliche Tabelle, den Freundesgraphen, an. Wir tun dies auf der Grundlage einiger Bedingungen. Für diese Bedingungen müssen wir 
müssen wir die Spalten, die wir verwenden, mit einem Hash versehen. Am Ende müssen wir die gesamte Spielertabelle gegenseitig lesen, um alle passenden Spieler zu finden. 
Und das ist schon alles in diesem Knoten

```
Hash Right Join (cost=74.06..123.89 rows=734 width=72)
  Hash Cond: (p2.id = f.player_2)
  -> Seq Scan on player p2 (cost=0.00..21.30 rows=1130 width=36)
  -> Hash (cost=72.44..72.44 rows=130 width=40)
```

Dieser Knoten ist genau derselbe Knoten wie der vorherige. Der einzige Unterschied ist, dass wir die Spalte player_2 
unseres Freundesgraphen verwenden.

Ich gehe nicht davon aus, dass du das alles schon verstehst, also lass dich jetzt nicht erschrecken. Du sollst damit nur ein besseres 
ein besseres Verständnis für die Abfragepläne zu bekommen. Das meiste Wissen erhältst du, wenn du weitere Pläne liest und einen Blick in die 
die oben verlinkten umfangreichen Postgres-Dokumente.

## Analyze

Postgres hat ein zusätzliches Schlüsselwort, nämlich `ANALYZE`. Dieses Schlüsselwort führt die Abfrage aus und zeigt die Unterschiede
zwischen den Schätzungen von `EXPLAIN` und der tatsächlichen Laufzeit der Abfrage. Außerdem liefert es einige zusätzliche
Informationen.

Schauen wir uns an, was wir noch bekommen, wenn wir uns unsere Abfrage von vorhin ansehen:

```sql
EXPLAIN ANALYZE
SELECT *
FROM player
WHERE id = 5;
```

Dies gibt zurück:

```
Seq Scan on player (cost=0.00..24.12 rows=6 width=44) (actual time=0.009..0.010 rows=1 loops=1)
  Filter: (id = 5)
  Durch Filter entfernte Zeilen: 9
Planungszeit: 0.037 ms
Ausführungszeit: 0.022 ms
```

Du siehst hier eine Menge vertrauter Dinge, die du schon in der Ausgabe von `EXPLAIN` gesehen hast. Aber jetzt bekommen wir auch die
tatsächlichen Daten. Du kannst sehen, dass das Ergebnis der erwarteten und der tatsächlichen Zeilen sehr unterschiedlich ist. Du kannst auch sehen
dass unser Filter 9 Zeilen aus der Ergebnismenge entfernt hat, die nicht dem Filter entsprachen.

Außerdem erhältst du die tatsächliche Planungs- und Ausführungszeit der Abfrage selbst.
