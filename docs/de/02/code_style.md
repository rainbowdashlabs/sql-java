# Code-Stil

SQL-Code sollte leicht lesbar sein.
Wenn du die unten angegebenen Namenskonventionen verwendest, geht das fast von alleine.
Mach dir vorerst keine Gedanken über den eigentlichen Code.
Du brauchst ihn noch nicht zu verstehen.

Ich werde die Syntaxhighlighting hier deaktivieren, um dir zu zeigen, dass er einfacher zu lesen ist.

## Schlüsselwörter

Alle SQL-Schlüsselwörter sollten in `GROSSBUCHSTABEN` geschrieben werden, damit klar wird, welche Wörter Namen und welche Schlüsselwörter sind.

```
-- Bad
select col from my_table;

-- Good
SELECT col FROM my_table;
```

## Akzente nur, wenn es nötig ist

Viele Leute neigen dazu, alles in akzente zu setzen, was nicht nötig ist.
Wenn du ein gutes Namensschema verwendest, wirst du deine Worte wahrscheinlich nie in Akzente setzen müssen.

```
-- Bad
SELECT `col` FROM `my_table`;

-- Good
SELECT col FROM my_table;
```

## Zeilenumbrüche sind gut

Zeilenumbrüche machen größere Abfragen viel lesbarer.
Eine gute Regel ist, jedem Schlüsselwort einen Zeilenumbruch voranzustellen.
Du kannst nach eigenem Ermessen weitere Zeilenumbrüche hinzufügen.

```
-- Bad
SELECT col1, col2 FROM my_table WHERE col1 = 'something' AND col2 = 'else' LIMIT 1;

-- Good
SELECT col1,
    col2
FROM my_table
WHERE col1 = 'something'
    AND col2 = 'else'
LIMIT 1;

-- Even better
SELECT 
    col1,
    col2
FROM
    my_table
WHERE
    col1 = 'something'
    AND col2 = 'else'
LIMIT 1;
```

## Aussagekräftiger Alias

Gib deiner Tabelle einen aussagekräftigen Alias.
Wähle einen beschreibenden, kürzeren Namen anstelle von einzelnen Zeichen wie "x", "y" oder "z".

## Verwende Common Table Expressions (CTE) anstelle von Unterabfragen

CTE kann deinen komplexen Code viel lesbarer machen.

Sieh dir dieses Beispiel einer Anfrage an, die die Anzahl der Freunde aller Spieler anzeigt, die in den letzten 10 Tagen gesehen wurden.

<!-- @formatter:off --> 
```sql
SELECT -- (4)
       id,
       COALESCE(friend.count, 0)
FROM ( -- (1)
         SELECT id
         FROM player
         WHERE last_online > NOW() - '10 DAYS':: INTERVAL
     ) active
         LEFT JOIN ( -- (3)
    SELECT user_id, COUNT(1) AS friend_count
    FROM ( -- (2)
             SELECT player_1 AS player_id
             FROM friend_graph
             UNION ALL
             SELECT player_2 AS player_id
             FROM friend_graph
         ) flat_friend_graph
    GROUP BY user_id
) friend
    ON active.id = friend.player_id
```
<!-- @formatter:on --> 

1. Wir wählen alle Spieler aus, die in den letzten 10 Tagen online waren.
2. Unsere Freundschaftsgraphtabelle ist ein unidirektionaler Graph, der einen Eintrag pro Freundschaft enthält. Wir verwenden ein UNION ALL, um die Spalte "Spieler_2" an die Spalte "Spieler_1" anzuhängen.
3. Wir berechnen die Anzahl der Freunde. Wir gruppieren nach der used_id und zählen, wie oft jede Nutzer-ID vorkommt.
4. Wir kombinieren unsere Freundeszahl mit den aktiven Spielern und erhalten eine Tabelle mit der Freundeszahl aller aktiven Spieler.

Wenn wir CTEs verwenden, würde es so aussehen.

<!-- @formatter:off --> 
```sql
WITH active_players AS ( -- (1)
    SELECT id
    FROM player
    WHERE last_online > NOW() - INTERVAL '10 DAYS'
),
flat_friend_graph AS ( -- (2)
    SELECT player_1 AS player_id
    FROM friend_graph
    UNION ALL
    SELECT player_2 AS player_id
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
2. Unsere Freundschaftsgraphtabelle ist ein unidirektionaler Graph, der einen Eintrag pro Freundschaft enthält. Wir verwenden ein UNION ALL, um die Spalte "Spieler_2" an die Spalte "Spieler_1" anzuhängen.
3. Wir berechnen die Freundesanzahl. Wir gruppieren nach der used_id und zählen, wie oft jede Nutzer-ID vorkommt.
4. Wir kombinieren unsere Freundesanzahl mit den aktiven Spielern und erhalten eine Tabelle mit der Freundesanzahl aller aktiven Spieler.
