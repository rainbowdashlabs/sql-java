# Setup

To get started with our database we first need two thing.

1. A database
2. A client to access our database

## Database Setup

I won't explain the setup of every database here in detail. A lot of people did this already. I will link the debian,
ubuntu and windows installation guides here. If you are an "I use arch btw" user you will find your own way like you
always do.

If you are a docker user you know what to do.

### MySQL

Many guides out there will include an installation of apache with a phpmyadmin instance.

I highly discourage the usage of phpmyadmin. It has several known exploits and is in general a pure security issue on
your server. There are much better ways.

[Debian](https://www.digitalocean.com/community/tutorials/how-to-install-the-latest-mysql-on-debian-10)
| [Ubuntu](https://www.digitalocean.com/community/tutorials/how-to-install-mysql-on-ubuntu-20-04)
| [Windows](https://dev.mysql.com/doc/refman/8.0/en/windows-installation.html)

### MariaDB

Many guides out there will include an installation of apache with a phpmyadmin instance.

I highly discourage the usage of phpmyadmin. It has several known exploits and is in general a pure security issue on
your server. There are much better ways.

[Debian](https://www.digitalocean.com/community/tutorials/how-to-install-mariadb-on-debian-10)
| [Ubuntu](https://www.digitalocean.com/community/tutorials/how-to-install-mariadb-on-ubuntu-20-04)
| [Windows](https://mariadb.com/kb/en/installing-mariadb-msi-packages-on-windows/)

### PostgreSQL

Some installation processes will suggest you to install pgadmin as well. I do not recommend this. It is a good tool, 
but superseded by the desktop tools I will show you next.

[Debian](https://linuxize.com/post/how-to-install-postgresql-on-debian-10/)
| [Ubuntu](https://www.digitalocean.com/community/tutorials/how-to-install-and-use-postgresql-on-ubuntu-20-04)
| [Windows](https://www.postgresqltutorial.com/install-postgresql/)

### SQLite

No installation required. Your desktop tool of your choice can create this database for you.

## Desktop Tool

After we have our database running we need to connect to it.

For this we need to choose our tool.

**Free**

- Unix or Windows [DBeaver](https://dbeaver.io/)
- Windows [HeidiSQL](https://www.heidisql.com/)

**Paid**

- Unix and Windows [DataGrip](https://www.jetbrains.com/datagrip/) (Free if you are part
  of [GitHub Education](https://education.github.com/))

## Connect

If you have installed the database locally on your machine this part will be simple since you can skip the ssh tunnel
fun.

You may be tempted to open the port of your remote database to access it, but DON'T do this! Database software is in 
general not designed to be secure enough to withstand outside attacks. It's always better to keep it secure behind 
your firewall.

All three tools have an option to use an ssh tunnel for connection to your database.

[If you haven't set up an ssh connection yet, you should do it now.
](https://phoenixnap.com/kb/ssh-to-connect-to-remote-server-linux-or-windows)

Now you need to connect to your database via an ssh tunnel.

[DBeaver](https://dbeaver.com/docs/wiki/Create-Connection/)
| [HeidiSQL](https://marcus-obst.de/wiki/Database%20-%20HeidiSQL%20SSH%20Tunnel%20Setup)
| [DataGrip](https://www.jetbrains.com/help/datagrip/configuring-ssh-and-ssl.html#ssh)

That's it. Now you are ready for some fun with the database.
