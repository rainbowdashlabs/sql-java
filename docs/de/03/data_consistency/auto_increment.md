# Auto increment

Du erinnerst dich, dass wir die Spieler-ID immer selbst festgelegt haben? Das fühlt sich nicht richtig an, oder? Wie sollen wir uns merken
welche ID wir bereits verwendet haben, zumal wir jetzt einen eindeutigen Index für unsere Spieler-ID haben. Ändern wir das also noch einmal.
Diesmal fügen wir wieder einen Standardwert hinzu, aber auf eine andere Art und Weise, als wir es gewohnt sind.

Dieser Standardwert heißt auto increment und wird jedes Mal hochgezählt, wenn wir ihn verwenden. Das macht es unmöglich
eine ID zweimal zu verwenden und wir müssen nicht mehr daran denken, welche ID wir zuletzt verwendet haben.

Leider ist das je nach verwendeter Datenbank ein wenig anders.

*Unabhängig davon, ob du sie brauchst oder nicht, sollte die Auto Increment-Spalte in 99,9 % der Fälle als Primärschlüssel der Tabelle festgelegt werden.
Fällen. Es gibt einfach keinen Grund, dies nicht zu tun. Manche Datenbanken verlangen sogar, dass sie ein Schlüssel oder sogar der Primärschlüssel ist.
Schlüssel sein.

## Postgres

In Postgres können wir von SMALLSERIAL, SERIAL und BIGSERIAL profitieren. Diese unterscheiden sich durch den Typ und die Größe, die sie zurückgeben können.

| Name        | Typ      | Bereich                         |
|-------------|----------|---------------------------------|
| SMALLSERIAL | SMALLINT | 1 bis 32.767                    |
| SERIAL      | INTEGER  | 1 bis 2.147.483.647             |
| BIGSERIAL   | BIGINT   | 1 bis 9.223.372.036.854.775.807 |

Alles, was wir tun müssen, um sie zu verwenden, ist, unseren Datentyp `INTEGER` mit dem Datentyp `SERIAL` in unserer Tabelle zu vertauschen. Beachte, dass
dieser Wert immer noch manuell gesetzt werden kann. Da er auch auf Null gesetzt werden kann, werden wir ihn zusätzlich als NOT NULL markieren.

*Vergiss nicht, deine Tabelle zuerst zu löschen*

```Postgresql
CREATE TABLE player
(
    id SERIAL NOT NULL,
    player_name TEXT NOT NULL,
    last_online TIMESTAMP NOT NULL DEFAULT NOW()
);
```

In Postgres muss das auto increment kein Schlüssel sein. Es wird jedoch dringend empfohlen, die auto increment
id als Primärschlüssel zu verwenden, denn das ist fast immer die richtige Wahl, da der Wert ohnehin eindeutig ist und
nicht null sein sollte.

```postgresql
CREATE TABLE player
(
    id SERIAL PRIMARY KEY,
    player_name TEXT NOT NULL,
    last_online TIMESTAMP NOT NULL DEFAULT NOW()
);
```

## SqLite

Um hier ein Autoinkrement zu erstellen, müssen wir den Primärschlüssel verwenden, den wir im
letzten Abschnitt gelernt haben. Im Grunde genommen erstellen wir hier einen Primärschlüssel, dessen Werte von einer auto increment
Sequenz. Klingt kompliziert? Denk nicht darüber nach. Alles, was du tun musst, ist, die Spalte als Primärschlüssel zu markieren und
`AUTOINCREMENT` anfügen.

```Sqlite
CREATE TABLE player
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    player_name TEXT NOT NULL,
    last_online TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP
);
```

## MariaDB und MySQL

MariaDB und MySQL verwenden zum Glück die gleiche Syntax. Bei einigen Engines musst du die auto increment Spalte als erste Spalte setzen.
erste Spalte zu setzen, also werden wir das auch beibehalten. Außerdem muss deine Autoinkrement-Spalte Teil eines Schlüssels sein.
eines Schlüssels sein, also werden wir sie als Primärschlüssel festlegen, was ohnehin empfohlen wird. Falls du einen Blick auf das
Beispiel von SqLite angesehen hast, wirst du feststellen, dass der einzige Unterschied der Unterstrich in `AUTO_INCREMENT` ist.

```mariadb
CREATE TABLE player
(
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    player_name TEXT NOT NULL,
    last_online TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

## Testen

Jetzt, wo wir unser Auto Increment eingerichtet haben, ist es an der Zeit zu testen, wie es funktioniert.

Jetzt werden zwei unserer drei Spalten von der Datenbank ausgefüllt, was großartig ist. Um einen neuen Spieler hinzuzufügen, müssen wir nur noch
um einen neuen Spieler hinzuzufügen, müssen wir seinen Namen in die Tabelle eingeben.

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

Schauen wir uns an, was wir am Ende erhalten haben:

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
| 8   | Marry     | 2022-11-26 12:32:39.021491 |
| 9   | Milana       | 2022-11-26 12:32:39.021491 |
| 10  | Lexi         | 2022-11-26 12:32:39.021491 |

Sieht gut aus! Auto increment ids beginnen immer mit 1. Sie machen standardmäßig 1-Schritte, können aber geändert werden, um größere Schritte zu machen.
Schritte zu machen. Ich werde hier nicht näher darauf eingehen, aber ich bin sicher, dass Google ein paar tolle Beispiele für dich hat.

Und das war's auch schon. Wir haben die automatischen Schritte in unserer Tabelle gemeistert und müssen nicht mehr über die eindeutigen IDs nachdenken, die wir
die wir für unsere Nutzer brauchen.
