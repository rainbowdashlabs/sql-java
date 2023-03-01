# Tabellen

Jetzt, wo wir unsere Datenbank eingerichtet und etwas über die Namensgebung und die Datentypen gelernt haben, können wir endlich unsere erste Tabelle erstellen.

Alles in unseren Datenbanken wird in Tabellen gespeichert.

Das Erstellen und Löschen von Tabellen ist eine sehr wichtige Fähigkeit. Natürlich gibt es noch viel mehr, aber wir gehen es erst einmal langsam an.

# Eine Tabelle erstellen

Das Erstellen einer Tabelle ist in allen Datenbanken gleich. Beginnen wir damit, die beiden Tabellen, die wir zuvor benutzt haben, neu zu erstellen

**player**

In der Spielertabelle wird jeder Spieler mit einem Namen und einer ID gespeichert. Außerdem speichern wir, wann der Spieler zuletzt online war.

| id | player\_name | last\_online |
|:----|:-------------|:-----------------|
| 1 | Mike | 2022-05-11 00:00 |
| 2 | Sarah | 2022-04-04 00:00 |
| 3 | john | 2022-04-08 00:00 |
| 4 | Lilly | 2022-04-01 00:00 |
| 5 | Matthias | 2022-03-06 00:00 |
| 6 | Lenny | 2022-03-08 00:00 |
| 7 | Summer | 2022-05-22 00:00 |
| 8 | Marry | 2022-06-04 00:00 |
| 9 | Milana | 2022-02-12 00:00 |
| 10 | Lexi | 2022-02-22 00:00 |

**friend_graph**

Der Freundschaftsgraph ist ein bidirektionaler Graph von Freundschaften. Im Allgemeinen gehen wir davon aus, dass wenn `Spieler_1` ein Freund von
Spieler_2` befreundet ist, dass Spieler_2` auch ein Freund von Spieler_1` ist.

Wir speichern hier nur die IDs der Spieler. Die anderen Informationen wie z. B. die Namen stehen in der Tabelle "Spieler", und wir wollen keine
doppelte Daten speichern.

| player_1 | player_2 |
|----------|----------|
| 1 | 2 |
| 2 | 3 |
| 4 | 3 |

Bitte versuche zunächst, die Anweisungen selbst zu erstellen, indem du das Gelernte auf den Datentypseiten anwendest. Du kannst dich auf das
das [datatype cheatsheet](../02/sql_datatypes.md). Wir kümmern uns im Moment nicht um den Inhalt.

Du kannst natürlich auch einen Desktop-Client deiner Wahl verwenden, um die Tabellen zu erstellen, aber ich empfehle dir, auch die 
Aber ich empfehle dir, auch die SQL-Syntax zu lernen, denn das macht die Fehlersuche später sehr viel einfacher.

Die allgemeine Syntax lautet:

<!-- @formatter:off -->
```sql
CREATE TABLE table_name
(
    col_name TYPE,
    spalte_name TYP
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
    player_1 INTEGER,
    player_2 INTEGER
);
```

</details>

Du kannst das Schlüsselwort `IF NOT EXISTS` verwenden, wenn du Konflikte vermeiden willst. Damit wird die Tabelle nur erstellt, wenn der Name 
nicht bereits in Gebrauch ist.

<!-- @formatter:off -->

```sql
CREATE TABLE IF NOT EXISTS table_name
(
    col_name TYPE,
    spalten_name TYP
);
```
<!-- @formatter:on --> 

# Löschen von Tabellen

Manchmal brauchst du eine Tabelle vielleicht nicht mehr. In diesem Fall wollen wir sie löschen.

```sql
DROP TABLE player;
```

Wir können hier das Schlüsselwort `IF EXISTS` verwenden, um Fehler zu vermeiden, wenn die Tabelle nicht mehr existiert.

```sql
DROP TABLE IF EXISTS player;
```
