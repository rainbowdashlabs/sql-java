# Tabellen

Jetzt, wo wir unsere Datenbank eingerichtet und etwas über die Namensgebung und die Datentypen gelernt haben, können wir endlich unsere erste Tabelle erstellen.
Das Meiste in unseren Datenbanken wird in Tabellen gespeichert.
Das Erstellen und Löschen von Tabellen ist eine sehr wichtige Fähigkeit. Natürlich gibt es noch viel mehr, aber wir gehen es erst einmal langsam an.

# Eine Tabelle erstellen

Das Erstellen einer Tabelle ist in allen Datenbanken gleich.
Beginnen wir damit, die beiden Tabellen neu zu erstellen, die wir zuvor benutzt haben

**player**

In der `player` Tabelle wird jeder Spieler mit einem Namen und einer ID gespeichert.
Außerdem speichern wir, wann der Spieler zuletzt online war.

| id  | player\_name | last\_online     |
|:----|:-------------|:-----------------|
| 1   | Mike         | 2022-05-11 00:00 |
| 2   | Sarah        | 2022-04-04 00:00 |
| 3   | john         | 2022-04-08 00:00 |
| 4   | Lilly        | 2022-04-01 00:00 |
| 5   | Matthias     | 2022-03-06 00:00 |
| 6   | Lenny        | 2022-03-08 00:00 |
| 7   | Sommer       | 2022-05-22 00:00 |
| 8   | Heiraten     | 2022-06-04 00:00 |
| 9   | Milana       | 2022-02-12 00:00 |
| 10  | Lexi         | 2022-02-22 00:00 |

**friend_graph**

Der `friend_graph` ist ein bidirektionaler Graph von Freundschaften.
Im Allgemeinen gehen wir davon aus, dass, wenn `player_1` mit `player_2` befreundet ist, `player_2` auch mit `player_1` befreundet ist.

Wir speichern hier nur die IDs der Spieler.
Die anderen Informationen wie die Namen befinden sich in der Tabelle `player`, und wir wollen keine doppelten Daten speichern.

| player_1 | player_2 |
|----------|----------|
| 1        | 2        |
| 2        | 3        |
| 4        | 3        |

Bitte versuche zuerst, die Anweisungen selbst zu erstellen, indem du das Gelernte aus den Datentypseiten anwendest.
Du kannst dich auf das [datatype cheatsheet](datatypes/index.md) beziehen.
Der Inhalt ist uns im Moment noch egal.

Du kannst natürlich auch einen Desktop-Client deiner Wahl verwenden, um die Tabellen zu erstellen, aber ich empfehle dir, auch die SQL-Syntax zu lernen, denn das macht die Fehlersuche später sehr viel einfacher.

Die allgemeine Syntax lautet:

<!-- @formatter:off -->
```sql
CREATE TABLE tabellen_name
(
    spalten_name TYPE,
    spalten_name TYPE
);
```
<!-- @formatter:on --> 

<Details>
<summary>Lösung</summary>

Um diese Tabellen zu erstellen, verwende diese Anweisungen:

```sql
CREATE TABLE player
(
    id INTEGER,
    player_name TEXT,
    last_online TIMESTAMP
);

CREATE TABLE friend_graph
(
    spieler_1 INTEGER,
    spieler_2 INTEGER
);
```

</details>

Du kannst das Schlüsselwort `IF NOT EXISTS` verwenden, wenn du Konflikte vermeiden willst.
Damit wird die Tabelle nur erstellt, wenn der Name nicht bereits verwendet wird.

<!-- @formatter:off -->

```sql
CREATE TABLE IF NOT EXISTS tabellen_name
(
    spalten_name TYPE,
    spalten_name TYPE
);
```
<!-- @formatter:on --> 

# Löschen von Tabellen

Manchmal brauchst du eine Tabelle vielleicht nicht mehr.
In diesem Fall wollen wir sie löschen.

```sql
DROP TABLE player;
```

Wir können hier das Schlüsselwort `IF EXISTS` verwenden, um Fehler zu vermeiden, wenn die Tabelle nicht mehr existiert.

```sql
DROP TABLE IF EXISTS player;
```
