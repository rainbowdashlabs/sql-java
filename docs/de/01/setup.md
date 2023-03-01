# Einrichtung

Um mit unserer Datenbank zu beginnen, brauchen wir zunächst zwei Dinge.

1. Eine Datenbank
2. Einen Client für den Zugriff auf unsere Datenbank

## Datenbank einrichten

Ich werde hier nicht die Einrichtung jeder Datenbank im Detail erklären.
Viele Leute haben das bereits getan.
Ich werde die Installationsanleitungen für Debian, Ubuntu und Windows hier verlinken.
Du bist ein "Ich benutze Arch btw"-Nutzer und wirst deinen eigenen Weg finden, so wie du es immer tust.

Du bist ein Docker Nutzer und weißt, was zu tun ist.

### MySQL

Viele Anleitungen enthalten eine Installation von Apache mit einer phpmyadmin-Instanz.

Ich rate dringend von der Verwendung von phpmyadmin ab.
Es hat mehrere bekannte Schwachstellen und ist generell ein reines Sicherheitsproblem auf deinem Server.
Es gibt viel bessere Möglichkeiten.

[Debian](https://www.digitalocean.com/community/tutorials/how-to-install-the-latest-mysql-on-debian-10) | [Ubuntu](https://www.digitalocean.com/community/tutorials/how-to-install-mysql-on-ubuntu-20-04) | [Windows](https://dev.mysql.com/doc/refman/8.0/en/windows-installation.html)

### MariaDB

Viele Anleitungen enthalten eine Installation von Apache mit einer phpmyadmin-Instanz.

Ich rate dringend von der Verwendung von phpmyadmin ab.
Es hat mehrere bekannte Schwachstellen und ist im Allgemeinen ein reines Sicherheitsproblem für deinen Server.
Es gibt viel bessere Möglichkeiten.

[Debian](https://www.digitalocean.com/community/tutorials/how-to-install-mariadb-on-debian-10) | [Ubuntu](https://www.digitalocean.com/community/tutorials/how-to-install-mariadb-on-ubuntu-20-04) | [Windows](https://mariadb.com/kb/en/installing-mariadb-msi-packages-on-windows/)

### PostgreSQL

Einige Installationsprozesse schlagen vor, dass du auch pgadmin installierst.
Ich empfehle das nicht.
Es ist zwar ein gutes Tool, wird aber von den Desktop-Tools abgelöst, die ich dir als Nächstes zeigen werde.

[Debian](https://linuxize.com/post/how-to-install-postgresql-on-debian-10/) | [Ubuntu](https://www.digitalocean.com/community/tutorials/how-to-install-and-use-postgresql-on-ubuntu-20-04) | [Windows](https://www.postgresqltutorial.com/install-postgresql/)

### SQLite

Keine Installation erforderlich. Das Desktop-Tool deiner Wahl kann diese Datenbank für dich erstellen.

## Desktop-Tool

Nachdem wir unsere Datenbank eingerichtet haben, müssen wir uns mit ihr verbinden.

Dazu müssen wir ein Tool deiner Wahl auswählen.

**Frei**

- Unix oder Windows [DBeaver](https://dbeaver.io/)
- Windows [HeidiSQL](https://www.heidisql.com/)

**Bezahlt**

- Unix und Windows [DataGrip](https://www.jetbrains.com/datagrip/) (Kostenlos, wenn du Teil
  von [GitHub Education](https://education.github.com/))

## Verbinden

Wenn du die Datenbank lokal auf deinem Rechner installiert hast, ist dieser Teil einfach, da du den ssh-Tunnel-Spaß überspringen kannst.

Du könntest versucht sein, den Port deiner entfernten Datenbank zu öffnen, um auf sie zuzugreifen, aber tu das NICHT!
Datenbanksoftware ist im Allgemeinen nicht so sicher, dass sie Angriffen von außen standhält.
Es ist immer besser, sie hinter deiner Firewall zu schützen.

Alle drei Tools bieten die Möglichkeit, einen SSH-Tunnel für die Verbindung zu deiner Datenbank zu verwenden.

[Wenn du noch keine ssh-Verbindung eingerichtet hast, solltest du das jetzt tun.(https://phoenixnap.com/kb/ssh-to-connect-to-remote-server-linux-or-windows)

Jetzt musst du dich über einen ssh-Tunnel mit deiner Datenbank verbinden.

[DBeaver](https://dbeaver.com/docs/wiki/Create-Connection/) | [HeidiSQL](https://marcus-obst.de/wiki/Database%20-%20HeidiSQL%20SSH%20Tunnel%20Setup) | [DataGrip](https://www.jetbrains.com/help/datagrip/configuring-ssh-and-ssl.html#ssh)

Das war's schon. Jetzt kannst du mit der Datenbank loslegen.
