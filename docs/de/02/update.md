# Aktualisieren

Jetzt können wir Daten einfügen und lesen.
Aber was ist, wenn wir bereits eingefügte Daten aktualisieren wollen?

Hier kommt die `UPDATE`-Anweisung zum Einsatz.
Um Daten zu aktualisieren, müssen wir auch die `WHERE`-Anweisung verwenden, die im vorherigen [Kapitel] (../02/select.md#where) eingeführt wurde.

Erinnere dich an unsere bereits gut bekannte Tabelle mit den Spielern.

| id | player\_name | last\_online |
|:----|:-------------|:---------------------------|
| 1 | Mike | 2022-05-11 00:00:00.000000 |
| 2 | Sarah | 2022-04-04 00:00:00.000000 |
| 3 | John | 2022-04-08 00:00:00.000000 |
| 4 | Lilly | 2022-04-01 00:00:00.000000 |
| 5 | Matthias | 2022-03-06 00:00:00.000000 |
| 6 | Lenny | 2022-03-08 00:00:00.000000 |
| 7 | Sommer | 2022-05-22 00:00:00.000000 |
| 8 | Heiraten | 2022-06-04 00:00:00.000000 |
| 9 | Milana | 2022-02-12 00:00:00.000000 |
| 10 | Lexi | 2022-02-22 00:00:00.000000 |

In der Spalte "last_online" wird das letzte Mal gespeichert, als der Spieler online war.

Wenn Lexy wieder online ist, müssen wir den Wert für "last_online" gegenseitig aktualisieren.
Wir haben jetzt zwei Möglichkeiten.
Die erste ist, den Eintrag zu löschen und einen neuen einzufügen.
Das ist sehr unsauber und auch keine gute Praxis.
Deshalb verwenden wir die Anweisung `UPDATE` und legen in der `WHERE`-Klausel fest, wo wir aktualisieren wollen und was wir aktualisieren wollen.

Die allgemeine Syntax lautet:

```sql
UPDATE player
SET spalte_x = wert_x,
    spalte_y = wert_y
WHERE Bedingung
```

## Grundlegende Aktualisierung

Mit diesem Wissen wollen wir nun versuchen, die "last_login"-Zeit von Lexy anhand ihrer ID "10" auf die aktuelle Zeit zu aktualisieren.

Wir brauchen wieder eine integrierte Funktion, um die aktuelle Zeit zu ermitteln.

- SqLite: `CAST(STRFTIME('%s', 'NOW') AS INTEGER)` wir haben keine Zeitstempel. Wir verwenden die aktuelle Zeit als Unix-Timestamp.
- MariaDB/MySQL: `current_timestamp()` gibt den aktuellen Zeitstempel zurück. Du kannst auch nur `current_timestamp` verwenden, das ist eine Konstante für die aktuelle Transaktion.
- PostgreSQL: `now()` gibt den aktuellen Zeitstempel zurück

**Lösungen:**

<Details>
<summary>SqLite</summary>


```sql
UPDATE player
SET last_online = CAST(strftime('%s', 'NOW') AS INTEGER)
WHERE id = 10;
```

</details>

<Details>
<summary>MariaDB/MySQL</summary>

```sql
UPDATE player
SET last_online = CURRENT_TIMESTAMP
WHERE id = 10;
```

</details>

<Details>
<summary>PostgreSQL</summary>

```sql
UPDATE player
SET last_online = NOW()
WHERE id = 10;
```

</details>

Wenn du nun den Eintrag von Lexy mit auswählst:

```sql
SELECT id,
       player_name,
       last_online
FROM player
WHERE id = 10
```

Du solltest einen Zeitstempel mit der aktuellen Uhrzeit in ihrer Spalte `last_online` erhalten.

## Mit aktuellem Wert aktualisieren

Natürlich können wir auch den aktuellen Wert der Spalte verwenden, die wir aktualisieren wollen.

Erinnerst du dich an unsere money-Tabelle, die wir im [insert chapter](../02/insert.md#create-tables-with-content) erstellt haben? 
Die brauchen wir jetzt wieder.

Nehmen wir an, wir wollen 600 unserer Währung von lexy nehmen, aber nur, wenn sie mindestens 600 hat.
Die Syntax dafür ist dieselbe wie die von vorhin.
Wir verweisen einfach auf den Spaltenwert selbst.

```sql
UPDATE player
SET spalte_x = wert_x,
    spalte_y = wert_y + <wert>
WHERE Bedingung
```

Versuche, das money zu entfernen und passe die Bedingung mit der Prüfung für das money an.

<Details>
<summary>Lösung</summary>

```sql
UPDATE money
SET money = money - 600
WHERE id = 10
  AND money >= 600
```

</details>


Lass uns überprüfen, was sich geändert hat:

```sql
SELECT id, money
FROM money
WHERE id = 10
```

| id | money |
|:----|:------|
| 10 | 400 |

Wir können jetzt sehen, dass Lexy nur 400 unserer Währung hat.
600 weniger als ursprünglich.
Wenn wir unser Update erneut ausführen, wird der wird dieser Wert immer noch derselbe sein.
Dieser Mechanismus kann sehr nützlich sein, wenn du sicher sein willst, dass der Spieler wirklich über Geld hat und das Geld direkt abheben kann.
