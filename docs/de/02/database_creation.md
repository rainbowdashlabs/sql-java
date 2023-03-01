# Datenbankerstellung

Jetzt, wo wir wissen, was wir in unserer Datenbank haben können und wie wir unsere Sachen benennen, können wir endlich 
mit ihr zu arbeiten.

Stelle sicher, dass du mit deinem RDBMS verbunden bist. Verwende einen ssh-Tunnel, wie ich es dir auf der [setup](../01/setup.md) 
Seite gezeigt habe, wenn du dich mit einer entfernten Datenbank verbindest.

Die Erstellung von Datenbanken ist bei allen unseren Datenbanken sehr einfach.

## SQLite

In SQLite ist eine Datenbank gleichbedeutend mit einer Datei. Du hast deine Datenbank bereits erstellt, indem du die SQL-Datei und den Eintrag erstellt hast.

## MySQL, MariaDB und PostgreSQL

Um unsere Datenbank zu erstellen, können wir den Befehl `CREATE` verwenden.

```sql
CREATE DATABASE sql_starter; -- (1)
```

1. Wir erstellen eine neue Datenbank mit dem Namen `sql_starter`

Unsere Desktop-Clients erlauben es uns, die Datenbank über die Benutzeroberfläche zu erstellen.

**DataGrip**

Klicke mit der rechten Maustaste auf deinen Datenbankeintrag und wähle `Neu > Datenbank`

Klicke mit der rechten Maustaste auf deine neue Datenbank und wähle "Zur Abfragekonsole springen" und wähle die Standarddatenbank.

**DBeaver**

Klicke mit der rechten Maustaste auf deinen Datenbankeintrag und wähle "Neue Datenbank erstellen".

Klicke mit der rechten Maustaste auf deine neue Datenbank und wähle "SQL-Editor > SQL-Konsole öffnen".

**HeidiSQL**

TBD

### Nur Postgres

Wenn du PostgreSQL verwendest, befindet sich alles, was wir tun, standardmäßig im öffentlichen Schema.

Dieses Schema existiert aus Kompatibilitätsgründen mit anderen Datenbanken, da die Schema-Unterteilung irgendwie einzigartig ist.

Wenn du möchtest, kannst du jetzt ein Schema erstellen, um dein öffentliches Schema sauber zu halten.

```sql
create schema my_schema;
```

**DataGrip**

Klicke mit der rechten Maustaste auf deine Datenbank und wähle `Neu > Schema`

Die Konsole in DataGrip ist standardmäßig an eine Datenbank und nicht an ein Schema gebunden.

Du kannst das ändern, indem du das neue Schema oben rechts im Konsolenfenster auswählst.


**DBeaver**

Klicke mit der rechten Maustaste auf deine Datenbank und wähle "Erzeugen > Schema".

Klicke mit der rechten Maustaste auf dein neues Schema und wähle "SQL-Editor > SQL-Konsole öffnen". Anstatt an das öffentliche Schema gebunden zu sein 
Schema gebunden ist, ist deine Konsole jetzt an das neue Schema gebunden.

Alternativ kannst du auch das Schema ändern, indem du oben im Konsolenfenster auf `public@<Datenbankname>` klickst 
und wähle dein neues Schema aus.

**HeidiSQL**

TBD


## Datenbankkonfiguration
Die Konfiguration deiner Datenbank wie Kodierung und Zeitzone richtet sich nach deinen Systemeinstellungen.

Die Optionen, die du an deiner Datenbank ändern kannst, sind zahlreich und ich werde hier nicht darauf eingehen.

Als Anfänger sollten die Standardeinstellungen für alles, was du tun willst, ausreichend sein.
