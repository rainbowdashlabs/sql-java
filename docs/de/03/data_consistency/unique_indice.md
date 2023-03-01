# Eindeutige Indizes

Jetzt haben wir einen weiteren Fehler in unserer Tabelle.
Wir haben unsere ID, die wir jedem Spieler zuweisen.
Diese Kennung sollte eindeutig sein, aber das ist sie nicht, wie wir bereits festgestellt haben.
Wir können derzeit mehrere Spieler mit derselben ID haben.

An dieser Stelle kommen eindeutige Indizes ins Spiel.
Sie stellen sicher, dass alle Werte in einer Spalte oder eine Kombination aus mehreren Werten eindeutig sind.
Fügen wir zunächst einen eindeutigen Index hinzu, um sicherzustellen, dass unsere IDs eindeutig sind.

```sql
CREATE UNIQUE INDEX player_id_uindex
    ON player (id);
```

Der Name ist eigentlich egal, aber wenn du dich an unsere Namenskonventionen vom Anfang erinnerst, wirst du dich wahrscheinlich an das Namensschema erinnern, das wir hier verwenden.
Es lautet `<Tabelle>_<Spalte>_uindex`.

Führe diese Abfrage nun zweimal aus:

```sql
INSERT INTO player(id, player_name)
VALUES (11, 'Jonathan');
```

Beim ersten Mal wird es funktionieren, aber beim zweiten Mal nicht mehr, weil wir versuchen, die ID ein zweites Mal einzufügen.

Natürlich können wir auch weitere Spalten zu unserem Index hinzufügen, die eine Kombination einzigartig machen.
Ein Beispiel wäre, dass ein Spieler einen Kanal abonnieren kann.
Aber nur ein einziges Mal.
In diesem Fall können der Kanal und der Spieler mehrfach in ihrer Spalte vorkommen, aber die Kombination aus Spieler und Kanal muss eindeutig sein.

```sql
CREATE TABLE channel_subscription
(
    player_id INTEGER NOT NULL,
    channel_id INTEGER NOT NULL
);

CREATE UNIQUE INDEX channel_subscription_player_id_channel_id_uindex
    ON channel_subscription (player_id, channel_id);
```

Lass uns auch das testen, um zu sehen, wie es funktioniert.

```sql
INSERT INTO channel_subscription
VALUES (1, 1);
INSERT INTO channel_subscription
VALUES (1, 2);
INSERT INTO channel_subscription
VALUES (2, 1);
```

Alle diese Eingaben funktionieren.
Spieler 1 hat jetzt die Kanäle 1 und 2 abonniert, während Spieler 2 den Kanal 1 abonniert hat.
Wenn einer dieser Spieler nun versucht, erneut einen Kanal zu abonnieren, wird dies fehlschlagen.

```sql
INSERT INTO channel_subscription
VALUES (1, 1);
```
