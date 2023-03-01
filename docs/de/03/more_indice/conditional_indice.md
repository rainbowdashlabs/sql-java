# Bedingte Indizes

Bis jetzt haben wir nur einen Index für eine oder mehrere Zeilen hinzugefügt. Das ist natürlich gut, wenn wir nach einzelnen Einträgen suchen 
Einträge suchen oder unsere Tabelle sortieren wollen. Aber es gibt auch andere Fälle. Wir könnten zum Beispiel alle Einträge finden wollen, die 
ungerade in der money Tabelle sind. Dieses Beispiel könnte der Einfachheit halber etwas konstruiert sein, aber es ist ein gutes 
Beispiel, um dir zu zeigen, wie bedingte Indizes funktionieren.

Werfen wir zunächst einen Blick auf unsere ursprüngliche Abfrage, die wir ausführen wollen:

```sql
SELECT player_id, money
FROM money
WHERE money % 2 != 0;
```

Wenn du dir den Abfrageplan ansiehst, wirst du feststellen, dass wir hier keinen Index verwenden, obwohl wir 
einen Index für die Spalte money haben 

Lass uns einen Index für unsere Berechnung hinzufügen! Anstatt nur einen Spaltenwert zu unserem Index hinzuzufügen, fügen wir nun den 
den transformierten Spaltenwert in unseren Index ein, was im Grunde genommen unsere Prüfung ist, die wir zuvor durchgeführt haben.

```sql
CREATE INDEX money_is_odd 
    ON money((money % 2 != 0))
```

Nicht, dass wir den Ausdruck in einen weiteren Satz geschweifter Klammern schreiben. Das ist notwendig, weil unsere Datenbank einen Wert erwartet 
und nicht einen Ausdruck.

```sql
SELECT player_id, money
FROM money
WHERE money % 2 != 0;
```

Wenn wir uns unseren Abfrageplan jetzt noch einmal ansehen, werden wir feststellen, dass wir tatsächlich einen Index für unsere Prüfung verwenden!

Versuchen wir das Gleiche mit einer geraden Prüfung zu tun:

```sql
SELECT player_id, money
FROM money
WHERE money % 2 = 0;
```

Wenn wir uns hier den Abfrageplan ansehen, sehen wir leider, dass unsere Datenbank nicht weiß, dass die Lösung einfach ein 
invertierter Index ist, aber das ist eigentlich in Ordnung. Wir haben jetzt zwei Möglichkeiten:

1. Wir fügen einen zweiten Index für gerade Zahlen hinzu

```sql
CREATE INDEX money_is_even
    ON money ((money % 2 = 0));

SELECT player_id, money
FROM money
WHERE money % 2 = 0;
```
2. Das Ergebnis unserer Bedingung einfach umkehren!

```sql
SELECT player_id, money
FROM money
WHERE NOT money % 2 != 0;
```

Die Wahl ist vielleicht schon ziemlich klar. Indizes brauchen Platz, deshalb lohnt es sich, ein bisschen länger zu überlegen, ob du 
einen bestehenden Index wiederverwenden kannst, anstatt einen neuen zu erstellen, lohnt es sich normalerweise immer. Im Allgemeinen werden Indizes nur 
verwendet, wenn der Ausdruck in der Abfrage auch im Index selbst enthalten ist. Mehr dazu im nächsten Kapitel!
