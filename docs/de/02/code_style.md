# Code-Stil

SQL-Code sollte leicht lesbar sein. Wenn du die oben genannten Namenskonventionen verwendest, geht das fast von
von selbst. Mach dir vorerst keine Gedanken über den eigentlichen Code. Du brauchst ihn noch nicht zu verstehen.

Ich werde die Syntaxhervorhebung an dieser Stelle deaktivieren, um ihn klarer zu machen.

## Schlüsselwörter

Alle SQL-Schlüsselwörter sollten in GROSSBUCHSTABEN geschrieben werden, damit klar wird, welche Wörter Namen und welche Schlüsselwörter sind.

```
-- Schlecht
select col from my_table;

-- Gut
SELECT col FROM meine_tabelle;
```

## Zitiere nur, wenn es nötig ist

Viele Leute neigen dazu, viel zu zitieren, was nicht nötig ist. Wenn du ein gutes Benennungsschema verwendest, musst du wahrscheinlich nie
zitieren.

```
-- Schlecht
SELECT `col` FROM `meine_tabelle`;

-- Gut
SELECT col FROM meine_tabelle;
```

## Zeilenumbrüche sind gut

Zeilenumbrüche machen größere Abfragen viel lesbarer. Eine gute Regel ist, jedem Schlüsselwort einen Zeilenumbruch voranzustellen. Du
kannst nach eigenem Ermessen weitere Zeilenumbrüche hinzufügen

```
-- Schlecht
SELECT col1, col2 FROM meine_tabelle WHERE col1 = 'etwas' AND col2 = 'sonst' LIMIT 1;

-- Gut
SELECT sp1,
    Spalte2
FROM meine_tabelle
WHERE Spalte1 = 'irgendwas'
    AND col2 = 'sonst'
LIMIT 1;

-- Noch besser
SELECT 
    col1,
    Spalte2
FROM
    meine_tabelle
WHERE
    Spalte1 = 'etwas'
    AND col2 = 'sonst'
LIMIT 1;
```

## Aussagekräftiger Alias

Gib deiner Tabelle einen aussagekräftigen Alias. Wähle einen aussagekräftigen, kürzeren Namen anstelle von einzelnen Zeichen wie "x", "y" oder "z".

## Verwende Common Table Expressions (CTE) anstelle von Unterabfragen

CTE kann deinen komplexen Code viel lesbarer machen.

Sieh dir dieses Beispiel für eine Unterabfrage an, die die Anzahl der Freunde aller Spieler anzeigt, die in den letzten 10 Tagen gesehen wurden.

<!-- @formatter:off --> 
```sql
SELECT -- (4)
       id,
       COALESCE(friend.count, 0)
FROM ( -- (1)
         SELECT id
         FROM spieler
         WHERE last_online > NOW() - '10 DAYS':: INTERVAL
     ) aktiv
         LEFT JOIN ( -- (3)
    SELECT user_id, COUNT(1) AS friend_count
    FROM ( -- (2)
             SELECT spieler_1 AS spieler_id
             FROM friend_graph
             UNION ALL
             SELECT spieler_2 AS spieler_id
             FROM friend_graph
         ) flat_friend_graph
    GROUP BY user_id
) Freund
                   ON active.id = friend.player_id
```
<!-- @formatter:on --> 

1. Wir wählen alle Spieler aus, die in den letzten 10 Tagen online waren.
2. Unsere Freundschaftsgraphen-Tabelle ist ein unidirektionaler Graph, der einen Eintrag pro Freundschaft enthält Wir verwenden ein UNION ALL, um die
   die Spalte "Spieler_2" an die Spalte "Spieler_1" an.
3. Wir berechnen die Anzahl der Freunde. Wir gruppieren nach der used_id und zählen, wie oft jede Nutzer-ID vorkommt.
4. Wir kombinieren unsere Freundeszahl mit den aktiven Spielern und erhalten eine Tabelle mit der Freundeszahl aller aktiven Spieler.

Wenn wir CTEs verwenden, würde es so aussehen.

<!-- @formatter:off --> 
```sql
WITH active_players AS ( -- (1)
    SELECT id
    FROM spieler
    WHERE last_online > NOW() - INTERVAL '10 DAYS'
),
flat_friend_graph AS ( -- (2)
    SELECT spieler_1 AS spieler_id
FROM friend_graph
    UNION ALL
    SELECT spieler_2 AS spieler_id
    FROM friend_graph
),
friend_count AS ( -- (3)
    SELECT user_id, COUNT(1) AS friend_count
    FROM flat_friend_graph
    GROUP BY user_id
)
SELECT -- (4)
       id,
       COALESCE(friend.count, 0)
FROM active_players active
LEFT JOIN friend_count friend 
    ON active.id = friend.player_id
```
<!-- @formatter:on --> 

1. Wir wählen alle Spieler aus, die in den letzten 10 Tagen online waren.
2. Unsere Freundschaftsgraphen-Tabelle ist ein unidirektionaler Graph, der einen Eintrag pro Freundschaft enthält Wir verwenden ein UNION ALL, um die
   die Spalte "Spieler_2" an die Spalte "Spieler_1" an.
3. Wir berechnen die Anzahl der Freunde. Wir gruppieren nach der used_id und zählen, wie oft jede Nutzer-ID vorkommt.
4. Wir kombinieren unsere Freundeszahl mit den aktiven Spielern und erhalten eine Tabelle mit der Freundeszahl aller aktiven Spieler.
