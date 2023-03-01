# Select

Die Anweisung `SELECT` ist eine weitere sehr wichtige Anweisung. Nachdem wir nun Daten in unsere Tabellen eingefügt haben, wollen wir sie auch lesen.
sie auch lesen.

Die `SELECT`-Anweisung gibt immer eine sogenannte Ergebnismenge zurück. Diese Menge enthält alle von uns angeforderten Daten. Beachte, dass 
obwohl es "Menge" genannt wird, dies nicht bedeutet, dass die Ergebnisse in irgendeiner Weise eindeutig sind.

## Basic select

Die reine Basic-Select-Anweisung ist recht einfach:

```sql
SELECT id,
       spieler_name
FROM player;
```

Diese Anweisung gibt dir den Inhalt von `id` und `name` der Tabelle `player`.

| id | player_name |
|:----|:-------------|
| 1 | Mike |
| 2 | Sarah |
| 3 | John |
| 4 | Lilly |
| 5 | Matthias |
| 6 | Lenny |
| 7 | Sommer |
| 8 | Heiraten |
| 9 | Milana |
| 10 | Lexi |

Wenn du eine solche Anweisung siehst oder schreibst, wählst du vielleicht viel mehr aus, als du brauchst. Von der Verwendung der einfachen Select-Anweisung
kann generell nur abgeraten werden. In 99% dieser Fälle gibt es bessere und verfeinerte Anweisungen.

> **Lies, was du brauchst**

Das ist im Allgemeinen immer eine gute Sache, die man sich merken sollte. Lies nie mehr Daten aus der Datenbank, als du brauchst. Die folgenden
Schlüsselwörter werden dir dabei helfen.

## Column Alias

Mit dem Schlüsselwort `as` können wir Spalten umbenennen, wenn wir sie auswählen

```sql
SELECT id,
       spieler_name as name
FROM player;
```

Gibt zurück:

| id | name |
|:----|:---------|
| 1 | Mike |
| 2 | Sarah |
| 4 | Lilly |
| 5 | Matthias |
| 6 | Lenny |
| 7 | Sommer |
| 8 | Heiraten |
| 9 | Milana |
| 10 | Lexi |
| 3 | John |

Die Spalte "Spielername" heißt jetzt "Name" in unserer Ergebnismenge

## Tabellen-Alias

Auch Tabellen können Aliasnamen haben. Du wirst sie jetzt nicht brauchen, aber in der Regel werden sie direkt hinter dem Tabellennamen definiert.

Sie werden verwendet, um einen eindeutigen Tabellennamen zu definieren, wenn du mehrere Tabellen verwendest. Sie werden auch verwendet, um lange Tabellennamen abzukürzen 
Namen abzukürzen, wenn du den Namen explizit verwenden musst.

```sql
SELECT pl.id,
spieler_name as name
FROM player pl;
```


## WHERE

Die where-Anweisung ist vielleicht das wichtigste Schlüsselwort in Sql. Das where-Schlüsselwort wertet für jede Zeile einen Booleschen Wert aus
und bestimmt, ob eine Zeile zurückgegeben wird oder nicht.

