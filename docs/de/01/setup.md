# Einrichtung

Um mit unserer Datenbank zu beginnen, brauchen wir zunächst zwei Dinge.

1. Eine Datenbank
2. Einen Client für den Zugriff auf unsere Datenbank

## Datenbank einrichten

Ich werde hier nicht die Einrichtung jeder Datenbank im Detail erklären.
Viele Leute haben das bereits getan.
Ich werde die Installationsanleitungen für Debian, Ubuntu und Windows hier verlinken.
Wenn du ein "I use Arch btw"-Nutzer bist, wirst deinen eigenen Weg finden, so wie du es immer tust.

Wenn du ein Docker Nutzer bist, weißt du auch was zu tun ist.

### MySQL

Viele Anleitungen enthalten eine Installation von Apache mit einer PhpMyAdmin-Instanz.

Ich rate dringend von der Verwendung von PhpMyAdmin ab.
Es hat mehrere bekannte Schwachstellen und ist generell ein reines Sicherheitsproblem auf deinem Server.
Es gibt viel bessere Möglichkeiten, welche ich dir gleich Zeigen werde.

[Debian](https://www.digitalocean.com/community/tutorials/how-to-install-the-latest-mysql-on-debian-10) | [Ubuntu](https://www.digitalocean.com/community/tutorials/how-to-install-mysql-on-ubuntu-20-04) | [Windows](https://dev.mysql.com/doc/refman/8.0/en/windows-installation.html)

### MariaDB

Viele Anleitungen enthalten eine Installation von Apache mit einer PhpMyAdmin-Instanz.

Ich rate dringend von der Verwendung von PhpMyAdmin ab.
Es hat mehrere bekannte Schwachstellen und ist im Allgemeinen ein reines Sicherheitsproblem für deinen Server.
Es gibt viel bessere Möglichkeiten, welche ich dir gleich Zeigen werde.

[Debian](https://www.digitalocean.com/community/tutorials/how-to-install-mariadb-on-debian-10) | [Ubuntu](https://www.digitalocean.com/community/tutorials/how-to-install-mariadb-on-ubuntu-20-04) | [Windows](https://mariadb.com/kb/en/installing-mariadb-msi-packages-on-windows/)

### PostgreSQL

Einige Installationsprozesse schlagen vor, dass du auch pgadmin installierst.
Es ist zwar ein gutes Tool, wird aber von den Desktop-Tools abgelöst, die ich dir als Nächstes zeigen werde.
Es ist also nicht zwingend notwendig.
Solltest du es dennoch installieren wollen, empfehle ich es lokal zu installieren und nicht auf dem Server.

[Debian](https://linuxize.com/post/how-to-install-postgresql-on-debian-10/) | [Ubuntu](https://www.digitalocean.com/community/tutorials/how-to-install-and-use-postgresql-on-ubuntu-20-04) | [Windows](https://www.postgresqltutorial.com/install-postgresql/)

### SQLite

Keine Installation erforderlich.
Das Desktop-Tool deiner Wahl kann diese Datenbank für dich erstellen.
SqLite ist am Ende lediglich eine Datei.
Es kann zusätzlich auch nur im Arbeitsspeicher liegen und muss nicht mal zwingend als Datei existieren.

## Desktop-Tool

Nachdem wir unsere Datenbank eingerichtet haben, müssen wir uns mit ihr verbinden.

Dazu musst du ein Tool deiner Wahl auswählen.

**Kostenlos**

- Unix oder Windows [DBeaver](https://dbeaver.io/)
- Windows [HeidiSQL](https://www.heidisql.com/)

**Bezahlt**

- Unix und Windows [DataGrip](https://www.jetbrains.com/datagrip/) (Kostenlos, wenn du Teil von [GitHub Education](https://education.github.com/) bist)

**Wenn du die Möglichkeit hast, DataGrip zu verwenden tu es!**

## Verbinden

Wenn du die Datenbank lokal auf deinem Rechner installiert hast, ist dieser Teil einfach, da du den SSH-Tunnel-Spaß überspringen kannst.

Du könntest versucht sein, den Port deiner remote Datenbank zu öffnen, um auf sie zuzugreifen, aber tu das NICHT!
Datenbanksoftware ist im Allgemeinen nicht dafür ausgelegt, dass sie Angriffen von außen standhält. Einige Datenbanken wie Redis haben mittlerweile standardmäßig keine Authentifizierung mehr.
Es ist immer besser, sie hinter deiner Firewall zu schützen.
Um sich dennoch mit der Datenbank zu verbinden, gibt es SSH-Tunnel.
Ein SSH Tunnel macht einen Dienst der auf einem remote Server läuft auf deinem lokalen System verfügbar.
Für deine Anwendung macht es dann keinen Unterschied mehr, ob die Anwendung lokal läuft oder nicht.
Es ist nicht mal möglich für das Programm den Unterschied zu erkennen.

Alle drei Tools bieten die Möglichkeit, einen SSH-Tunnel für die Verbindung zu deiner Datenbank zu verwenden. Das Ganze ist sehr einfach mit einem schönen UI.

[Wenn du noch keine ssh-Verbindung eingerichtet hast, solltest du das jetzt tun.(https://phoenixnap.com/kb/ssh-to-connect-to-remote-server-linux-or-windows)

Jetzt musst du dich über einen ssh-Tunnel mit deiner Datenbank verbinden. Dafür verlinke ich wieder Tutorials für jedes der Tools.

[DBeaver](https://dbeaver.com/docs/wiki/Create-Connection/) | [HeidiSQL](https://marcus-obst.de/wiki/Database%20-%20HeidiSQL%20SSH%20Tunnel%20Setup) | [DataGrip](https://www.jetbrains.com/help/datagrip/configuring-ssh-and-ssl.html#ssh)

Das war's schon. Jetzt kannst du mit der Datenbank loslegen.
