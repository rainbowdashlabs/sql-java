# Weitere Indizes

Wir kennen bereits den [unique index](data_consistency/unique_indice.md), der sicherstellt, dass ein einzelner Wert oder eine Kombination von Werten eindeutig ist.
Aber es gibt noch mehr!
Viel mehr und wir werden in diesem Abschnitt noch mehr coole Sachen behandeln.

Ein Index ist mehr als nur eine einfache Möglichkeit, Einschränkungen für Spalten in Form von Werten zu erzwingen.
Das ist natürlich eine schöne Sache, aber wir können ihn noch für mehr nutzen, vor allem um unsere Suche zu beschleunigen.
Außerdem muss ein Index nicht eindeutig sein und kann Daten sogar umwandeln, bevor er sie indiziert.

Im Allgemeinen kannst du dir einen Index als eine sortierte Kopie deiner Spalte vorstellen, die sogar umgewandelte oder vorgeprüfte Daten enthalten kann.
Denn das ist es, was ein Index im Grunde ist.
Und ein Index wird nicht auf magische Weise berechnet, wenn du ihn brauchst, sondern er ist bereits irgendwo in der Datenbank vorberechnet.
Das bedeutet auch, dass nicht nur deine Tabelle Speicherplatz braucht, sondern auch deine Indizes.
Es ist nicht einmal ungewöhnlich, dass die Indizes einer Tabelle viel mehr Platz benötigen als die Tabelle selbst.
