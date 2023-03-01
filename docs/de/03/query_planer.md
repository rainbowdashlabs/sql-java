# Query Planer

Jedes Mal, wenn wir auf irgendeine Weise Daten aus unserer Datenbank auswählen, erstellt die Datenbank zunächst einen Abfrageplan. Dieser Abfrageplan 
legt fest, wie die Datenbank die Daten lesen wird. Die Abfragepläne sind bei fast jeder Datenbank anders und bieten einen 
unterschiedlichen Detailgrad. Deshalb werde ich jeden Query Planer in einem eigenen Abschnitt behandeln.

Doch bevor wir uns mit dem Layout des Abfrageplans selbst beschäftigen, wollen wir zunächst über die allgemeine Verwendung sprechen.

## Warum einen Query Planer verwenden?

Wenn wir nach einem Eintrag in unserer Tabelle suchen, sagt uns der Abfrageplan, wie die Datenbank nach unserem Eintrag sucht.

Wenn wir zum Beispiel Folgendes ausführen

```sql
SELECT id, player_name, last_online
FROM player
WHERE id = 5;
```

Unsere Datenbank wird alle Zeilen in unserer Datenbank durchgehen und alle Einträge zurückgeben, deren id 5 ist. 
alle Einträge durchzugehen, wird teuer und langsam. Deshalb können wir einen Index hinzufügen. Du musst nicht verstehen 
Wie ein Index funktioniert, musst du nicht verstehen, aber im Grunde gibt er dem Abfrageplan zusätzliche Informationen. Zum Beispiel, dass 
die ID 5 nur einmal existiert, wenn der Index eindeutig ist. Auf diese Weise sucht unsere Datenbank nur nach dem ersten Eintrag 
mit der ID 5 suchen und aufhören, da sie weiß, dass es nur einen passenden Eintrag gibt. Ein schöner Zusatz ist auch, dass ein 
Index auf eine Spalte sortiert ist. Das bedeutet, dass die Datenbank nicht alle Zeilen lesen muss, die vor der 5 liegen, sondern sie kann 
fast direkt zu ihr springen, ohne alle Spalten davor zu lesen.

Was dabei passiert, lässt sich nur anhand der Abfragepläne erkennen. Und deshalb ist es so wichtig, sie zu kennen und zu lesen. 
Sie helfen dir, deine Datenbank besser zu verstehen, und du kannst überprüfen, ob die Indizes, die du hinzugefügt hast 
tatsächlich von deiner Abfrage verwendet werden.

Um den Abfrageplan zu sehen, müssen wir nur ein `EXPLAIN` vor unserer Abfrage hinzufügen.

```sql
SELECT id, player_name, last_online
FROM player
WHERE id = 5;
```

Wichtig ist auch, dass explain die Abfrage nicht selbst ausführt. Alle Werte sind also 
Schätzungen und keine echten Werte. Diese Abfragepläne werden auf der Grundlage interner Statistiken über eine Tabelle erstellt. Wenn du große 
Änderungen an den Daten einer Tabelle können dazu führen, dass die Abfragepläne manchmal nicht korrekt sind.

Jetzt können wir loslegen.
