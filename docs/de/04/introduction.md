# Einführung

Endlich sind wir an dem Punkt angekommen, auf den du wahrscheinlich gewartet hast. Wenn du diesen Punkt übersprungen hast, solltest du wissen, dass ich in diesem Abschnitt keine
SQL-Abfragen in diesem Abschnitt nicht mehr erklären werde. Alles, was in den vorherigen Kapiteln erklärt wurde, nehme ich als gegeben hin.

**Bitte geh zurück, wenn du etwas nicht verstehst**

In diesem Kapitel dreht sich alles um JDBC und wie wir es nutzen können, um mit unserer Datenbank zu interagieren. Das heißt, wir werden endlich anfangen
Java zu benutzen und unsere Datenbank über unsere Java-Anwendung abzufragen! Einige wichtige Wörter solltest du kennen und ihre
Bedeutung verstehen solltest, sind DataSource, jdbc, driver und try with resources. Das letzte Wort ist ein allgemeines Konzept
von Java, aber wir werden uns auch damit beschäftigen, da es für unsere weiteren Schritte entscheidend sein wird.

Wir werden alle Operationen lernen, die wir zuvor in SQL gelernt haben. Da die Unterschiede zwischen den Datenbanken nun
geringer sind, werde ich im Allgemeinen Postgres verwenden. Wenn es Unterschiede gibt, insbesondere bei der Treiber
Implementierung selbst geht, werde ich natürlich trotzdem zeigen, wie es mit jeder Datenbank gemacht wird. Beachte nur, dass die Abfragen
die ich zeige, möglicherweise nicht funktionieren, wenn du eine andere Datenbank verwendest.