Erinnere dich an die [logischen Operatoren](../02/operators.md#logical) weiter oben in diesem Kapitel.

```sql
SELECT spalte_x,
       spalte_y
FROM meine_tabelle
WHERE spalte_z = wert_a; --(1)
```

1. Die Bedingung kann alles sein. Es können auch mehrere Bedingungen oder Prüfungen sein, die mit `AND` oder `OR` kombiniert werden. Die Spalte
   die du prüfst, muss nicht in der Select-Anweisung selbst enthalten sein.

Versuche, alle Spieler mit einer ID größer als 5 auszuwählen.

Am Ende solltest du eine Tabelle wie diese haben:

| id | player\_name | last\_online |
|:----|:-------------|:---------------------------|
| 6 | Lenny | 2022-03-08 00:00:00.000000 |
| 7 | Sommer | 2022-05-22 00:00:00.000000 |
| 8 | Heiraten | 2022-06-04 00:00:00.000000 |
| 9 | Milana | 2022-02-12 00:00:00.000000 |
| 10 | Lexi | 2022-02-22 00:00:00.000000 |

<Details>
<summary>Lösung</summary>

```sql
SELECT id,
       spieler_name,
       letzte_online
FROM player
WHERE id > 5;
```

</details>

Alles, was wir hier brauchen, ist eine einfache Überprüfung der id-Spalte.

Wir können hier auch spezifischer werden. Fügen wir eine weitere Prüfung für alle Namen hinzu, die mit dem Buchstaben "m" beginnen. (Groß- und
unsensibel)

| id | player\_name | last\_online |
|:----|:-------------|:---------------------------|
| 8 | Marry | 2022-06-04 00:00:00.000000 |
| 9 | Milana | 2022-02-12 00:00:00.000000 |

<Details>
<summary>Lösung</summary>

**MySQL, MariaDB, SqLite**

```sql
SELECT id,
       spieler_name,
       letzte_online
FROM player
WHERE id > 5
  AND player_name LIKE 'm%';
```

Das Schlüsselwort `LIKE` vergleicht unabhängig von der Groß- und Kleinschreibung. Das `%` ist ein Platzhalter für eine unendliche Anzahl anderer Zeichen.

**PostgreSQL**

```sql
SELECT id,
       spieler_name,
       letzte_online
FROM player
WHERE id > 5
  AND player_name ILIKE 'm%';
```

Im Gegensatz zu anderen Datenbanken wird bei Postgres bei `LIKE` zwischen Groß- und Kleinschreibung unterschieden. Deshalb müssen wir hier `ILIKE` verwenden.


</details>

Du kannst weitere Bedingungen aneinanderreihen oder ein `OR` anstelle des `AND` ausprobieren und schauen, wie sich die Ausgabe verändert.

## ORDER BY

Derzeit werden unsere Spieler in der Reihenfolge abgefragt, in der sie in die Datenbank eingefügt wurden. Das muss nicht unbedingt die Reihenfolge sein
die wir wollen.

Nehmen wir an, wir wollen unsere Spieler in der Reihenfolge abrufen, in der sie zuletzt gesehen wurden, d.h. die neuesten Spieler zuerst und die inaktiven Spieler
zuletzt. Das können wir erreichen, indem wir das Schlüsselwort `ORDER BY` für die Spalte `last_online` verwenden.

Die allgemeine Syntax lautet:

```sql
SELECT spalte_x,
       spalte_y
FROM meine_tabelle
ORDER BY spalte_x [DESC|ASC]; --(1)
```

1. Die ASC (aufsteigend) oder DESC (absteigend) Klausel legt die Sortierreihenfolge fest. ASC ist die Standardeinstellung.

Versuche, eine Abfrage zu definieren, um die Spielertabelle nach `last_online` in absteigender Reihenfolge zu sortieren.

Wir wollen diese Tabelle am Ende haben:

| id | player\_name | last\_online |
|:----|:-------------|:---------------------------|
| 8 | Heiraten | 2022-06-04 00:00:00.000000 |
| 7 | Sommer | 2022-05-22 00:00:00.000000 |
| 1 | Mike | 2022-05-11 00:00:00.000000 |
| 3 | John | 2022-04-08 00:00:00.000000 |
| 2 | Sarah | 2022-04-04 00:00:00.000000 |
| 4 | Lilly | 2022-04-01 00:00:00.000000 |
| 6 | Lenny | 2022-03-08 00:00:00.000000 |
| 5 | Matthias | 2022-03-06 00:00:00.000000 |
| 10 | Lexi | 2022-02-22 00:00:00.000000 |
| 9 | Milana | 2022-02-12 00:00:00.000000 |

<Details>
<summary>Lösung</summary>

```sql
SELECT id,
       spieler_name,
       letzte_online
FROM player
ORDER BY last_online DESC;
```

</details>


Die `ORDER BY` Klausel definiert die Spalte, nach der wir sortieren wollen. Das Schlüsselwort `DESC` sagt SQL, dass wir eine absteigende
Reihenfolge. Wenn wir keine Reihenfolge festlegen, werden die Elemente immer in aufsteigender Reihenfolge sortiert.

Wir können auch nach mehreren Spalten sortieren. In diesem Fall werden sie zuerst nach der ersten Spalte sortiert und jeder Wert mit einem
gleichem Wert wird nach dem zweiten Wert sortiert.

Lassen Sie uns als zweites Beispiel nach dem ersten Zeichen des Namens und der ID sortieren.

Um das erste Zeichen eines Strings zu erhalten, müssen wir wieder eine Funktion verwenden.

substr(Spielername, 1, 1)` gibt das erste Zeichen des Spielernamens zurück.

Am Ende wollen wir eine Tabelle wie diese:

| id | player_name | last\_online |
|:----|:-------------|:---------------------------|
| 3 | John | 2022-04-08 00:00:00.000000 |
| 4 | Lilly | 2022-04-01 00:00:00.000000 |
| 6 | Lenny | 2022-03-08 00:00:00.000000 |
| 10 | Lexi | 2022-02-22 00:00:00.000000 |
| 1 | Mike | 2022-05-11 00:00:00.000000 |
| 5 | Matthias | 2022-03-06 00:00:00.000000 |
| 8 | Heiraten | 2022-06-04 00:00:00.000000 |
| 9 | Milana | 2022-02-12 00:00:00.000000 |
| 2 | Sarah | 2022-04-04 00:00:00.000000 |
| 7 | Sommer | 2022-05-22 00:00:00.000000 |

<Details>
<summary>Lösung</summary>

```sql
SELECT id,
       spieler_name,
       letzte_online
FROM player
ORDER BY SUBSTR(spieler_name, 1, 1), id;
```

</details>

Du siehst, dass wir zuerst nach dem ersten Zeichen des Namens sortieren und alle Namen mit demselben ersten Zeichen
nach id.

## LIMIT

Das Schlüsselwort `LIMIT` ist auch sehr wichtig, um übermäßig große Lesevorgänge zu vermeiden.

Die allgemeine Syntax lautet:

```sql
SELECT spalte_x,
       spalte_y
FROM meine_tabelle
LIMIT [n |ALL];
```

Das Schlüsselwort `LIMIT` schränkt die Menge deiner Ergebnisse auf die eingegebene Zahl `n` ein. Alternativ kannst du mit
`ALL` oder `NULL` verwenden, um den Parameter zu deaktivieren. Das ist etwas, was Frameworks manchmal benutzen, um ein Limit zu deaktivieren.

Zuvor haben wir die Spieler bereits nach den letzten Online-Zeiten sortiert. Versuchen wir nun, nur die letzten 5 Spieler zu erhalten, die zuletzt
Spieler zu erhalten, indem wir die Limit-Klausel zu unserer vorherigen Abfrage hinzufügen.

*Tipp: Das Limit ist immer der letzte Parameter deiner Abfrage.

| id | player\_name | last\_online |
|:----|:-------------|:---------------------------|
| 8 | Heiraten | 2022-06-04 00:00:00.000000 |
| 7 | Sommer | 2022-05-22 00:00:00.000000 |
| 1 | Mike | 2022-05-11 00:00:00.000000 |
| 3 | John | 2022-04-08 00:00:00.000000 |
| 2 | Sarah | 2022-04-04 00:00:00.000000 |

<Details>
<summary>Lösung</summary>

```sql
SELECT id,
       spieler_name,
       letzte_online
FROM player
ORDER BY last_online DESC
LIMIT 5;
```

</details>

## OFFSET

Das Schlüsselwort `OFFSET` wird oft verwendet, um eine Art Paginierung der Ergebnisse zu ermöglichen. Deshalb wird es auch oft mit
mit limit kombiniert.

Das Schlüsselwort `OFFSET` überspringt die ersten `n` Zeilen der Ergebnismengen.

Die allgemeine Syntax lautet:

```sql
SELECT spalte_x,
       spalte_y
FROM meine_tabelle
OFFSET n;
```

Versuchen wir, unsere Abfrage aus dem Limit-Teil noch zu erweitern. Wir haben bereits die ersten 5 Einträge erhalten. Jetzt wollen wir die 
die nächsten 5 Einträge erhalten, indem wir einen `OFFSET` von `5` hinzufügen und die Schlüsselwörter `LIMIT` und `ORDER BY` beibehalten.

*Hinweis: Das Schlüsselwort `OFFSET` steht direkt vor dem Schlüsselwort `LIMIT`.

| id | player\_name | last\_online |
|:----|:-------------|:---------------------------|
| 4 | Lilly | 2022-04-01 00:00:00.000000 |
| 6 | Lenny | 2022-03-08 00:00:00.000000 |
| 5 | Matthias | 2022-03-06 00:00:00.000000 |
| 10 | Lexi | 2022-02-22 00:00:00.000000 |
| 9 | Milana | 2022-02-12 00:00:00.000000 |

<Details>
<summary>Lösung</summary>

```sql
SELECT id,
       spieler_name,
       letzte_online
FROM player
ORDER BY last_online DESC
OFFSET 5
LIMIT 5;
```

</details>

Was hier im Grunde genommen passiert, ist Folgendes:

Zuerst ordnen wir die gesamte Tabelle nach der Spalte `last_online`. Danach überspringen wir die ersten 5 Zeilen und lesen die nächsten 5 Zeilen. 
5` Zeilen. Es ist wichtig, daran zu denken, dass wir zuerst die gesamte Tabelle sortieren müssen. Das kann sehr kostspielig sein 
bei großen Tabellen sein. Zum Glück gibt es Möglichkeiten, das schneller zu machen. Wir werden das später behandeln.

## Resümee

Wir haben jetzt die vier wichtigsten Schlüsselwörter zum Suchen, Sortieren und Eingrenzen der abgerufenen Daten gelernt. Die 
Schlüsselwörter müssen in einer bestimmten Reihenfolge verwendet werden und können nicht frei verändert werden.

Die allgemeine Syntax dafür ist:

```sql
SELECT spalte_x,
       spalte_y
FROM meine_tabelle
WHERE Bedingung
ORDER BY spalte_z DESC
OFFSET n
LIMIT m;
```
