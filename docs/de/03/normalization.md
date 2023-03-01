# Normalisierung

Normalisierung ist ein wichtiges Thema bei der Modellierung deiner Datenbank. Aber es ist gar nicht so schwer. Eigentlich haben wir das schon die ganze 
schon gemacht.

## Beziehungen

Normalerweise haben Einträge eine Art von Beziehung. Um diese zu definieren, verwenden wir den Begriff "One to Many" (1:n) "Many to One" (n:1), 
Eins zu Eins" (1:1).
Du hast in der Regel keine "Many to Many" (n:n) und wenn doch, dann ist das höchstwahrscheinlich ein Zeichen für eine schlechte Normalisierung.

### One to Many und Many to One

One to Many bedeutet, dass eine einzelne Entität von dir mehrere Einträge in einer einzigen Tabelle hat. Wir haben dies 
bereits in unserem `friend_graph`. _Ein_ Spieler kann _viele_ Freunde haben. So einfach ist das!

Viele zu einem ist normalerweise dasselbe, nur in eine andere Richtung. _Viele_ Freundschaftsverbindungen beziehen sich auf _einen_ Spieler. 

### Eins zu Eins

Eins zu Eins ist das, was es schon sagt. Eine einzelne Entität steht in Beziehung zu einer anderen einzelnen Entität in einer anderen Tabelle. Wir haben das 
mit unserer `player` und `money` Tabelle. Die Geldtabelle enthält genau _einen_ Eintrag für _einen_ Spieler.

### Viele zu vielen

Mir fällt nicht einmal ein gutes Beispiel für eine Many-to-Many-Beziehung ein. Es ist einfach gegen das Gesetz. 

## Normalformen

Wenn wir Daten normalisieren, verwenden wir oft die Begriffe der Normalform. Das reine Konzept ist immer Gegenstand von Diskussionen 
und wird etwas unscharf, je mehr man versucht, zu normalisieren.

[Wikipedia](https://en.wikipedia.org/wiki/Database_normalization#Example_of_a_step_by_step_normalization) hat einige 
gute Beispiele dafür, wenn du dich damit beschäftigen willst. In Wirklichkeit wird dich nie jemand fragen, welche Normalform deine 
Tabelle verwenden soll.

## Faustregeln

Im Allgemeinen gibt es ein paar Regeln, die das Normalisieren von Daten schon recht einfach machen. Denke nicht über deine Normalform nach. 
Alles, was zählt, ist die Beziehung einer Entität zu einer anderen Entität.

### Schlüssel sind der Schlüssel

Denke an eindeutige Bezeichner für deine Entität. Dieser Bezeichner sollte sich nicht ändern. Das kann eine Art UID sein oder 
wenn deine Entität keine hat, sollte es eine automatisch inkrementierende ID sein.

### One to many in a column

Versuche gar nicht erst, One-to-Many-Beziehungen für Listen und Ähnliches in einer einzigen Spalte zu serialisieren. Das mag eine Zeit lang funktionieren 
Zeit funktionieren, aber später wirst du dich dafür hassen. Wenn du eine One-to-Many-Beziehung hast, mache zwei Tabellen dafür.

### Je feiner, desto besser

Teile deine Sachen auf. Gruppiere die Dinge, die zusammen bleiben sollen und die zusammen angefordert werden. Generell gilt: Wenn du es nicht 
nicht zur gleichen Zeit brauchst, gehört es nicht zusammen.

### Fremdschlüssel sind der Schlüssel

Wenn deine Tabelle nicht von anderen Tabellen referenziert wird oder nicht auf eine andere Tabelle verweist, sind deine Daten wahrscheinlich nicht 
nicht richtig gekoppelt. Letztendlich gehören alle Daten zu irgendeiner Art von Entität und dein Tabellenlayout sollte dies widerspiegeln.
