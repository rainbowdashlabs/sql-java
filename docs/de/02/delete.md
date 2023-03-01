# Löschen

Nachdem wir alle unsere Daten eingefügt haben, fehlt uns nur noch eine grundlegende Funktion. Manchmal ist das Aktualisieren oder Einfügen von Daten nicht
genug. Wir müssen auch Daten löschen.

Dafür verwenden wir das Schlüsselwort `DELETE`. Das Schlüsselwort `DELETE` ist der [`SELECT`-Anweisung] (select.md) sehr ähnlich, die wir
bereits kennen.
Wir müssen `FROM` definieren, welche Tabelle wir löschen wollen und `WHERE`, wo wir diese Zeilen löschen wollen

Daher können wir davon ausgehen, dass die allgemeine `DELETE`-Anweisung wie folgt aussehen würde:

```sql
DELETE
FROM table_name
WHERE spalte_1 = wert_1; 
```

Versuchen wir nun, eine Anweisung zu schreiben, die den Nutzer mit der ID 10 aus unserer Nutzertabelle löscht:



<Details>
<summary>Lösung</summary>

```sql
DELETE
FROM player
WHERE id = 10;
```

</details>

Jetzt haben wir nur noch ein Problem... Unser Freundschaftsdiagramm und die Geldtabelle enthalten immer noch Verweise auf den Spieler mit der
id 10. Um das zu ändern, müssen wir diese Einträge auch aus diesen beiden Tabellen löschen.

Schreibe zwei Anweisungen.

1. Lösche alle Einträge aus friend_graph, **bei denen** der Spieler 1 **oder** Spieler 2 die ID 10 hat. (Mach das in einer Anweisung.)
2. Lösche den Eintrag von Spieler 10 aus der Tabelle `Geld` (Wenn du die Tabelle Geld nicht erstellt hast, kannst du sie einfach
   ignorieren.)

<Details>
<summary>Lösung</summary>

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
   
Natürlich gibt es eine bessere und sicherere Lösung, um "tote" Einträge in anderen Tabellen zu vermeiden. Das werden wir in 
einem anderen Kapitel.

Da wir nun die Anweisungen für `SELECT`, `INSERT`, `UPDATE` und `DELETE` kennen, sind wir bereit für das nächste Kapitel.
