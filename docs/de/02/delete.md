# Löschen

Nachdem wir alle unsere Daten eingefügt haben, fehlt uns nur noch eine grundlegende Funktion.
Manchmal ist es mit dem Aktualisieren oder Einfügen von Daten nicht getan. Wir müssen auch Daten löschen.

Dafür verwenden wir das Schlüsselwort `DELETE`. Das Schlüsselwort `DELETE` ist der [`SELECT`-Anweisung](dev/private/java/!tutorial/basicsql-pages/docs/de/02/select.md) ziemlich ähnlich, die wir
bereits kennen.
Wir müssen `FROM` definieren, welche Tabelle wir löschen wollen und `WHERE`, wo wir diese Zeilen löschen wollen

Mit diesem Wissen können wir davon ausgehen, dass die allgemeine `DELETE`-Anweisung wie folgt aussehen würde

```sql
DELETE
FROM table_name
WHERE column_1 = value_1; 
```

Versuchen wir, eine Anweisung zu schreiben, die den Nutzer mit der ID 10 aus unserer Nutzertabelle löscht:


<details>
<summary>Solution</summary>

```sql
DELETE
FROM player
WHERE id = 10;
```

</details>

Jetzt haben wir nur noch ein Problem...
Unser Freundesdiagramm und die money-Tabelle enthalten immer noch Verweise auf den Spieler mit der ID 10.
Um das zu ändern, müssen wir diese Einträge auch aus diesen beiden Tabellen löschen.

Schreibe zwei Anweisungen.

1. Lösche alle Einträge aus friend_graph, **bei denen** der Spieler 1 **oder** Spieler 2 die ID 10 hat. (Mach das in einer Anweisung.)
2. Lösche den Eintrag des Spielers 10 aus der Tabelle `money` (Wenn du die Tabelle money nicht erstellt hast, kannst du sie einfach ignorieren).

<details>
<summary>Solution</summary>

   ```sql
DELETE
FROM friend_graph
WHERE player_1 = 10
   OR player_2 = 10;

DELETE
FROM money
WHERE id = 10;
```

</details>
   
Natürlich gibt es eine bessere und sicherere Lösung, um "tote" Einträge in anderen Tabellen zu vermeiden.
Das werden wir in einem anderen Kapitel lernen.

Da wir nun die Anweisungen für `SELECT`, `INSERT`, `UPDATE` und `DELETE` kennen, sind wir bereit für das nächste Kapitel.
