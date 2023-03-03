# Auto increment

Du erinnerst dich daran, dass wir die `id` in unserer `player` Tabelle immer selbst festgelegt haben?
Das fühlt sich nicht richtig an, oder?
Wie sollen wir uns merken, welche `id` wir bereits verwendet haben, vor allem, da wir jetzt einen eindeutigen Index für unsere `id` haben.
Ändern wir dies also noch einmal.
Diesmal fügen wir wieder einen Standardwert hinzu, aber auf eine andere Art und Weise, als wir es gewohnt sind.

Dieser Standardwert heißt auto increment und wird jedes Mal hochgezählt, wenn wir ihn verwenden.
So ist es unmöglich, eine ID zweimal zu verwenden und wir müssen nicht mehr daran denken, welche ID wir zuletzt verwendet haben.

Leider ist das je nach verwendeter Datenbank ein wenig anders.

*Egal, ob du es brauchst oder nicht, die auto increment Spalte sollte in 99,9% der Fälle als `Primary Key` der Tabelle festgelegt werden.
Es gibt einfach keinen Grund, dies nicht zu tun.
Einige Datenbanken verlangen sogar, dass sie ein Schlüssel oder sogar der Primärschlüssel ist.

## Postgres

In Postgres können wir SMALLSERIAL, SERIAL und BIGSERIAL nutzen.
Diese unterscheiden sich durch den Typ und die Größe, die sie zurückgeben können.

| Name        | Typ      | Bereich                         |
|-------------|----------|---------------------------------|
| SMALLSERIAL | SMALLINT | 1 bis 32.767                    |
| SERIAL      | INTEGER  | 1 bis 2.147.483.647             |
| BIGSERIAL   | BIGINT   | 1 bis 9.223.372.036.854.775.807 |

Alles, was wir tun müssen, um sie zu verwenden, ist, unseren Datentyp `INTEGER` mit dem Datentyp `SERIAL` in unserer Tabelle auszutauschen.
Beachte, dass dieser Wert immer noch manuell gesetzt werden kann.
Da er auch auf `NULL` gesetzt werden kann, markieren wir ihn zusätzlich als `NOT NULL`.

*Vergiss nicht, deine Tabelle zuerst zu löschen*

```Postgresql
CREATE TABLE player (
	id          SERIAL    NOT NULL,
	player_name TEXT      NOT NULL,
	last_online TIMESTAMP NOT NULL DEFAULT now()
);
```

In Postgres muss das Autoinkrement kein Schlüssel sein.
Es wird jedoch dringend empfohlen, die auto increment id als `Primary Key` zu verwenden, da dies fast immer die richtige Wahl ist, da dein Wert ohnehin eindeutig ist und nicht `NULL` sein sollte.

```postgresql
CREATE TABLE player (
	id          SERIAL PRIMARY KEY,
	player_name TEXT      NOT NULL,
	last_online TIMESTAMP NOT NULL DEFAULT now()
);
```

## SqLite

Um hier ein Autoinkrement zu erstellen, müssen wir den `Primary Key` verwenden, den wir im letzten Abschnitt gelernt haben.
Im Grunde genommen erstellen wir hier einen `Primary Key`, dessen Werte von einer Auto Increment-Sequenz geliefert werden.
Klingt kompliziert?
Denk nicht darüber nach.
Du musst nur die Spalte als `Primary KEY` markieren und `AUTOINCREMENT` anfügen.

```sqlite
CREATE TABLE player (
	id          INTEGER PRIMARY KEY AUTOINCREMENT,
	player_name TEXT      NOT NULL,
	last_online TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP
);
```

## MariaDB und MySQL

MariaDB und MySQL verwenden zum Glück die gleiche Syntax.
Bei einigen Engines musst du die Auto-Inkrement-Spalte als erste Spalte setzen, also bleiben wir auch hier dabei.
Außerdem muss deine Autoinkrement-Spalte Teil eines Schlüssels sein, also werden wir sie als Primärschlüssel festlegen, was ohnehin empfohlen wird.
Falls du dir das obige Beispiel von SqLite angesehen hast, wirst du feststellen, dass der einzige Unterschied der Unterstrich in "AUTO_INCREMENT" ist.

```mariadb
CREATE TABLE player (
	id          INTEGER PRIMARY KEY AUTO_INCREMENT,
	player_name TEXT      NOT NULL,
	last_online TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

## Testen

Jetzt, wo wir unser Auto Increment eingerichtet haben, ist es an der Zeit zu testen, wie es funktioniert.

Jetzt werden zwei unserer drei Spalten von der Datenbank ausgefüllt, was großartig ist.
Um einen neuen Spieler hinzuzufügen, müssen wir nur noch seinen Namen in die Tabelle einfügen.

```sql
INSERT INTO player(player_name)
VALUES ('Mike'),
       ('Sarah'),
       ('John'),
       ('Lilly'),
       ('Matthias'),
       ('Lenny'),
       ('Summer'),
       ('Marry'),
       ('Milana'),
       ('Lexi');
```

Schauen wir uns an, was wir bekommen haben:

```sql
SELECT id, player_name, last_online
FROM player;
```

| id  | player\_name | last\_online               |
|:----|:-------------|:---------------------------|
| 1   | Mike         | 2022-11-26 12:32:39.021491 |
| 2   | Sarah        | 2022-11-26 12:32:39.021491 |
| 3   | John         | 2022-11-26 12:32:39.021491 |
| 4   | Lilly        | 2022-11-26 12:32:39.021491 |
| 5   | Matthias     | 2022-11-26 12:32:39.021491 |
| 6   | Lenny        | 2022-11-26 12:32:39.021491 |
| 7   | Summer       | 2022-11-26 12:32:39.021491 |
| 8   | Marry        | 2022-11-26 12:32:39.021491 |
| 9   | Milana       | 2022-11-26 12:32:39.021491 |
| 10  | Lexi         | 2022-11-26 12:32:39.021491 |

Sieht gut aus! Auto increment ids beginnen immer mit 1.
Sie machen standardmäßig 1-Schritte, können aber geändert werden, um größere Schritte zu machen.
Ich werde hier nicht näher darauf eingehen, aber ich bin sicher, dass Google ein paar tolle Beispiele für dich hat.

Und das war's auch schon.
Wir haben die automatischen Schritte in unserer Tabelle gemeistert und müssen uns keine Gedanken mehr über die eindeutigen IDs machen, die wir für unsere Nutzer brauchen.
