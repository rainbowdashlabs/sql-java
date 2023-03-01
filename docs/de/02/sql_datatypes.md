# SQL Datentypen

SQL hat seine eigenen Datentypen.
Einige davon sind mit den Java-Typen identisch, andere nicht.
Um uns noch mehr zu verwirren, unterstützen MySQL, MariaDB, SQLite und PostgreSQL unterschiedliche Datentypen.
Einige von ihnen verhalten sich unterschiedlich, obwohl sie den gleichen Namen haben.

Ich werde hier eine kurze Liste mit den meisten korrekten Zuordnungen von Java-Datentypen zu ihrem SQL-Gegenstück aufführen.

Ich werde jedoch nicht alle Typen abdecken, da einige Datenbanken wie PostgreSQL sehr spezifische Datentypen haben, die nur in sehr speziellen Fällen verwendet werden.
Ich werde nur die Datentypen behandeln, die für dich in den meisten Fällen wichtig sein könnten.

Schau dir die Dokumentation an, wenn du tiefer eintauchen willst.

[MySQL](https://dev.mysql.com/doc/refman/8.0/en/data-types.html) | [SQLite](https://www.sqlite.org/datatype3.html) | [MariaDB](https://mariadb.com/kb/en/data-types/) | [PostgreSQL](https://www.postgresql.org/docs/9.5/datatype.html)

## Cheatsheet für die Schnellreferenz

| **Java Typ**  | **Bereich/Länge**                | **MySQL / MariaDB** | **SQLite**            | **PostgreSQL**    |
|:--------------|:---------------------------------|:--------------------|:----------------------|:------------------|
| String        | Fest < 255 mit Auffüllung        | CHAR                |                       | CHAR(Bis zu 1 GB) |
| String        | < 255                            | TINYTEXT, VARCHAR   | TEXT                  | TEXT, VARCHAR     |
| String        | < 65.353                         | TEXT, VARCHAR       | TEXT                  | TEXT, VARCHAR     |
| String        | < 16.777.215                     | MEDIUMTEXT          | TEXT                  | TEXT, VARCHAR     |
| String        | < 4.294.967.295                  | LONGTEXT            | TEXT                  | TEXT, VARCHAR     |
| String        | unbegrenzt                       |                     | TEXT                  | TEXT, VARCHAR     |
| Integer/Short | -128 und 127                     | TINYINT             | INTEGER               | SMALLINT          |
| Integer/Short | -32.768 und 32.767               | SMALLINT            | INTEGER               | SMALLINT          |
| Integer       | -8.288.608 und 8.388.607         | MEDIUMINT           | INTEGER               | INTEGER           | 
| Integer       | -2.147.483.648 und 2.147.483.647 | INT(INTEGER)        | INTEGER               | INTEGER           | 
| Long          |                                  | BIGINT              | INTEGER (max. 64 Bit) | BIGINT            |
| Double        | exact fixed point                | DECIMAL             | REAL                  | DECIMAL(NUMERIC)  |
| Double        | doppelte Genauigkeit             | DOUBLE              | REAL                  | DOUBLE            |
| Float         | einfache Genauigkeit             | FLOAT               | REAL                  |                   |
| Boolean       |                                  | BOOLEAN             | BOOLEAN(INTEGER)      | BOOLEAN           |
| Bytes         | Fixed < 255 with padding         | BINARY              |                       |                   |
| Bytes         | < 255                            | TINYBLOB, VARBINARY | BLOB                  | BYTEA             |
| Bytes         | < 65.353                         | BLOB, VARBINARY     | BLOB                  | BYTEA             |
| Bytes         | < 16.777.215                     | MEDIUMBLOB          | BLOB                  | BYTEA             |
| Bytes         | < 4.294.967.295                  | LONGBLOB            | BLOB                  | BYTEA             |
| LocalDate     |                                  | DATE                | TEXT, REAL, INTEGER   | DATE              |
| LocalTime     |                                  | TIME                | TEXT, REAL, INTEGER   | TIME              |
| LocalDateTime |                                  | TIMESTAMP           | TEXT, REAL, INTEGER   | TIMESTAMPTZ       |
| Timestamp     |                                  | TIMESTAMP           | TEXT, REAL, INTEGER   | TIMESTAMP         |

## Null

NULL in sql ist dasselbe wie in java in all unseren Datenbanken.
Allerdings gibt es in Sql keine NullPointerException.
Jedes Mal, wenn du etwas mit NULL machst, versucht Sql, etwas damit zu tun.
Deshalb kann die Durchführung logischer Operationen mit NULL-Werten schwierig und fehleranfällig sein, wie du auf der vorherigen Seite gesehen hast.

Ich werde dir auf den nächsten Seiten die verschiedenen Datentypen für jede Datenbank zeigen.
Du kannst die Datenbanken auslassen, die dich nicht interessieren.
