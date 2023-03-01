# Geänderte Daten zurückgeben

**Gilt für Postgres, SqLite und MariaDB**

Wenn wir Daten durch Update- oder Delete-Anweisungen ändern oder sie einfügen, können wir die geänderten oder eingefügten Daten zurückgeben.

Bei Aktualisierungen bedeutet das, dass wir die jetzt eingestellten Werte erhalten, was jetzt vielleicht nicht hilfreich ist, aber es gibt Mechanismen in Datenbanken, die Daten zwischen Aktualisierung und Schreiben ändern.
Bei Löschungen erhalten wir die gelöschten Einträge, die nicht mehr in der Datenbank vorhanden sind.
Das könnte noch hilfreicher sein.
Richtig interessant wird es bei Einfügungen.
Im nächsten Kapitel erfährst du, wie du automatisch IDs erstellen oder Standardwerte für Spalten festlegen kannst.
Diese werden natürlich nicht von dir gesetzt, wenn du Daten einfügst, aber du kannst sie trotzdem über die `RETURNING`-Klausel erhalten.

Die Nutzung dieser Funktion ist ganz einfach.
Wenn du ein `DELETE`, `UPDATE` oder `INSERT` ausführst, fügst du einfach ein `RETURNING` an und listest die Spalten auf, die du zurückbekommen möchtest.

```mariadb
DELETE
FROM player
WHERE id = 10
RETURNING id, player_name;
```

**Hinweis:** Nicht alle Datenbank-Clients verstehen, dass diese Abfrage Daten zurückgibt.
Meistens werden sie hier keine Daten anzeigen, aber sei sicher, dass Java das mit dem richtigen Code kann.
