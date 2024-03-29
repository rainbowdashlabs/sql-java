# Datenbanken

## Die gängigen Datenbanken

Es gibt eine Menge verschiedener Datenbanken. 
Sie funktionieren auf viele Arten und werden für unterschiedliche Anwendungsfälle genutzt.
Es gibt keine ultimative Datenbank, die mit jeder Art von Daten umgehen kann, obwohl einige sehr gut mit vielen Datentypen umgehen können.

Wenn wir über Datentypen sprechen, unterscheiden wir normalerweise zwischen strukturierten und unstrukturierten Daten.

Einige Datenbanken sind auf die Suche in unstrukturierten Daten spezialisiert, wie z. B. [Solr](https://solr.apache.org/) oder [ElasticSearch](https://www.elastic.co/elastic-stack/), die am häufigsten für die Suche und Aggregation von Logs verwendet werden.
Eine weitere gängige Datenbank für unstrukturierte Daten ist [MongoDB](https://www.mongodb.com/).
Wir nennen diese Datenbanken dokumentenbasierte Datenbanken, da wir Daten in Dokumenten speichern.

Auf der anderen Seite haben wir unsere Datenbanken für strukturierte Daten. Du hast vielleicht schon von [MySQL](https://www.mysql.com/) und [MariaDB](https://mariadb.org/) gehört.
Diese beiden sind Geschwister und MariaDB wurde auf der Basis von MySQL entwickelt und kann als Ersatz dienen und einige Verbesserungen bieten.
Eine weitere populäre Datenbank für Unternehmen ist [PostgreSQL](https://www.postgresql.org/), die in der Datenwissenschaft und bei Projekten mit großen Datenmengen eingesetzt wird.
Eine weitere spezielle Datenbank ist [SQLite](https://www.sqlite.org), eine serverlose Datenbank, die ohne einen laufenden Server auskommt.
Sie wird verwendet, wenn die Datenmenge gering ist und die Anwendung ohne weitere Einrichtungsschritte laufen soll. 
Sie wird in den Speicher geladen und ist daher durch den verfügbaren Speicher deines Systems begrenzt.

Auf dieser Seite werden wir uns auf relationale Datenbanken konzentrieren.
Genauer gesagt werden wir uns auf SqLite, MySql, MariaDB und PostgreSQL konzentrieren.

## Sind meine Daten strukturiert oder unstrukturiert?

In den meisten Fällen sind deine Daten strukturiert.
Viele Menschen neigen dazu, Datenbanken für unstrukturierte Daten zu verwenden, weil das anfangs weniger Arbeit zu sein scheint.
Im Allgemeinen können fast alle Daten, die auf den ersten Blick unstrukturiert aussehen, strukturiert sein.

Verwende keine dokumentenbasierte Datenbank, nur weil du deine Daten irgendwo ablegen willst.
Fange an zu lernen, mit relationalen Daten zu arbeiten, und nutze die vielen Vorteile, die sich daraus ergeben.

## Verschiedene Datenbanken, verschiedene Geschmäcker

Wenn wir Daten aus einer unserer Datenbanken abrufen, verwenden wir [SQL (Structured Query Language)](https://en.wikipedia.org/wiki/SQL).

Alle unsere Datenbanken verwenden SQL, aber nicht dasselbe SQL.
Obwohl sie alle eine ähnliche Syntax haben, verwenden sie unterschiedliche [Flavours](https://en.wikipedia.org/wiki/SQL#Procedural_extensions) von SQL.
Die meisten deiner Abfragen funktionieren in allen Datenbanken gleich oder müssen nur leicht angepasst werden.

MySQL und SQLite verwenden [SQL/PSM (SQL/Persistent Stored Module)](https://en.wikipedia.org/wiki/SQL/PSM), das ist die standardisierteste Art von SQL.

MariaDB verwendet [SQL/PSM](https://en.wikipedia.org/wiki/SQL/PSM) und [PL/SQL](https://en.wikipedia.org/wiki/PL/SQL), das das Schreiben von prozeduralen Anweisungen mit Schleifen und anderen Dingen ermöglicht.
Sie hat ihren Ursprung in der [OracleDB](https://www.oracle.com/database/).

PostgreSQL verwendet [PL/pgSQL](https://en.wikipedia.org/wiki/PL/pgSQL), das eine Erweiterung von [PL/SQL](https://en.wikipedia.org/wiki/PL/SQL) ist und die allgemeinen Möglichkeiten von SQL auf eine Ebene erweitert, die hochkomplexe Operationen ermöglicht.
Manche Leute sagen, dass du dein komplettes Backend in PL/pgSQL schreiben kannst.

## Datenbank oder DBMS

Wenn wir von einer Datenbank sprechen, verwechseln wir meist drei Dinge:

### DBMS

DBMS bedeutet [Database Management System](https://en.wikipedia.org/wiki/Database#Database_management_system). 
Diese Systeme sind dazu da, deine Datenbanken zu verwalten.
Da SQL-Datenbanken für relationale Daten sind, sind unsere DBMS eigentlich RDBMS (Relational Database Management System).

### Datenbank

Eine Datenbank ist eine Datenbank innerhalb deines DBMS.
Sie enthält deine Daten in Tabellen, speichert Prozeduren und macht andere Dinge.

### Schema

Einige Datenbanken wie PostgreSQL haben eine weitere Unterteilung in einer Datenbank, die [Schema](https://www.postgresqltutorial.com/postgresql-schema/) genannt wird.

Obwohl MariaDB und MySQL ihre Datenbanken in den Statements "Datenbank" nennen, bezeichnen sie sie intern auch als [Schema](https://www.tutorialspoint.com/difference-between-schema-and-database-in-mysql).

Eine zweite Unterteilung in Schemas hat den Vorteil einer besseren Organisation. 
Es ist üblich, über verschiedene Schemas auf Daten zuzugreifen, aber unüblich, gleichzeitig auf Daten in verschiedenen Datenbanken zuzugreifen, obwohl einige RDBMS dies zulassen.

### Struktur eines RDBMS

Im Allgemeinen können wir uns unsere Struktur wie folgt vorstellen:

**MariaDB and MySQL**

```
RDBMS
├── Database/Schema
│   ├── Table
│   └── Table
└── Database/Schema
    ├── Table
    └── Table
```

**PostgreSQL**

```
RDBMS
├── Database
│   ├── Schema
│   │   ├── Table
│   │   └── Table
│   └── Schema
│       └── Table
└── Database
    └── Schema
        ├── Table
        └── Table
```
