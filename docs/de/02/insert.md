# INSERT

Jetzt, wo wir unsere Tabellen erstellt haben, wollen wir ihnen Daten hinzufügen.
Beginnen wir mit unseren Spielern

| id  | player\_name | last\_online     |
|:----|:-------------|:-----------------|
| 1   | Mike         | 2022-05-11 00:00 |
| 2   | Sarah        | 2022-04-04 00:00 |
| 3   | john         | 2022-04-08 00:00 |
| 4   | Lilly        | 2022-04-01 00:00 |
| 5   | Matthias     | 2022-03-06 00:00 |
| 6   | Lenny        | 2022-03-08 00:00 |
| 7   | Summer       | 2022-05-22 00:00 |
| 8   | Marry        | 2022-06-04 00:00 |
| 9   | Milana       | 2022-02-12 00:00 |
| 10  | Lexi         | 2022-02-22 00:00 |

Um Daten einzufügen, müssen wir die Anweisung `INSERT` verwenden.
Die allgemeine Syntax zum Einfügen einer Zeile in eine Tabelle lautet:

```sql
INSERT INTO table_name(column_1, column_2)
VALUES (value_1, value_2)
```

Du kannst außerdem mehrere Zeilen gleichzeitig einfügen:

```sql
INSERT INTO table_name(column_1, column_2)
VALUES (value_1, value_2),
       (value_1, value_2),
       (value_1, value_2);
```

Für die nächste Aufgabe musst wissen, wie du einen String in einen Zeitstempel umwandeln kannst.
Das hängt von der Datenbank ab, die du verwendest:

- Postgres: `'2022-05-11 00:00'::TIMESTAMP`\
  Wir können einen String einfach in einen Zeitstempel umwandeln
- MariaDB/MySQL: `timestamp('2022-05-11 00:00')`\
  Wir benutzen die Timestamp-Funktion, um unseren String zu parsen
- SqLite: `CAST(STRFTIME('%s', '2022-05-11 00:00') AS INTEGER)`\
  Wir speichern die Zeitstempel als Epochensekunden, da Sqlite nicht wirklich einen Zeitstempeltyp hat

Versuche nun, die Tabelle von oben mit den zuvor genannten Methoden und Anweisungen neu zu erstellen.
Normalerweise kann die Zeit weggelassen werden, wenn sie auf 00:00 gesetzt ist.

<details>
<summary>Lösung</summary>

**MariaDB/MySQL**
```sql
INSERT INTO player(id, player_name, last_online)
VALUES (1, 'Mike', TIMESTAMP('2022-05-11 00:00')),
       (2, 'Sarah', TIMESTAMP('2022-04-04 00:00')),
       (3, 'john', TIMESTAMP('2022-04-08 00:00')),
       (4, 'Lilly', TIMESTAMP('2022-04-02 00:00')),
       (5, 'Matthias', TIMESTAMP('2022-03-06 00:00')),
       (6, 'Lenny', TIMESTAMP('2022-03-08 00:00')),
       (7, 'Summer', TIMESTAMP('2022-05-22 00:00')),
       (8, 'Marry', TIMESTAMP('2022-06-04 00:00')),
       (9, 'Milana', TIMESTAMP('2022-02-12 00:00')),
       (10, 'Lexi', TIMESTAMP('2022-02-22 00:00'));
```

**PostgreSQL**
```sql
INSERT INTO player(id, player_name, last_online)
VALUES (1, 'Mike', '2022-05-11 00:00'::TIMESTAMP),
       (2, 'Sarah', '2022-04-04 00:00'::TIMESTAMP),
       (3, 'john', '2022-04-08 00:00'::TIMESTAMP),
       (4, 'Lilly', '2022-04-01 00:00'::TIMESTAMP),
       (5, 'Matthias', '2022-03-06 00:00'::TIMESTAMP),
       (6, 'Lenny', '2022-03-08 00:00'::TIMESTAMP),
       (7, 'Summer', '2022-05-22 00:00'::TIMESTAMP),
       (8, 'Marry', '2022-06-04 00:00'::TIMESTAMP),
       (9, 'Milana', '2022-02-12 00:00'::TIMESTAMP),
       (10, 'Lexi', '2022-02-22 00:00'::TIMESTAMP);
```

