# Joins

Vor allem nachdem du von der Normalisierung und der Aufteilung der Daten in verschiedene Tabellen gehört hast, fragst du dich jetzt vielleicht, wie wir 
diese Daten verbinden, wenn wir Daten aus mehreren Tabellen gleichzeitig benötigen.

Mit Joins können wir Tabellen anhand von Schlüsseln in der Tabelle miteinander verbinden. Das ist ein weiterer Grund, warum es wichtig ist, eindeutige 
Identifikatoren für Entitäten. Ohne diese ist das Verbinden von Tabellen ziemlich mühsam.

Bei Joins unterscheiden wir zwischen `LEFT JOIN` (das ist dasselbe wie `JOIN` und `RIGHT JOIN`, nur umgekehrt) 
und `INNER JOIN`. Weitere spezielle Joins sind `CROSS JOIN` und `FULL OUTER JOIN` (Dieser wird nicht von allen Datenbanken unterstützt. 
Datenbanken unterstützt. Wir werden ihn hier nicht behandeln).

## Left Join

Der Left Join ist der häufigste Join. Du wirst ihn in der Regel am häufigsten verwenden und in vielen Fällen reicht er aus. 
Fällen. Im Grunde genommen fügt er die rechte Tabelle der linken Tabelle hinzu.

```postgresql
-- Wir verwenden eine normale Select-Anweisung
SELECT *
-- Wir wählen unsere erste Tabelle aus
FROM left_table l
    -- Wir deklarieren einen Left Join und fügen unsere rechte Tabelle hinzu 
LEFT JOIN right_table r 
    -- Wir legen fest, welche Werte zur Definition unseres Join-Schlüssels verwendet werden.
    ON l.id = r.id
```

Nun gut. Das ist ziemlich abstrakt... Schauen wir mal, wie das mit unseren Daten aussieht. Zunächst fügen wir das money neben den Spielernamen 
den Spielernamen hinzu, denn die IDs sind ziemlich schwer zu lesen.

```postgresql
SELECT player_name, money
FROM player p 
LEFT JOIN money m ON p.id = m.player_id
ORDER BY p.id
```

| player_name | money |
|:-------------|:------|
| Lexy | 4117 |
| John | 7795 |
| Milana | 9843 |
| Mike | 4570 |
| Lenny | 984 |
| Marry | 2570 |
| Sommer | 1858 |
| Lilly | 3602 |
| Lexi | 6057 |
| Matthias | 6244 |
| Sarah | 268 |

Du siehst, dass wir unsere money-Werte jetzt schön neben den Spielernamen haben. Du könntest natürlich auch die ID neben 
hinzufügen, wenn du sie brauchst.

Die rechte Verknüpfung ist im Grunde die gleiche. Der einzige Unterschied besteht darin, wie die Datenbank mit Werten umgeht, die nicht 
nicht in einer der beiden Tabellen vorhanden sind. Das wird ganz klar, wenn wir stattdessen die Kanaltabelle joinen.

```postgresql
SELECT player_name, channel_id
FROM player
LEFT JOIN channel_subscription cs ON player.id = cs.player_id;
```

| player_name | channel_id |
|:-------------|:------------|
| Mike | 1 |
| Mike | 2 |
| Sarah | 1 |
| Sarah | 2 |
| Sarah | 3 |
| John | 1 |
| Lexi | null |
| Matthias | null |
| Marry | null |
| Lenny | null |
| Lilly | null |
| Lexy | null |
| Milana | null |
| Summer | null |

Du kannst hier sehen, dass die Werte für Spieler, die keinen Eintrag in der Tabelle `channel_subcription` haben, einfach `null` sind.

Wenn wir andersherum verbinden:

```postgresql
SELECT player_name, channel_id
FROM player
RIGHT JOIN channel_subscription cs ON player.id = cs.player_id;
```

| player_name | channel_id |
|:-------------|:------------|
| Mike | 1 |
| Mike | 2 |
| Sarah | 1 |
| Sarah | 2 |
| Sarah | 3 |
| John | 1 |

Es werden nur die Einträge angezeigt, die in der `channel_subscription` vorhanden sind, da dies unsere Referenztabelle ist.

## Inner Join

Der `INNER JOIN` ist genau das, wofür du ihn hältst. Er verbindet alle Daten, deren Schlüssel in beiden Tabellen vorhanden sind. 
Er ist sogar noch restriktiver als der `LEFT JOIN` und der `RIGHT JOIN`. Wenn wir den `INNER JOIN` verwenden, müssen wir uns nicht 
müssen wir uns nicht um fehlende Werte in unserer Tabelle kümmern, aber es kann sein, dass wir nicht alle Daten unserer ersten Tabelle erhalten.

```postgresql
SELECT player_name, channel_id
FROM player
INNER JOIN channel_subscription cs ON player.id = cs.player_id;
```

| player_name | channel_id |
|:-------------|:------------|
| Mike | 1 |
| Mike | 2 |
| Sarah | 1 |
| Sarah | 2 |
| Sarah | 3 |
| John | 1 |


Der `INNER JOIN` hier ist im Grunde dasselbe wie unser `RIGHT JOIN` oben. Anstatt jedoch die Tabelle mit den 
Daten zu wählen, verwenden wir einfach die kleinste Teilmenge unserer Tabellen.
