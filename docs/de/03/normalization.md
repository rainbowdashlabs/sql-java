# Normalisierung

Normalisierung ist ein wichtiges Thema bei der Modellierung deiner Datenbank.
Aber das ist gar nicht so schwer.
Eigentlich haben wir das schon die ganze Zeit gemacht.

## Beziehungen

Normalerweise haben Einträge eine Art von Beziehung.
Um diese zu definieren, verwenden wir den Begriff `One to Many` (1:n) `Many to One` (n:1), `One to One` (1:1).
`Many to Many` (n:n) ist etwas, das du normalerweise nicht hast, und wenn doch, ist es wahrscheinlich ein Zeichen für eine schlechte Normalisierung.

### One to Many und Many to One

`One to Many` bedeutet, dass eine einzelne Entität von dir mehrere Einträge in einer einzigen Tabelle hat.
Wir haben das bereits in unserem `friend_graph`.
_Ein_ Spieler kann _viele_ Freunde haben.
So einfach ist das!

`Many to One` ist normalerweise dasselbe, nur in eine andere Richtung.
_Viele_ Freundschaftsverbindungen beziehen sich auf _einen_ Spieler. 

### One to One

`One to One` ist das, was es schon sagt.
Eine einzelne Entität steht in Beziehung zu einer anderen einzelnen Entität in einer anderen Tabelle.
Das haben wir mit unserer Tabelle `player` und `money`.
Die Geldtabelle enthält genau _einen_ Eintrag für _einen_ Spieler.

### Many to Many

Mir fällt nicht einmal ein gutes Beispiel für eine `Many to Many` Beziehung ein.
Es ist einfach gegen das Gesetz. 

## Normalformen

Wenn wir Daten normalisieren, verwenden wir oft die Begriffe der Normalform.
Das reine Konzept ist immer Gegenstand von Diskussionen und wird etwas unscharf, je mehr man versucht, zu normalisieren.

[Wikipedia](https://en.wikipedia.org/wiki/Database_normalization#Example_of_a_step_by_step_normalization) hat einige gute Beispiele dafür, wenn du dich damit beschäftigen willst.
In Wirklichkeit wird dich nie jemand fragen, welche Normalform deine Tabelle haben soll.

## Faustregeln

Im Allgemeinen gibt es ein paar Regeln, die das Normalisieren von Daten schon recht einfach machen.
Denke nicht über deine Normalform nach. 
Alles, was zählt, ist die Beziehung einer Entität zu einer anderen Entität.

### Schlüssel sind der Schlüssel

Denke an eindeutige Bezeichnungen für deine Entität.
Diese Bezeichnungen sollte sich nicht ändern.
Das kann eine Art UID sein, oder wenn deine Entität keine hat, sollte es eine automatisch inkrementierende ID sein.

### One to many in a column

Versuche gar nicht erst, One-to-Many-Beziehungen für Listen und Ähnliches in einer einzigen Spalte zu serialisieren.
Das mag eine Zeit lang funktionieren, aber später wirst du dich dafür hassen.
Wenn du eine One-to-Many-Beziehung hast, mache zwei Tabellen dafür.

### Je feiner, desto besser

Teile deine Sachen auf. Gruppiere die Dinge, die zusammen bleiben sollen und zusammen angefordert werden.
Generell gilt: Wenn du es nicht zur gleichen Zeit brauchst, gehört es nicht zusammen.

### Foreign keys sind der Schlüssel

Wenn deine Tabelle nicht von anderen Tabellen referenziert wird oder nicht auf eine andere Tabelle verweist, sind deine Daten wahrscheinlich nicht auf die richtige Weise gekoppelt.
Letztendlich gehören alle Daten zu irgendeiner Art von Entität und dein Tabellenlayout sollte dies widerspiegeln.
