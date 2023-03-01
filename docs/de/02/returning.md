# Geänderte Daten zurückgeben

**Gilt für Postgres, SqLite und MariaDB**

Wenn wir Daten durch Update- oder Delete-Anweisungen ändern oder sie einfügen, können wir die geänderten oder eingefügten Daten zurückgeben.

Bei Aktualisierungen bedeutet das, dass wir die jetzt gesetzten Werte erhalten, was jetzt vielleicht nicht hilfreich ist, aber es gibt Mechanismen in Datenbanken
die Daten zwischen Update und Schreiben ändern. Bei Löschungen erhalten wir die gelöschten Einträge, die nicht mehr in der Datenbank vorhanden sind.
in der Datenbank vorhanden sind. Das könnte noch hilfreicher sein. Richtig interessant wird es bei den Einfügungen. Im nächsten Kapitel wirst du lernen, wie man automatisch IDs erstellt oder Standardwerte für Spalten definiert. Diese werden natürlich nicht von dir festgelegt, wenn du
du Daten einfügst, aber du kannst sie über die `RETURNING`-Klausel abrufen.

Die Verwendung dieser Funktion ist ganz einfach. Wenn du ein `DELETE`, `UPDATE` oder `INSERT` ausführst, füge einfach ein `RETURNING` nach
und gib die Spalten an, die du zurückgeben möchtest.

```mariadb
DELETE
FROM player
WHERE id = 10
RETURNING id, player_name;
```

**Hinweis:** Nicht alle Datenbank-Clients verstehen, dass diese Abfrage Daten zurückgibt. Meistens werden sie hier keine Daten anzeigen, aber sei sicher, dass Java das mit dem richtigen Code kann.