**SqLite**
```sql
INSERT INTO player(id, player_name, last_online)
VALUES (1, 'Mike', CAST(STRFTIME('%s', '2022-05-11 00:00') AS INTEGER)),
       (2, 'Sarah', CAST(STRFTIME('%s', '2022-04-04 00:00') AS INTEGER)),
       (3, 'John', CAST(STRFTIME('%s', '2022-04-08 00:00') AS INTEGER)),
       (4, 'Lilly', CAST(STRFTIME('%s', '2022-04-02 00:00') AS INTEGER)),
       (5, 'Matthias', CAST(STRFTIME('%s', '2022-03-06 00:00') AS INTEGER)),
       (6, 'Lenny', CAST(STRFTIME('%s', '2022-03-08 00:00') AS INTEGER)),
       (7, 'Summer', CAST(STRFTIME('%s', '2022-05-22 00:00') AS INTEGER)),
       (8, 'Marry', CAST(STRFTIME('%s', '2022-06-04 00:00') AS INTEGER)),
       (9, 'Milana', CAST(STRFTIME('%s', '2022-02-12 00:00') AS INTEGER)),
       (10, 'Lexi', CAST(STRFTIME('%s', '2022-02-22 00:00') AS INTEGER));

```
  
</details>

Lass uns das Gleiche mit dem `friend_graph` machen.
Versuche, die folgenden Werte in die Tabelle `friend_graph` einzufügen.

| player\_1 | player\_2 |
|:----------|:----------|
| 1         | 2         |
| 2         | 3         |
| 4         | 3         |
| 5         | 3         |
| 7         | 2         |
| 6         | 1         |
| 6         | 2         |
| 1         | 10        |
| 4         | 10        |

<details>
<summary>Lösung</summary>

```sql
INSERT INTO friend_graph(player_1, player_2)
VALUES (1, 2),
       (2, 3),
       (4, 3),
       (5, 3),
       (7, 2),
       (6, 1),
       (6, 2),
       (1, 10),
       (4, 10);
```

</details>

## Tabellen mit Inhalt erstellen

Du kannst auch eine alternative Syntax verwenden, um direkt eine Tabelle mit Inhalt zu erstellen.

Nehmen wir an, wir wollen eine Tabelle mit dem Geld der Spieler erstellen.
Die Tabelle soll zunächst die ID und einen festen Geldbetrag für jeden Spieler enthalten.

```sql
CREATE TABLE money AS 
    SELECT id, 1000.0 AS money
    FROM player

```

Dadurch wird eine Tabelle wie diese erstellt:

| id  | money |
|:----|:------|
| 1   | 1000  |
| 2   | 1000  |
| 3   | 1000  |
| 4   | 1000  |
| 5   | 1000  |
| 6   | 1000  |
| 7   | 1000  |
| 8   | 1000  |
| 9   | 1000  |
| 10  | 1000  |

Bei dieser Methode musst du einige Dinge beachten.
Die Datenbank entscheidet über den Datentyp der Spalte.
Wenn wir `1000.0` verwenden, erhalten wir einen numerischen Typ.
Wenn wir `1000` verwenden, erhalten wir einen Integer-Typ.
Es ist auch wichtig, einen Alias für neu erstellte Spalten zu verwenden, die nur einen Typ haben.
Verwende dazu das Schlüsselwort "as".
Wenn du keinen Alias definierst, hat die Spalte einen Standardnamen, der normalerweise nicht das ist, was du willst. 


## Konflikte
Du hast vielleicht schon bemerkt, dass wir die Spieler so oft einfügen können, wie wir wollen, und dass unsere IDs dann nicht mehr eindeutig sind.
Damit werden wir uns später beschäftigen.
